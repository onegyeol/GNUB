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

// session_id를 클라이언트에서 생성
let sessionId = generateSessionId();

// 새로고침 시 새로운 session_id 생성
window.onload = () => {
    sessionId = generateSessionId();
};

// session_id 생성 함수
function generateSessionId() {
    return Math.random().toString(36).substr(2, 9); // 간단한 랜덤 문자열 생성
}


// OpenAI API 호출 함수
async function fetchAIResponse(query) {
    try {
        const response = await fetch('http://localhost:5000/ask/ai', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache'
            },
            body: JSON.stringify({ query: query, session_id: sessionId }),
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



// sendDataToBackend 함수 정의
async function sendDataToBackend(userMessage, aiResponse) {
    try {
        const response = await fetch('http://localhost:5000/log', { // 예: 로그 엔드포인트
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userMessage, aiResponse }),
        });
        if (!response.ok) throw new Error('백엔드로 데이터 전송 실패');
    } catch (error) {
        console.error("데이터 백엔드 전송 중 오류 발생:", error);
    }
}


// 메시지 전송 처리 함수
async function handleMessageSend() {
    const userMessage = userInput.value.trim();
    if (userMessage.length === 0) {
        alert('입력된 내용이 없습니다. 다시 시도해주세요.');
        return;
    }

    userInput.value = ''; // 입력 필드 초기화
    showLoading(); // 로딩 메시지 표시

    try {
        const aiResponse = await fetchAIResponse(userMessage); // AI 응답 받기
        hideLoading(); // 로딩 메시지 숨기기

        // 메시지 그룹 생성
        createMessageGroup(userMessage, aiResponse); 

        // 데이터 백엔드로 전송
        await sendDataToBackend(userMessage, aiResponse); 
    } catch (error) {
        hideLoading(); // 로딩 메시지 숨기기
        alert('오류 발생: ' + error.message);
    }
}




// DOM 요소 가져오기
const chatMessages = document.querySelector('#chat-messages');
const userInput = document.querySelector('#userInput');
const sendButton = document.querySelector('#searchButton');

// 메시지 그룹을 생성하는 함수
function createMessageGroup(userMessage, aiMessage) {
    const messageGroup = document.createElement('div');
    messageGroup.className = 'message-group';

    // 사용자 메시지 추가
    const userMessageElement = document.createElement('div');
    userMessageElement.className = 'message user';
    userMessageElement.textContent = `나: ${userMessage}`;
    messageGroup.appendChild(userMessageElement);

    // AI 응답 추가
    const aiMessageElement = document.createElement('div');
    aiMessageElement.className = 'message ai';

    // AI 응답 데이터 처리
    if (Array.isArray(aiMessage) && aiMessage.length > 0) {
        const firstResponse = aiMessage[0];

        if (typeof firstResponse === 'string') {
            // 응답이 단순 문자열일 경우
            aiMessageElement.textContent = `GNUB: ${firstResponse}`;
        } else if (typeof firstResponse === 'object' && firstResponse.message) {
            // 응답이 객체일 경우
            aiMessageElement.textContent = `GNUB: ${firstResponse.message}`;

            // 링크가 있을 경우 추가
            if (firstResponse.link) {
                const aiLinkElement = document.createElement('a');
                aiLinkElement.href = firstResponse.link;
                aiLinkElement.textContent = '메뉴 바로보기'; // 링크 텍스트
                aiLinkElement.target = '_blank'; // 새 탭에서 링크 열기
                aiMessageElement.appendChild(document.createElement('br')); // 줄바꿈 추가
                aiMessageElement.appendChild(aiLinkElement); // 링크 추가
            }
        } else {
            aiMessageElement.textContent = `GNUB: 알 수 없는 응답 형식입니다.`;
        }
    } else {
        aiMessageElement.textContent = `GNUB: AI 응답이 없습니다.`;
    }

    messageGroup.appendChild(aiMessageElement);
    chatMessages.appendChild(messageGroup);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}



// 로딩 메시지 표시 함수
function showLoading() {
    const loadingElement = document.createElement('div');
    loadingElement.className = 'message ai';
    loadingElement.id = 'loading';
    loadingElement.textContent = '답변을 작성 중입니다...';
    chatMessages.appendChild(loadingElement);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// 로딩 메시지 숨김 함수
function hideLoading() {
    const loadingElement = document.getElementById('loading');
    if (loadingElement) chatMessages.removeChild(loadingElement);
}

// 메시지 전송 처리 함수
async function handleMessageSend() {
    const userMessage = userInput.value.trim();
    if (userMessage.length === 0) {
        alert('입력된 내용이 없습니다. 다시 시도해주세요.');
        return;
    }

    userInput.value = ''; // 입력 필드 초기화
    showLoading(); // 로딩 메시지 표시

    try {
        const aiResponse = await fetchAIResponse(userMessage); // AI 응답 받기
        hideLoading(); // 로딩 메시지 숨기기

        createMessageGroup(userMessage, aiResponse); // 메시지 그룹 생성
        await sendDataToBackend(userMessage, aiResponse); // 데이터 백엔드로 전송
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

window.onload = () => {
    sessionStorage.clear(); // 세션 데이터 초기화
};
