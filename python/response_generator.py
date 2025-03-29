import os
from dotenv import load_dotenv
import openai

load_dotenv()  # .env 파일에서 환경 변수 로드
openai.api_key = os.getenv("OPENAI_API_KEY")  # API 키 설정

def generate_response(user_query, restaurant_results):
    # 추천 관련 키워드 목록
    recommendation_keywords = ["음식점", "추천", "점심", "저녁", "야식"]

    # 질문에 추천 관련 키워드가 포함되고, 음식점 결과가 존재한다면
    if any(keyword in user_query for keyword in recommendation_keywords) and restaurant_results:
        # 음식점 추천은 최대 2개만 사용
        limited_results = restaurant_results[:2]
        prompt = f"사용자의 질문: '{user_query}'\n\n추천 음식점 정보 (최대 2개):\n"
        for res in limited_results:
            prompt += f"- {res['name']} (주소: {res['address']}, 특징: {res['info']})\n"
        prompt += "\n위 정보를 바탕으로 사용자에게 친절한 추천 문장을 작성해주세요."
    else:
        # 추천이 아닌 일반 질문의 경우
        prompt = f"사용자의 질문: '{user_query}'\n\n친절하고 상세하게 답변해주세요."

    response = openai.ChatCompletion.create(
        model="gpt-4",  # 또는 필요에 따라 'gpt-4-32k'
        messages=[{"role": "user", "content": prompt}],
        max_tokens=300,
        temperature=0.6
    )
    generated_text = response.choices[0].message.content.strip()
    return generated_text
