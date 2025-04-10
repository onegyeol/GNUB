// service/MemberApi.js
import axios from 'axios';

const BASE = 'http://localhost:8080';

export const login = (form) =>
  axios.post(`${BASE}/api/member/login`, form, { withCredentials: true });

export const signup = (form) =>
  axios.post(`${BASE}/api/member/new`, form, { withCredentials: true });
