import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/main'; // 스프링 서버 주소


export const fetchTaggedShops = async () => {
  const response = await axios.get(`${BASE_URL}/tags`);
  return response.data;
};