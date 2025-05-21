import axios from 'axios';

const BASE = process.env.REACT_APP_API_URL; // 백엔드 API 경로

export const fetchShopDetails = async (id) => {
  try {
    const response = await axios.get(`${BASE}/api/foodDetails/${id}`);
    return response.data;
  } catch (error) {
    console.error('❌ 음식점 상세정보 불러오기 실패:', error);
    throw error;
  }
};
