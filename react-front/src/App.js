// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Signup from './pages/Signup';
import Login from './pages/Login';
import Mypage from './pages/Mypage';
import Main from './pages/Main';
import MapPage from './pages/Map'; 
import BookmarkList from './pages/BookmarkList';
import LikeList from './pages/LikeList';
import FoodDetailsPage from './pages/FoodDetails';
import SearchPage from './pages/Search';
import BoardMain from './pages/BoardMain';
import BoardDetails from './pages/BoardDetails';
import ASKAI from './pages/AskAI'

function App() {
  return (
    <Router basename='/mobile'>
      <Routes>
        <Route path="/member/new" element={<Signup />} />
        <Route path="/member/login" element={<Login />} />
        <Route path="/myPage" element={<Mypage />} />
        <Route path="/" element={<Main />} />
        <Route path="/foodDetails/:id" element={<FoodDetailsPage />} />
        <Route path="/map" element={<MapPage />} />
        <Route path="/myPage/bookmarkList" element={<BookmarkList />} />
        <Route path="/myPage/likeList" element={<LikeList />} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="/search/:tag" element={<SearchPage />} />
        <Route path="/board/main" element={<BoardMain />}/>
        <Route path="/board/:id" element={<BoardDetails />}/>
        <Route path="/ask" element={<ASKAI />}/>
      </Routes>
    </Router>
  );
}

export default App;
