# app.py
from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd
from nlp import load_model, get_category_embeddings, parse_query, get_best_sentence_index
from db_conn import generate_sql_query, query_database
from response_generator import generate_response
from weather import get_weather_forecast
import traceback

app = Flask(__name__)
CORS(app)

# 앱 시작 시에 모델과 카테고리 임베딩, 음식점 데이터 로드 (한번만 로드)
model = load_model()
category_dict, category_embeddings = get_category_embeddings(model)
df = pd.read_csv("python/prepare_sentence_utf8.csv", encoding="utf-8-sig")
restaurant_sentences = df['sentence'].tolist()

@app.route('/chat', methods=['POST'])
def chat():
    try:
        data = request.get_json()
        user_query = data.get('query', '')
        
        if not user_query:
            return jsonify({"error": "No query provided"}), 400

        # 1. 사용자 입력 분석 (필요하다면 분석 결과를 활용할 수 있음)
        _ = parse_query(user_query, model, category_embeddings, category_dict)
        
        # 2. 입력과 음식점 문장들 사이의 유사도 계산 후 가장 유사한 문장 인덱스 선택
        best_idx = get_best_sentence_index(user_query, restaurant_sentences, model)
        
        # (임시) 파싱결과를 확인
        parsed = parse_query(user_query, model, category_embeddings, category_dict)
        print("▶ 파싱 결과:", parsed)

        # 3. SQL 쿼리 생성 후 DB 조회
        sql_query = generate_sql_query(parsed)
        restaurant_results = query_database(sql_query)

        # 날씨 정보 조회 (기본 좌표/시간 사용, 필요 시 쿼리 파라미터로 받을 수 있음)
        weather_info = get_weather_forecast()
        
        # 4. GPT를 통해 최종 응답 생성
        final_response = generate_response(user_query, restaurant_results, weather_info)
        
        return jsonify({"reply": final_response})
    except Exception as e:
        import traceback
        traceback.print_exc()  # 🔴 이 줄이 없으면 콘솔에 에러 로그가 안 보임
        app.logger.error(traceback.format_exc())
        return jsonify({"error": "서버 내부 오류가 발생했습니다."}), 500

if __name__ == '__main__':
    # 0.0.0.0으로 외부 접근 가능하게 하고, 포트는 5000번으로 실행
    app.run(host='0.0.0.0', port=5050)
