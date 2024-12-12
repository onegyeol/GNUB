// 데이터베이스 응답을 가져오는 함수
async function fetchDatabaseResponse(query) {
    try {
        const response = await fetch('http://localhost:5000/ask/db', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ query }),
        });
        if (!response.ok) throw new Error('데이터베이스에서 음식점 정보를 가져올 수 없습니다.');
        const data = await response.json();
        return data.shops || [];
    } catch (error) {
        console.error("데이터베이스 호출 중 오류 발생:", error);
        return [];
    }
}

// session_id 생성
let sessionId = generateSessionId();
function generateSessionId() {
    return Math.random().toString(36).substr(2, 9); // 간단한 랜덤 문자열 생성
}

// 새로고침 시 새로운 session_id 생성
window.onload = () => {
    sessionId = generateSessionId();
};

// OpenAI API 호출 함수
async function fetchAIResponse(query) {
    try {
        const response = await fetch('http://localhost:5000/ask/ai', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache'
            },
            body: JSON.stringify({ query, session_id: sessionId }),
        });

        if (!response.ok) {
            throw new Error('Failed to fetch AI response');
        }

        const data = await response.json();
        if (!data.gptResponse || data.gptResponse.length === 0) {
            console.error("No AI response received");
            return ["AI 응답이 없습니다."];
        }

        return data.gptResponse;
    } catch (error) {
        console.error('AI 응답 호출 중 오류 발생:', error);
        return ["AI 응답을 가져오는 중 오류가 발생했습니다."];
    }
}

// 데이터 백엔드 전송 함수
async function sendDataToBackend(userMessage, aiResponse) {
    try {
        const response = await fetch('http://localhost:5000/log', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userMessage, aiResponse }),
        });
        if (!response.ok) throw new Error('백엔드로 데이터 전송 실패');
    } catch (error) {
        console.error("데이터 백엔드 전송 중 오류 발생:", error);
    }
}

// DOM 요소 가져오기
const chatMessages = document.querySelector('#chat-messages');
const userInput = document.querySelector('#userInput');
const sendButton = document.querySelector('#searchButton');

// 메시지 표시 함수
function addMessageToChat(type, content) {
    // 메시지 생성
    const messageElement = document.createElement("div");
    messageElement.className = `chat-message ${type}`;

    // Link 처리
    if (content instanceof HTMLElement) {
        // HTML element (링크)
        messageElement.appendChild(content);
    } else {
        // Text Content
        const messageText = document.createElement("p");
        messageText.innerText = content;
        messageElement.appendChild(messageText);
    }

    chatMessages.appendChild(messageElement);
    chatMessages.scrollTop = chatMessages.scrollHeight; // 스크롤 아래로 이동
}


// 로딩 메시지 표시 함수
function showLoading() {
    addMessageToChat('answer', '답변을 작성 중입니다...');
}

// 로딩 메시지 숨김 함수
function hideLoading() {
    const loadingMessages = document.querySelectorAll('.chat-message.answer');
    loadingMessages.forEach((msg) => {
        if (msg.textContent === '답변을 작성 중입니다...') {
            chatMessages.removeChild(msg);
        }
    });
}

// 메시지 전송 처리 함수
async function handleMessageSend() {
    const userMessage = userInput.value.trim();
    if (userMessage.length === 0) {
        alert('입력된 내용이 없습니다. 다시 시도해주세요.');
        return;
    }

    userInput.value = ''; // 입력 필드 초기화
    addMessageToChat('question', `사용자 : ${userMessage}`); // 사용자 메시지 추가

    showLoading(); // 로딩 메시지 표시

    try {
        const aiResponse = await fetchAIResponse(userMessage); // AI 응답 받기
        hideLoading(); // 로딩 메시지 숨기기

        const responseText = Array.isArray(aiResponse) && aiResponse.length > 0 
            ? aiResponse[0] 
            : { message: "AI 응답이 없습니다." }; // 객체 형태로 전달

        // 응답이 객체일 경우 처리 (message와 link)
        if (typeof responseText === 'object' && responseText.message) {
            // 'GNUB: ' + 응답 메시지
            addMessageToChat('answer', `GNUB: ${responseText.message}`);

            // 링크가 있을 경우 처리
            if (responseText.link) {
                const linkContent = document.createElement("a");
                linkContent.href = responseText.link;
                linkContent.target = "_blank"; // 새 창에서 열기
                linkContent.innerText = "학식 메뉴 바로보기"; // 링크 텍스트
                addMessageToChat('answer', linkContent); // 링크만 추가
            }
        } else {
            // 객체가 아닌 경우 그냥 메시지 출력
            addMessageToChat('answer', `GNUB: ${responseText}`);
        }

        await sendDataToBackend(userMessage, responseText); // 데이터 백엔드로 전송
    } catch (error) {
        hideLoading(); // 로딩 메시지 숨기기
        alert('오류 발생: ' + error.message);
    }
}

// 버튼 클릭 이벤트 처리
sendButton.addEventListener('click', handleMessageSend);

// Enter 키로 메시지 전송
userInput.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') sendButton.click();
});
