import axios from 'axios';
import { useState , useEffect } from 'react';
// ------------------- mui table 관련 컴포넌트 import------------ //
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Pagination from "@mui/material/Pagination";

export default function LeaveRequestList( props ){
   // 0. 컴포넌트 상태변수 관리
       let [ pageDto , setPageDto ] = useState( {
               someList : [] , totalPages : 0 , totalCount : 0
           } );

       // 0. 스프링에게 전달할 객체
               const [ pageInfo , setPageInfo ] = useState( {
                   page : 1 , key : 'empNo' , keyword : '' , view : 5 , empRk : 0 , dptmNo : 0 , lrqType: 0 , lrqSrtype : 2 , DateSt : '' , DateEnd : ''
               }); console.log( pageInfo );

        // 특정 레코드 클릭시 해당 레코드 상세보기
         const loadView = ( lrqNo ) => {
                window.location.href = '/leaveRequest/view?lrqNo='+lrqNo
                // 조회수 처리 할 예정
            }
         // 1-1.  axios를 이용한 스프링의 컨트롤과 통신
             const getlrq = (e) => {
                 axios.get('/leaveRequest/getAll' , { params : pageInfo } )
                       .then( r =>{  // r.data : PageDto  // r.data.boardDtos : PageDto 안에 있는 boardDtos
                            setPageDto( r.data ); // 응답받은 모든 게시물을 상태변수에 저장 // setState : 해당 컴포넌트가 업데이트(새로고침/재랜더링/return재실행)
                         });
             }

            // 1-2 컴포넌트가 생성될 때 // + 의존성 배열 : page 변경될떄 // + 의존성 배열 : view 변경될 때
                useEffect( () => { getlrq(); }, [ pageInfo.page , pageInfo.view , pageInfo.empRk , pageInfo.dptmNo , pageInfo.lrqType ,pageInfo.lrqSrtype , pageInfo.DateSt , pageInfo.DateEnd ] );

                // 2. 페이지 번호를 클릭했을 때.
                const onPageSelect = ( e , value )=>{
                    pageInfo.page = value; // 클릭한 페이지번호로 변경
                    setPageInfo( { ...pageInfo } ); // 새로고침 [ 상태변수의 주소 값이 바뀌면 재랜더링 ]
                }
                // 3. 검색 버튼을 눌렀을 때.
                const onSearch = ( e ) => {
                    setPageInfo({ ...pageInfo , page :1 } ); // 첫페이지 1페이지 초기화
                    getlrq();
                }

                // 날짜 출력 함수 (오늘이면 시간 , 오늘이 아니면 날짜)
                function formatDateTime(dateTime) {
                    const now = new Date();
                    const date = new Date(dateTime);

                    if (isToday(now, date)) {
                        // 오늘이면 시간만 표시
                        return date.toLocaleTimeString();
                    } else {
                        // 오늘이 아니면 날짜만 표시
                        return date.toLocaleDateString();
                    }
                }

                function isToday(now, date) {
                    // 현재 날짜와 비교 대상 날짜가 같은 날인지 확인
                    return (
                        now.getDate() === date.getDate() &&
                        now.getMonth() === date.getMonth() &&
                        now.getFullYear() === date.getFullYear()
                    );
                }

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
function getrankLabel(empRk) {
    switch (empRk) {
    case 1:
      return "사원";
    case 2:
      return "주임";
    case 3:
      return "대리";
    case 4:
      return "과장";
    case 5:
      return "팀장";
    case 6:
      return "부장";
    default:
      return "직급";
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
function getdptmLabel(dptmNo) {
    switch (dptmNo) {
      case 1:
        return "인사";
      case 2:
        return "기획";
      case 3:
        return "개발";
      case 4:
        return "경영지원";
      case 5:
        return "마케팅";
      default:
        return "부서";
    }
}
    return(<>
        <div className="contentBox">
        <div class="pageinfo"><span class="lv0">근태관리</span> > <span class="lv1">전 사원 연차조회</span></div>
        {/*<p> page : { pageInfo.page  } totalCount : { pageDto.totalCount  } </p>*/}
        <div class="divflex">
            <div class="divflex w90">
                <div>
                    <input type="date" className="periodStart" onChange={ (e)=> { setPageInfo( { ...pageInfo , DateSt : e.target.value} ); } } style={{width:'115px'}}/>~
                    <input type="date" className="periodEnd" onChange={ (e)=> { setPageInfo( { ...pageInfo , DateEnd : e.target.value} ); } } style={{width:'115px'}}/>
                </div>
            </div>
            <select
                value = { pageInfo.dptmNo }
                onChange={ (e)=>{  setPageInfo( { ...pageInfo , dptmNo : e.target.value} );  } }
            >
                <option  value="0"> 전체부서 </option>
                <option  value="1"> 인사 </option>
                <option  value="2"> 기획 </option>
                <option  value="3"> 개발 </option>
                <option  value="4"> 경영지원 </option>
                <option  value="5"> 마케팅 </option>
            </select>
            <select
                value = { pageInfo.empRk }
                onChange={ (e)=>{  setPageInfo( { ...pageInfo , empRk : e.target.value} );  } }
            >
                <option  value="0"> 전체직급 </option>
                <option  value="1"> 사원 </option>
                <option  value="2"> 주임 </option>
                <option  value="3"> 대리 </option>
                <option  value="4"> 과장 </option>
                <option  value="5"> 팀장 </option>
                <option  value="6"> 부장 </option>
            </select>

            <select
                value = { pageInfo.lrqType }
                onChange={ (e)=>{  setPageInfo( { ...pageInfo ,  lrqType : e.target.value} );  } }
                >
                <option  value="0"> 전체연차 </option>
                <option  value="1"> 휴직 </option>
                <option  value="2"> 연차 </option>
                <option  value="3"> 병가 </option>

            </select>
             <select
                value = { pageInfo.lrqSrtype }
                onChange={ (e)=>{  setPageInfo( { ...pageInfo ,  lrqSrtype : e.target.value} );  } }
                >
                <option  value="2"> 전체급여 </option>
                <option  value="0"> 무급 </option>
                <option  value="1"> 유급 </option>

            </select>
            { /* 검색 */}
            <div>
               <select
                   value={ pageInfo.key}
                   onChange = {
                       (e)=>{ setPageInfo( { ...pageInfo , key : e.target.value } )  }
                       }
                   >
                   <option value="empNo"> 사번 </option>

               </select>

               <input type="text"
                   style={{width:'80px'}}
                   value={ pageInfo.keyword }
                   onChange = {
                       (e)=>{ setPageInfo( { ...pageInfo , keyword : e.target.value } )  }
                   }
               />
               <button type="button" class="searchbtn" onClick={ onSearch }>검색 </button>
            </div>

            <select
                value = { pageInfo.view }
                onChange={ (e)=>{  setPageInfo( { ...pageInfo , view : e.target.value} );  } }
                >
                <option value="5"> 5 </option>
                <option value="10"> 10 </option>
                <option value="20"> 20 </option>
            </select>
        </div>
                    { /* 삼항연산자 를 이용한 조건부 랜더링 */}
                    {
                        pageInfo.keyword == '' ?
                        (<> </>)
                        :
                        (<> <button  type="button" class="po_abtr0" onClick = { (e)=> { window.location.href="/attendance/leaveRequestlist"; }  } > 검색제거 </button>  </>)
                    }
            <hr class="hr00"/>
            <TableContainer
                sx={{
                    width: 900,
                    height: 500,
                    'td': {
                        textAlign: 'center',
                        fontSize: '0.8rem',
                        paddingTop: '9px',
                        paddingBottom: '9px',
                        paddingLeft: '2px',
                        paddingRight: '2px',
                        border:'solid 1px var(--lgray)'
                    }
                }}
                component={Paper}>
                     <Table sx={{ minWidth: 650 }} aria-label="simple table">
                     {/* 테이블 제목 구역 */}
                       <TableHead
                        sx={{
                            'th':{
                                textAlign: 'center',
                                fontSize: '0.8rem',
                                bgcolor: 'var(--main04)',
                                color: '#fff',
                                paddingTop: '10px' ,
                                paddingBottom: '10px',
                            }
                        }}

                      >
                         <TableRow>
                           <TableCell style={{ width : '8%' }} align="right">번호</TableCell>
                           <TableCell style={{ width : '13%' }} align="right">연차신청날짜</TableCell>
                           <TableCell style={{ width : '13%' }} align="right">연차종료날짜</TableCell>
                           <TableCell style={{ width : '10%' }} align="right">연차유형</TableCell>
                           <TableCell style={{ width : '10%' }} align="right">급여유형</TableCell>
                            <TableCell style={{ width : '10%' }} align="right">결재번호</TableCell>
                           <TableCell style={{ width : '12%' }} align="right">이름<br/>(사원번호)</TableCell>
                           <TableCell style={{ width : '10%' }} align="right">직급</TableCell>
                            <TableCell style={{ width : '10%' }} align="right">부서</TableCell>
                         </TableRow>
                       </TableHead>
                       {/* 테이블 내용 구역 */}
                       <TableBody>
                         {pageDto.someList.map((row) => (
                           <TableRow
                             key={row.name}
                            >
                             <TableCell style={{ width : '8%' }} onClick={ ( ) => loadView( row.lrqNo ) } align="right">{row.lrqNo}</TableCell>
                             <TableCell style={{ width : '13%' }} onClick={ ( ) => loadView( row.lrqNo ) } align="right">{row.lrqSt}</TableCell>
                             <TableCell style={{ width : '13%' }} onClick={ ( ) => loadView( row.lrqNo ) } align="right">{row.lrqEnd}</TableCell>
                             <TableCell style={{ width : '10%' }} onClick={ ( ) => loadView( row.lrqNo ) } align="right">{getlrqTypeLabel(row.lrqType)}</TableCell>
                             <TableCell style={{ width : '10%' }} onClick={ ( ) => loadView( row.lrqNo ) } align="right">{getlrqSrtypeLabel(row.lrqSrtype)}</TableCell>
                             <TableCell style={{ width : '10%' }} onClick={ ( ) => loadView( row.lrqNo ) } align="right">{row.aprvNo}</TableCell>
                             <TableCell style={{ width : '12%' }} onClick={ ( ) => loadView( row.lrqNo ) } align="right">{row.empName + "(" + row.empNo + ")"}</TableCell>
                             <TableCell style={{ width : '10%' }} onClick={ ( ) => loadView( row.lrqNo ) } align="right">{getrankLabel(row.empRk)}</TableCell>
                             <TableCell style={{ width : '10%' }} onClick={ ( ) => loadView( row.lrqNo ) } align="right">{getdptmLabel(row.dptmNo)}</TableCell>
                           </TableRow>
                         ))}
                       </TableBody>
                     </Table>
                   </TableContainer>
                   <div style = {{ display : 'flex' , flexDirection : 'column' , alignItems : 'center' , margin : '10px' }} >

                               { /* page : 현재페이지    count : 전체페이지수   onChange : 페이지번호를 클릭/변경 했을떄 이벤트 */}
                               <Pagination page = { pageInfo.page }  count={ pageDto.totalPages } onChange={ onPageSelect } />



                     </div>
    </div>

    </>)
}
