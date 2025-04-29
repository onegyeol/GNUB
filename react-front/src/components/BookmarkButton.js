import React, { useState } from 'react';
import BookmarkModal from './BookmarkModal'; // 모달 컴포넌트 가져오기

const BookmarkButton = ({ shopId, isBookmarked, isLoggedIn, onToggle }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleClick = () => {
    if (!isLoggedIn) {
      alert('로그인이 필요합니다.');
      window.location.href = '/member/login';
      return;
    }

    if (isBookmarked) {
      // 이미 북마크 되어있으면 바로 "삭제"
      fetch(`/api/bookmarks?shopId=${shopId}`, {
        method: 'DELETE',
        credentials: 'include',
      })
        .then(() => onToggle(false))
        .catch(err => console.error('❌ 북마크 삭제 실패:', err));
    } else {
      // 북마크 안 되어있으면 "모달 열기"
      setIsModalOpen(true);  // ✅ 이게 handleClick 안에 있어야 해
    }
  };

  
  return (
    <>
      <div className="save-button" onClick={handleClick}>
        <svg className="save-icon" width="24" height="24" viewBox="0 0 24 24">
          <polygon
            fill={isBookmarked ? 'black' : 'none'}
            points="20 21 12 13.44 4 21 4 3 20 3 20 21"
            stroke="black"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth="2"
          />
        </svg>
      </div>

      {/* 모달 열기 */}
      {isModalOpen && (
        <BookmarkModal
          shopId={shopId}
          onClose={() => setIsModalOpen(false)} // 모달 닫기
          onBookmarkSuccess={() => onToggle(true)} // 북마크 성공 시 토글
        />
      )}
    </>
  );
};

export default BookmarkButton;
