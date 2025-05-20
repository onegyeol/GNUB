import axios from 'axios';

axios.defaults.withCredentials = true;

const BASE = 'http://3.39.233.211:8080';

export const fetchMyInfo = () =>
  axios.get(`${BASE}/api/myPage/info`, { withCredentials: true }).then(res => res.data);

export const fetchLikes = () =>
  axios.get(`${BASE}/api/myPage/likes`, { withCredentials: true }).then(res => res.data);

export const fetchBookmarks = () =>
  axios.get(`${BASE}/api/myPage/bookmarks`, { withCredentials: true }).then(res => res.data);
