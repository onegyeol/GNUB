import React, { useState, useEffect } from 'react';

const LikeButton = ({ shopId }) => {
  const [likeCount, setLikeCount] = useState(0);

  useEffect(() => {
    fetch(`/api/likes/count?shopId=${shopId}`)
      .then(res => res.json())
      .then(data => setLikeCount(data.count));
  }, [shopId]);

  const handleClick = () => {
    fetch(`/api/likes?shopId=${shopId}`, { method: 'POST' })
      .then(res => res.json())
      .then(data => setLikeCount(data.newCount));
  };

  return (
    <div className="like-container">
      <button className="like-btn" onClick={handleClick}>
        <img
          className="heart-icon"
          src="https://cdn-icons-png.flaticon.com/512/833/833472.png"
          alt="Heart Icon"
        />
      </button>
      <span>{likeCount}</span>
    </div>
  );
};

export default LikeButton;
