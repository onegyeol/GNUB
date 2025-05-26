import React, { useEffect, useState } from 'react';
import { fetchSearchResults, fetchTagSearchResults } from '../service/SearchApi';
import { useNavigate, Link, useParams, useSearchParams } from 'react-router-dom';
import './css/Search.css';

const SearchPage = () => {
    const [searchParams] = useSearchParams();
    const [shops, setShops] = useState([]);
    const [query, setQuery] = useState('');
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState('');
    const [searchTerm, setSearchTerm] = useState('');
    const [showCampusBanner, setShowCampusBanner] = useState(true);
    const [activeTags, setActiveTags] = useState([]);
    const [campusFilter, setCampusFilter] = useState({ gajwa: true, chilam: true });
    const { tag } = useParams();
    const [isLoading, setIsLoading] = useState(false);

    const handleSearchSubmit = (e) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            navigate(`/search?query=${encodeURIComponent(searchQuery.trim())}`);
        }
    };

    const toggleCampus = (campus) => {
        setCampusFilter((prev) => ({ ...prev, [campus]: !prev[campus] }));
    };

    const filteredShops = (shops) =>
        shops.filter((shop) => {
            const isChilam = shop.restId?.startsWith('C');
            const campus = isChilam ? 'chilam' : 'gajwa';
            const campusMatch = campusFilter[campus];
            const tagMatch =
                activeTags.length === 0 ||
                (shop.tags && shop.tags.some((tag) => activeTags.includes(tag)));
            const menuMatch = true;
            return campusMatch && tagMatch && menuMatch;
        });

    useEffect(() => {
        const q = searchParams.get('query');
        const menuFilter = searchParams.get('menu');
        const target = q || tag;

        if (target) {
            setQuery(target);
            setSearchQuery(target);
            setSearchTerm(menuFilter || '');
            setIsLoading(true); // 검색 시작

            const fetchData = q ? fetchSearchResults : fetchTagSearchResults;

            fetchData(target)
                .then(data => {
                    let resultShops = data.shops;
                    if (menuFilter) {
                        resultShops = resultShops.filter(shop =>
                            (shop.shopMenus || []).some(menu =>
                                menu.menuName && menu.menuName.includes(menuFilter)
                            )
                        );
                    }
                    setShops(resultShops);
                })
                .catch(err => {
                    console.error('검색 에러:', err);
                    setShops([]);
                })
                .finally(() => setIsLoading(false)); // 검색 완료
        } else {
            setQuery('');
            setShops([]);
        }
    }, [searchParams, tag]);


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
                                        value={searchQuery}
                                        onChange={(e) => setSearchQuery(e.target.value)}
                                        placeholder="메뉴 또는 식당명 입력"
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

            <div className="banner-content-wrap">
                {showCampusBanner && (
                    <div className="top-nav-key-visual">
                        <div className="image-container">
                            <div
                                className={`image-wrapper left-image ${!campusFilter.gajwa ? 'blurred' : ''}`}
                                onClick={() => toggleCampus('gajwa')}
                            >
                                <img src="https://www.gnu.ac.kr/upload/main/na/bbs_1047/ntt_2258160/img_796b61c4-e42a-44bc-8dff-4887eaa1c37f1730876309843.jpg" alt="가좌캠퍼스" />
                                <p className="campus-text">가좌캠퍼스</p>
                            </div>
                            <div
                                className={`image-wrapper right-image ${!campusFilter.chilam ? 'blurred' : ''}`}
                                onClick={() => toggleCampus('chilam')}
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

            <p className="search-result-text">
                {query ? `"${query}" 검색결과` : '검색 결과'}
            </p>

            <div className="tag-container">
                <form
                    onSubmit={(e) => {
                        e.preventDefault();
                        const base = tag || '전체';
                        const menu = searchTerm.trim();
                        if (menu) {
                            fetchTagSearchResults(base, menu)
                                .then((data) => {
                                    setShops(data.shops || []);
                                })
                                .catch((err) => {
                                    console.error('검색 실패:', err);
                                    setShops([]);
                                });
                        }
                    }}
                >
                    <input
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder="메뉴명을 입력하세요 (예: 국밥)"
                        className="tag-search-input"
                    />
                </form>
            </div>


            <main className="search_menu">
                <div className="restaurant_list">
                    {isLoading ? (
                        <p style={{ textAlign: 'center', color: '#666' }}>🔍 검색 중...</p>
                    ) : filteredShops(shops).length > 0 ? (
                        filteredShops(shops).map(shop => (
                            <div className="restaurant_item" key={shop.id}>
                                <div className="restaurant_info">
                                    <Link to={`/foodDetails/${shop.id}`}>
                                        <span className="restaurant_name">{shop.name}</span>
                                    </Link>
                                    <div className="like-section">
                                        <img className="heart-icon" src="https://cdn-icons-png.flaticon.com/512/833/833472.png" alt="Heart Icon" />
                                        <span className="restaurant_like">{shop.likeCount}</span>
                                    </div>
                                    <div>{shop.address}</div>
                                    <div className="restaurant_tags">
                                        {(shop.tags || []).map((tag, index) => (
                                            <Link key={index} to={`/search/${tag}`} className="restaurant_tag">
                                                {tag}
                                            </Link>
                                        ))}
                                    </div>
                                </div>
                                <div className="restaurant_image">
                                    <Link to={`/foodDetails/${shop.id}`}>
                                        <img src={shop.imgUrl} alt="가게 이미지" />
                                    </Link>
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
                            <svg className="nav-icon" viewBox="0 0 24 24" fill="none">
                                <path d="M19 3H5C3.895 3 3 3.895 3 5v14c0 1.105.895 2 2 2h14c1.105 0 2-.895 2-2V5c0-1.105-.895-2-2-2Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                                <path d="M3 9h18" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                                <path d="M9 21V9" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                            </svg>
                            <span>매거진</span>
                        </Link>
                        <Link to="/" className="nav-item">
                            <svg className="nav-icon" viewBox="0 0 24 24" fill="none">
                                <path d="M3 9.5L12 3l9 6.5V21a1 1 0 0 1-1 1h-5a1 1 0 0 1-1-1v-5h-4v5a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                            </svg>
                            <span>홈</span>
                        </Link>
                        <Link to="/map" className="nav-item">
                            <svg className="nav-icon" viewBox="0 0 24 24" fill="none">
                                <polygon points="3 6 9 3 15 6 21 3 21 18 15 21 9 18 3 21 3 6" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                                <line x1="9" y1="3" x2="9" y2="18" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                                <line x1="15" y1="6" x2="15" y2="21" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                            </svg>
                            <span>지도</span>
                        </Link>
                        <Link to="/myPage/bookmarkList" className="nav-item">
                            <svg class="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M3 7h5l2 3h11v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7z" />
                            </svg>
                            <span>보관함</span>
                        </Link>
                        <Link to="/myPage" className="nav-item">
                            <svg className="nav-icon" viewBox="0 0 24 24" fill="none">
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