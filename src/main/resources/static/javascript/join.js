// 이용약관을 비동기로 불러오는 함수
function loadTermsFromTxt(elementId, filePath) {
    fetch(filePath)
        .then(response => response.text())
        .then(data => {
            document.getElementById(elementId).innerText = data;
        })
        .catch(error => {
            console.error('Error loading terms:', error);
            document.getElementById(elementId).innerText = "이용약관을 불러오는데 실패했습니다.";
        });
}

// 페이지가 로드되면 이용약관을 불러옴
window.onload = function() {
    loadTermsFromTxt("termsContent1", "../Terms/이용약관1.txt");  
    loadTermsFromTxt("termsContent2", "../Temrs/이용약관2.txt");  
};



// 단과대 선택 함수
function updateDepartments() {
    const college = document.getElementById('college').value;
    const departmentSelect = document.getElementById('department');
    // 학과 초기화
    departmentSelect.innerHTML = '<option value="">단과대를 선택하세요</option>';

    const departments={
        Humanities:["영어영문학부 영어영문학전공","영어영문학부 영어전공","국어국문학과","독어독문학과","러시아학과","민속예술무용학과","불어불문학과","사학과","중어중문학과","철학과","한문학과"],
        Social_Science:["경제학부","사회복지학부","미디어커뮤니케이션학과","사회학과","아동가족학과","정치외교학과","행정학과","심리학과"],
        Natural_Sciences:["생명과학부","물리학과","수학과","식품영양양학과","의류학과","정보통계학과","제약공학과","지질과학과","항노화신소재과학과","화학과"],
        Business_Administration:["경영학부","회계세무학부","경영정보학과","국제통상학과","산업경영학과","스마트유통물류학과"],
        Engineering:["건축공학부","기계공학부","나노/신소재공학부 고분자공학전공","나노/신소재공학부 금속재료공학전공","나노/신소재공학부 세라믹공학전공","산업시스템공학부","건축학과","기계융합공학과","도시공학과","미래자동차공학과","에너지공학과","토목공학과","화학공학과",],
        IT_Engineering:["메카트로닉스공학부","전자공학부","반도체공학과","소프트웨어공학과","전기공학과","제어로봇공학과","컴퓨터공학과","AI정보공학과"],
        Space_and_Aeronautics:["항공우주공학부"],
        Agriculture_and_Life_Science:["식품자원경제학과","동물생명융합학부","식품공학과","원예과학부", "축산과학부", "환경산림과학부","농학과","스마트농산업학과","식물의학과", "환경생명화학과","환경재료과학과","생물산업기계공학과","지역시스템공학과" ],
        Law:["법학과"],
        Education:["교육학과","국어교육과","역사교육과","영어교육과","유아교육과","윤리교육과", "일반사회교육과","일어교육과","지리교육과","물리교육과","생물교육과","수학교육과","화학교육과","미술교육과","음악교육과","체육교육과"],
        Veterinary_Medicine:["수의예과","수의학과"],
        Medicine:["의예과","의학과"],
        Nursing:["간호학과"],
        Marine_Science:["해양수산경영학과","미래산업융합학과","수산생명의학과","해양경찰시스템학과","해양생명과학과","기계시스템공학과","스마트에너지기계공학과", "조선해양공학과","해양식품공학과","해양토목공학과", "해양환경공학과"],
        Pharmacy:["약학과"],
        Civil_and_Environmental_Engineering:["건설시스템공학과","인테리어재료공학과","조경학과","환경공학과","디자인비즈니스학과"],
        Main_Administration:["휴먼헬스케어학과"]

    }

    // 선택한 단과대에 따른 학과 목록 업데이트
    if (departments[college]) {
        departments[college].forEach(function(department) {
            const option = document.createElement('option');
            option.value = department;
            option.text = department;
            departmentSelect.add(option);
        });
    }   
}

// 이메일 중복 확인 (예시)
function checkUsername() {
    const email = document.getElementsByName("email")[0].value;

    if (!email) {
        alert("이메일을 입력하세요.");
        return;
    }

    // 서버에 이메일 중복 확인 요청 보내기
    fetch(`/check-email?email=${encodeURIComponent(email)}`)
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                alert("이미 사용 중인 이메일입니다.");
            } else {
                alert("사용 가능한 이메일입니다.");
            }
        })
        .catch(error => {
            console.error("Error checking email:", error);
            alert("이메일 중복 확인 중 오류가 발생했습니다.");
        });
}

// 비밀번호 일치 여부 확인
function checkPasswordMatch() {
    const password = document.getElementsByName("password")[0].value;
    const confirmPassword = document.getElementsByName("confirmPassword")[0].value;
    const statusMessage = document.getElementById("statusMessage");

    if (password !== confirmPassword) {
        statusMessage.innerText = "비밀번호가 일치하지 않습니다.";
        statusMessage.style.color = "red";
    } else {
        statusMessage.innerText = "비밀번호가 일치합니다.";
        statusMessage.style.color = "green";
    }
}

// 회원가입 버튼 클릭 시 알림
function showAlert() {
    alert("회원가입이 완료되었습니다.");
}



/*
function checkPasswordMatch() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const statusMessage = document.getElementById('statusMessage');
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,16}$/;

    if (!passwordRegex.test(password)) {
        statusMessage.textContent = '비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.';
        statusMessage.className = 'status-message error';
    } else if (password === confirmPassword) {
        statusMessage.textContent = '비밀번호가 일치합니다.';
        statusMessage.className = 'status-message';
    } else {
        statusMessage.textContent = '비밀번호가 일치하지 않습니다.';
        statusMessage.className = 'status-message error';
    }
}

function showAlert() {
    const name = document.getElementById('name').value.trim();
    const college = document.getElementById('college').value;
    const department = document.getElementById('department').value;
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (!name || !college || !department || !username || !password || !confirmPassword) {
        alert("모든 항목을 입력하세요.");
        return;
    }

    const termsCheckboxes = document.querySelectorAll('.terms-container input[type="checkbox"]');
    const allChecked = Array.from(termsCheckboxes).every(checkbox => checkbox.checked);

    if (!allChecked) {
        alert("모든 약관에 동의해야 합니다.");
        return;
    }

    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
    }

    alert("회원가입을 환영합니다!");
    window.location.href = "@{/main}";
}

function checkCheckboxes() {
    const termsCheckboxes = document.querySelectorAll('.terms-container input[type="checkbox"]');
    const registerButton = document.getElementById('registerButton');
    const allChecked = Array.from(termsCheckboxes).every(checkbox => checkbox.checked);
    registerButton.disabled = !(allChecked && isUsernameChecked);
}


function loadTerms(elementId, url) {
    fetch(url)
        .then(response => {
            if (response.ok) return response.text();
            throw new Error(`Error loading terms: ${response.statusText}`);
        })
        .then(text => document.getElementById(elementId).innerHTML = text)
        .catch(error => console.error(error));
}
        */