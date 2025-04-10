import axios from 'axios';

const BASE = 'http://localhost:8080';

export const fetchMyInfo = () =>
  axios.get(`${BASE}/api/myPage/info`, { withCredentials: true }).then(res => res.data);

export const fetchLikes = () =>
  axios.get(`${BASE}/api/myPage/likes`, { withCredentials: true }).then(res => res.data);

export const fetchBookmarks = () =>
  axios.get(`${BASE}/api/myPage/bookmarks`, { withCredentials: true }).then(res => res.data);
