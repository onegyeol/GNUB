// src/pages/AskAI.js
import React, { useState } from 'react';
import { askQuestion } from '../service/AskAIApi';
import { Link } from 'react-router-dom';
import './css/AskAI.css';

const AskAI = () => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    // 사용자 메시지 추가
    const newMessages = [...messages, { type: 'question', text: input }];
    setMessages(newMessages);
    setInput('');

    try {
      const response = await askQuestion(input);

      // response가 null인 경우 (로그인 리디렉션됨)
      if (!response) return;

      setMessages((prev) => [
        ...prev,
        { type: 'answer', text: response.reply }
      ]);
    } catch (error) {
      setMessages((prev) => [
        ...prev,
        { type: 'answer', text: '⚠️ 응답 실패!' }
      ]);
    }
  };

  return (
    <div className="ask-container">
      <div className="chat-box">
        {messages.map((msg, idx) => (
          <div key={idx} className={`chat-message ${msg.type}`}>
            {msg.text}
          </div>
        ))}
      </div>
      <form className="input-form" onSubmit={handleSubmit}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="질문을 입력하세요..."
        />
        <button type="submit">전송</button>
      </form>
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

                        <Link to="/" className="nav-item">
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
    </div>
    
  );
};

export default AskAI;
