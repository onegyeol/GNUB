import React from 'react';

const BookmarkButton = ({ shopId, isBookmarked, isLoggedIn, onToggle }) => {
  const handleClick = () => {
    if (!isLoggedIn) {
      alert('로그인이 필요합니다.');
      window.location.href = '/member/login';
      return;
    }

    const method = isBookmarked ? 'DELETE' : 'POST';
    fetch(`/api/bookmarks?shopId=${shopId}`, { method })
      .then(() => onToggle(!isBookmarked));
  };

  return (
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
  );
};

export default BookmarkButton;
