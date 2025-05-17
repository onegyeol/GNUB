# db_conn.py

import datetime
import pandas as pd
import pymysql

def get_today_column() -> str:
    """현재 요일을 MySQL 컬럼명('Mon','Tue',…)으로 반환"""
    return datetime.datetime.today().strftime('%a').capitalize()

def get_today_korean_prefix() -> str:
    """현재 요일의 한글 접두어('월','화',…)를 반환"""
    mapping = {'Mon':'월','Tue':'화','Wed':'수','Thu':'목','Fri':'금','Sat':'토','Sun':'일'}
    return mapping.get(datetime.datetime.today().strftime('%a'), '월')

def get_time_condition(day_column: str, time: str) -> str:
    """
    요일별 영업시간을 SQL WHERE 절로 만들어 줍니다.
    - 점심: 11:00 ~ 14:00
    - 저녁: 17:00 ~ 21:00
    - 야식: 21:00 ~ 23:59 OR 00:00 ~ 02:00
    """
    ranges = {
        "점심": ("11:00", "14:00"),
        "저녁": ("17:00", "21:00"),
        "야식": ("22:00", "02:00"),
    }
    if time not in ranges:
        return ""

    start, end = ranges[time]
    prefix = get_today_korean_prefix()

    # 일반 점심/저녁 처리
    if time in ("점심","저녁"):
        return f"""
        {day_column} LIKE '%{prefix}%' AND {day_column} NOT LIKE '%정기휴무%' AND 
        STR_TO_DATE(
            SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE({day_column}, '{prefix}', ''), '\\n', 1), ' - ', 1),
            '%H:%i'
        ) <= STR_TO_DATE('{start}', '%H:%i')
        AND
        STR_TO_DATE(
            SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE({day_column}, '{prefix}', ''), '\\n', 1), ' - ', -1),
            '%H:%i'
        ) >= STR_TO_DATE('{end}', '%H:%i')
        """

    # 야식: 22:00~23:59 OR 00:00~02:00
    # 첫 줄 영업시간 문자열에 prefix가 붙어있으니 동일하게 처리
    return f"""
    {day_column} LIKE '%{prefix}%' AND {day_column} NOT LIKE '%정기휴무%' AND (
        (
            STR_TO_DATE(
                SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE({day_column}, '{prefix}', ''), '\\n', 1), ' - ', 1),
                '%H:%i'
            ) <= STR_TO_DATE('23:59', '%H:%i')
            AND
            STR_TO_DATE(
                SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE({day_column}, '{prefix}', ''), '\\n', 1), ' - ', -1),
                '%H:%i'
            ) >= STR_TO_DATE('21:00', '%H:%i')
        )
        OR
        (
            STR_TO_DATE(
                SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE({day_column}, '{prefix}', ''), '\\n', 2), ' - ', 1),
                '%H:%i'
            ) <= STR_TO_DATE('02:00', '%H:%i')
            AND
            STR_TO_DATE(
                SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE({day_column}, '{prefix}', ''), '\\n', 2), ' - ', -1),
                '%H:%i'
            ) >= STR_TO_DATE('00:00', '%H:%i')
        )
    )
    """

def generate_sql_query(parsed: dict):
    """
    parsed: {'place':…, 'category':…, 'time':…} 형태로 들어오면
    SQL WHERE 절을 만들어 반환.
    """
    current_day = get_today_column()
    place    = parsed.get("place")
    category = parsed.get("category")
    time     = parsed.get("time")
    conditions = []
    if pd.notna(place) and place:
        conditions.append(f"campus LIKE '%{place}%'")
    # ✅ 카테고리 필터는 항상 적용
    if pd.notna(category) and category:
        conditions.append(f"category LIKE '%{category}%'")
    #if pd.notna(time) and time:
    #    conditions.append(get_time_condition(current_day, time))

    sql = "SELECT * FROM shop"
    if conditions:
        sql += " WHERE " + " AND ".join(conditions)
    return sql

def query_database(sql_query: str) -> list:
    """MySQL에 접속해 쿼리 실행 후 결과(dict 리스트)를 반환"""
    conn = pymysql.connect(
        host='54.180.147.144',
        user='gnub_user',
        password='qwer1234',
        database='gnub',
        charset='utf8mb4',
        cursorclass=pymysql.cursors.DictCursor
    )
    with conn:
        with conn.cursor() as cur:
            cur.execute(sql_query)
            return cur.fetchall()
