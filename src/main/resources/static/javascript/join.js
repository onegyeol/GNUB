//----------------------------------------------------------헤더부분
function loadHeader() {
    console.log('헤더 로드를 시도합니다...'); // 디버그 로그
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '../layout/header2.html', true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            console.log('헤더가 성공적으로 로드되었습니다.');
            document.getElementById('header').innerHTML = xhr.responseText;
        } else {
            console.error('헤더 로드 오류:', xhr.status, xhr.statusText);
        }
    };
    xhr.onerror = function() {
        console.error('헤더 로드 요청 오류.');
    };
    xhr.send();
}

//---------------------------------------------------------------
let isUsernameChecked = false; // 아이디 중복 확인 여부

function checkUsername() {
    const username = document.getElementById('username').value;
    const usernameRegex = /^[a-z0-9_-]{5,20}$/; // 5~20자의 영문 소문자, 숫자, 특수기호(_, -)

    if (username.trim() === "") {
        alert("아이디를 입력해 주세요.");
        return; // 함수 종료
    }

    if (!usernameRegex.test(username)) {
        alert("아이디: 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
        return; // 함수 종료
    }

    if (username === "운영자") {
        alert("운영자: 안되니까 딴 거 해라");
        isUsernameChecked = false; // 중복일 경우 체크 해제
    } else {
        alert("사용 가능한 아이디입니다.");
        isUsernameChecked = true; // 중복이 아닐 경우 체크
    }
    checkCheckboxes(); // 체크박스 상태 확인
}

function checkPasswordMatch() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const statusMessage = document.getElementById('statusMessage');
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,16}$/; // 대문자, 소문자, 숫자 포함

    if (!passwordRegex.test(password)) {
        statusMessage.textContent = '비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.';
        statusMessage.className = 'status-message error';
        return; // 함수 종료
    }

    if (password === confirmPassword) {
        statusMessage.textContent = '비밀번호가 일치합니다.';
        statusMessage.className = 'status-message';
    } else {
        statusMessage.textContent = '비밀번호가 일치하지 않습니다.';
        statusMessage.className = 'status-message error';
    }
}

function showAlert() {
    const name = document.getElementById('name').value;
    const college = document.getElementById('college').value;
    const department = document.getElementById('department').value;
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (!name || !college || !department || !username || !password || !confirmPassword) {
        alert("모든 항목을 입력하세요.");
        return; // 함수 종료
    }

    const termsCheckboxes = document.querySelectorAll('.terms-container input[type="checkbox"]');
    const allChecked = Array.from(termsCheckboxes).every(checkbox => checkbox.checked);
    if (!allChecked) {
        alert("모든 약관에 동의해야 합니다.");
        return; // 함수 종료
    }

    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        return; // 함수 종료
    }

    alert("회원가입을 환영합니다!");
    window.location.href = 'index.html'; // index.html로 이동
}

function checkCheckboxes() {
    const termsCheckboxes = document.querySelectorAll('.terms-container input[type="checkbox"]');
    const registerButton = document.getElementById('registerButton');

    const allChecked = Array.from(termsCheckboxes).every(checkbox => checkbox.checked);
    registerButton.disabled = !(allChecked && isUsernameChecked);
}

window.onload = function() {
    loadHeader(); // 헤더 로드
    loadTerms1(); // 페이지가 로드될 때 이용약관1을 가져옵니다.
    loadTerms2(); // 페이지가 로드될 때 이용약관2를 가져옵니다.

    const termsCheckboxes = document.querySelectorAll('.terms-container input[type="checkbox"]');
    termsCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', checkCheckboxes);
    });
};

function updateDepartments() {
    const college = document.getElementById('college').value;
    const departmentSelect = document.getElementById('department');
    departmentSelect.innerHTML = ''; // 이전 옵션 제거

    // 기본 옵션 추가
    const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.textContent = '선택하세요'; // 기본 텍스트
    departmentSelect.appendChild(defaultOption);

    let departments = [];

    switch (college) {
        case 'humanities':
            departments = [
                "영어영문학부 영어영문학전공",
                "영어영문학부 영어전공",
                "국어국문학과",
                "독어독문학과",
                "러시아학과",
                "민속예술무용학과",
                "불어불문학과",
                "사학과",
                "중어중문학과",
                "철학과",
                "한문학과"
            ];
            break;
        case 'social_science':
            departments = [
                "경제학부",
                "사회복지학부",
                "미디어커뮤니케이션학과",
                "사회학과",
                "아동가족학과",
                "정치외교학과",
                "행정학과",
                "심리학과"
            ];
            break;
        case 'natural_sciences':
            departments = [
                "생명과학부",
                "물리학과",
                "수학과",
                "식품영양학과",
                "의류학과",
                "정보통계학과",
                "제약공학과",
                "지질과학과",
                "항노화신소재과학과",
                "화학과"
            ];
            break;
        case 'business':
            departments = [
                "경영학부",
                "회계세무학부",
                "경영정보학과",
                "국제통상학과",
                "산업경영학과",
                "스마트유통물류학과"
            ];
            break;
        case 'engineering':
            departments = [
                "건축공학부",
                "기계공학부",
                "나노/신소재공학부 고분자공학전공",
                "나노/신소재공학부 금속재료공학전공",
                "나노/신소재공학부 세라믹공학전공",
                "산업시스템공학부",
                "건축학과",
                "기계융합공학과",
                "도시공학과",
                "미래자동차공학과",
                "에너지공학과",
                "토목공학과",
                "화학공학과"
            ];
            break;
        // 기타 단과대학에 대한 경우도 추가할 수 있음
    }

    // 학과 리스트 추가
    departments.forEach(department => {
        const option = document.createElement('option');
        option.value = department;
        option.textContent = department;
        departmentSelect.appendChild(option);
    });
}

//----------------------------------------------------------------------------------------이용약관
function loadTerms1() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '../text/이용약관1.txt', true); // 이용약관1.txt 파일을 가져옵니다.
    xhr.onload = function() {
        if (xhr.status === 200) {
            document.getElementById('termsContent1').innerHTML = xhr.responseText; // 가져온 내용을 삽입
        } else {
            console.error('Error loading terms1:', xhr.statusText);
        }
    };
    xhr.send();
}

function loadTerms2() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '../text/이용약관2.txt', true); // 이용약관2.txt 파일을 가져옵니다.
    xhr.onload = function() {
        if (xhr.status === 200) {
            document.getElementById('termsContent2').innerHTML = xhr.responseText; // 가져온 내용을 삽입
        } else {
            console.error('Error loading terms2:', xhr.statusText);
        }
    };
    xhr.send();
}
