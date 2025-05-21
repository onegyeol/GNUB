import axios from 'axios';

axios.defaults.withCredentials = true;

const BASE = process.env.REACT_APP_API_URL;

export const fetchSearchResults = (query) =>
  axios.get(`${BASE}/api/search`, {
    params: { query }
  }).then(res => res.data);
