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

export default function SalaryMain(props) {
    // 0. 컴포넌트 상태변수 관리
    let [rows, setRows] = useState([]);
    /*
    // 3. 현재 로그인된 회원의 번호
    const login = JSON.parse(sessionStorage.getItem('login_token'));
    const empNo = login != null ? login.empNo : null;


    useEffect(() => { // 컴포넌트가 생성될때 1번 실행되는 axios
        axios.get('/salary/getMe', { params: { empNo: empNo } })
            .then(r => {
                setRows(r.data); // 응답받은 모든 게시물을 상태변수에 저장
            })
    }, []);
    */
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

    return (<>
            <h3>{/* row.empNo */} 이젠님 급여보기 ( 추후에 사번으로 이름 찾아서 대입 )</h3>
            <a href="/salary/write">(인사팀전용)급여 작성</a>   <br/>
            <a href="/salary/list">(인사팀전용)전체 급여 목록</a>  <br/>

             {/*
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell align="right">번호</TableCell>
                            <TableCell align="right">지급날짜</TableCell>
                            <TableCell align="right">지급금액</TableCell>
                            <TableCell align="right">지급유형</TableCell>
                            <TableCell align="right">결재번호</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {rows.map((row) => (
                            <TableRow
                                key={row.name}
                                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                <TableCell align="right">{row.slryNo}</TableCell>
                                <TableCell align="right">{row.slryDate}</TableCell>
                                <TableCell align="right">{row.slryPay}</TableCell>
                                <TableCell align="right">{getSlryTypeLabel(row.slryType)}</TableCell>
                                <TableCell align="right">{row.aprvNo}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            */}
        </>)
}
