// src/pages/Map.js

import React, { useEffect, useRef, useState } from 'react';
import { Wrapper } from '@googlemaps/react-wrapper';
import { fetchGoogleMapsKey, fetchShopsInBounds } from '../service/MapApi';
import { useNavigate, Link } from 'react-router-dom';

const containerStyle = {
  width: '100%',
  height: 'calc(100vh - 74px)',
};

function MapContent({ center, shops, onBoundsChanged }) {
  const mapRef = useRef(null);
  const mapInstance = useRef(null);
  const markersRef = useRef([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (!mapRef.current || !window.google || !center) return;

    if (!mapInstance.current) {
      mapInstance.current = new window.google.maps.Map(mapRef.current, {
        center,
        zoom: 15,
      });

      mapInstance.current.addListener('bounds_changed', () => {
        const bounds = mapInstance.current.getBounds();
        if (bounds && onBoundsChanged) {
          onBoundsChanged(bounds);
        }
      });

      new window.google.maps.Marker({
        position: center,
        map: mapInstance.current,
        title: '현재 위치',
        icon: {
          url: 'https://github.com/user-attachments/assets/a2a22265-fb9e-4c91-ad09-ecfd3ea83057',
          scaledSize: new window.google.maps.Size(40, 40),
        },
      });
    }

    markersRef.current.forEach(marker => marker.setMap(null));
    markersRef.current = [];

    shops.forEach(shop => {
      const marker = new window.google.maps.Marker({
        position: { lat: parseFloat(shop.lat), lng: parseFloat(shop.lng) },
        map: mapInstance.current,
        title: shop.name,
        icon: {
          url: 'https://github.com/user-attachments/assets/e17ed7d7-903e-495c-9f1e-83f144adfdff',
          scaledSize: new window.google.maps.Size(30, 30),
        },
      });

      marker.addListener('click', () => {
        navigate(`/foodDetails/${shop.id}`);
      });

      markersRef.current.push(marker);
    });
  }, [center, shops, onBoundsChanged]);

  return <div ref={mapRef} style={containerStyle} />;
}

export default function MapPage() {
  const navigate = useNavigate();
  const [apiKey, setApiKey] = useState('');
  const [currentLocation, setCurrentLocation] = useState(null);
  const [shops, setShops] = useState([]);
  const [shopsVisible, setShopsVisible] = useState(false);
  const [mapBounds, setMapBounds] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/search?query=${encodeURIComponent(searchQuery.trim())}`);
    }
  };

  useEffect(() => {
    fetchGoogleMapsKey()
      .then(key => setApiKey(key.trim()))
      .catch(err => console.error('API 키 실패', err));
  }, []);

  useEffect(() => {
    navigator.geolocation.getCurrentPosition(
      pos => {
        setCurrentLocation({
          lat: pos.coords.latitude,
          lng: pos.coords.longitude,
        });
      },
      err => {
        console.error('❌ 위치 정보 가져오기 실패:', err);
        setCurrentLocation({ lat: 35.154410, lng: 128.100856 });
      }
    );
  }, []);

  const loadShops = async () => {
    if (!mapBounds) return;

    const ne = mapBounds.getNorthEast();
    const sw = mapBounds.getSouthWest();

    const data = await fetchShopsInBounds(
      ne.lat(),
      ne.lng(),
      sw.lat(),
      sw.lng()
    );
    setShops(data);
    setShopsVisible(true);
  };

  const hideShops = () => {
    setShops([]);
    setShopsVisible(false);
  };

  const toggleShops = () => {
    if (shopsVisible) hideShops();
    else loadShops();
  };

  if (!apiKey || !currentLocation) {
    return <div>지도 불러오는 중...</div>;
  }

  return (
    <>
      <header className="header-content">
        <div className="common-desk-header">
          <div className="header-wrap">
            <div className="search-form">
              <form onSubmit={handleSearchSubmit}>
                <div className="input-wrap">
                  <input
                    className="search-input"
                    type="search"
                    id="query"
                    name="query"
                    placeholder="지역, 음식 또는 식당명 입력"
                    maxLength={255}
                    autoComplete="off"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                  />
                  <button type="submit" className="btn-search">
                    <img src="https://github.com/user-attachments/assets/19865e59-1076-4b33-ae6a-9cfbd7b5bbb2" alt="검색버튼" />
                  </button>
                </div>
              </form>
            </div>
            <div className="auth-Box"></div>
          </div>
        </div>
      </header>

      <Wrapper apiKey={apiKey} libraries={['places']}>
        <MapContent center={currentLocation} shops={shops} onBoundsChanged={setMapBounds} />
      </Wrapper>

      <div
        onClick={toggleShops}
        style={{
          position: 'absolute',
          top: 90,
          right: 20,
          zIndex: 10,
          backgroundColor: '#fff',
          border: '1px solid #ccc',
          borderRadius: '8px',
          padding: '8px 12px',
          cursor: 'pointer',
          boxShadow: '0 2px 6px rgba(0,0,0,0.3)',
        }}
      >
        {shopsVisible ? '❌ 음식점 숨기기' : '🍽 음식점 띄우기'}
      </div>

      <div className="bottom-nav-wrapper">
        <nav className="bottom-nav">
          <div className="nav-container">
            <Link to="/board/main" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M19 3H5C3.89543 3 3 3.89543 3 5V19C3 20.1046 3.89543 21 5 21H19C20.1046 21 21 20.1046 21 19V5C21 3.89543 20.1046 3 19 3Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M3 9H21" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M9 21V9" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
              <span>매거진</span>
            </Link>

            <Link to="/" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M3 9.5L12 3l9 6.5V21a1 1 0 0 1-1 1h-5a1 1 0 0 1-1-1v-5H10v5a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5z" />
              </svg>
              <span>홈</span>
            </Link>

            <Link to="/map" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <polygon points="3 6 9 3 15 6 21 3 21 18 15 21 9 18 3 21 3 6" />
                <line x1="9" y1="3" x2="9" y2="18" />
                <line x1="15" y1="6" x2="15" y2="21" />
              </svg>
              <span>지도</span>
            </Link>

            <Link to="/myPage" className="nav-item">
              <svg className="nav-icon" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <circle cx="12" cy="7" r="4" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
              <span>마이</span>
            </Link>
          </div>
        </nav>
      </div>
    </>
  );
}
