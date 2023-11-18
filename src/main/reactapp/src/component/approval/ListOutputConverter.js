// Approval 출력 시 공통 사용 메소드


// 타입에 따른 결재의 유형 변환
export const getTypeName = (aprvType) => {

    switch( aprvType ){

        case 1: return (<>신규 사원등록</>)
        case 2: return (<>사원정보 수정</>)
        case 3: return (<>퇴사처리</>)
        case 4: return (<>사원 부서이동</>)
        case 5: return (<>사원 직급변경</>)
        case 6: return (<>사원 휴직등록</>)
        case 7: return (<>사원 휴직 수정</>)
        case 8: return (<>사원 병가신청</>)
        case 9: return (<>사원 병가 수정</>)
        case 10: return (<>사원 연차신청</>)
        case 11: return (<>사원 연차 수정</>)
        case 12: return (<>신규 프로젝트 등록</>)
        case 13: return (<>프로젝트 수정</>)
        case 14: return (<>프로젝트 삭제</>)
        case 15: return (<>프로젝트팀 투입</>)
        case 16: return (<>프로젝트팀 투입일자 수정</>)
        case 17: return (<>프로젝트팀원 삭제</>)
        case 18: return (<>급여 기본금 지급</>)
        case 19: return (<>급여 정기상여금 지급</>)
        case 20: return (<>급여 특별상여금 지급</>)
        case 21: return (<>급여 성과금 지급</>)
        case 22: return (<>급여 명절휴가비 지급</>)
        case 23: return (<>급여 퇴직금 지급</>)
        case 24: return (<>급여 경조사비 지급</>)
        case 25: return (<>급여 연가보상비 지급</>)

    }
}






















