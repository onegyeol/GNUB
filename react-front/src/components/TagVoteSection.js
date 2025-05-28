import React from 'react';
import './css/TagVoteSection.css';

const TagVoteSection = ({ shopId, tagCounts, isLoggedIn, votedMap, onVote }) => {
  return (
    <div className={`tag-vote-list ${!isLoggedIn ? 'blurred' : ''}`}>
      {Object.entries(tagCounts).map(([tagName, count]) => (
        <button
          key={tagName}
          className={`tag-item ${votedMap?.[shopId]?.[tagName] ? 'voted' : ''}`}
          onClick={() => onVote(tagName)}
          data-tagname={tagName}
        >
          {`${tagName} (${count})`}
        </button>
      ))}
    </div>
  );
};

export default TagVoteSection;
