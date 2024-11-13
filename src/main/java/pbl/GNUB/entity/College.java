package pbl.GNUB.entity;

public enum College {
    Humanities("인문대학"),
    Social_Science("사회과학대학"),
    Natural_Sciences("자연과학대학"),
    Business_Administration("경영대학"),
    Engineering("공과대학"),
    IT_Engineering("IT공과대학"),
    Space_and_Aeronautics("우주항공대학"),
    Agriculture_and_Life_Science("농업생명과학대학"),
    Law("법과대학"),
    Education("사범대학"),
    Veterinary_Medicine("수의과대학"),
    Medicine("의과대학"),
    Nursing("간호대학"),
    Marine_Science("해양과학대학"),
    Pharmacy("약학대학"),
    Civil_and_Environmental_Engineering("건설환경공과대학"),
    Main_Administration("본부대학");

    private final String koreanName;

    College(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() { // 한글 이름으로 반환
        return koreanName;
    }
}
