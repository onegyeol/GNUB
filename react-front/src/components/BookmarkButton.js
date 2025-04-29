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
    setIsModalOpen(true); // 모달 오픈
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
      {isModalOpen && (
        <BookmarkModal
          shopId={shopId}
          onClose={() => setIsModalOpen(false)}
          onBookmarkSuccess={() => onToggle(true)}
        />
      )}
    </>
  );
};

export default BookmarkButton;
