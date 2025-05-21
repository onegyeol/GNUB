// service/LikeApi.js
import axios from 'axios';

const BASE = 'http://localhost:8080/api';

axios.defaults.withCredentials = true;

export const toggleLike = (shopId) =>
  axios.post(`${BASE}/like/toggle`, { shopId }).then(res => res.data);
