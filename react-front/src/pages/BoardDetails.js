import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchBoardById } from '../service/BoardApi';
import './css/BoardDetails.css'; // 스타일 파일(optional)

const BoardDetails = () => {
  const { id } = useParams();
  const [board, setBoard] = useState(null);

  useEffect(() => {
    fetchBoardById(id)
      .then(data => setBoard(data))
      .catch(error => console.error(error));
  }, [id]);

  if (!board) return <div>📄 게시글을 불러오는 중입니다...</div>;

  return (
    <div className="board-details">
      <h1>{board.title}</h1>
      <p><strong>작성자:</strong> {board.author}</p>
      <p><strong>작성일:</strong> {new Date(board.createdDate).toLocaleDateString()}</p>
      <hr />
      <div className="content">{board.content}</div>
    </div>
  );
};

export default BoardDetails;
