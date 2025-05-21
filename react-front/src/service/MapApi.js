import axios from 'axios';

const BASE = process.env.REACT_APP_API_URL;

// 👉 백엔드에서 Google Maps API 키 받아오기
export const fetchGoogleMapsKey = async () => {
  const response = await axios.get(`${BASE}/api/google-maps-key`);
  console.log('📦 API Key Response:', response.data); // 데이터 출력

  return response.data;
};

// 👉 음식점 데이터 불러오기
export const fetchShopsInBounds = async (neLat, neLng, swLat, swLng) => {
    const response = await axios.get(`${BASE}/api/shops`, {
      params: { neLat, neLng, swLat, swLng }
    });
    return response.data;
  };
  
