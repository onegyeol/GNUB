import React, { useEffect, useState } from 'react';
import { fetchBookmarks } from '../service/MypageApi';
import { deleteFolder } from '../service/BookmarkApi'; 
import './css/BookmarkList.css';

export default function BookmarkList() {
  const [groupedBookmarks, setGroupedBookmarks] = useState({});

  useEffect(() => {
    fetchBookmarks()
      .then(data => {
        console.log("✅ fetchBookmarks 성공:", data);
        setGroupedBookmarks(data);
      })
      .catch(err => {
        console.error("❌ fetchBookmarks 에러:", err);
      });
  }, []);
  
  const handleDeleteFolder = (folderName) => {
    if (!window.confirm(`'${folderName}' 폴더를 정말 삭제할까요?`)) {
      return;
    }
  
    deleteFolder(folderName)
      .then(() => {
        alert('폴더 삭제 완료!');
        setGroupedBookmarks(prev => {
          const updated = { ...prev };
          delete updated[folderName];
          return updated;
        });
        
      })
      .catch(err => {
        console.error('❌ 폴더 삭제 실패:', err);
        alert('폴더 삭제에 실패했습니다.');
      });
  };
  

  const renderBookmarkCards = (bookmarks) => (
    <ul className="slide-card-list">
      {bookmarks.map((bookmark, idx) => (
        <li className="slide-card-item" key={idx}>
          <a href={`/shopDetails/${bookmark.shopId}`}>
            <img src={bookmark.shopImgUrl} alt={bookmark.shopName} loading="lazy" />
            <p className="card-rest-name">{bookmark.shopName}</p>
          </a>
          <div className="image-btn-container">
            <div className="like-section">
              <form action="/toggleLike" method="POST">
                <input type="hidden" name="shopId" value={bookmark.shopId} />
                <button type="submit" className="like-btn">
                  <img className="like-icon" src="https://cdn-icons-png.flaticon.com/512/833/833472.png" alt="Heart Icon" />
                </button>
              </form>
              <p className="number-display">{bookmark.likeCount}</p>
            </div>
          </div>
        </li>
      ))}
    </ul>
  );


  return (
    <div id="root">
      <header className="header-content">
        <div className="common-desk-header">
          <div className="header-wrap">
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
        <h1 className="h1">🏠 내가 저장한 가게</h1>
      </div>

      <main className="content">
        <div className="body-box">
          {Object.entries(groupedBookmarks).map(([folder, bookmarks], idx) => (
            <section className="card-section" key={idx}>
              <div className="card-section-wrap">
                <div className="card-section-title-box">
                  <h2 className="card-section-title">
                    {folder === 'null' ? '저장한 음식점들' : `${folder} (${bookmarks.length})`}
                  </h2>
                  {folder !== 'null' && (
                    <button className="toggle-btn" style={{ marginLeft: '10px' }} onClick={() => handleDeleteFolder(folder)} >
                      삭제
                    </button>
                  )}
                </div>
                <div className="card-slide-wrap">
                  {renderBookmarkCards(bookmarks)}
                </div>
              </div>
            </section>
          ))}
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
