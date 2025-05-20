import axios from 'axios';

axios.defaults.withCredentials = true;

const BASE = 'http://3.39.233.211:8080';

export const fetchSearchResults = (query) =>
  axios.get(`${BASE}/api/search`, {
    params: { query }
  }).then(res => res.data);
