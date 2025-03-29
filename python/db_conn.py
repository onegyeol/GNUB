# db.py

import datetime
import pandas as pd
import pymysql

def get_today_column():
    """현재 요일을 MySQL 컬럼명 형식('Mon', 'Tue', ...)으로 반환"""
    return datetime.datetime.today().strftime('%a').capitalize()

def get_today_korean_prefix():
    """현재 요일의 한글 접두어를 반환 (예: '월', '화', ...)"""
    weekday_map = {
        'Mon': '월', 'Tue': '화', 'Wed': '수', 'Thu': '목',
        'Fri': '금', 'Sat': '토', 'Sun': '일'
    }
    today = datetime.datetime.today().strftime('%a')
    return weekday_map.get(today, '월')

def get_time_condition(day_column, time):
    """
    요일별 영업시간을 SQL 조건으로 변환.
    실사용에 필요한 핵심 기능만 포함.
    """
    time_ranges = {
        "점심": ("11:00", "14:00"),
        "저녁": ("17:00", "21:00"),
        "야식": ("21:00", "02:00")
    }
    if time not in time_ranges:
        return ""
    target_start, target_end = time_ranges[time]
    korean_prefix = get_today_korean_prefix()
    return f"""
    {day_column} LIKE '%:%' AND {day_column} NOT LIKE '%정기휴무%' AND (
        STR_TO_DATE(
            SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE({day_column}, '{korean_prefix}', ''), '\\n', 1), ' - ', 1),
            '%H:%i'
        ) <= STR_TO_DATE('{target_start}', '%H:%i')
        AND 
        STR_TO_DATE(
            SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE({day_column}, '{korean_prefix}', ''), '\\n', 1), ' - ', -1),
            '%H:%i'
        ) >= STR_TO_DATE('{target_end}', '%H:%i')
    )
    """

def generate_sql_query(best_idx, df):
    """
    유사도가 가장 높은 문장의 인덱스(best_idx)와 CSV 데이터(df)를 기반으로
    SQL 쿼리문을 생성하여 반환.
    """
    current_day = get_today_column()
    parsed_sentence = df.iloc[best_idx]
    place = parsed_sentence.get('place')
    category = parsed_sentence.get('category')
    time = parsed_sentence.get('time')

    sql_conditions = []
    if pd.notna(place) and place:
        sql_conditions.append(f"address LIKE '%{place}%'")
    if pd.notna(category) and category:
        sql_conditions.append(f"type='{category}'")
    if pd.notna(time) and time:
        sql_conditions.append(get_time_condition(current_day, time))
    
    sql_query = "SELECT * FROM shop"
    if sql_conditions:
        sql_query += " WHERE " + " AND ".join(sql_conditions)
    
    return sql_query

def query_database(sql_query):
    """
    지정된 SQL 쿼리문을 실행하여 데이터베이스에서 결과를 조회하고 반환.
    """
    connection = pymysql.connect(
        host='3.34.226.191',
        user='gnub_user',
        password='qwer1234',
        database='gnub',
        charset='utf8mb4',
        cursorclass=pymysql.cursors.DictCursor
    )
    with connection:
        with connection.cursor() as cursor:
            cursor.execute(sql_query)
            results = cursor.fetchall()
    return results
