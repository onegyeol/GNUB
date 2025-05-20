import React, { useEffect, useState } from 'react';
import { fetchMyInfo } from '../service/MypageApi';
import { logout } from '../service/MemberApi';
import './css/Mypage.css'; // 스타일은 기존 CSS를 기반으로 따로 분리해서 여기에 import
import { useNavigate, Link } from 'react-router-dom';

export default function MyPage() {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');
  const [info, setInfo] = useState(null);
  const [showPasswordModal, setShowPasswordModal] = useState(false);

  // 검색 제출 함수
  const handleSearchSubmit = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/search?query=${encodeURIComponent(searchQuery.trim())}`);
    }
  };

  useEffect(() => {
    fetchMyInfo()
      .then(data => {
        console.log("✅ 받은 회원 정보:", data);
        setInfo(data);
      })
      .catch(err => {
        if (err.response?.status === 401) {
          navigate('/member/login'); // 로그인 안 했으면 로그인 페이지로 이동
        } else {
          console.error(err); // 그 외 에러는 콘솔 확인
        }
      });
  }, [navigate]);


  const handleOpenModal = () => setShowPasswordModal(true);
  const handleCloseModal = () => setShowPasswordModal(false);
  const handleLogout = async () => {
    try {
      await logout();
      navigate('/'); // 혹은 메인 페이지로 리디렉션
    } catch (error) {
      console.error('❌ 로그아웃 실패:', error);
    }
  };

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

      <div className="page-container">
        <div className="profile">
          <div className="name">{info?.name}</div>
          <div className="department">{info?.departmentCollege} / {info?.departmentName}</div>
        </div>

        <div className="content-container profile-info">
          <div className="content-title">
            <span className="content-title-text">내 프로필</span>
          </div>
          <div className="content-body">
            <div className="content-item">
              <span className="content-label">이름:</span>
              <span className="content-value">{info?.name}</span>
            </div>
            <div className="content-item">
              <span className="content-label">학부/학과:</span>
              <span className="content-value">{info?.departmentCollege} / {info?.departmentName}</span>
            </div>
            <div className="content-item">
              <span className="content-label">이메일:</span>
              <span className="content-value">{info?.email}</span>
            </div>
            <div className="content-item">
              <span className="content-label">비밀번호:</span>
              <span className="content-value">********</span>
              <button className="edit-btn" onClick={handleOpenModal}>수정</button>
            </div>
          </div>
        </div>

        <div className="activity-section">
          <h2 className="activity-title">✨ 활동 내역</h2>
          <div className="activity-grid">
            <div className="activity-card">
              <div className="activity-text">🏠 내가 저장한 가게</div>
              <button className="go-btn" onClick={() => navigate('/myPage/bookmarkList')}>바로가기</button>
            </div>
            <div className="activity-card">
              <div className="activity-text">👍 내가 좋아요한 가게</div>
              <button className="go-btn" onClick={() => navigate('/myPage/likeList')}>바로가기</button>
            </div>
          </div>
        </div>


        <div style={{ marginTop: '40px', textAlign: 'center' }}>
          <button type="button" className="go-btn" style={{ backgroundColor: '#e74c3c', marginTop: '20px' }} onClick={handleLogout}>
            로그아웃
          </button>
        </div>

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

      {showPasswordModal && (
        <div className="modal" onClick={handleCloseModal}>
          <div className="modal-content content-container" onClick={e => e.stopPropagation()}>
            <div className="content-title">
              <span className="content-title-text">비밀번호 변경</span>
              <span className="close" onClick={handleCloseModal}>&times;</span>
            </div>
            <div className="content-body">
              <form action="/member/change-password" method="post">
                <div className="content-item">
                  <label className="content-label">현재 비밀번호:</label>
                  <input type="password" name="currentPassword" required />
                </div>
                <div className="content-item">
                  <label className="content-label">새 비밀번호:</label>
                  <input type="password" name="newPassword" required />
                </div>
                <div className="content-item">
                  <label className="content-label">새 비밀번호 확인:</label>
                  <input type="password" name="confirmPassword" required />
                </div>
                <div className="content-item">
                  <button type="submit" className="edit-btn">변경</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
