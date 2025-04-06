# nlp.py

import torch
from sentence_transformers import SentenceTransformer, util

def load_model():
    """
    미세조정된 SBERT 모델 로드.
    """
    model = SentenceTransformer("fine_tuned_sbert_v17")
    return model

def get_category_embeddings(model):
    """
    카테고리 사전과 각 카테고리에 대한 임베딩을 생성하여 반환.
    """
    category_dict = {
        "place": ["가좌", "칠암", "가좌동", "칠암동"],  
        "time": ["점심", "저녁", "야식"],
        "category": [
            "한식", "양식", "게요리", "생선회", "일식당", "국밥", "레스토랑", "주점",
            "스파게티", "파스타", "다이어트", "샐러드", "찌개", "전골", "스페인음식", "인도음식", "이자카야",
            "돈가스", "퓨전", "중식", "오리", "술집", "육류", "고기요리", "도시락", "컵밥",
            "소고기", "구이", "오뎅", "꼬치", "초밥", "롤", "칼국수", "만두", "베트남", "토스트",
            "전통", "민속", "죽", "바", "(BAR)", "치킨", "닭강정", "분식", "마라탕", "국수",
            "카페", "디저트", "베이커리", "빵", "케이크", "샤브", "곱창", "막창", "양", "호두과자",
            "냉면", "멕시코", "남미음식", "맥주", "호프", "샌드위치", "햄버거", "족발", "보쌈", "아이스크림",
            "뷔페", "우동", "소바", "닭갈비", "김밥", "추어탕", "커피", "닭",
            "포장마차", "비빔밥", "해장국", "피자", "찜", "와플", "주꾸미",
            "돼지고기", "해물", "생선", "커피번", "장어", "먹장어요리", "감자탕",
            "정육식당", "차", "육류", "한정식", "찜닭", "닭발", "떡볶이", "야식",
            "매운탕", "해물탕", "이탈리아", "고양이카페", "일본식라면", "핫도그", "카레", "커리",
            "전복요리", "채식", "빙수", "스테이크,립", "아시아음식", "양꼬치", "설빙",
            "쌈밥", "브런치", "라이브카페", "양갈비"
        ]
    }
    category_embeddings = {key: model.encode(category_dict[key]) for key in category_dict}
    return category_dict, category_embeddings

def parse_query_sbert(query, model, category_embeddings, category_dict, top_k=3, place_threshold=0.6, time_threshold=0.7, category_threshold=0.7, diff_threshold=0.05):
    """
    사용자 질문을 SBERT 기반으로 분석하여 'place', 'time', 'category' 정보를 추출.
    """
    query_embedding = model.encode(query, convert_to_tensor=True)
    parsed = {"place": None, "time": None, "category": None}
    best_matches = {}

    for key, embeddings in category_embeddings.items():
        embeddings_tensor = torch.tensor(embeddings)
        similarities = util.cos_sim(query_embedding, embeddings_tensor)[0]
        top_k_indices = torch.argsort(similarities, descending=True)[:top_k]
        top_k_values = [category_dict[key][i] for i in top_k_indices]
        top_k_scores = [similarities[i].item() for i in top_k_indices]

        threshold = place_threshold if key == "place" else (time_threshold if key == "time" else category_threshold)
        
        if top_k_scores[0] > threshold:
            if len(top_k_scores) == 1 or (top_k_scores[0] - top_k_scores[1]) > diff_threshold:
                best_match = top_k_values[0]
            else:
                best_match = None
        else:
            best_match = None

        best_matches[key] = (best_match, top_k_scores[0])
    
    if best_matches["place"][0] in ["칠암", "칠암동"]:
        parsed["place"] = "칠암동"
    elif best_matches["place"][1] > place_threshold:
        parsed["place"] = best_matches["place"][0]

    if best_matches["time"][1] > time_threshold and parsed["place"] is None:
        parsed["time"] = best_matches["time"][0]

    if best_matches["category"][1] > category_threshold and parsed["place"] is None and parsed["time"] is None:
        parsed["category"] = best_matches["category"][0]

    return parsed

def get_best_sentence_index(query, sentences, model):
    """
    사용자 질문과 음식점 문장들 간의 유사도를 계산하여 가장 유사한 문장의 인덱스를 반환.
    """
    query_emb = model.encode(query, convert_to_tensor=True)
    sentence_embs = model.encode(sentences, convert_to_tensor=True)
    cos_sim = util.cos_sim(query_emb, sentence_embs)[0]
    best_idx = torch.argmax(cos_sim).item()
    return best_idx
