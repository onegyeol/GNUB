# python/Dockerfile
FROM python:3.9-slim

WORKDIR /app

# 의존성 설치를 위한 requirements.txt 복사 및 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Python 소스 파일 전체 복사 (app.py, 기타 파일들)
COPY . .

# Flask 앱이 사용하는 포트 노출 (예: 5000)
EXPOSE 5000

# Flask 애플리케이션 실행 (app.py가 진입점)
CMD ["python", "app.py"]
