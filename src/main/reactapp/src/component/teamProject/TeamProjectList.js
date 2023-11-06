import axios from 'axios';
import { useState , useEffect } from 'react';
import {BrowserRouter, Routes, Route, Link, useSearchParams} from 'react-router-dom';
import styles from '../../css/teamProject/TeamProjectList.css';
// -------------- mui table ----------------
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
// -------------------------------------------


export default function TeamProjectList(props){

    // 컴포넌트 상태변수 관리
    let [rows, setRows] = useState( [] )


    useEffect( () => {
        axios
            .get('/teamproject/getAll')
            .then(r => {
                setRows(r.data);    // 응답받은 값들을 상태변수에 저장
            })
    }, []);

    const checkOnlyOne = (element) => {
      const checkboxes = document.getElementsByName("cb");

      checkboxes.forEach((c) => {
          c.checked = false;
      });

      element.checked = true;
    };



    return(<>
        <div className="listWrap">
            <div className="checkboxList">
                <span>전체보기<input type="checkbox" name="cb" value='all' className="cb1" onClick={(e) => checkOnlyOne(e.target)} defaultChecked /></span>
                <span>승인완료<input type="checkbox" name="cb" value=1 className="cb2" onClick={(e) => checkOnlyOne(e.target)} /></span>
                <span>반려됨<input type="checkbox" name="cb" value=2 className="cb3" onClick={(e) => checkOnlyOne(e.target)} /></span>
                <span>검토중<input type="checkbox" name="cb" value=3 className="cb4" onClick={(e) => checkOnlyOne(e.target)} /></span>
            </div>
            <TableContainer component={Paper}>
              <Table sx={{ minWidth: 650 }} aria-label="simple table">
                <TableHead>
                  <TableRow className="table_head">
                    <TableCell align="center">번호</TableCell>
                    <TableCell align="center">프로젝트명</TableCell>
                    <TableCell align="center">PM</TableCell>
                    <TableCell align="center">프로젝트시작일</TableCell>
                    <TableCell align="center">프로젝트기한</TableCell>
                    <TableCell align="center">프로젝트상태</TableCell>
                  </TableRow>
                {/* 테이블 제목 구역 */}
                </TableHead>
                {/* 테이블 내용 구역 */}
                <TableBody>
                  {rows.map((row) => (
                    <TableRow
                      key={row.name}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                    >
                      <TableCell align="center">{row.pjtNo}</TableCell>
                      <TableCell align="center">
                      {row.pjtName}
                      </TableCell>
                      <TableCell align="center">{row.empName}</TableCell>
                      <TableCell align="center">{row.pjtSt}</TableCell>
                      <TableCell align="center">{row.pjtEND}</TableCell>
                      <TableCell align="center">{row.pjtSta == 1 ? '진행중' : '완료'}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
        </div>
    </>)
}