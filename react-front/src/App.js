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

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/member/new" element={<Signup />} />
        <Route path="/member/login" element={<Login />} />
        <Route path="/myPage" element={<Mypage />} />
        <Route path="/main" element={<Main />} />
        <Route path="/shopDetails/:id" element={<FoodDetailsPage />} />
        <Route path="/map" element={<MapPage />} />
        <Route path="/myPage/bookmarkList" element={<BookmarkList />} />
        <Route path="/myPage/likeList" element={<LikeList />} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="/board/main" element={<BoardMain />}/>
        <Route path="/board/:id" element={<BoardDetails />}/>
      </Routes>
    </Router>
  );
}

export default App;
