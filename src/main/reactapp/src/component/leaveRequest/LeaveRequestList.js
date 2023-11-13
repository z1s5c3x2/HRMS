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
                   page : 1 , key : 'empNo' , keyword : '' , view : 5
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
                useEffect( () => { getlrq(); }, [ pageInfo.page , pageInfo.view ] );

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
        <h3> 연차 목록 </h3>
                <a href="/leaveRequest/write">연차신청</a>
     <p> page : { pageInfo.page  } totalCount : { pageDto.totalCount  } </p>
            <select
                        value = { pageInfo.view }
                        onChange={ (e)=>{  setPageInfo( { ...pageInfo , view : e.target.value} );  } }
                        >
                        <option value="5"> 5 </option>
                        <option value="10"> 10 </option>
                        <option value="20"> 20 </option>
                    </select>
                    { /* 삼항연산자 를 이용한 조건부 랜더링 */}
                    {
                        pageInfo.keyword == '' ?
                        (<> </>)
                        :
                        (<> <button  type="button" onClick = { (e)=> { window.location.href="/salary/list"; }  } > 검색제거 </button>  </>)
                    }
            <TableContainer component={Paper}>
                     <Table sx={{ minWidth: 650 }} aria-label="simple table">
                     {/* 테이블 제목 구역 */}
                       <TableHead>
                         <TableRow>
                           <TableCell style={{ width : '10%' }} align="right">번호</TableCell>
                           <TableCell style={{ width : '20%' }} align="right">연차신청날짜</TableCell>
                           <TableCell style={{ width : '20%' }} align="right">연차종료날짜</TableCell>
                           <TableCell style={{ width : '10%' }} align="right">연차유형</TableCell>
                           <TableCell style={{ width : '10%' }} align="right">급여유형</TableCell>
                            <TableCell style={{ width : '10%' }} align="right">결재번호</TableCell>
                            <TableCell style={{ width : '20%' }} align="right">사원번호</TableCell>
                         </TableRow>
                       </TableHead>
                       {/* 테이블 내용 구역 */}
                       <TableBody>
                         {pageDto.someList.map((row) => (
                           <TableRow
                             key={row.name}
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>

                             <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.lrqNo}</TableCell>
                             <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.lrqSt}</TableCell>
                             <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.lrqEnd}</TableCell>
                             <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{getlrqTypeLabel(row.lrqType)}</TableCell>
                             <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{getlrqSrtypeLabel(row.lrqSrtype)}</TableCell>
                             <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.aprvNo}</TableCell>
                             <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.empNo}</TableCell>
                           </TableRow>
                         ))}
                       </TableBody>
                     </Table>
                   </TableContainer>
                     <div style = {{ display : 'flex' , flexDirection : 'column' , alignItems : 'center' , margin : '10px' }} >

                               { /* page : 현재페이지    count : 전체페이지수   onChange : 페이지번호를 클릭/변경 했을떄 이벤트 */}
                               <Pagination page = { pageInfo.page }  count={ pageDto.totalPages } onChange={ onPageSelect } />

                               { /* 검색 */}
                               <div style ={{  margin : '20px' }}>
                                   <select
                                       value={ pageInfo.key}
                                       onChange = {
                                           (e)=>{ setPageInfo( { ...pageInfo , key : e.target.value } )  }
                                           }
                                       >
                                       <option value="empNo"> 사번 </option>
                                       <option value="slryType"> 급여유형 </option>
                                   </select>

                                   <input type="text"
                                       value={ pageInfo.keyword }
                                       onChange = {
                                           (e)=>{ setPageInfo( { ...pageInfo , keyword : e.target.value } )  }
                                       }
                                   />
                                   <button type="button" onClick={ onSearch }>검색 </button>
                               </div>

                     </div>

    </>)
}
