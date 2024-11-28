// API 키를 가져오는 함수
async function fetchApiKey() {
    try {
        const response = await fetch('http://localhost:8080/config/apiKey');
        if (!response.ok) throw new Error('API 키를 가져올 수 없습니다.');
        return await response.text();
    } catch (error) {
        console.error("API 키 호출 중 오류 발생:", error);
        return null;
    }
}

// 데이터베이스 응답을 가져오는 함수
async function fetchDatabaseResponse() {
    try {
        const response = await fetch('http://localhost:8080/config/database');
        if (!response.ok) throw new Error('데이터베이스 정보를 가져올 수 없습니다.');
        const data = await response.json();
        if (!data || Object.keys(data).length === 0) {
            console.warn('데이터베이스에 음식점 정보가 없습니다.');
            return null;
        }
        return data;
    } catch (error) {
        console.error("데이터베이스 호출 중 오류 발생:", error);
        return null;
    }
}

// OpenAI ChatGPT API를 호출하는 함수
async function fetchAIResponse(prompt) {
    const apiKey = await fetchApiKey();
    if (!apiKey) return 'API 키를 가져오는 데 실패했습니다.';

    const databaseResponse = await fetchDatabaseResponse();
    if (!databaseResponse) return '데이터베이스에 음식점 정보가 없습니다.';

    const messages = [
        { role: "system", content: "너는 음식점을 추천해주는 역할을 하고 있어. Only use the provided database information to answer. 친절하게 해줘." },
        { role: "user", content: `Database: ${JSON.stringify(databaseResponse)}` },
        { role: "user", content: prompt }
    ];

    try {
        const response = await fetch('https://api.openai.com/v1/chat/completions', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${apiKey}` },
            body: JSON.stringify({ model: "gpt-3.5-turbo", messages, temperature: 0.8, max_tokens: 1024 }),
        });

        const data = await response.json();
        return data.choices[0]?.message?.content || '응답이 없습니다.';
    } catch (error) {
        console.error('OpenAI API 호출 중 오류 발생:', error);
        return 'API 호출 실패';
    }
}

// 백엔드로 데이터 전송 함수
async function sendDataToBackend(userInput, aiResponse) {
    const apiUrl = 'http://localhost:8080/api/data';
    const requestBody = { userInput, gptResponse: aiResponse };

    try {
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody),
        });
        if (!response.ok) throw new Error('백엔드 데이터 전송 실패');
        console.log('백엔드로 데이터 전송 완료:', await response.text());
    } catch (error) {
        console.error('데이터 전송 중 오류 발생:', error);
    }
}

// DOM 요소 가져오기
const chatMessages = document.querySelector('#chat-messages');
const userInput = document.querySelector('#user-input input');
const sendButton = document.querySelector('#user-input button');

// 메시지 그룹을 생성하는 함수
function createMessageGroup(userMessage, aiMessage) {
    const messageGroup = document.createElement('div');
    messageGroup.className = 'message-group';

    const userMessageElement = document.createElement('div');
    userMessageElement.className = 'message user';
    userMessageElement.textContent = `나: ${userMessage}`;
    messageGroup.appendChild(userMessageElement);

    const aiMessageElement = document.createElement('div');
    aiMessageElement.className = 'message ai';
    aiMessageElement.textContent = `GNUB: ${aiMessage}`;
    messageGroup.appendChild(aiMessageElement);

    chatMessages.appendChild(messageGroup);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function showLoading() {
    const loadingElement = document.createElement('div');
    loadingElement.className = 'message ai';
    loadingElement.id = 'loading';
    loadingElement.textContent = '답변을 작성 중입니다...';
    chatMessages.appendChild(loadingElement);
}

function hideLoading() {
    const loadingElement = document.getElementById('loading');
    if (loadingElement) chatMessages.removeChild(loadingElement);
}

// 버튼 클릭 이벤트 처리
sendButton.addEventListener('click', async () => {
    const userMessage = userInput.value.trim();
    if (userMessage.length === 0) return;

    userInput.value = '';

    showLoading();
    const aiResponse = await fetchAIResponse(userMessage);
    hideLoading();

    createMessageGroup(userMessage, aiResponse);
    await sendDataToBackend(userMessage, aiResponse);
});

userInput.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') sendButton.click();
});
