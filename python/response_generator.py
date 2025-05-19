import os
import random
import openai
from dotenv import load_dotenv

# 환경 변수 로드
load_dotenv()
openai.api_key = os.getenv("OPENAI_API_KEY")
USE_MOCK = os.getenv("USE_MOCK", "false").lower() == "true"

# 대화 이력
conversation_history = []
last_indices = {}

# 시간/카테고리 필터
EXCLUDE_CATEGORY_BY_TIME = {
    "점심": ["카페", "디저트", "포장마차", "맥주", "요리주점", "민속주점", "베이커리", "케이크전문", "이자카야", "커피", "술집"],
    "저녁": ["카페", "디저트", "포장마차", "맥주", "요리주점", "민속주점", "베이커리", "케이크전문", "이자카야", "커피", "술집"],
    "야식": ["카페", "디저트", "커피", "베이커리", "케이크전문", "김밥", "도시락", "종합분식", "아귀찜,해물찜"]
}

EXCLUDE_CATEGORY_BY_CATEGORY = {
    "카페": ["한식", "분식", "중식", "일식", "양식", "치킨", "패스트푸드", "국밥", "고기", "술집"],
    "디저트": ["한식", "중식", "분식", "치킨"],
    "술집": ["카페", "디저트", "커피", "베이커리", "케이크전문"]
}


def apply_category_filters(restaurant_list, parsed_query):
    exclude = set()
    time = parsed_query.get("time")
    category = parsed_query.get("category")

    if time in EXCLUDE_CATEGORY_BY_TIME:
        exclude.update(EXCLUDE_CATEGORY_BY_TIME[time])
    if category in EXCLUDE_CATEGORY_BY_CATEGORY:
        exclude.update(EXCLUDE_CATEGORY_BY_CATEGORY[category])

    if exclude:
        return [r for r in restaurant_list if all(x not in r.get("category", "") for x in exclude)]
    return restaurant_list

# 순환식 샘플링
def rotate_sample(results: list, k: int = 3, tag: str = ""):
    n = len(results)
    if n == 0:
        return []
    start = last_indices.get(tag, 0) % n
    sampled = [results[(start + i) % n] for i in range(min(k, n))]
    last_indices[tag] = (start + k) % n
    return sampled


def generate_mock_response(user_query, restaurant_results, parsed_query):
    print(f"▶ 질문: {user_query}")
    base_list = list(restaurant_results)
    print("🔍 DB에서 넘어온 원본:", [r["name"] for r in base_list])

    if parsed_query:
        base_list = apply_category_filters(base_list, parsed_query)
        print("🧹 필터링 결과:", [r["name"] for r in base_list])

    scored_list = []
    for r in base_list:
        score = r.get("like_count", 0) + random.uniform(0, 20)
        r["recommend_score"] = score
        scored_list.append(r)

    scored_list.sort(key=lambda r: r["recommend_score"], reverse=True)

    print("❤️ 추천 후보 점수:")
    for r in scored_list[:10]:
        print(f"  - {r['name']:20} | 좋아요: {r['like_count']:>4} | 점수: {r['recommend_score']:.2f}")

    sample = scored_list[:2]
    print("✅ 최종 추천 2곳:", [r["name"] for r in sample])

    if not sample:
        return f"[MOCK] '{user_query}' → 추천할 음식점이 없습니다."
    lines = "\n".join(f"- {r['name']} (주소: {r['address']})" for r in sample)
    return f"[MOCK] '{user_query}' → 다음 식당을 추천합니다:\n{lines}"


def generate_response(user_query: str, restaurant_results: list, weather_info: dict, parsed_query: dict = None) -> str:
    if USE_MOCK:
        return generate_mock_response(user_query, restaurant_results, parsed_query)

    system_msg = {
        "role": "system",
        "content": (
            "당신은 진주 경상대학교 주변(가좌동·칠암동) 맛집 전문가입니다. "
            "친근하고 자연스러운 말투로, 사용자의 질문에 맞춰 최대 3곳을 추천해주세요."
        )
    }

    few_shot = [
        {"role": "user", "content": "저녁에 뭐 먹을까?"},
        {"role": "assistant", "content": "저녁으로는 [본도시락 가좌점]을 추천드려요. "}
    ]

    weather_text = (
        f"현재 기온은 {weather_info['temperature']}°C이고, {weather_info['recommendation']}\n"
        if weather_info and weather_info.get("temperature") is not None else
        "현재 날씨 정보가 없습니다.\n"
    )

    sampled = []
    if restaurant_results:
        base_list = list(restaurant_results)
        print("🔍 DB에서 넘어온 원본:", [r["name"] for r in base_list])
        scored_list = []
        for r in base_list:
            score = r.get("like_count", 0) + random.uniform(0, 20)
            r["recommend_score"] = score
            scored_list.append(r)
        scored_list.sort(key=lambda r: r["recommend_score"], reverse=True)
        print("❤️ 추천 후보 점수:")
        for r in scored_list[:10]:
            print(f"  - {r['name']:20} | 좋아요: {r['like_count']:>4} | 점수: {r['recommend_score']:.2f}")
        sampled = scored_list[:2]

    resto_text = (
        "추천 음식점 (2곳):\n" +
        "\n".join(f"- {r['name']} (주소: {r['address']})" for r in sampled) + #, 특징: {r['info']}
        "\n"
        if sampled else
        "추천할 음식점이 없습니다.\n"
    )

    user_msg = {
        "role": "user",
        "content": (
            f"질문: {user_query}\n"
            f"{weather_text}"
            f"{resto_text}"
            "위 정보를 바탕으로 추천 문장을 작성해주세요."
        )
    }

    conversation_history.append(user_msg)
    if len(conversation_history) > 2:
        conversation_history.pop(0)

    response = openai.ChatCompletion.create(
        model="gpt-3.5-turbo",
        messages=[system_msg] + few_shot + conversation_history,
        temperature=0.75,
        top_p=0.95,
        max_tokens=200,
    )

    answer = response.choices[0].message.content.strip()
    conversation_history.append({"role": "assistant", "content": answer})
    return answer

print(f"✅ 현재 적용된 키: {openai.api_key}")
