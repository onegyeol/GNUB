// service/MemberApi.js
import axios from 'axios';

const BASE = process.env.REACT_APP_API_URL;

export const login = (form) =>
  axios.post(`${BASE}/api/member/login`, form, { withCredentials: true });

export const signup = (form) =>
  axios.post(`${BASE}/api/member/new`, form, { withCredentials: true });

export const logout = () =>
  axios.post(`${BASE}/api/member/logout`, {}, { withCredentials: true });
