import axios from 'axios';

const BASE_URL = 'http://3.39.233.211:8080/api/main'; // 스프링 서버 주소


export const fetchTaggedShops = async () => {
  const response = await axios.get(`${BASE_URL}/tags`);
  return response.data;
};