# nlp.py

import torch
from sentence_transformers import SentenceTransformer, util
from typing import Dict, List, Optional, Tuple, Union
import numpy as np

# 상수 설정: 별칭(alias) -> 정규명(canonical) 매핑
PLACE_ALIASES: Dict[str, str] = {
    "칠암": "칠암",
    "칠암동": "칠암",
    "강남로" : "칠암",
    "가좌": "가좌",
    "가좌동": "가좌",
}

CATEGORY_ALIASES: Dict[str, List[str]] = {
    "스파게티": ["파스타"],
    "초밥": ["스시"],
    "롤": ["스시"],
    "다이어트": ["샐러드"],
    "(BAR)": ["바"],
    "베이커리": ["빵"],
    "립": ["스테이크"],
    "술": ["술집","맥주", "호프", "요리주점", "민속주점", "이자카야", "포장마차"],
    "술집": ["술집","맥주", "호프", "요리주점", "민속주점", "이자카야", "포장마차"]
    # 필요 시 추가
}

# 카테고리 정규화 함수: 별칭이 있으면 정규명으로, 없으면 그대로 반환
def normalize_category(cat: str, query: str, model: SentenceTransformer) -> List[str]:
    return CATEGORY_ALIASES.get(cat, [cat])



# 카테고리 사전 생성 및 임베딩 로드
def load_model() -> SentenceTransformer:
    model = SentenceTransformer("python/fine_tuned_sbert_v17")
    return model


def get_category_embeddings(
    model: SentenceTransformer
) -> Tuple[Dict[str, List[str]], Dict[str, np.ndarray]]:
    """
    카테고리 사전과 각 카테고리에 대한 임베딩을 생성하여 반환.
    """
    category_dict = {
        "place": ["가좌", "칠암", "가좌동", "칠암동"],
        "time": ["점심", "저녁", "야식"],
        "category": [
            "한식", "양식", "게요리", "생선회", "일식당", "국밥", "레스토랑", "주점",
            "파스타", "샐러드", "찌개", "전골", "스페인음식",
            "인도음식", "이자카야", "돈가스", "퓨전", "중식", "오리", "술집", "육류",
            "고기요리", "도시락", "컵밥", "소고기", "구이", "오뎅", "꼬치", "스시",
            "칼국수", "만두", "베트남", "토스트", "전통", "민속", "죽", "바",
            "치킨", "닭강정", "분식", "마라탕", "국수", "카페", "디저트", "빵",
            "케이크", "샤브", "곱창", "막창", "양", "호두과자", "냉면", "멕시코", "남미음식",
            "맥주", "호프", "샌드위치", "햄버거", "족발", "보쌈", "아이스크림", "뷔페", "우동",
            "소바", "닭갈비", "김밥", "추어탕", "커피", "닭", "포장마차", "비빔밥", "해장국",
            "피자", "찜", "와플", "주꾸미", "돼지고기", "해물", "생선", "커피번", "장어",
            "먹장어요리", "감자탕", "정육식당", "차", "한정식", "찜닭", "닭발",
            "떡볶이", "매운탕", "해물탕", "이탈리아", "고양이카페", "일본식라면",
            "핫도그", "카레", "커리", "전복요리", "채식", "빙수", "스테이크", "아시아음식",
            "양꼬치", "설빙", "쌈밥", "브런치", "라이브카페", "양갈비"
        ]
    }
    # 카테고리별 임베딩 생성
    category_embeddings: Dict[str, np.ndarray] = {
        key: model.encode(values) for key, values in category_dict.items()
    }
    return category_dict, category_embeddings


