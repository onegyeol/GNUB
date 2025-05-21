import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../service/MemberApi';

import './css/Login.css';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error')) {
      alert('입력하신 아이디나 비밀번호가 올바르지 않습니다.');
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await login({ email, password });
      console.log('로그인 성공:', response.data);
      navigate('/');
    } catch (error) {
      console.error('로그인 실패:', error);
      window.location.href = '/member/login?error=true';
    }
  };

  return (
    <div className="login-wrapper">
      <div className="login-container">
        <a href="/">
          <img
            src="https://github.com/onegyeol/GNUB/blob/main/%E1%84%85%E1%85%A9%E1%84%80%E1%85%A9(%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BCX).png?raw=true"
            alt="로고"
            className="login-logo"
          />
        </a>
        <h2 className="login-title">로그인</h2>
        <form onSubmit={handleSubmit} className="login-form">
          <input
            type="text"
            placeholder="이메일"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="login-input"
          />
          <input
            type="password"
            placeholder="비밀번호"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="login-input"
          />
          <button type="submit" className="login-button">
            로그인
          </button>
        </form>
        <div className="login-footer-links">
          <a href="#">비밀번호 찾기</a>
          <a href="#">아이디 찾기</a>
          <a href="/member/new">회원가입</a>
        </div>
      </div>
    </div>
  );
}
