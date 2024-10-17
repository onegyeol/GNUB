let isLoggedIn = false; // 로그인 상태 변수

function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;


    if (username === validUsername && password === validPassword) {
        isLoggedIn = true; // 로그인 성공 시 상태 변경
        updateLoginStatus(); // 로그인 상태 업데이트
        alert('환영합니다!'); // 환영 메시지
        window.location.href = 'index.html'; // 메인화면으로 이동.
    } else {
        alert('아이디 또는 비밀번호가 잘못되었습니다.');
        document.getElementById('password').value = ''; // 비밀번호 필드 초기화
    }
}

// 페이지 로드 시 헤더와 로그인 상태 업데이트
window.onload = function() {
    loadHeader();
};

// 헤더 로드 함수
function loadHeader() {
    const headerContainer = document.createElement('div');
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '../layout/header2.html', true); // header2.html
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            headerContainer.innerHTML = xhr.responseText;
            document.body.insertBefore(headerContainer, document.querySelector('.login-form'));
            updateLoginStatus(); // 헤더가 로드된 후 로그인 상태 업데이트
        }
    };
    xhr.send();
}

function logout() {
    isLoggedIn = false; // 로그아웃 시 상태 변경
    updateLoginStatus(); // 로그인 상태 업데이트
}

function updateLoginStatus() {
    const loginLink = document.getElementById('loginLink');
    const logoutLink = document.getElementById('logoutLink');
    
    if (isLoggedIn) {
        loginLink.style.display = 'none'; // 로그인 링크 숨기기
        logoutLink.style.display = 'block'; // 로그아웃 링크 보이기
    } else {
        loginLink.style.display = 'block'; // 로그인 링크 보이기
        logoutLink.style.display = 'none'; // 로그아웃 링크 숨기기
    }
}

// Enter 키를 눌렀을 때 로그인 함수 호출
function handleKeyPress(event) {
    if (event.key === 'Enter') {
        login();
    }
}
