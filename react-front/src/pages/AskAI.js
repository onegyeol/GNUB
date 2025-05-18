// src/pages/AskAI.js
import React, { useState } from 'react';
import { askQuestion } from '../service/AskAIApi';
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
    </div>
  );
};

export default AskAI;
