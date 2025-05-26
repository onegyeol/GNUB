import axios from 'axios';

axios.defaults.withCredentials = true;

const BASE = process.env.REACT_APP_API_URL;

console.log("🔍 API BASE URL:", BASE);  // 👉 여기!

export const fetchSearchResults = (query) =>
  axios.get(`${BASE}/api/search`, {
    params: { query }
  }).then(res => res.data);

export const fetchTagSearchResults = (tag, menu) =>
    axios.get(`${BASE}/api/search/${encodeURIComponent(tag)}`, {
      params: { menu }
    }).then(res => res.data);
  