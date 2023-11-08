import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import axios from "axios";

import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';

import Paper from '@mui/material/Paper';

export default function LeaveRequestMain(props) {
    // 0. 컴포넌트 상태변수 관리
    let [rows, setRows] = useState([]);
    /*
    // 3. 현재 로그인된 회원의 번호
    const login = JSON.parse(sessionStorage.getItem('login_token'));
    const empNo = login != null ? login.empNo : null;


    useEffect(() => { // 컴포넌트가 생성될때 1번 실행되는 axios
        axios.get('/leaveRequest/getMe', { params: { empNo : empNo } })
            .then(r => {
                setRows(r.data); // 응답받은 모든 게시물을 상태변수에 저장
            })
    }, []);
    */
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

    return (<>
            <h3>{/* row.empNo */} 이젠님 연차보기 ( 추후에 사번으로 이름 찾아서 대입 )</h3>
            <a href="/leaveRequest/write">연차 신청</a>   <br/>
            <a href="/leaveRequest/list">(인사팀전용)전체 연차목록</a>  <br/>

             {/*
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell align="right">번호</TableCell>
                            <TableCell align="right">타입</TableCell>
                            <TableCell align="right">시작날짜</TableCell>
                            <TableCell align="right">종료날짜</TableCell>
                            <TableCell align="right">급여정보</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {rows.map((row) => (
                            <TableRow
                                key={row.name}
                                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                <TableCell align="right">{row.lrqNo}</TableCell>
                                <TableCell align="right">{getlrqTypeLabel(row.lrqType)}</TableCell>
                                <TableCell align="right">{row.lrqSt}</TableCell>
                                <TableCell align="right">{row.lrqEnd}</TableCell>
                                <TableCell align="right">{getlrqSrtypeLabel(row.lrqSrtype)}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            */}
        </>)
}