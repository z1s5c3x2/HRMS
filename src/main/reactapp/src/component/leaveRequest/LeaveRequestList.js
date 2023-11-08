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

export default function LeaveRequestList( props ){
    // 0. 컴포넌트 상태변수 관리
        let [ rows , setRows ] = useState( [ ] )
        // 1. axios를 이용한 스프링의 컨트롤과 통신
         const loadView = ( lrqNo ) => {
                window.location.href = '/leaveRequest/view?lrqNo='+lrqNo
                // 조회수 처리 할 예정
            }
        useEffect( ()=>{ // 컴포넌트가 생성될때 1번 실행되는 axios
                axios.get('/leaveRequest/getAll').then( r =>{
                        setRows(r.data); // 응답받은 모든 게시물을 상태변수에 저장
                        // setState : 해당 컴포넌트가 업데이트 (새로고침/재랜더링/return재실행)
                     });
            } , []);
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
                <TableContainer component={Paper}>
                         <Table sx={{ minWidth: 650 }} aria-label="simple table">
                         {/* 테이블 제목 구역 */}
                           <TableHead>
                             <TableRow>
                               <TableCell align="right">번호</TableCell>
                               <TableCell align="right">타입</TableCell>
                               <TableCell align="right">시작날짜</TableCell>
                               <TableCell align="right">종료날짜</TableCell>
                                <TableCell align="right">급여정보</TableCell>
                                <TableCell align="right">사원번호</TableCell>
                             </TableRow>
                           </TableHead>
                           {/* 테이블 내용 구역 */}
                           <TableBody>
                             {rows.map((row) => (
                               <TableRow
                                 key={row.name}
                                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>

                                 <TableCell onClick={ ( ) => loadView( row.lrqNo ) } align="right">{row.lrqNo}</TableCell>
                                 <TableCell onClick={ ( ) => loadView( row.lrqNo ) } align="right">{getlrqTypeLabel(row.lrqType)}</TableCell>
                                 <TableCell onClick={ ( ) => loadView( row.lrqNo ) } align="right">{row.lrqSt}</TableCell>
                                 <TableCell onClick={ ( ) => loadView( row.lrqNo ) } align="right">{row.lrqEnd}</TableCell>
                                 <TableCell onClick={ ( ) => loadView( row.lrqNo ) } align="right">{getlrqSrtypeLabel(row.lrqSrtype)}</TableCell>
                                 <TableCell onClick={ ( ) => loadView( row.lrqNo ) } align="right">{row.empNo}</TableCell>

                               </TableRow>
                             ))}
                           </TableBody>
                         </Table>
                       </TableContainer>
    </>)
}
/*
*     return(
       <div class="contentBox">
                   <div class="pageinfo"><span class="lv0">퇴사직원</span>  > <span class="lv1">조회</span> </div>
                   <div class="list_content">
                        <Table class="tTpyeA">
                            <tr>
                                <th>No</th>
                                <th>부서명</th>
                                <th>사원명</th>
                                <th>직급</th>
                                <th>재직기간</th>
                            </tr>

                   		</Table>



                   		</div>

                   </div>
    )
}
* */