import React, { useEffect, useRef } from 'react';
import Chart from 'chart.js/auto';

const RadarChartGroup = ({ tagCounts }) => {
  const chart6Ref = useRef(null);
  const chart5Ref = useRef(null);

  useEffect(() => {
    const safeGet = (key) => typeof tagCounts[key] === 'number' ? tagCounts[key] : 0;

    const label6 = ['맛있어요', '신선해요', '가성비 좋아요', '청결해요', '친절해요', '분위기 좋아요'];
    const label5 = ['혼밥하기 좋아요', '데이트하기 좋아요', '단체로 가기 좋아요', '주차가 가능해요', '아쉬워요'];

    if (chart6Ref.current) {
      new Chart(chart6Ref.current, {
        type: 'radar',
        data: {
          labels: label6,
          datasets: [{
            label: '음식점 유형1',
            data: label6.map(k => safeGet(k)),
            backgroundColor: 'rgba(255, 165, 0, 0.3)',
            borderColor: 'orange',
            pointBackgroundColor: 'orange',
          }]
        },
        options: {
          responsive: true,
          scales: {
            r: {
              suggestedMin: 0,
              suggestedMax: 3000,
              pointLabels: {
                font: { size: 16, family: 'Noto Sans KR', weight: 'bold' },
                color: '#333'
              },
              ticks: { display: false }
            }
          }
        }
      });
    }

    if (chart5Ref.current) {
      new Chart(chart5Ref.current, {
        type: 'radar',
        data: {
          labels: label5,
          datasets: [{
            label: '음식점 유형2',
            data: label5.map(k => safeGet(k)),
            backgroundColor: 'rgba(0,123,255,0.3)',
            borderColor: 'blue',
            pointBackgroundColor: 'blue',
          }]
        },
        options: {
          responsive: true,
          scales: {
            r: {
              suggestedMin: 0,
              suggestedMax: 3000,
              pointLabels: {
                font: { size: 16, family: 'Noto Sans KR', weight: 'bold' },
                color: '#333'
              },
              ticks: { display: false }
            }
          }
        }
      });
    }
  }, [tagCounts]);

  return (
    <div className="tag-chart-wrapper">
      <canvas ref={chart6Ref} width="500" height="500"></canvas>
      <canvas ref={chart5Ref} width="500" height="500"></canvas>
    </div>
  );
};

export default RadarChartGroup;
