# weather.py
import os
import requests
import datetime
from urllib.parse import quote
from dotenv import load_dotenv

load_dotenv(override=True)

def get_requested_forecast_datetime(user_query):
    """
    사용자 쿼리에서 미래 시각 요청에 따라 YYYYMMDDHHMM 형식의 문자열을 반환합니다.
    
    - "저녁"이라는 단어가 있으면:
        * 현재 시간이 18:00 이전이면 오늘 18:00,
        * 18:00 이상 21:00 미만이면 오늘 18:00,
        * 21:00 이상이면 내일 18:00.
    - "3시간"이란 단어가 있으면 현재 시각 + 3시간.
    - 그 외에는 기본적으로 현재 시각에서 40분을 뺀 시각을 반환.
    """
    now = datetime.datetime.now()
    query = user_query.lower()
    
    if "저녁" in query:
        dinner_time = now.replace(hour=18, minute=0, second=0, microsecond=0)
        # 현재 시간이 18:00 이전이면 오늘 18:00 사용
        if now < dinner_time:
            target = dinner_time
        # 18:00 이상 21:00 미만이면 오늘 18:00 사용
        elif dinner_time <= now < now.replace(hour=21, minute=0, second=0, microsecond=0):
            target = dinner_time
        # 21:00 이상이면 내일 18:00 사용
        else:
            target = dinner_time + datetime.timedelta(days=1)
        return target.strftime("%Y%m%d%H%M")
    elif "3시간" in query:
        target = now + datetime.timedelta(hours=3)
        return target.strftime("%Y%m%d%H%M")
    else:
        target = now - datetime.timedelta(minutes=40)
        return target.strftime("%Y%m%d%H%M")

def get_base_datetime(requested_dt=None):
    """
    requested_dt: YYYYMMDDHHMM 형식의 문자열로, 사용자가 요청한 예보 시각.
    제공되지 않으면 기본값으로 현재 시각에서 40분을 뺀 값을 사용.
    반환값: (base_date, base_time) 튜플.
    """
    if requested_dt:
        try:
            dt = datetime.datetime.strptime(requested_dt, "%Y%m%d%H%M")
        except Exception as e:
            dt = datetime.datetime.now() - datetime.timedelta(minutes=40)
    else:
        dt = datetime.datetime.now() - datetime.timedelta(minutes=40)
    base_date = dt.strftime("%Y%m%d")
    base_time = dt.strftime("%H%M")
    return base_date, base_time

def get_weather_forecast(nx="81", ny="76", forecast_datetime=None):
    """
    nx, ny: 지역 좌표 (문자열)
    forecast_datetime: YYYYMMDDHHMM 형식의 예보 시각 문자열.
                       (예를 들어, get_requested_forecast_datetime()의 반환값)
                       제공되지 않으면 기본값으로 현재 시각 - 40분을 사용.
    """
    base_date, base_time = get_base_datetime(forecast_datetime)
    # Dokcer 삭제해서 지금 안되고 나중에 Docker 재설치하면 사용
    raw_key = os.getenv("KOREA_WEATHER_API_KEY")
    if raw_key is None:
        raise Exception("KOREA_WEATHER_API_KEY 환경 변수가 설정되어 있지 않습니다.")
    SERVICE_KEY = raw_key.strip()
   
    
    url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"
    params = {
        "serviceKey": SERVICE_KEY,
        "pageNo": "1",
        "numOfRows": "100",
        "dataType": "JSON",
        "base_date": base_date,
        "base_time": base_time,
        "nx": nx,
        "ny": ny
    }
    
    response = requests.get(url, params=params)
    if response.status_code != 200:
        raise Exception(f"API 호출 실패: {response.status_code} - {response.text}")
    
    try:
        data = response.json()
    except Exception as e:
        raise Exception(f"JSON 파싱 실패: {e}\n응답 내용: {response.text}")
    
    items = data.get("response", {}).get("body", {}).get("items", {}).get("item", [])
    
    temp = None
    for item in items:
        if item.get("category") == "T1H":
            try:
                temp = float(item.get("fcstValue"))
            except (TypeError, ValueError):
                temp = None
            break
    
    print("추출된 기온 정보 (T1H):", temp)
    
    if temp is None:
        recommendation = "날씨 정보를 확인할 수 없습니다."
    elif temp > 30:
        recommendation = "매우 덥습니다. 실내 음식점이나 시원한 메뉴를 추천합니다."
    elif temp > 25:
        recommendation = "더운 날씨입니다. 가볍고 시원한 메뉴를 즐기기 좋은 날이에요."
    elif temp > 20:
        recommendation = "따뜻한 날씨입니다. 여유로운 분위기의 식당을 추천합니다."
    elif temp > 10:
        recommendation = "약간 쌀쌀합니다. 따뜻한 분위기의 식당을 추천합니다."
    else:
        recommendation = "날씨가 춥습니다. 따뜻한 음식과 실내 분위기의 식당을 추천합니다."
    
    result = {
        "nx": nx,
        "ny": ny,
        "base_date": base_date,
        "base_time": base_time,
        "temperature": temp,
        "recommendation": recommendation
    }
    
    print("API 호출 결과:", result)
    return result
