from sqlalchemy import Column, Integer, String, Float, ForeignKey, Text
from sqlalchemy.orm import relationship
from config import Base

class Shop(Base):
    __tablename__ = "shop"  # 테이블 이름

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), nullable=False)          # 음식점 이름
    main_menu = Column(String(255), name="main_menu")   # 주메뉴
    address = Column(String(255), nullable=False)       # 주소
    phone = Column(String(20), nullable=True)           # 전화번호
    lat = Column(Float, nullable=True)                  # 위도
    lng = Column(Float, nullable=True)                  # 경도
    img_url = Column(String(1024), nullable=True)       # 이미지 URL
    like_count = Column(Integer, default=0)             # 좋아요 수

    # ShopTag와의 관계 설정 (1:N 관계)
    tags = relationship("ShopTag", back_populates="shop")

    def __repr__(self):
        return f"<Shop(name={self.name}, address={self.address})>"


class ShopTag(Base):
    __tablename__ = "shoptag"

    id = Column(Integer, primary_key=True, index=True)
    shop_id = Column(Integer, ForeignKey("shop.id"))     # Shop 테이블과 연결
    name = Column(String(255), nullable=True, index=True) # 태그 이름
    tags = Column(Text, nullable=True)                   # 태그 데이터
    alone = Column(Integer, default=0)                   # 혼밥
    chilam_dong = Column(Integer, default=0)             # 칠암동
    delicious = Column(Integer, default=0)               # 맛있는
    fresh = Column(Integer, default=0)                   # 신선한
    gajwa_dong = Column(Integer, default=0)              # 가좌동
    good_value = Column(Integer, default=0)              # 가성비
    hygiene = Column(Integer, default=0)                 # 위생
    kindness = Column(Integer, default=0)                # 친절
    mood = Column(Integer, default=0)                    # 분위기
    recent = Column(Integer, default=0)                  # 최근 방문
    revisit = Column(Integer, default=0)                 # 재방문률

    # Shop과의 관계 설정
    shop = relationship("Shop", back_populates="tags")

    def __repr__(self):
        return f"<ShopTag(name={self.name}, shop_id={self.shop_id})>"
