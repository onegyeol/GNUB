import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { fetchSearchResults } from '../service/SearchApi';
import { useNavigate, Link } from 'react-router-dom';
import './css/Search.css';

const SearchPage = () => {
    const [searchParams] = useSearchParams();
    const [shops, setShops] = useState([]);
    const [query, setQuery] = useState('');
    const navigate = useNavigate(); // 추가
    const [searchQuery, setSearchQuery] = useState(''); // 추가

    // 검색 제출 함수
    const handleSearchSubmit = (e) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            navigate(`/search?query=${encodeURIComponent(searchQuery.trim())}`);
        }
    };

    useEffect(() => {
        const q = searchParams.get('query');
        console.log('🔍 쿼리:', q); // 콘솔에 출력되는가?
        if (q) {
            setQuery(q);
            fetchSearchResults(q)
                .then(data => {
                    setShops(data.shops);
                })
                .catch(err => {
                    console.error('검색 에러:', err);
                    setShops([]);
                });
        } else {
            setQuery('');
            setShops([]);
        }
    }, [searchParams]);

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

            <p className="search-result-text">
                {query ? `"${query}" 검색결과` : '검색 결과'}
            </p>

            <main className="search_menu">
                <div className="restaurant_list">
                    {shops.length > 0 ? (
                        shops.map(shop => (
                            <div className="restaurant_item" key={shop.id}>
                                <div className="restaurant_info">
                                    <a href={`/shopDetails/${shop.id}`}>
                                        <span className="restaurant_name">{shop.name}</span>
                                    </a>
                                    <div className="like-section">
                                        <img className="heart-icon" src="https://cdn-icons-png.flaticon.com/512/833/833472.png" alt="Heart" />
                                        <span className="restaurant_like">{shop.likeCount}</span>
                                    </div>
                                    <div>{shop.address}</div>
                                </div>
                                <div className="search-result">
                                    <div className="restaurant_image">
                                        <a href={`/shopDetails/${shop.id}`}>
                                            <img src={shop.imgUrl} alt="가게 이미지" />
                                        </a>
                                    </div>
                                </div>

                            </div>
                        ))
                    ) : (
                        <p style={{ textAlign: 'center', color: '#888' }}>검색 결과가 없습니다.</p>
                    )}
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

                        <Link to="/main" className="nav-item">
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

export default SearchPage;
