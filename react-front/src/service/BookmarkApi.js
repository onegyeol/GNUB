import axios from 'axios';

const BASE = 'http://localhost:8080/api/bookmarks';

export const fetchFolders = () =>
  axios.get(`${BASE}/folders`, { withCredentials: true }).then(res => res.data);

export const saveBookmark = (shopId, folderId = null) =>
  axios.post(`${BASE}`, null, {
    params: { shopId, folderId },
    withCredentials: true,
  });

export const deleteBookmark = (shopId) =>
  axios.delete(`${BASE}`, {
    params: { shopId },
    withCredentials: true,
  });

export const createFolder = (name) =>
  axios.post(`${BASE}/folders`, null, {
    params: { name },
    withCredentials: true,
  });
