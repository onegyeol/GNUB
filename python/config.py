from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base

DATABASE_URL = "mysql+mysqlconnector://root:qwer1234@localhost:3306/GNUB"

# 데이터베이스 연결 설정
engine = create_engine(DATABASE_URL, echo=True)  # echo=True: SQL 쿼리를 출력

# 세션 생성기
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# ORM 모델 베이스 클래스
Base = declarative_base()
try:
    # 데이터베이스 연결 테스트
    connection = engine.connect()
    print("데이터베이스에 성공적으로 연결되었습니다!")
    connection.close()
except Exception as e:
    print(f"데이터베이스 연결 실패: {e}")