import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/main'; // 스프링 서버 주소

// categorizedShops API 호출 함수
export const fetchCategorizedShops = async () => {
  const response = await axios.get(`${BASE_URL}/shops`);
  return response.data;
};

