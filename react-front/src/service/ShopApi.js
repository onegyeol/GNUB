import axios from 'axios';

const BASE = process.env.REACT_APP_API_URL;

export const fetchShopDetails = (id) =>
  axios.get(`${BASE}/api/shop/${id}`, { withCredentials: true }).then(res => res.data);

export const toggleBookmark = (shopId) =>
  axios.post(`${BASE}/api/bookmark/toggle`, { shopId }, { withCredentials: true }).then(res => res.data);

export const fetchBookmarkFolders = () =>
  axios.get(`${BASE}/api/bookmark/folders`, { withCredentials: true }).then(res => res.data);

export const createBookmarkFolder = (name) =>
  axios.post(`${BASE}/api/bookmark/folders`, { name }, { withCredentials: true }).then(res => res.data);
