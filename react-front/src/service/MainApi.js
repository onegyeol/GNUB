import axios from 'axios';

const BASE = process.env.REACT_APP_API_URL;

export const fetchMainPageData = async () => {
  const response = await axios.get(`${BASE}/api/main`);
  return response.data;
};
