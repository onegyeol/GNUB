// service/LikeApi.js
import axios from 'axios';

const BASE = 'http://3.39.233.211:8080/api';

axios.defaults.withCredentials = true;

export const toggleLike = (shopId) =>
  axios.post(`${BASE}/like/toggle`, { shopId }).then(res => res.data);

export const getLikeCount = (shopId) =>
  axios.get(`${BASE}/like/count`, { params: { shopId } }).then(res => res.data);