import axios from 'axios';


const BASE = process.env.REACT_APP_API_URL;


export const askQuestion = async (query) => {
  try {
    const response = await axios.post(`${BASE}/ask`, { query });

    const contentType = response.headers['content-type'];
    if (contentType && contentType.includes('text/html')) {
      alert("로그인이 필요한 서비스입니다.");
      window.location.href = "/member/login";
      return null;
    }

    // 예외: 응답 구조가 비정상인 경우
    if (!response.data || typeof response.data.reply !== 'string') {
      return { reply: "⚠️ 올바르지 않은 응답입니다." };
    }

    return response.data;

  } catch (error) {
    console.error("챗봇 응답 오류:", error);
    return { reply: "❌ 서버 오류가 발생했습니다." };
  }
};
