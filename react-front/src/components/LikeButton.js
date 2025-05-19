import React, { useState, useEffect } from 'react';
import { getLikeCount, toggleLike } from '../service/LikeApi';

const LikeButton = ({ shopId }) => {
  const [likeCount, setLikeCount] = useState(0);

  useEffect(() => {
    if (!shopId) return;
    getLikeCount(shopId)
      .then(data => {
        console.log("❤️ 좋아요 수:", data.count);
        setLikeCount(data.count);
      })
      .catch(err => console.error("좋아요 수 가져오기 실패:", err));
  }, [shopId]);

  const handleClick = () => {
    toggleLike(shopId)
      .then(data => {
        setLikeCount(data.likeCount);
      })
      .catch(err => {
        if (err.response?.status === 401) {
          alert("로그인이 필요합니다.");
        } else {
          console.error("좋아요 토글 실패:", err);
        }
      });
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
      <span className="heart-icon-text">{likeCount}</span>
    </div>
  );
};

export default LikeButton;
