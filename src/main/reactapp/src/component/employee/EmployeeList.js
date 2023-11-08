import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {useEffect, useState} from "react";
import axios from "axios";
import Pagination from "@mui/material/Pagination";

export default function EmployeeList() {
    const rkList = ["대기","사원","주임","대리","과장","팀장","부장"]
    const dptList = ['인사팀','기획팀(PM)','개발팀','DBA팀']
    function createData(name, calories, fat, carbs, protein) {
        return { name, calories, fat, carbs, protein };
    }
    const [empList, setEmpList] = useState([]) // 사원 리스트 저장
    const [pageInfo, setPageInfo] = useState({ page:1, dptmNo:0,Sta:'0',totalCount:0,totalPages:0 })  // 페이지 정보 저장

    useEffect(() => {
        console.log( pageInfo )
        getList()
    }, [pageInfo.page,pageInfo.Sta,pageInfo.dptmNo]); //페이지가 바뀌거나 카테고리가 바뀌면 호출

    const getList = (event)=>{ // 페이지 정보를 이용하여 사원 리스트 요청
        axios
            .get("/employee/findAll",{params:pageInfo})
            .then( (r) => {
                //console.log( r.data )
                //console.log( r.data.someList )
                setEmpList( r.data.someList )
                setPageInfo( {...pageInfo,totalCount: r.data.totalCount,totalPages:r.data.totalPages} )
                //console.log( empList )
            })
            .catch( (e) =>{
                console.log( e )
            })
    }
    return (<>
        <div className="contentBox">
            <div className="pageinfo"><span className="lv0">인사관리</span> > <span className="lv1">사원목록</span></div>
            <select
                value = { pageInfo.sta }
                onChange={ (e)=>{  setPageInfo( { ...pageInfo , sta : e.target.value,page:'1'} );  } }
            >
                <option value="0"> 상태 전체 </option>
                <option value="1"> 재직 </option>
                <option value="2"> 휴직 </option>
            </select>
            <select
                value = { pageInfo.dptmNo }
                onChange={ (e)=>{  setPageInfo( { ...pageInfo , dptmNo : e.target.value,page:'1'} );  } }
            >
                <option value="0"> 부서 전체 </option>
                <option value="0"> 인사팀 </option>
                <option value="1"> 기획팀(PM) </option>
                <option value="2"> 개발팀 </option>
                <option value="3"> DBA팀 </option>

            </select>
            <div className="emp_regs_content"></div>
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell align="center">이름</TableCell>
                            <TableCell align="center">전화번호 </TableCell>
                            <TableCell align="center">성별</TableCell>
                            <TableCell align="center">근무 상태</TableCell>
                            <TableCell align="center">직급</TableCell>
                            <TableCell align="center">부서</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {empList.map((emp) => (
                            <TableRow sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                <TableCell align="center">{emp.empName}</TableCell>
                                <TableCell align="center">{emp.empPhone}</TableCell>
                                <TableCell align="center">{emp.empSex}</TableCell>
                                <TableCell align="center">{ emp.empSta ? '재직':'휴직' }</TableCell>
                                <TableCell align="center">{rkList[emp.empRk]}</TableCell>
                                <TableCell align="center">{dptList[emp.dtpmNo]}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <div style = {{ display : 'flex' , flexDirection : 'column' , alignItems : 'center' , margin : '10px' }} >
                <Pagination page = { pageInfo.page }  count={ pageInfo.totalPages }  onChange = { (e,value) => {
                    setPageInfo( { ...pageInfo , page : value })
                }
                } />
            </div>
        </div>
        </>);
}