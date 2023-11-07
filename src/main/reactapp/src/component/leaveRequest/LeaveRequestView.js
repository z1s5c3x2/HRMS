import { useSearchParams , Link } from 'react-router-dom'
import axios from 'axios'
import { useState , useEffect } from 'react'

export default function LeaveRequestView( props ){
    // 1. HTTP 경로상의 쿼리스트링 매개변수 호출
    const [ searchParams, setSearchParams ] = useSearchParams(); console.log( searchParams );
    const lrqNo = searchParams.get('lrqNo'); console.log( lrqNo );

    // 2. 현재 게시물의 정보를 가지는 상태관리 변수
    const [ board , setBoard ] = useState( { } )
    /*
    // 3. 현재 로그인된 회원의 번호
    const login = JSON.parse( sessionStorage.getItem('login_token') ) ;
    const loginMno = login != null ? login.mno : null ;
    */
    // 4. 개별 게시물 axios [ 실행조건 : 컴포넌트 최초 1번 실행 ]
    const onGet = (e) => {
        axios.get( '/leaveRequest/get' , { params : { lrqNo : lrqNo } }  )
            .then( r => { setBoard(r.data); })
    }
    useEffect( ()=>{ onGet() } , [] )

        function getlrqTypeLabel(lrqType) {
                      switch (lrqType) {
                        case 1:
                          return "휴직";
                        case 2:
                          return "연차";
                        case 3:
                          return "병가";
                        default:
                          return "알 수 없는 타입";
                      }
         }
       function getlrqSrtypeLabel(lrqSrtype) {
                          switch (lrqSrtype) {
                            case 0:
                              return "무급";
                            case 1:
                              return "유급";
                            default:
                              return "알 수 없는 타입";
                          }
        }

    return(<>
        <div>

            <h3> 연차 상세보기 </h3>
            <div> 연차 식별번호 : { lrqNo }</div>
            <div> 연차 타입 : {getlrqTypeLabel(board.lrqType)}</div>
            <div> 연차 시작날짜 : { board.lrqSt }</div>
            <div> 연차 종료날짜: { board.lrqEnd }</div>
            <div> 급여여부 : {getlrqSrtypeLabel(board.lrqSrtype)}</div>
            <div> 사원 번호 : { board.empNo }</div>

        </div>
    </>)
}