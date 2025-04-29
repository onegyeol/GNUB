import axios from 'axios';

const BASE = 'http://localhost:8080/api';

export const fetchShopDetails = (id) =>
  axios.get(`${BASE}/shop/${id}`, { withCredentials: true }).then(res => res.data);

export const toggleBookmark = (shopId) =>
  axios.post(`${BASE}/bookmark/toggle`, { shopId }, { withCredentials: true }).then(res => res.data);

export const fetchBookmarkFolders = () =>
  axios.get(`${BASE}/bookmark/folders`, { withCredentials: true }).then(res => res.data);

export const createBookmarkFolder = (name) =>
  axios.post(`${BASE}/bookmark/folders`, { name }, { withCredentials: true }).then(res => res.data);
