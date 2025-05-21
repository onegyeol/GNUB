import React, { useEffect, useState } from 'react';
import { fetchTaggedShops } from '../service/MainApi';
import { useNavigate } from 'react-router-dom';
import './css/Main.css';

const Main = () => {
  const [taggedShops, setTaggedShops] = useState({});
  const [uniqueShopCount, setUniqueShopCount] = useState(0);
  const [tagSearch, setTagSearch] = useState('');
  const [activeTags, setActiveTags] = useState([]);
  const [campusFilter, setCampusFilter] = useState({ gajwa: true, chilam: true });
  const navigate = useNavigate();

  useEffect(() => {
    fetchTaggedShops().then(data => {
      console.log('✅ fetchTaggedShops 응답:', data); // 응답 전체 확인
      setTaggedShops(data.taggedShops);
      setUniqueShopCount(data.uniqueShopCount);
    });
  }, []);
  
  const toggleTag = (tag) => {
    setActiveTags(prev =>
      prev.includes(tag) ? prev.filter(t => t !== tag) : [...prev, tag]
    );
  };

  const isTagVisible = (tag) => {
    return activeTags.length === 0 || activeTags.includes(tag);
  };

  const toggleCampus = (campus) => {
    setCampusFilter(prev => ({ ...prev, [campus]: !prev[campus] }));
  };

  const isCampusVisible = (restId) => {
    return restId.startsWith('C') ? campusFilter.chilam : campusFilter.gajwa;
  };

  return (
    <div id="root">
      <header className="header-content">
        <div className="common-desk-header">
          <div className="header-wrap">
            <div className="search-form">
              <form>
                <div className="input-wrap">
                  <input
                    className="search-input"
                    type="search"
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

      <div className="banner-box" onClick={() => { }}>
        <p className="banner-content">클릭하면 원하는 캠퍼스를 선택할 수 있어용</p>
      </div>

      <div className="top-nav-content">
        <div className="image-container">
          <div className={`image-wrapper ${!campusFilter.gajwa ? 'blurred' : ''}`} onClick={() => toggleCampus('gajwa')}>
            <img src="https://www.gnu.ac.kr/upload/main/na/bbs_1047/ntt_2258160/img_796b61c4-e42a-44bc-8dff-4887eaa1c37f1730876309843.jpg" alt="가좌캠퍼스" />
            <p className="campus-text">가좌캠퍼스</p>
          </div>
          <div className={`image-wrapper ${!campusFilter.chilam ? 'blurred' : ''}`} onClick={() => toggleCampus('chilam')}>
            <img src="https://www.gnu.ac.kr/common/nttEditorImgView.do?imgKey=96b1e7e4b113c43914996108683bca1b" alt="칠암캠퍼스" />
            <p className="campus-text">칠암캠퍼스</p>
          </div>
        </div>
      </div>

      <div className="tag-container">
        <div className="tag-search">
          <input
            type="text"
            placeholder="태그 검색..."
            className="tag-search-input"
            value={tagSearch}
            onChange={e => setTagSearch(e.target.value)}
          />
        </div>
        <div className="tag-list">
          <button className={`tag-item ${activeTags.length === 0 ? 'active' : ''}`} onClick={() => setActiveTags([])}>모두보기</button>
          {Object.entries(taggedShops)
            .filter(([tag]) => tag.toLowerCase().includes(tagSearch.toLowerCase()))
            .map(([tag]) => (
              <button
                key={tag}
                className={`tag-item ${activeTags.includes(tag) ? 'active' : ''}`}
                onClick={() => toggleTag(tag)}>
                {tag}
              </button>
            ))}
        </div>
      </div>

      <main className="content">
        <div className="body-box">
          {Object.entries(taggedShops)
            .filter(([tag]) => isTagVisible(tag))
            .map(([tag, info]) => (
              <section key={tag} className="card-section">
                <div className="card-section-title-box">
                  <h2 className="card-section-title">{tag}</h2>
                  <span className="card-section-rest-cnt">({uniqueShopCount})</span>
                </div>
                <div className="card-slide-wrap">
                  <ul className="slide-card-list">
                    {info?.filter(shop => isCampusVisible(shop.restId))
                      .slice(0, 100)
                      .map(shop => (
                        <li key={shop.id} className="slide-card-item">
                          <a href={`/shopDetails/${shop.id}`}>
                            <img src={shop.imgUrl} alt={shop.name} />
                            <p className="card-rest-name">{shop.name}</p>
                          </a>
                          <div className="image-btn-container">
                            <div className="like-section">
                              <button className="like-btn">
                                <img className="like-icon" src="https://cdn-icons-png.flaticon.com/512/833/833472.png" alt="Heart Icon" />
                              </button>
                              <p className="like-count">{shop.likeCount}</p>
                            </div>
                          </div>
                        </li>
                      ))}
                  </ul>
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
              <span>매거진</span>
            </a>
            <a href="/" className="nav-item">
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
};

export default Main;
