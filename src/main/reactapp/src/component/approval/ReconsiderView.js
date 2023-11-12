import styles from '../../css/approval.css';
import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

export default function ReconsiderView(props){


    const [ reconsiderList, setReconsiderList ] = useState( [] )

    // 상신내역 요청
    useEffect( () => {

        axios.get( '/approval/getReconsiderHistory' )
            .then( result => {
                console.log( result.data );
                setReconsiderList( result.data );
        })

    }, [])

    // 결재 타입에 따른 결재의 유형출력
    const getTypeName = (aprvType) => {

        switch( aprvType ){

            case 1: return (<>신규 사원등록</>)
            case 2: return (<>사원정보 수정</>)
            case 3: return (<>퇴사처리</>)
            case 4: return (<>사원 부서이동</>)
            case 5: return (<>사원 직급변경</>)
            case 6: return (<>사원 휴직등록</>)
            case 7: return (<>사원 휴직기간/지급유형 변경</>)
            case 8: return (<>사원 병가신청</>)
            case 9: return (<>사원 병가기간/지급유형 변경</>)
            case 10: return (<>사원 연차신청</>)
            case 11: return (<>사원 연차기간/지급유형 변경</>)
            case 12: return (<>신규 프로젝트 등록</>)
            case 13: return (<>프로젝트 수정</>)
            case 14: return (<>프로젝트 삭제</>)
            case 15: return (<>프로젝트팀 투입</>)
            case 16: return (<>프로젝트팀 투입/종료일 수정</>)
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


    return (<>

        <h3> 상신관리 </h3>

        <table className="reconsiderTable">

            <tr>
                <th> 결재번호 </th>
                <th> 상신유형 </th>
                <th> 내용 </th>
                <th> 상신일시 </th>
                <th> 진행상태 </th>
            </tr>
            { reconsiderList.map( r =>(
                <tr>
                    <td> 제 {r.apState}호 </td>
                    <td> { getTypeName( r.aprvType ) } </td>
                    <td> {r.aprvCont} </td>
                    <td>
                        <span> { (r.cdate.split("T"))[0] } </span>
                        <span> { ((r.cdate.split("T"))[1].split("."))[0].substring(0, 5) } </span>
                    </td>
                    <td> {
                        r.apState == 1 ? <> 결재완료 </> :  r.apState == 2 ? <> 반려 </> : <> 검토중 </>
                    }
                    </td>
                </tr>
            ))}

        </table>

    </>)

}