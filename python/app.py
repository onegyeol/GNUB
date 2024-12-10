from flask import Flask, request, jsonify, g
from flask_cors import CORS, cross_origin
from flask_executor import Executor
from sqlalchemy import or_
from sqlalchemy.orm import aliased
from config import SessionLocal
from models import Shop, ShopTag
import openai
import logging
from typing import List
from rapidfuzz import fuzz
from dotenv import load_dotenv
import os


app = Flask(__name__)
CORS(app, resources={r"/ask/*": {"origins": "*"}})

# Flask-Executor 설정
executor = Executor(app)

# GPT API 키 설정
load_dotenv()  # .env 파일에서 환경 변수 로드
openai.api_key = os.getenv("OPENAI_API_KEY")  # API 키 설정

# 로깅 설정
logging.basicConfig(level=logging.DEBUG)

# 간단한 캐싱 시스템
response_cache = {}

# 요청 단위로 세션 생성 및 정리
@app.before_request
def create_session():
    g.db = SessionLocal()

@app.teardown_request
def cleanup_session(exception=None):
    db = g.pop('db', None)
    if db:
        db.close()

@app.route("/")
def home():
    return "Hello, World!"

# 중복된 답변 제거
def remove_duplicate_responses(responses):
    return list(set(responses))

# 키워드 추출 함수
def extract_keywords_with_retries(user_input, retries=5):
    keywords_set = set()
    for _ in range(retries):
        try:
            app.logger.debug(f"Extracting keywords from input: {user_input}")
            response = openai.ChatCompletion.create(
                model="gpt-3.5-turbo",
                messages=[
                    {"role": "system", "content": 
                     "주어진 문장에서 중요한 음식 단어만 추출하세요. 결과를 콤마(,)로 구분해 반환하세요."},
                    {"role": "user", "content": user_input},
                ],
            )
            keywords = response["choices"][0]["message"]["content"]
            extracted_keywords = [kw.strip() for kw in keywords.split(",")]
            keywords_set.update(extracted_keywords)
        except Exception as e:
            app.logger.error(f"Error extracting keywords: {e}")

    clean_keywords = [kw.rstrip("집가게식당") for kw in keywords_set]
    return list(set(clean_keywords))

# 시간대 추출 함수
def extract_time_of_day(user_input):
    if "아침" in user_input:
        return "아침"
    elif "점심" in user_input:
        return "점심"
    elif "저녁" in user_input:
        return "저녁"
    else:
        return "기타" 

# 데이터베이스 검색 함수
def search_shops_by_keywords(keywords: List[str]) -> List[Shop]:
    shop_tag_alias = aliased(ShopTag)
    query = g.db.query(Shop).outerjoin(shop_tag_alias, Shop.id == shop_tag_alias.id)

    filters = []
    for keyword in keywords:
        filters.extend([Shop.name.like(f"%{keyword}%"),
                        Shop.main_menu.like(f"%{keyword}%"),
                        Shop.address.like(f"%{keyword}%"),
                        shop_tag_alias.name.like(f"%{keyword}%"),
                        shop_tag_alias.tags.like(f"%{keyword}%")])

    if filters:
        query = query.filter(or_(*filters))
    return query.all()

def search_shops_with_retries(keywords: List[str], retries: int = 5) -> List[Shop]:
    results_set = set()
    for _ in range(retries):
        results = search_shops_by_keywords(keywords)
        results_set.update(results)
    return list(results_set)

# GPT 호출 함수
def ask_gpt_with_context(input_prompt, keywords=None, time_of_day=None):
    context = "응답은 최대 세 문장으로 작성하세요."
    if time_of_day:
        context += f" {time_of_day}에 적합한 음식 추천 부탁드립니다."
    if keywords:
        context += f" 다음 키워드를 고려하세요: {', '.join(keywords)}."

    response = openai.ChatCompletion.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": context},
            {"role": "user", "content": input_prompt},
        ],
        temperature=0.5,
        max_tokens=200
    )
    return response["choices"][0]["message"]["content"]

# 사용자 입력과 음식점 매칭 확인
def verify_shop_match(user_input: str, shops: List[Shop]) -> bool:
    user_keywords = extract_keywords_with_retries(user_input, retries=1)
    for shop in shops:
        shop_text = shop.name + " " + shop.main_menu
        for keyword in user_keywords:
            if fuzz.partial_ratio(keyword, shop_text) > 80:
                return True
    return False

# 비동기 작업 함수
def handle_query(user_input):
    try:
        time_of_day = extract_time_of_day(user_input)

        if time_of_day == "아침":
            return {
                "gptResponse": [
                    {
                        "message": "아침으로는 '천원의 아침밥' 학식을 추천드립니다.",
                        "link": "https://www.gnu.ac.kr/main/ad/fm/foodmenu/selectFoodMenuView.do?mi=1341"
                    }
                ]
            }

        keywords = extract_keywords_with_retries(user_input, retries=5)
        shops = search_shops_with_retries(keywords, retries=5)

        if shops:
            shop_info = "\n".join([f"{shop.name} - 주소: {shop.address}" for shop in shops])
            gpt_input = f"추천할 음식과 음식점:\n{shop_info}"
        else:
            gpt_input = "추천할 만한 음식을 알려주세요."

        gpt_responses = [ask_gpt_with_context(gpt_input, keywords, time_of_day)]

        # 응답이 비어 있으면 기본 메시지 추가
        if not gpt_responses or not gpt_responses[0]:
            return {"gptResponse": [{"message": "추천할 음식을 알려주세요."}]}

        if not shops:
            return {"gptResponse": gpt_responses}

        if verify_shop_match(user_input, shops):
            response_cache[user_input] = gpt_responses
            return {"gptResponse": response_cache[user_input]}

        return {"gptResponse": [{"message": "추천한 음식점이 입력과 일치하지 않습니다. 다시 시도해주세요."}]}

    except Exception as e:
        app.logger.error(f"Error while processing query: {e}")
        return {"gptResponse": [{"message": "죄송합니다. 시스템 오류가 발생했습니다."}]}



# 간단한 캐싱 시스템
@app.route("/ask/ai", methods=["POST", "OPTIONS"])
@cross_origin()
def ask_ai():
    data = request.get_json()
    user_input = data.get("query")
    session_id = data.get("session_id")

    if not user_input or not session_id:
        return jsonify({"error": "Invalid query or session_id"}), 400

    # 세션별 캐시 관리
    if session_id not in response_cache:
        response_cache[session_id] = {}  # 새로운 세션 캐시 생성

    session_cache = response_cache[session_id]

    # 캐시 확인
    if user_input in session_cache:
        app.logger.debug(f"세션 {session_id} 캐시에서 응답 반환: {session_cache[user_input]}")
        return jsonify(session_cache[user_input]), 200

    # 비동기 작업 실행
    future = executor.submit(handle_query, user_input)
    result = future.result()  # handle_query 결과는 이미 JSON 호환 형식

    # AI 응답 누락 시 기본 메시지 설정
    if not result or "gptResponse" not in result or not result["gptResponse"]:
        result = {"gptResponse": [{"message": "추천할 수 없습니다. 다시 시도해주세요."}]}

    # 결과를 세션 캐시에 저장
    session_cache[user_input] = result
    app.logger.debug(f"세션 {session_id}에 응답 저장: {result}")

    return jsonify(result)  # JSON 응답 반환


@app.route('/log', methods=['POST'])
@cross_origin()
def log_data():
    data = request.get_json()
    app.logger.info(f"Received log data: {data}")
    return {'status': 'success'}, 200

if __name__ == "__main__":
    app.run(debug=True)
