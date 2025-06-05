import React, { useEffect, useState } from 'react';
import { fetchMainPageData } from '../service/MainApi';
import { useNavigate, Link } from 'react-router-dom';
import './css/Main.css';

const Main = () => {
  const [taggedShops, setTaggedShops] = useState({});
  const [activeTags, setActiveTags] = useState([]);
  const [campusFilter, setCampusFilter] = useState({ gajwa: true, chilam: true });
  const [searchTerm, setSearchTerm] = useState('');
  const [showCampusBanner, setShowCampusBanner] = useState(true);
  const [searchInput, setSearchInput] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchMainPageData().then((data) => {
      console.log("📦 받아온 taggedShops 데이터:", data); // ← 여기에서 확인
      setTaggedShops(data.taggedShops || {});
    });
  }, []);
  

  const toggleTag = (tag) => {
    setActiveTags((prev) =>
      prev.includes(tag) ? prev.filter((t) => t !== tag) : [...prev, tag]
    );
  };

  const toggleCampus = (campus) => {
    setCampusFilter((prev) => ({
      ...prev,
      [campus]: !prev[campus],
    }));
  };


  const handleSubmit = (e) => {
    e.preventDefault();
    if (searchInput.trim()) {
      navigate(`/search?query=${encodeURIComponent(searchInput.trim())}`);
    }
  };

  const filteredSections = Object.entries(taggedShops)
    .filter(([tag]) => activeTags.length === 0 || activeTags.includes(tag))
    .filter(([_, value]) => value && value.shops);

    const filteredShops = (shops, tagName) =>
      shops.filter((shop) => {
        const isChilam = shop.restId?.startsWith('C');
        const campus = isChilam ? 'chilam' : 'gajwa';
        const campusMatch = campusFilter[campus];
        const tagMatch =
          activeTags.length === 0 || activeTags.includes(tagName);
        return campusMatch && tagMatch;
      });
    

  useEffect(() => {
    console.log('캠퍼스 상태 변경됨:', campusFilter);
  }, [campusFilter]);



  return (
    <div id="root">
      <header className="header-content">
        <div className="common-desk-header">
          <div className="header-wrap">
            <div className="search-form">
              <form onSubmit={handleSubmit}>  {/* ✅ onSubmit 추가 */}
                <div className="input-wrap">
                  <input
                    className="search-input"
                    type="search"
                    placeholder="메뉴 또는 식당명 입력"
                    maxLength={255}
                    autoComplete="off"
                    value={searchInput}
                    onChange={(e) => setSearchInput(e.target.value)}
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

      <div className="banner-content-wrap">
        {showCampusBanner && (
          <div className="top-nav-key-visual">
            <div className="image-container">
              <div
                className={`image-wrapper left-image ${!campusFilter.gajwa ? 'blurred' : ''}`}
                onClick={() => toggleCampus('gajwa')}
                style={{ pointerEvents: 'auto' }}
              >
                <img src="https://www.gnu.ac.kr/upload/main/na/bbs_1047/ntt_2258160/img_796b61c4-e42a-44bc-8dff-4887eaa1c37f1730876309843.jpg" alt="가좌캠퍼스" />
                <p className="campus-text">가좌캠퍼스</p>
              </div>
              <div
                className={`image-wrapper right-image ${!campusFilter.chilam ? 'blurred' : ''}`}
                onClick={() => toggleCampus('chilam')}
                style={{ pointerEvents: 'auto' }}
              >
                <img src="https://www.gnu.ac.kr/common/nttEditorImgView.do?imgKey=96b1e7e4b113c43914996108683bca1b" alt="칠암캠퍼스" />
                <p className="campus-text">칠암캠퍼스</p>
              </div>
            </div>
          </div>
        )}
        <div className="campus-toggle-bar" onClick={() => setShowCampusBanner(!showCampusBanner)}>
          <p>
            {showCampusBanner
              ? '원하지 않는 캠퍼스를 숨길 수 있어요'
              : '캠퍼스를 다시 보고 싶다면 여기를 클릭하세요'}
            <span>{showCampusBanner ? '▲' : '▼'}</span>
          </p>
        </div>
      </div>
      <div className="tag-container">
        <div className="tag-search">
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="태그 검색..."
            className="tag-search-input"
          />
        </div>
        <div className="tag-list">
          <button className="tag-item active" onClick={() => setActiveTags([])}>
            모두보기
          </button>
          {Object.keys(taggedShops)
            .filter((tag) => tag.toLowerCase().includes(searchTerm.toLowerCase()))
            .map((tag) => (
              <button
                key={tag}
                className={`tag-item ${activeTags.includes(tag) ? 'active' : ''}`}
                onClick={() => toggleTag(tag)}
              >
                {tag}
              </button>
            ))}
        </div>
      </div>

      <main className="content">
        <div className="body-box">
          {filteredSections.map(([tag, info]) => (
            <section key={tag} className="card-section" data-tag={tag}>
              <div className="card-section-title-box">
                <h2 className="card-section-title">
                  <Link to={`/search/${encodeURIComponent(tag)}`}>{tag}</Link>
                </h2>
              </div>
              <div className="card-slide-wrap">
                <ul className="slide-card-list">
                  {filteredShops(info.shops, tag).slice(0, 100).map((shop) => (
                    <li key={shop.id} className="slide-card-item">
                      <Link to={`/foodDetails/${shop.id}`}>
                        <img src={shop.imgUrl} alt={shop.name} loading="lazy" />
                        <p className="card-rest-name">{shop.name}</p>
                      </Link>

                      <div className="image-btn-container">
                        <div className="like-section">
                          <button className="like-btn">
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
                  ))}
                </ul>
              </div>
            </section>
          ))}
          <Link to="/ask" className="fixed-button">
            <img src="https://www.gnu.ac.kr/images/web/main/sub_cnt/bs03.png" alt="캐릭터 기본형" width="50" height="50" />
          </Link>
        </div>
      </main>

      <div className="bottom-nav-wrapper">
        <nav className="bottom-nav">
          <div className="nav-container">
            <Link to="/board/main" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M19 3H5C3.89543 3 3 3.89543 3 5V19C3 20.1046 3.89543 21 5 21H19C20.1046 21 21 20.1046 21 19V5C21 3.89543 20.1046 3 19 3Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M3 9H21" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M9 21V9" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
              <span>매거진</span>
            </Link>

            <Link to="/map" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <polygon points="3 6 9 3 15 6 21 3 21 18 15 21 9 18 3 21 3 6" />
                <line x1="9" y1="3" x2="9" y2="18" />
                <line x1="15" y1="6" x2="15" y2="21" />
              </svg>
              <span>지도</span>
            </Link>

            <Link to="/" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M3 9.5L12 3l9 6.5V21a1 1 0 0 1-1 1h-5a1 1 0 0 1-1-1v-5H10v5a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5z" />
              </svg>
              <span>홈</span>
            </Link>

            <Link to="/myPage/bookmarkList" className="nav-item">
              <svg class="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none"
                stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M3 7h5l2 3h11v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7z" />
              </svg>
              <span>보관함</span>
            </Link>

            <Link to="/myPage" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <circle cx="12" cy="7" r="4" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
              <span>마이</span>
            </Link>
          </div>
        </nav>
      </div>
    </div>
  );
};

export default Main;
