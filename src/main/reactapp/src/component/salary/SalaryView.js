import { useSearchParams , Link } from 'react-router-dom'
import axios from 'axios'
import { useState , useEffect } from 'react'
import styles from '../../css/Table.css';

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
    <div className="contentBox">
        <div className="pageinfo"><span className="lv0">급여관리</span> >  <span className="lv1">급여지급 상세보기</span></div>
            <div className="emp_regs_content">
                <div style={{textAlign:'center'}}><img class="logoA" src="../../../logo_no.png"/></div>
                <hr class="hr00"/>
                <div class="eregInputBox pdt10_0">
                    <div class="w40 ls5"> 급여식별번호</div>
                    <div class="">{ slryNo }</div>
                </div>
                <div class="eregInputBox">
                    <div class="w40 ls5"> 급여지급날짜</div>
                    <div class="">{ board.slryDate }</div>
                </div>
                <div class="eregInputBox">
                    <div class="w40 ls5"> 급여지급금액</div>
                    <div class="">{ board.slryPay }</div>
                </div>
                <div class="eregInputBox">
                    <div class="w40 ls5"> 급여지급타입</div>
                    <div class=""> {getSlryTypeLabel(board.slryType)}</div>
                </div>
                <div class="eregInputBox">
                    <div class="w40 ls18"> 결재번호</div>
                    <div class=""> { board.aprvNo }</div>
                </div>
                <div class="eregInputBox">
                    <div class="w40 ls18"> 사원번호</div>
                    <div class="">{ board.empNo }</div>
                </div>

                {/* 삭제 와 수정 은 본인(본인확인) 만 가능 */}
                {/*    삼항연산자         조건 ? (<>참일때</>) : (<>거짓일때</>)           */}

            </div>
    </div>
    </>)
}