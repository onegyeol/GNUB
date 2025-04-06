import React, { useEffect, useState } from 'react';
import { fetchMyInfo } from '../service/MypageApi';
import './css/Mypage.css'; // 스타일은 기존 CSS를 기반으로 따로 분리해서 여기에 import

export default function MyPage() {
  const [info, setInfo] = useState(null);
  const [showPasswordModal, setShowPasswordModal] = useState(false);

  useEffect(() => {
    fetchMyInfo().then(data => setInfo(data));
  }, []);

  const handleOpenModal = () => setShowPasswordModal(true);
  const handleCloseModal = () => setShowPasswordModal(false);

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
              <button className="go-btn" onClick={() => window.location.href='/mypage/bookmarkList'}>바로가기</button>
            </div>
            <div className="activity-card">
              <div className="activity-text">👍 내가 좋아요한 가게</div>
              <button className="go-btn" onClick={() => window.location.href='/mypage/likeList'}>바로가기</button>
            </div>
          </div>
        </div>

        <div style={{ marginTop: '40px', textAlign: 'center' }}>
          <form action="/member/logout" method="post">
            <button type="submit" className="go-btn" style={{ backgroundColor: '#e74c3c', marginTop: '20px' }}>로그아웃</button>
          </form>
        </div>
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
