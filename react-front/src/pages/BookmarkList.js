import React, { useEffect, useState } from 'react';
import { fetchBookmarks } from '../services/MypageApi';
import './css/BookmarkList.css';

export default function BookmarkList() {
  const [groupedBookmarks, setGroupedBookmarks] = useState({});

  useEffect(() => {
    fetchBookmarks().then(data => setGroupedBookmarks(data));
  }, []);

  const renderBookmarkCards = (bookmarks) => (
    <ul className="slide-card-list">
      {bookmarks.map((bookmark, idx) => (
        <li className="slide-card-item" key={idx}>
          <a href={`/shopDetails/${bookmark.shop.id}`}>
            <img src={bookmark.shop.imgUrl} alt={bookmark.shop.name} loading="lazy" />
            <p className="card-rest-name">{bookmark.shop.name}</p>
          </a>
          <div className="image-btn-container">
            <div className="like-section">
              <form action="/toggleLike" method="POST">
                <input type="hidden" name="shopId" value={bookmark.shop.id} />
                <button type="submit" className="like-btn">
                  <img className="like-icon" src="https://cdn-icons-png.flaticon.com/512/833/833472.png" alt="Heart Icon" />
                </button>
              </form>
              <p className="number-display">{bookmark.shop.likeCount}</p>
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
                    <form action="/bookmarks/folder/delete" method="post" style={{ marginLeft: '10px' }}>
                      <input type="hidden" name="folderName" value={folder} />
                      <button type="submit" className="toggle-btn">삭제</button>
                    </form>
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
    </div>
  );
}
