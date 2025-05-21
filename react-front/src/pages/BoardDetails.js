import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useSearchParams, Link } from 'react-router-dom';
import { fetchBoardById } from '../service/BoardApi';
import './css/BoardDetails.css'; // 스타일 파일(optional)

const BoardDetails = () => {
  const { id } = useParams();
  const [board, setBoard] = useState(null);
  const [searchParams, setSearchParams] = useSearchParams();
  const query = searchParams.get('query') || '';
  const sort = searchParams.get('sort') || 'created';
  const [searchQuery, setSearchQuery] = useState(query);

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    setSearchParams({ query: searchQuery, sort, page: 1 });
};

  useEffect(() => {
    fetchBoardById(id)
      .then(data => setBoard(data))
      .catch(error => console.error(error));
  }, [id]);

  const handleSortChange = (e) => {
    searchParams.set('sort', e.target.value);
    setSearchParams(searchParams);
};

  const handleSearch = (e) => {
    e.preventDefault();
    const form = e.target;
    const newQuery = form.query.value;
    setSearchParams({ query: newQuery, sort, page: 1 });
  };

  if (!board) return <div>📄 게시글을 불러오는 중입니다...</div>;

  return (
    <div id='root'>
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
                    value={searchQuery} // ✅ 상태 바인딩
                    onChange={(e) => setSearchQuery(e.target.value)} // ✅ 입력 반영
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

      <div className="board-details">
        <h1>{board.title}</h1>
        <p><strong>작성자:</strong> {board.authorName}</p>
        <p><strong>작성일:</strong> {board.boardCreatedTime}</p>
        <p><strong>조회수:</strong> {board.boardHits}</p>
        <hr />
        <div className="board-content" dangerouslySetInnerHTML={{ __html: board.content }} />
      </div>

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

            <Link to="/" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M3 9.5L12 3l9 6.5V21a1 1 0 0 1-1 1h-5a1 1 0 0 1-1-1v-5H10v5a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5z" />
              </svg>
              <span>홈</span>
            </Link>

            <Link to="/map" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <polygon points="3 6 9 3 15 6 21 3 21 18 15 21 9 18 3 21 3 6" />
                <line x1="9" y1="3" x2="9" y2="18" />
                <line x1="15" y1="6" x2="15" y2="21" />
              </svg>
              <span>지도</span>
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

export default BoardDetails;
