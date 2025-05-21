import axios from 'axios';

const BASE = process.env.REACT_APP_API_URL; // 스프링 서버 주소


export const fetchTaggedShops = async () => {
  const response = await axios.get(`${BASE}/api/main/tags`);
  return response.data;
};