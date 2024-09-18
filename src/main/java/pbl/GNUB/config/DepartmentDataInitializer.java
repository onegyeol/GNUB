package pbl.GNUB.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import pbl.GNUB.entity.College;
import pbl.GNUB.service.DepartmentService;

@Component
public class DepartmentDataInitializer {

    @Autowired
    private DepartmentService departmentService;

    @PostConstruct
    public void init(){ // 각 단과대 소속 학과 저장
        // 인문대학
        departmentService.saveDepartment("영어영문학부 영어영문학전공", College.Humanities);
        departmentService.saveDepartment("영어영문학부 영어전공", College.Humanities);
        departmentService.saveDepartment("국어국문학과", College.Humanities);
        departmentService.saveDepartment("독어독문학과", College.Humanities);
        departmentService.saveDepartment("러시아학과", College.Humanities);
        departmentService.saveDepartment("민속예술무용학과", College.Humanities);
        departmentService.saveDepartment("불어불문학과", College.Humanities);
        departmentService.saveDepartment("사학과", College.Humanities);
        departmentService.saveDepartment("중어중문학과", College.Humanities);
        departmentService.saveDepartment("철학과", College.Humanities);
        departmentService.saveDepartment("한문학과", College.Humanities);

        // 사회과학대학
        departmentService.saveDepartment("경제학부", College.Social_Science);
        departmentService.saveDepartment("사회복지학부", College.Social_Science);
        departmentService.saveDepartment("미디어커뮤니케이션학과", College.Social_Science);
        departmentService.saveDepartment("사회학과", College.Social_Science);
        departmentService.saveDepartment("아동가족학과", College.Social_Science);
        departmentService.saveDepartment("정치외교학과", College.Social_Science);
        departmentService.saveDepartment("행정학과", College.Social_Science);
        departmentService.saveDepartment("심리학과", College.Social_Science);

        // 자연과학대학
        departmentService.saveDepartment("생명과학부", College.Natural_Sciences);
        departmentService.saveDepartment("물리학과", College.Natural_Sciences);
        departmentService.saveDepartment("수학과", College.Natural_Sciences);
        departmentService.saveDepartment("식품영양양학과", College.Natural_Sciences);
        departmentService.saveDepartment("의류학과", College.Natural_Sciences);
        departmentService.saveDepartment("정보통계학과", College.Natural_Sciences);
        departmentService.saveDepartment("제약공학과", College.Natural_Sciences);
        departmentService.saveDepartment("지질과학과", College.Natural_Sciences);
        departmentService.saveDepartment("항노화신소재과학과", College.Natural_Sciences);
        departmentService.saveDepartment("화학과", College.Natural_Sciences);

        // 경영대학
        departmentService.saveDepartment("경영학부", College.Business_Administration);
        departmentService.saveDepartment("회계세무학부", College.Business_Administration);
        departmentService.saveDepartment("경영정보학과", College.Business_Administration);
        departmentService.saveDepartment("국제통상학과", College.Business_Administration);
        departmentService.saveDepartment("산업경영학과", College.Business_Administration);
        departmentService.saveDepartment("스마트유통물류학과", College.Business_Administration);

        // 공과대학
        departmentService.saveDepartment("건축공학부", College.Engineering);
        departmentService.saveDepartment("기계공학부", College.Engineering);
        departmentService.saveDepartment("나노/신소재공학부 고분자공학전공", College.Engineering);
        departmentService.saveDepartment("나노/신소재공학부 금속재료공학전공", College.Engineering);
        departmentService.saveDepartment("나노/신소재공학부 세라믹공학전공", College.Engineering);
        departmentService.saveDepartment("산업시스템공학부", College.Engineering);
        departmentService.saveDepartment("건축학과", College.Engineering);
        departmentService.saveDepartment("기계융합공학과", College.Engineering);
        departmentService.saveDepartment("도시공학과", College.Engineering);
        departmentService.saveDepartment("미래자동차공학과", College.Engineering);
        departmentService.saveDepartment("에너지공학과", College.Engineering);
        departmentService.saveDepartment("토목공학과", College.Engineering);
        departmentService.saveDepartment("화학공학과", College.Engineering);

        // IT 공과대학
        departmentService.saveDepartment("메카트로닉스공학부", College.IT_Engineering);
        departmentService.saveDepartment("전자공학부", College.IT_Engineering);
        departmentService.saveDepartment("반도체공학과", College.IT_Engineering);
        departmentService.saveDepartment("소프트웨어공학과", College.IT_Engineering);
        departmentService.saveDepartment("전기공학과", College.IT_Engineering);
        departmentService.saveDepartment("제어로봇공학과", College.IT_Engineering);
        departmentService.saveDepartment("컴퓨터공학과", College.IT_Engineering);
        departmentService.saveDepartment("AI정보공학과", College.IT_Engineering);

        // 우주항공대학
        departmentService.saveDepartment("항공우주공학부", College.Space_and_Aeronautics);

        // 농업생명과학대학
        departmentService.saveDepartment("식품자원경제학과", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("동물생명융합학부", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("식품공학과", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("원예과학부", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("축산과학부", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("환경산림과학부", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("농학과", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("스마트농산업학과", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("식물의학과", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("환경생명화학과", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("환경재료과학과", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("생물산업기계공학과", College.Agriculture_and_Life_Science);
        departmentService.saveDepartment("지역시스템공학과", College.Agriculture_and_Life_Science);
    
        // 법과대학
        departmentService.saveDepartment("법학과", College.Law);

        // 사범대학
        departmentService.saveDepartment("교육학과", College.Education);
        departmentService.saveDepartment("국어교육과", College.Education);
        departmentService.saveDepartment("역사교육과", College.Education);
        departmentService.saveDepartment("영어교육과", College.Education);
        departmentService.saveDepartment("유아교육과", College.Education);
        departmentService.saveDepartment("윤리교육과", College.Education);
        departmentService.saveDepartment("일반사회교육과", College.Education);
        departmentService.saveDepartment("일어교육과", College.Education);
        departmentService.saveDepartment("지리교육과", College.Education);
        departmentService.saveDepartment("물리교육과", College.Education);
        departmentService.saveDepartment("생물교육과", College.Education);
        departmentService.saveDepartment("수학교육과", College.Education);
        departmentService.saveDepartment("화학교육과", College.Education);
        departmentService.saveDepartment("미술교육과", College.Education);
        departmentService.saveDepartment("음악교육과", College.Education);
        departmentService.saveDepartment("체육교육과", College.Education);

        // 수의과대학
        departmentService.saveDepartment("수의예과", College.Veterinary_Medicine);
        departmentService.saveDepartment("수의학과", College.Veterinary_Medicine);

        // 의과대학
        departmentService.saveDepartment("의예과", College.Medicine);
        departmentService.saveDepartment("의학과", College.Medicine);

        // 간호대학
        departmentService.saveDepartment("간호학과", College.Nursing);

        // 해양과학대학
        departmentService.saveDepartment("해양수산경영학과", College.Marine_Science);
        departmentService.saveDepartment("미래산업융합학과", College.Marine_Science);
        departmentService.saveDepartment("수산생명의학과", College.Marine_Science);
        departmentService.saveDepartment("해양경찰시스템학과", College.Marine_Science);
        departmentService.saveDepartment("해양생명과학과", College.Marine_Science);
        departmentService.saveDepartment("기계시스템공학과", College.Marine_Science);
        departmentService.saveDepartment("스마트에너지기계공학과", College.Marine_Science);
        departmentService.saveDepartment("조선해양공학과", College.Marine_Science);
        departmentService.saveDepartment("해양식품공학과", College.Marine_Science);
        departmentService.saveDepartment("해양토목공학과", College.Marine_Science);
        departmentService.saveDepartment("해양환경공학과", College.Marine_Science);

        // 약학대학
        departmentService.saveDepartment("약학과", College.Pharmacy);

        // 건설환경공과대학
        departmentService.saveDepartment("건설시스템공학과", College.Civil_and_Environmental_Engineering);
        departmentService.saveDepartment("인테리어재료공학과", College.Civil_and_Environmental_Engineering);
        departmentService.saveDepartment("조경학과", College.Civil_and_Environmental_Engineering);
        departmentService.saveDepartment("환경공학과", College.Civil_and_Environmental_Engineering);
        departmentService.saveDepartment("디자인비즈니스학과", College.Civil_and_Environmental_Engineering);
        
        // 본부대학
        departmentService.saveDepartment("휴먼헬스케어학과", College.Main_Administration);
    }

}
