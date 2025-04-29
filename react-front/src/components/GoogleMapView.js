import React, { useEffect, useRef, useState } from 'react';

const GoogleMapView = ({ lat, lng }) => {
  const mapRef = useRef(null);
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    const apiKey = process.env.REACT_APP_GOOGLE_MAPS_API_KEY;

    if (!apiKey) {
      console.error('❗Google Maps API 키가 없습니다. .env 파일을 확인하세요.');
      return;
    }

    const script = document.createElement('script');
    script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}`;
    script.async = true;
    script.defer = true;
    script.onload = () => setIsReady(true); // ✅ script가 다 로드되면 isReady true로
    document.head.appendChild(script);
  }, []);

  useEffect(() => {
    if (!isReady || !mapRef.current) return;

    const shopLocation = { lat: parseFloat(lat), lng: parseFloat(lng) };

    const map = new window.google.maps.Map(mapRef.current, {
      zoom: 18,
      center: shopLocation,
    });

    const customIcon = {
      url: 'https://github.com/user-attachments/assets/e17ed7d7-903e-495c-9f1e-83f144adfdff',
      scaledSize: new window.google.maps.Size(50, 50),
      anchor: new window.google.maps.Point(15, 30),
    };

    new window.google.maps.Marker({
      position: shopLocation,
      map,
      icon: customIcon,
    });
  }, [isReady, lat, lng]);

  return <div ref={mapRef} style={{ height: '380px', width: '100%' }} />;
};

export default GoogleMapView;
