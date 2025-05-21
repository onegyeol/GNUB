import axios from 'axios';

const BASE = process.env.REACT_APP_API_URL;

export const fetchBoardList = ({ query = '', sort = 'created', page = 1 }) =>
  axios
    .get(BASE, {
      params: { query, sort, page },
      withCredentials: true,
    })
    .then(res => res.data);


export const fetchBoardById = async (id) => {
  try {
    const response = await fetch(`${BASE}/${id}`);
    if (!response.ok) {
      throw new Error('게시글을 불러오는 데 실패했습니다.');
    }
    return await response.json();
  } catch (error) {
    console.error('❌ 게시글 API 오류:', error);
    throw error;
  }
};