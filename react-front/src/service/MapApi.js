import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api';

// 👉 백엔드에서 Google Maps API 키 받아오기
export const fetchGoogleMapsKey = async () => {
  const response = await axios.get(`${BASE_URL}/google-maps-key`);
  console.log('📦 API Key Response:', response.data); // 데이터 출력

  return response.data;
};

// 👉 음식점 데이터 불러오기
export const fetchShopsInBounds = async (neLat, neLng, swLat, swLng) => {
    const response = await axios.get(`${BASE_URL}/shops`, {
      params: { neLat, neLng, swLat, swLng }
    });
    return response.data;
  };
  
