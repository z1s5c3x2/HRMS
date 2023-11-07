import { useSearchParams , Link } from 'react-router-dom'
import axios from 'axios'
import { useState , useEffect } from 'react'

export default function SalaryView( props ){
    // 1. HTTP 경로상의 쿼리스트링 매개변수 호출
    const [ searchParams, setSearchParams ] = useSearchParams(); console.log( searchParams );
    const slryNo = searchParams.get('slryNo'); console.log( slryNo );

    // 2. 현재 게시물의 정보를 가지는 상태관리 변수
    const [ board , setBoard ] = useState( { } )
    /*
    // 3. 현재 로그인된 회원의 번호
    const login = JSON.parse( sessionStorage.getItem('login_token') ) ;
    const loginMno = login != null ? login.mno : null ;
    */
    // 4. 개별 게시물 axios [ 실행조건 : 컴포넌트 최초 1번 실행 ]
    const onGet = (e) => {
        axios.get( '/salary/get' , { params : { slryNo : slryNo } }  )
            .then( r => { setBoard(r.data); })
    }
    useEffect( ()=>{ onGet() } , [] )

        function getSlryTypeLabel(slryType) {
          switch (slryType) {
            case 1:
              return "기본급";
            case 2:
              return "정기상여";
            case 3:
              return "특별상여";
            case 4:
              return "성과금";
            case 5:
              return "명절휴가비";
            case 6:
              return "퇴직금";
            default:
              return "알 수 없는 타입";
          }
        }

    return(<>
        <div>

            <h3> 급여지급 상세보기 </h3>
            <div> 급여식별번호 : { slryNo }</div>
            <div> 급여 지급 날짜 : { board.slryDate }</div>
            <div> 급여 지급 금액 : { board.slryPay }</div>
            <div> 급여 지급 타입: {getSlryTypeLabel(board.slryType)}</div>
            <div> 결재 번호 : { board.aprvNo }</div>
            <div> 사원 번호 : { board.empNo }</div>

            {/* 삭제 와 수정 은 본인(본인확인) 만 가능 */}
            {/*    삼항연산자         조건 ? (<>참일때</>) : (<>거짓일때</>)           */}

        </div>
    </>)
}