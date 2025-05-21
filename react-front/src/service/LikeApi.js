// service/LikeApi.js
import axios from 'axios';

const BASE = process.env.REACT_APP_API_URL

axios.defaults.withCredentials = true;

export const toggleLike = (shopId) =>
  axios.post(`${BASE}/api/like/toggle`, { shopId }).then(res => res.data);

export const getLikeCount = (shopId) =>
  axios.get(`${BASE}/api/like/count`, { params: { shopId } }).then(res => res.data);