import React, { useEffect, useState } from 'react';
import {
  fetchFolders,
  saveBookmark,
  createFolder as createNewFolder
} from '../service/BookmarkApi';

const BookmarkModal = ({ shopId, onClose, onBookmarkSuccess }) => {
  const [folders, setFolders] = useState([]);
  const [newFolderName, setNewFolderName] = useState('');

  useEffect(() => {
    fetchFolders()
      .then(setFolders)
      .catch(err => {
        console.error('📛 폴더 목록 에러:', err);
        alert('폴더 정보를 불러오는 데 실패했습니다.');
      });
  }, []);

  const handleBookmark = (folderId = null) => {
    saveBookmark(shopId, folderId)
      .then(() => {
        onBookmarkSuccess?.(); // optional callback
        onClose();
      })
      .catch(err => {
        console.error('📛 북마크 저장 실패:', err);
        alert('북마크 저장에 실패했습니다.');
      });
  };

  const handleCreateFolder = () => {
    if (!newFolderName.trim()) return;

    createNewFolder(newFolderName)
      .then(newFolder => {
        setFolders([...folders, newFolder]);
        setNewFolderName('');
      })
      .catch(err => {
        console.error('📛 폴더 생성 실패:', err);
        alert('폴더 생성에 실패했습니다.');
      });
  };

  return (
    <>
      <div className="modal-overlay" onClick={onClose} />
      <div className="modal-sheet show">
        <div className="modal-header">
          <div className="modal-handle" />
          <h3 className="modal-title">폴더에 저장</h3>
        </div>

        <div className="modal-body">
          <button className="bookmark-default" onClick={() => handleBookmark(null)}>
            ⭐ 그냥 저장하기
          </button>

          <div className="folder-list">
            {folders.map(folder => (
              <button key={folder.id} onClick={() => handleBookmark(folder.id)}>
                {folder.name}
              </button>
            ))}
          </div>

          <div className="folder-create">
            <input
              type="text"
              value={newFolderName}
              onChange={e => setNewFolderName(e.target.value)}
              placeholder="새 폴더 이름"
            />
            <button className="create-folder-btn" onClick={handleCreateFolder}>
              폴더 만들기
            </button>
          </div>
        </div>

        <div className="modal-footer">
          <button onClick={onClose}>닫기</button>
        </div>
      </div>
    </>
  );
};

export default BookmarkModal;
