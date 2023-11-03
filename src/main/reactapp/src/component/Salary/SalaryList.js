/*
    mui : 리액트 전용 라이브러리
        1. 설치
            npm install @mui/material @emotion/react @emotion/styled
            npm install @mui/material @mui/styled-engine-sc styled-components
        2. 예제
           1. 호출을 컴포넌트 상단에 import 하기
              import Button from '@mui/material/Button';
           2. 호출된 mui 컴포넌트를 사용
              <Button variant="contained">Hello world</Button>;

*/
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
//----------------------------------------------------------------------//




export default function BoardList(props){
    // 0. 컴포넌트 상태변수 관리
    let [ rows , setRows ] = useState( [ ] )
    // 1. axios를 이용한 스프링의 컨트롤과 통신
     const loadView = ( slryNo ) => {
            window.location.href = '/salary/view/'+slryNo
            // 조회수 처리 할 예정
        }
    useEffect( ()=>{ // 컴포넌트가 생성될때 1번 실행되는 axios
        axios.get('http://localhost:80/salary/getAll').then( r =>{
                setRows(r.data); // 응답받은 모든 게시물을 상태변수에 ㅈ ㅓ장
                // setState : 해당 컴포넌트가 업데이트 (새로고침/재랜더링/return재실행)
             });
    } , []);

    return(<>
        <h3> 급여 목록 </h3>
        <a href="/salary/write">급여작성</a>
        <TableContainer component={Paper}>
                 <Table sx={{ minWidth: 650 }} aria-label="simple table">
                 {/* 테이블 제목 구역 */}
                   <TableHead>
                     <TableRow>
                       <TableCell align="right">번호</TableCell>
                       <TableCell align="right">지급날짜</TableCell>
                       <TableCell align="right">지급금액</TableCell>
                       <TableCell align="right">지급유형</TableCell>
                        <TableCell align="right">결재번호</TableCell>
                        <TableCell align="right">사원번호</TableCell>
                     </TableRow>
                   </TableHead>
                   {/* 테이블 내용 구역 */}
                   <TableBody>
                     {rows.map((row) => (
                       <TableRow
                         key={row.name}
                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>

                         <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.slryNo}</TableCell>
                         <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.slryDate}</TableCell>
                         <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.slryPay}</TableCell>
                         <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.slryType}</TableCell>
                         <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.aprvNo}</TableCell>
                         <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.empNo}</TableCell>

                       </TableRow>
                     ))}
                   </TableBody>
                 </Table>
               </TableContainer>
            </>)
}
