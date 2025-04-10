import React, { useEffect, useState } from 'react';
import $ from 'jquery';
import './css/Signup.css';

export default function Signup() {
  const [isEmailAvailable, setIsEmailAvailable] = useState(false);
  const [statusMessage, setStatusMessage] = useState('');

  useEffect(() => {
    // load header if needed
    // load terms text from files (terms1.txt, terms2.txt)
  }, []);

  const updateDepartments = () => {
    const departments = {
      Humanities: [
        { id: 1, name: "영어영문학부 영어영문학전공" },
        { id: 2, name: "영어영문학부 영어전공" },
        { id: 3, name: "국어국문학과" },
        { id: 4, name: "독어독문학과" },
        { id: 5, name: "러시아학과" },
        { id: 6, name: "민속예술무용학과" },
        { id: 7, name: "불어불문학과" },
        { id: 8, name: "사학과" },
        { id: 9, name: "중어중문학과" },
        { id: 10, name: "철학과" },
        { id: 11, name: "한문학과" }
      ],
      Social_Science: [
        { id: 12, name: "경제학부" },
        { id: 13, name: "사회복지학부" },
        { id: 14, name: "미디어커뮤니케이션학과" },
        { id: 15, name: "사회학과" },
        { id: 16, name: "아동가족학과" },
        { id: 17, name: "정치외교학과" },
        { id: 18, name: "행정학과" },
        { id: 19, name: "심리학과" }
      ],
      Natural_Sciences: [
        { id: 20, name: "생명과학부" },
        { id: 21, name: "물리학과" },
        { id: 22, name: "수학과" },
        { id: 23, name: "식품영양양학과" },
        { id: 24, name: "의류학과" },
        { id: 25, name: "정보통계학과" },
        { id: 26, name: "제약공학과" },
        { id: 27, name: "지질과학과" },
        { id: 28, name: "항노화신소재과학과" },
        { id: 29, name: "화학과" }
      ],
      Business_Administration: [
        { id: 30, name: "경영학부" },
        { id: 31, name: "회계세무학부" },
        { id: 32, name: "경영정보학과" },
        { id: 33, name: "국제통상학과" },
        { id: 34, name: "산업경영학과" },
        { id: 35, name: "스마트유통물류학과" }
      ],
      Engineering: [
        { id: 36, name: "건축공학부" },
        { id: 37, name: "기계공학부" },
        { id: 38, name: "나노/신소재공학부 고분자공학전공" },
        { id: 39, name: "나노/신소재공학부 금속재료공학전공" },
        { id: 40, name: "나노/신소재공학부 세라믹공학전공" },
        { id: 41, name: "산업시스템공학부" },
        { id: 42, name: "건축학과" },
        { id: 43, name: "기계융합공학과" },
        { id: 44, name: "도시공학과" },
        { id: 45, name: "미래자동차공학과" },
        { id: 46, name: "에너지공학과" },
        { id: 47, name: "토목공학과" },
        { id: 48, name: "화학공학과" }
      ],
      IT_Engineering: [
        { id: 49, name: "메카트로닉스공학부" },
        { id: 50, name: "전자공학부" },
        { id: 51, name: "반도체공학과" },
        { id: 52, name: "소프트웨어공학과" },
        { id: 53, name: "전기공학과" },
        { id: 54, name: "제어로봇공학과" },
        { id: 55, name: "컴퓨터공학과" },
        { id: 56, name: "AI정보공학과" }
      ],
      Space_and_Aeronautics: [
        { id: 57, name: "항공우주공학부" }
      ],
      Agriculture_and_Life_Science: [
        { id: 58, name: "식품자원경제학과" },
        { id: 59, name: "동물생명융합학부" },
        { id: 60, name: "식품공학과" },
        { id: 61, name: "원예과학부" },
        { id: 62, name: "축산과학부" },
        { id: 63, name: "환경산림과학부" },
        { id: 64, name: "농학과" },
        { id: 65, name: "스마트농산업학과" },
        { id: 66, name: "식물의학과" },
        { id: 67, name: "환경생명화학과" },
        { id: 68, name: "환경재료과학과" },
        { id: 69, name: "생물산업기계공학과" },
        { id: 70, name: "지역시스템공학과" }
      ],
      Law: [
        { id: 71, name: "법학과" }
      ],
      Education: [
        { id: 72, name: "교육학과" },
        { id: 73, name: "국어교육과" },
        { id: 74, name: "역사교육과" },
        { id: 75, name: "영어교육과" },
        { id: 76, name: "유아교육과" },
        { id: 77, name: "윤리교육과" },
        { id: 78, name: "일반사회교육과" },
        { id: 79, name: "일어교육과" },
        { id: 80, name: "지리교육과" },
        { id: 81, name: "물리교육과" },
        { id: 82, name: "생물교육과" },
        { id: 83, name: "수학교육과" },
        { id: 84, name: "화학교육과" },
        { id: 85, name: "미술교육과" },
        { id: 86, name: "음악교육과" },
        { id: 87, name: "체육교육과" }
      ],
      Veterinary_Medicine: [
        { id: 88, name: "수의예과" },
        { id: 89, name: "수의학과" }
      ],
      Medicine: [
        { id: 90, name: "의예과" },
        { id: 91, name: "의학과" }
      ],
      Nursing: [
        { id: 92, name: "간호학과" }
      ],
      Marine_Science: [
        { id: 93, name: "해양수산경영학과" },
        { id: 94, name: "미래산업융합학과" },
        { id: 95, name: "수산생명의학과" },
        { id: 96, name: "해양경찰시스템학과" },
        { id: 97, name: "해양생명과학과" },
        { id: 98, name: "기계시스템공학과" },
        { id: 99, name: "스마트에너지기계공학과" },
        { id: 100, name: "조선해양공학과" },
        { id: 101, name: "해양식품공학과" },
        { id: 102, name: "해양토목공학과" },
        { id: 103, name: "해양환경공학과" }
      ],
      Pharmacy: [
        { id: 104, name: "약학과" }
      ],
      Civil_and_Environmental_Engineering: [
        { id: 105, name: "건설시스템공학과" },
        { id: 106, name: "인테리어재료공학과" },
        { id: 107, name: "조경학과" },
        { id: 108, name: "환경공학과" },
        { id: 109, name: "디자인비즈니스학과" }
      ]
      ,
      Main_Administration: [
        { id: 110, name: "휴먼헬스케어학과" }
      ]
    };

    const selectedCollege = $('#college').val();
    const $department = $('#department');
    $department.empty().append('<option value="">단과대를 선택하세요</option>');
    if (departments[selectedCollege]) {
      departments[selectedCollege].forEach(d => {
        $department.append(`<option value="${d.id}">${d.name}</option>`);
      });
    }
  };

  const emailCheck = () => {
    const email = $('#email').val().trim();
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!regex.test(email)) return alert('올바른 이메일 형식이 아닙니다.');

    $.get('/member/check-email', { email }, (res) => {
      if (res.exists) {
        alert('이미 사용중인 이메일입니다.');
        setIsEmailAvailable(false);
      } else {
        alert('사용 가능한 이메일입니다.');
        setIsEmailAvailable(true);
      }
    }).fail(() => alert('중복 확인 실패'));
  };

  const checkPasswordMatch = () => {
    const pw = $('#password').val();
    const confirmPw = $('#confirmPassword').val();
    const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\[\]{};':"\\|,.<>/?]).{8,16}$/;


    if (!regex.test(pw)) {
      setStatusMessage('비밀번호: 8~16자의 영문, 숫자, 특수문자를 사용해 주세요.');
    } else if (pw !== confirmPw) {
      setStatusMessage('비밀번호가 일치하지 않습니다.');
    } else {
      setStatusMessage('비밀번호가 일치합니다.');
    }
  };

  const handleSubmit = async () => {
    const fields = ['#name', '#college', '#department', '#email', '#password', '#confirmPassword'];
    const empty = fields.some(selector => !$(selector).val());
    if (empty) return alert('모든 항목을 입력하세요.');
    if (!isEmailAvailable) return alert('이메일 중복 확인을 해주세요.');
    if ($('#password').val() !== $('#confirmPassword').val()) return alert('비밀번호가 일치하지 않습니다.');
  
    const memberData = {
      name: $('#name').val(),
      college: $('#college').val(),
      departmentId: $('#department').val(),
      email: $('#email').val(),
      password: $('#password').val(),
    };
  
    try {
      const res = await fetch('http://localhost:8080/api/member/new', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(memberData),
      });
      
  
      if (res.ok) {
        alert('회원가입이 완료되었습니다!');
        window.location.href = '/main'; // 리디렉션 등 원하는 동작
      } else {
        const err = await res.json();
        alert(`회원가입 실패: ${err.message}`);
      }
    } catch (e) {
      alert('회원가입 중 오류가 발생했습니다.');
    }
  };
  

  return (
    <div className='join-wrapper'>
      <form id="joinForm" className="join-form" action="/member/new" method="post">
        <h2>회원가입</h2>
        
        <label>성명</label>
        <input type="text" id="name" name="name" placeholder="성명" required />


          <label>단과대</label>
          <select id="college" onChange={updateDepartments}>
            <option value="">선택하세요</option>
            <option value="Humanities">인문대학</option>
            <option value="Social_Science">사회과학대학</option>
            <option value="Natural_Sciences">자연과학대학</option>
            <option value="Business_Administration">경영대학</option>
            <option value="Engineering">공과대학</option>
            <option value="IT_Engineering">IT 공과대학</option>
            <option value="Space_and_Aeronautics">우주항공대학</option>
            <option value="Agriculture_and_Life_Science">농업생명과학대학</option>
            <option value="Law">법과대학</option>
            <option value="Education">사범대학</option>
            <option value="Veterinary_Medicine">수의과대학</option>
            <option value="Medicine">의과대학</option>
            <option value="Nursing">간호대학</option>
            <option value="Marine_Science">해양과학대학</option>
            <option value="Pharmacy">약학대학</option>
            <option value="Civil_and_Environmental_Engineering">건설환경공과대학</option>
            <option value="Main_Administration">본부대학</option>
          </select>

          <label>학과</label>
          <select id="department" name="departmentId">
            <option value="">단과대를 선택하세요</option>
          </select>

          <label>이메일</label>
          <div className="email-row">
            <input type="text" id="email" name="email" className="email-input" placeholder="이메일 입력" required />
            <button type="button" className="email-check-button" onClick={emailCheck}>중복 확인</button>
          </div>

          <label>비밀번호</label>
          <input type="password" id="password" name="password" required />

          <label>비밀번호 확인</label>
          <input type="password" id="confirmPassword" name="confirmPassword" required onInput={checkPasswordMatch} />

          <div className={`status-message ${statusMessage.includes('일치하지') ? 'error' : ''}`}>
            {statusMessage}
          </div>

          <button type="button" className="submit-button" onClick={handleSubmit}>회원가입</button>
        </form>
      </div>
  );
}