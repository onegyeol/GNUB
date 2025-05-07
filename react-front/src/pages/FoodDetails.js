// src/pages/FoodDetails.js

import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchShopDetails } from '../service/ShopApi';
import BookmarkButton from '../components/BookmarkButton';
import LikeButton from '../components/LikeButton';
import GoogleMapView from '../components/GoogleMapView';
import './css/FoodDetails.css';

const FoodDetailsPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [shop, setShop] = useState(null);
  const [shopMenus, setShopMenus] = useState([]);
  const [isBookmarked, setIsBookmarked] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/search?query=${encodeURIComponent(searchQuery.trim())}`);
    }
  };

  useEffect(() => {
    fetchShopDetails(id).then(data => {
      console.log('📦 API 응답:', data);
      setShop(data.shop);
      setShopMenus(data.menus);
      setIsBookmarked(data.isBookmarked);
      setIsLoggedIn(data.isLoggedIn);
    });
  }, [id]);

  if (!shop) return <div>로딩 중...</div>;

  return (
    <div id="root">
      <header className="header-content">
        <div className="common-desk-header">
          <div className="header-wrap">
            <div className="search-form">
              <form onSubmit={handleSearchSubmit}>
                <div className="input-wrap">
                  <input
                    className="search-input"
                    type="search"
                    id="query"
                    name="query"
                    placeholder="지역, 음식 또는 식당명 입력"
                    maxLength={255}
                    autoComplete="off"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                  />
                  <button type="submit" className="btn-search">
                    <img src="https://github.com/user-attachments/assets/19865e59-1076-4b33-ae6a-9cfbd7b5bbb2" alt="검색버튼" />
                  </button>
                </div>
              </form>
            </div>
            <div className="auth-Box"></div>
          </div>
        </div>
      </header>

      <div className="restaurant-container">
        <div className="restaurant-card first-card">
          <h1 className="restaurant-name">{shop.name}</h1>

          <BookmarkButton
            shopId={shop.id}
            isBookmarked={isBookmarked}
            isLoggedIn={isLoggedIn}
            onToggle={setIsBookmarked}
          />

          <div className="map-container">
            <GoogleMapView lat={shop.lat} lng={shop.lng} />
          </div>

          <p className="restaurant-info">
            <img src="https://github.com/user-attachments/assets/35def7f7-a86e-43d0-905c-675c4212c54f" alt="주소 이모티콘" />
            {shop.address}
          </p>

          <p className="restaurant-info">
            <img src="https://github.com/user-attachments/assets/ed0707e8-ae3e-44ec-ab96-175a1c9d088b" alt="전화기 이모티콘" />
            {shop.number}
          </p>

          <LikeButton shopId={shop.id} />
        </div>

        <div className="container">
          <div className="restaurant-card">
            <h2>
              <img src="https://github.com/user-attachments/assets/9b60d89e-c39c-4fca-ae68-78b49d267127" alt="영업시간 이모티콘" />
              영업 시간
            </h2>
            <br/>

            {!shop.tue && !shop.wed && !shop.thu && !shop.fri && !shop.sat && !shop.sun ? (
              <div>{shop.mon}</div>
            ) : (
              <div className="business-hours">
                <p><b>월요일 :</b> {shop.mon || '휴무'}</p>
                <p><b>화요일 :</b> {shop.tue || shop.mon || '휴무'}</p>
                <p><b>수요일 :</b> {shop.wed || shop.mon || '휴무'}</p>
                <p><b>목요일 :</b> {shop.thu || shop.mon || '휴무'}</p>
                <p><b>금요일 :</b> {shop.fri || shop.mon || '휴무'}</p>
                <p><b>토요일 :</b> {shop.sat || shop.mon || '휴무'}</p>
                <p><b>일요일 :</b> {shop.sun || shop.mon || '휴무'}</p>
              </div>
            )}

            <br/> <br/>
            <h2>
              <img src="https://github.com/user-attachments/assets/7d9701cc-3c84-4f7d-9404-0af56f2afc16" alt="메뉴 이모티콘" />
              메뉴
            </h2>

            <div className="menu-section">
              <ul className="menu-grid">
                {shopMenus.slice(0, isExpanded ? shopMenus.length : 4).map(menu => (
                  <li key={menu.id} className="menu-item">
                    <span className="menu-name">{menu.menuName}</span>
                    <span className="dots"></span>
                    <span className="menu-price">{menu.price}</span>
                  </li>
                ))}
              </ul>

              {shopMenus.length > 4 && (
                <div className="more-menu-btn" onClick={() => setIsExpanded(!isExpanded)}>
                  <span>{isExpanded ? '접기' : '더보기'}</span>
                  <span>{isExpanded ? '▲' : '▼'}</span>
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="restaurant-card">
          <h2>음식 사진 </h2>
          <div className="image-gallery">
            {shopMenus.map(menu => (
              <div key={menu.id} className="menu-image">
                <img src={menu.imgUrl} alt={menu.menuName} />
                <p>{menu.menuName}</p>
              </div>
            ))}
          </div>
        </div>

        <div className="bottom-nav-wrapper">
        <nav className="bottom-nav">
          <div className="nav-container">
            <a href="/board/main" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M19 3H5C3.89543 3 3 3.89543 3 5V19C3 20.1046 3.89543 21 5 21H19C20.1046 21 21 20.1046 21 19V5C21 3.89543 20.1046 3 19 3Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M3 9H21" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M9 21V9" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
              <span>매거진</span>
            </a>
            <a href="/main" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M3 9.5L12 3l9 6.5V21a1 1 0 0 1-1 1h-5a1 1 0 0 1-1-1v-5H10v5a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5z" />
              </svg>
              <span>홈</span>
            </a>
            <a href="/map" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <polygon points="3 6 9 3 15 6 21 3 21 18 15 21 9 18 3 21 3 6" />
                <line x1="9" y1="3" x2="9" y2="18" />
                <line x1="15" y1="6" x2="15" y2="21" />
              </svg>
              <span>지도</span>
            </a>
            <a href="/myPage" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <circle cx="12" cy="7" r="4" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
              <span>마이</span>
            </a>
          </div>
        </nav>
      </div>
      </div>
    </div>
  );
};

export default FoodDetailsPage;
