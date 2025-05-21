import axios from 'axios';

const BASE = process.env.REACT_APP_API_URL;

export const fetchFolders = () =>
  axios.get(`${BASE}/api/bookmarks/folders`, { withCredentials: true }).then(res => res.data);

export const saveBookmark = (shopId, folderId = null) =>
  axios.post(`${BASE}/api/bookmarks`, null, {
    params: { shopId, folderId },
    withCredentials: true,
  });

export const deleteBookmark = (shopId) =>
  axios.delete(`${BASE}/api/bookmarks/`, {
    params: { shopId },
    withCredentials: true,
  });

export const createFolder = (name) =>
  axios.post(`${BASE}/api/bookmarks/folders`, null, {
    params: { name },
    withCredentials: true,
  });

export const deleteFolder = (name) =>
  axios.delete(`${BASE}/api/bookmarks/folders`, {
    params: { name },
    withCredentials: true,
  });