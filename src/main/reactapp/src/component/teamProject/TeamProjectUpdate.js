import {useEffect, useState} from "react";
import axios from "axios";
import ApprovalModal from "../approval/ApprovalModal";
import EmployeeList from '../employee/EmployeeList';
import {BrowserRouter, Routes, Route, Link, useSearchParams} from 'react-router-dom';

import styles from '../../css/teamProject/TeamProjectMain.css';

// --------- mui ---------//
import dayjs from 'dayjs';
import 'dayjs/locale/ko';
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DesktopDatePicker } from '@mui/x-date-pickers/DesktopDatePicker';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
// -------페이지네이션--------
import * as React from 'react';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
// -----------------------//
dayjs.locale('ko');



export default function TeamProjectUpdate( props ){

    // url param 값 가져오기
    const [ searchParams, setSearchParams ] = useSearchParams();
    const pjtNo = searchParams.get('pjtNo');

    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)

    // 모달창 열기/닫기 함수
    const modalController = (e)=> {
        setIsOn(!isOn)
    }

    // 프로젝트팀 데이터
    const [projectInfo, setProjectInfo] = useState({
        pjtName : null,
        pjtSt : null,
        pjtEND : null,
        empNo : null,
        pjtNo : 0
     });

     useEffect( () => {
         projectInfo.pjtNo = pjtNo;
         console.log(projectInfo);
      }, [searchParams])

    // 날짜 데이터
    const [selectedSDate, setSelectedSDate] = useState(null);
    const [selectedEDate, setSelectedEDate] = useState(null);

    // 사원정보 데이터
    const [empNumber, setEmpNumber] = useState('');
    const [empName, setEmpName] = useState('');

    // input 값이 입력되면 그 값을 appovalInfo에 저장하는 함수
    const updateApprovalInfo = (e) =>{
        projectInfo.pjtName = e.target.value;
        setProjectInfo({...projectInfo})
        console.log(projectInfo);
    }

    // 날짜값을 입력할때마다 프로젝트팀 데이터에 값 넣어줌
    useEffect( () =>{
        projectInfo.pjtSt = selectedSDate;
        projectInfo.pjtEND = selectedEDate;
        console.log(projectInfo);
    }, [selectedSDate, selectedEDate])


    // 프로젝트팀 매니저가 변경될때마다 projectInfo 값을 바꿔줌
    useEffect( () =>{
        projectInfo.empNo = empNumber;
        console.log(projectInfo);
    }, [empNumber])

    // -------------- 사원 데이터 -------------------
    const rkList = ["대기","사원","주임","대리","과장","팀장","부장"]
    const dptList = ['인사팀','기획팀(PM)','개발팀','DBA팀']

    const [empList, setEmpList] = useState([]) // 사원 리스트 저장

    useEffect( ()=>{
        getEmps();
    }, [] )

    const getEmps = (event)=>{
        axios
            .get("/employee/getteammebers")
            .then( (r) => {
                console.log(r.data);
                setEmpList(r.data)
            })
            .catch( (e) =>{
                console.log( e )
            })
    }



    // 프로젝트팀 정보 상태변수
    const [ data, setData ] = useState( {
        pjtName : null,
        pjtSt : null,
        pjtEND : null,
        empNo : null,
        empName : null,
    } );

    // url param으로 불러온 pjtNo로 프로젝트팀 정보 호출
    useEffect( () =>{
        axios
            .get("/teamproject/getOne",{ params: searchParams  })
            .then( (r) => {
                console.log(r.data);
                projectInfo.pjtName = r.data.pjtName;
                projectInfo.pjtSt = r.data.pjtSt;
                projectInfo.pjtEND = r.data.pjtEND;
                projectInfo.empNo = r.data.empNo;
                setEmpName(r.data.empName);
                console.log(projectInfo);
                console.log(empName);
            })
    }, [])


    return(<>
        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">프로젝트팀관리</span> > <span class="lv1">프로젝트팀조회</span> > <span class="lv1">프로젝트팀수정</span></div>
            <div class="emp_regs_content w100 divflex">
                <div class="w35">
                    <div class="divflex pd5_0">
                        <div class="w40 ls7 ma"> 프로젝트명</div>
                        <div class="w60 ls2"><input
                            type="text"
                            className=""
                            style={{width:'100%'}}
                            name="pjtName"
                            value={projectInfo.pjtName}
                            onChange={updateApprovalInfo}
                         /></div>
                    </div>
                    <div class="divflex pd5_0">
                        <div class="w40 ma ">프로젝트매니저</div>
                        <div class="w60">
                            <input value={empName} type="text" class="pmNo" name="empNo"/>
                        </div>
                    </div>
                </div>
                <div class="w40 ">
                    <div class="divflex pd5_0">
                        <div class="w40 ma ">프로젝트시작날짜</div>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                          <DesktopDatePicker
                            value={selectedSDate}
                            sx={{ width: '70%'}}
                            renderInput={(params) => <TextField {...params} label="날짜" />}
                            format="YYYY-MM-DD"
                            onChange={(date)=> setSelectedSDate(date)}
                          />
                        </LocalizationProvider>
                    </div>
                    <div class="divflex pd5_0">
                        <div class="w40 ma ">프로젝트완료날짜</div>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                          <DesktopDatePicker
                            value={selectedEDate}
                            sx={{ width: '70%'}}
                            renderInput={(params) => <TextField {...params} label="날짜" />}
                            format="YYYY-MM-DD"
                            onChange={(date)=> setSelectedEDate(date)}
                          />
                        </LocalizationProvider>
                    </div>
                </div>
                <div class="w15">
                    <button class="btn01 ma lh18" type="button" onClick={modalController}>프로젝트팀<br/>수정</button>
                </div>
            </div>
            {/*!-- 결제 모달 Start --> targetUrl: axios로 보낼 url aprvType: 결제 타입 설정  successUrl :결제 성공후 이동할 url  */   }
                { isOn ? <> <ApprovalModal data={projectInfo}
                                          targetUrl={"/teamproject/putAproval"}
                                          aprvType={13}
                                          successUrl={"/teamproject/listAll"}
                                          modalControll={modalController}>
                </ApprovalModal></> : <> </> }
        </div>
        {/* 사원리스트 출력 공간 */}
         <div className="contentBox2">
             <div className="cont">
                 <TableContainer
                     sx={{
                         width: 900,
                         height: 400,
                         'td': {
                             textAlign: 'center',
                             fontSize: '0.8rem',
                             paddingTop: '9px',
                             paddingBottom: '9px',
                             paddingLeft: '3px',
                             paddingRight: '3px',
                             border:'solid 1px var(--lgray)'
                         }
                     }} component={Paper}>
                   <Table sx={{ minWidth: 200 }} aria-label="simple table">
                     <TableHead
                          sx={{
                              'th':{
                                  textAlign: 'center',
                                  fontSize: '0.9rem',
                                  bgcolor: 'var(--main04)',
                                  color: '#fff',
                                  paddingTop: '10px' ,
                                  paddingBottom: '10px',
                              }
                          }}
                     >
                       <TableRow className="table_head th01">
                         <TableCell  align="center" className="th_cell">이름</TableCell>
                         <TableCell className="th_cell" align="center">직급</TableCell>
                         <TableCell className="th_cell" align="center">부서</TableCell>
                       </TableRow>
                     </TableHead>
                     <TableBody>
                       {empList.map((emp) => (
                            <TableRow
                               onClick={() => {
                                   setEmpName(emp.empName);
                                   setEmpNumber(emp.empNo);
                                 }}
                            >
                                <TableCell align="center">{emp.empName}</TableCell>
                                <TableCell align="center">{rkList[emp.empRk]}</TableCell>
                                <TableCell align="center">{dptList[emp.dptmNo - 1]}</TableCell>
                            </TableRow>
                        ))}
                     </TableBody>
                   </Table>
                 </TableContainer>
             </div>
         </div>


    </>)
}