def parse_keywords(
    query: str,
    category_dict: Dict[str, List[str]],
    model: SentenceTransformer
) -> Dict[str, Optional[Union[str, List[str]]]]:
    parsed: Dict[str, Optional[Union[str, List[str]]]] = {"place": None, "time": None, "category": None}
    q = query.lower()

    for alias, canonical in PLACE_ALIASES.items():
        if alias in q:
            parsed["place"] = canonical
            break

    for t in category_dict["time"]:
        if t in q:
            parsed["time"] = t
            break

    for c in category_dict["category"] + list(CATEGORY_ALIASES.keys()):
        if c in q:
            norm = normalize_category(c, query, model)
            if parsed["time"] and isinstance(norm, list) and parsed["time"] in norm:
                continue
            parsed["category"] = norm  # List[str] 형태로 저장
            break

    return parsed


def parse_with_sbert(
    query: str,
    model: SentenceTransformer,
    category_embeddings: Dict[str, np.ndarray],
    category_dict: Dict[str, List[str]],
    top_k: int = 3,
    place_threshold: float = 0.4,
    time_threshold: float = 0.7,
    category_threshold: float = 0.7,
    diff_threshold: float = 0.05
) -> Dict[str, Optional[str]]:
    """
    SBERT 임베딩 기반으로 place, time, category를 분석합니다.
    """
    query_embedding = model.encode(query, convert_to_tensor=True)
    best_matches: Dict[str, Tuple[Optional[str], float]] = {}
    parsed: Dict[str, Optional[str]] = {"place": None, "time": None, "category": None}

    for key, embeddings in category_embeddings.items():
        embeddings_tensor = torch.tensor(embeddings)
        similarities = util.cos_sim(query_embedding, embeddings_tensor)[0]
        top_k_indices = torch.argsort(similarities, descending=True)[:top_k]
        top_k_values = [category_dict[key][i] for i in top_k_indices]
        top_k_scores = [similarities[i].item() for i in top_k_indices]

        threshold = (
            place_threshold if key == "place"
            else (time_threshold if key == "time" else category_threshold)
        )

        if top_k_scores[0] > threshold and (
            len(top_k_scores) == 1 or (top_k_scores[0] - top_k_scores[1]) > diff_threshold
        ):
            best_match = top_k_values[0]
        else:
            best_match = None

        best_matches[key] = (best_match, top_k_scores[0])

    # 장소 정제
    place_match, place_score = best_matches["place"]
    if place_match in ["칠암", "칠암동"]:
        parsed["place"] = "칠암동"
    elif place_match and place_score > place_threshold:
        parsed["place"] = place_match

    # 시간, 장소 없을 때만
    time_match, time_score = best_matches["time"]
    if not parsed["place"] and time_score > time_threshold:
        parsed["time"] = time_match

    # 카테고리, 장소·시간 없을 때만
    cat_match, cat_score = best_matches["category"]
    if not parsed["place"] and not parsed["time"] and cat_score > category_threshold:
        parsed["category"] = cat_match

    return parsed


def parse_query(
    query: str,
    model: SentenceTransformer,
    category_embeddings: Dict[str, np.ndarray],
    category_dict: Dict[str, List[str]]
) -> Dict[str, Optional[Union[str, List[str]]]]:
    """
    통합 파싱: 키워드 매핑 후 SBERT 보완 로직을 적용하여 결과를 반환합니다.
    """
    # 1) 키워드 기반 + SBERT 정규화
    result = parse_keywords(query, category_dict, model)
    # 2) SBERT 임베딩 기반 보완
    sbert_result = parse_with_sbert(query, model, category_embeddings, category_dict)

    # 3) None인 항목만 보완
    for key in ("place", "time"):
        if result[key] is None:
            result[key] = sbert_result[key]
    if result["category"] is None:
        result["category"] = sbert_result["category"]

    return result

def get_best_sentence_index(
    query: str,
    sentences: List[str],
    model: SentenceTransformer
) -> int:
    """
    사용자 질문과 문장 리스트 간 유사도를 계산하여 가장 유사한 문장 인덱스를 반환합니다.
    """
    query_emb = model.encode(query, convert_to_tensor=True)
    sentence_embs = model.encode(sentences, convert_to_tensor=True)
    cos_sim = util.cos_sim(query_emb, sentence_embs)[0]
    best_idx = torch.argmax(cos_sim).item()
    return best_idx
