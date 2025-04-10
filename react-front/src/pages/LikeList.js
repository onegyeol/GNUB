import React, { useEffect, useState } from 'react';
import { fetchLikes } from '../services/MypageApi';
import './css/LikeList.css';

export default function LikeList() {
  const [likedShops, setLikedShops] = useState([]);

  useEffect(() => {
    fetchLikes().then(data => setLikedShops(data));
  }, []);

  return (
    <div id="root">
      <header className="header-content">
        <div className="common-desk-header">
          <div className="header-wrap">
            <div className="main-logo">
              <a href="/main">
                <img className="logo" src="https://github.com/user-attachments/assets/fc7341a0-da3e-4971-9100-ddac29772144" alt="Logo" />
              </a>
            </div>
            <div className="search-form">
              <form action="/search" method="get">
                <div className="input-wrap">
                  <input className="search-input" type="search" name="query" placeholder="지역, 음식 또는 식당명 입력" maxLength="255" autoComplete="off" />
                  <button type="submit" className="btn-search">
                    <img src="https://github.com/user-attachments/assets/19865e59-1076-4b33-ae6a-9cfbd7b5bbb2" alt="검색버튼" />
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </header>

      <div className="header-container">
        <h1 className="h1">👍 내가 좋아요한 가게</h1>
      </div>

      <main className="search_menu">
        <div className="restaurant_list">
          {likedShops.length === 0 ? (
            <h2 style={{ textAlign: 'center', fontSize: '5em', fontWeight: 'bold', color: '#333' }}>텅.</h2>
          ) : (
            likedShops.map((shop, idx) => (
              <div className="restaurant_item" key={idx}>
                <div className="restaurant_info">
                  <a href={`/shopDetails/${shop.id}`}>
                    <span className="restaurant_name">{shop.name}</span>
                  </a>
                  <div className="like-section">
                    <img className="heart-icon" src="https://cdn-icons-png.flaticon.com/512/833/833472.png" alt="Heart Icon" />
                    <span className="restaurant_like">{shop.likeCount}</span>
                  </div>
                  <span className="restaurant_address">{shop.address}</span> <br />
                  <span className="restaurant_addressinfo">{shop.addressInfo}</span>
                </div>

                <div className="restaurant_image">
                  <a href={`/shopDetails/${shop.id}`}>
                    <img src={shop.imgUrl} alt="가게이미지" />
                  </a>
                </div>
              </div>
            ))
          )}
        </div>
      </main>
    </div>
  );
}
