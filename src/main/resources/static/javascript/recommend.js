// static/javascript/recommend.js
document.addEventListener("DOMContentLoaded", function () {
    const searchButton = document.getElementById("searchButton");
    const userInput = document.getElementById("userInput");
    const chatMessages = document.getElementById("chat-messages");
  
    // 메시지 추가 함수
    function appendMessage(message, type) {
      const messageDiv = document.createElement("div");
      messageDiv.classList.add("chat-message", type);
      messageDiv.textContent = message;
      chatMessages.appendChild(messageDiv);
      chatMessages.scrollTop = chatMessages.scrollHeight;
    }
  
    // 전송 버튼 클릭 이벤트 처리
    searchButton.addEventListener("click", function () {
      const query = userInput.value.trim();
      if (!query) return;
      appendMessage(query, "question");
      userInput.value = "";
  
      fetch("/chat", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ query: query })
      })
        .then(response => response.json())
        .then(data => {
          if (data.response) {
            appendMessage(data.response, "answer");
          } else {
            appendMessage("서버에서 응답을 받지 못했습니다.", "answer");
          }
        })
        .catch(error => {
          console.error("Error:", error);
          appendMessage("오류가 발생했습니다.", "answer");
        });
    });
  
    userInput.addEventListener("keypress", function (e) {
      if (e.key === "Enter") {
        e.preventDefault();
        searchButton.click();
      }
    });
  });
  