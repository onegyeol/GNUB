import React, { useEffect, useState } from 'react';
import { fetchCategorizedShops } from '../service/MainApi';
import { toggleLike } from '../service/LikeApi';
import './css/Main.css';

export default function MainPage() {
  const [showCampusImages, setShowCampusImages] = useState(false);
  const [tagSearch, setTagSearch] = useState('');
  const [categorizedShops, setCategorizedShops] = useState({});

  const handleLikeToggle = async (shopId, category) => {
    console.log("📍 shopId 확인:", shopId);

    try {
      const data = await toggleLike(shopId); // axios 함수 사용

      setCategorizedShops(prev => {
        const updated = { ...prev };
        updated[category] = updated[category].map(shop => {

          return shop.shopId === shopId
            ? { ...shop, likeCount: data.likeCount }
            : shop;
        });

        return updated;
      });
    } catch (err) {
      if (err.response?.status === 401) {
        window.location.href = "/member/login";
      } else {
        console.error("좋아요 처리 실패:", err);
      }
    }
  };


  const toggleCampusView = () => {
    setShowCampusImages(prev => !prev);
  };

  useEffect(() => {
    fetchCategorizedShops()
      .then(data => {
        setCategorizedShops(data);
      })
      .catch(error => console.error('가게 데이터 불러오기 실패:', error));
  }, []);

  useEffect(() => {
    const adjustLayout = () => {
      const header = document.querySelector('.header-content');
      const banner = document.querySelector('.banner-box');
      const topNavContent = document.querySelector('.top-nav-content');
      if (header && banner) {
        banner.style.marginTop = `${header.offsetHeight}px`;
        if (topNavContent) {
          topNavContent.style.marginTop = showCampusImages ? `-${banner.offsetHeight}px` : '0';
        }
      }
    };
    adjustLayout();
    window.addEventListener('resize', adjustLayout);
    return () => window.removeEventListener('resize', adjustLayout);
  }, [showCampusImages]);

  return (
    <div id="root">
      <header className="header-content">
        <div className="common-desk-header">
          <div className="header-wrap">
            <div className="search-form">
              <form action="/search" method="get">
                <div className="input-wrap">
                  <input
                    className="search-input"
                    type="search"
                    id="query"
                    name="query"
                    placeholder="지역, 음식 또는 식당명 입력"
                    maxLength={255}
                    autoComplete="off"
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

      <div className="banner-box" onClick={toggleCampusView}>
        <div className="Banner-Wrap">
          <p className="Banner-Content">
            클릭하면 원하는 캠퍼스를 선택할 수 있어용
          </p>
        </div>
      </div>

      {showCampusImages && (
        <div className="top-nav-content">
          <div className="top-nav-key-visual">
            <div className="image-container">
              <div className="image-wrapper left-image">
                <img src="https://www.gnu.ac.kr/upload/main/na/bbs_1047/ntt_2258160/img_796b61c4-e42a-44bc-8dff-4887eaa1c37f1730876309843.jpg" alt="가좌캠퍼스" />
                <p className="campus-text">가좌캠퍼스</p>
              </div>
              <div className="image-wrapper right-image">
                <img src="https://www.gnu.ac.kr/common/nttEditorImgView.do?imgKey=96b1e7e4b113c43914996108683bca1b" alt="칠암캠퍼스" />
                <p className="campus-text">칠암캠퍼스</p>
              </div>
            </div>
          </div>
        </div>
      )}

      <div className="tag-container">
        <div className="tag-search">
          <input
            type="text"
            id="tagSearchInput"
            placeholder="태그 검색..."
            className="tag-search-input"
            value={tagSearch}
            onChange={(e) => setTagSearch(e.target.value)}
          />
        </div>
        <div className="tag-list">
          {/* 태그 리스트는 백엔드 데이터 연결 후 map으로 동적 처리 예정 */}
        </div>
      </div>

      <main className="content">
        <div className="body-box">
          {Object.entries(categorizedShops).map(([category, shops]) => (
            <section className="card-section" key={category} id={`category-${category}`}>
              <div className="card-section-wrap">
                <div className="card-section-title-box">
                  <h2 className="card-section-title">
                    {category}
                    <span className="card-section-rest-cnt">({shops.length})</span>
                  </h2>
                </div>
                <div className="card-slide-wrap">
                  <ul className="slide-card-list">
                    {shops.map((shop) => {
                      return (
                        <li className="slide-card-item" key={shop.id}>
                          <a href={`/shopDetails/${shop.id}`}>
                            <img src={shop.imgUrl} alt="" loading="lazy" />
                            <p className="card-rest-name">{shop.name}</p>
                          </a>
                          <div className="image-btn-container">
                            <div className="like-section">
                              <button
                                type="button"
                                className="like-btn"
                                onClick={() => handleLikeToggle(shop.id, category)}
                              >
                                <img
                                  className="like-icon"
                                  src="https://cdn-icons-png.flaticon.com/512/833/833472.png"
                                  alt="Heart Icon"
                                />
                              </button>
                              <p className="like-count">{shop.likeCount}</p>
                            </div>
                          </div>
                        </li>
                      );
                    })}

                  </ul>
                </div>
              </div>
            </section>
          ))}

          <a href="/ask" className="fixed-button">
            <img src="https://www.gnu.ac.kr/images/web/main/sub_cnt/bs03.png" alt="캐릭터 기본형" width="50" height="50" />
          </a>
        </div>
      </main>

      <div className="bottom-nav-wrapper">
        <nav className="bottom-nav">
          <div className="nav-container">
            <a href="/board/main" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M19 3H5C3.89543 3 3 3.89543 3 5V19C3 20.1046 3.89543 21 5 21H19C20.1046 21 21 20.1046 21 19V5C21 3.89543 20.1046 3 19 3Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M3 9H21" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M9 21V9" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
              <span>게시판</span>
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
  );
}
