// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Signup from './pages/Signup';
import Login from './pages/Login';
import Mypage from './pages/Mypage';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/member/new" element={<Signup />} />
        <Route path="/member/login" element={<Login />} />
        <Route path="/myPage" element={<Mypage />} />
      </Routes>
    </Router>
  );
}

export default App;
