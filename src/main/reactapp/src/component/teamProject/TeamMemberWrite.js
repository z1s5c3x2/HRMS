import {useEffect, useState} from "react";
import axios from "axios";
import ApprovalModal from "../approval/ApprovalModal";
import EmployeeList from '../employee/EmployeeList';

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
// ------------ page nation -----------------
import * as React from 'react';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
// -----------------------//
dayjs.locale('ko');


export default function TeamMemberWrite( props ){
    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)
    // 모달창 열기/닫기 함수
    const modalController = (e)=> {
        setIsOn(!isOn)
    }

    // 프로젝트팀 팀원 데이터
    const [teamMemberInfo, setteamMemberInfo] = useState({
        tmSt : null,
        pjtNo : 0,
        empNo : null,
     });
    // 날짜 데이터
    const [selectedSDate, setSelectedSDate] = useState(null);

    // 날짜값을 입력할때마다 프로젝트팀 팀원 데이터에 값 넣어줌
    useEffect( () =>{
        teamMemberInfo.tmSt = selectedSDate;
        console.log(teamMemberInfo);
    }, [selectedSDate])

    // 사원정보 데이터
    const [empNumber, setEmpNumber] = useState('');
    const [empName, setEmpName] = useState('');

    // 프로젝트팀 데이터
    const [pjtNumber, setPjtNumber] = useState('');
    const [pjtName, setPjtName] = useState('');

    // -------------- 프로젝트팀 데이터 ---------------

    // 프로젝트팀 상태변수 관리
    let [ projectTeam , setProjectTeam ] = useState( [] )

    useEffect( ()=>{
          axios
            .get('/teamproject/getSelectAll', { params : { approval : 3 } })
            .then( r =>{
                  console.log(r.data);
                  setProjectTeam(r.data); // 응답받은 값을 projectTeam에 저장
               });
    } , []);

    // 프로젝트팀이 변경될때마다 teamMemberInfo 값을 바꿔줌
    useEffect( () =>{
        teamMemberInfo.pjtNo = pjtNumber;
        console.log(teamMemberInfo);
    }, [pjtNumber])

    // ---------------- 사원 데이터 ---------------------

    // 프로젝트팀 팀원이 변경될때마다 teamMemberInfo 값을 바꿔줌
    useEffect( () =>{
        teamMemberInfo.empNo = empNumber;
        console.log(teamMemberInfo);
    }, [empNumber])

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


    return(<>
        <div className="teamMemberWrap">
            <div class="contentBox">
                <div class="pageinfo"><span class="lv0">프로젝트팀관리</span> > <span class="lv1">프로젝트팀 팀원등록</span></div>
                <div class="emp_regs_content divflex w100">
                    <div class="divflex w30">
                        <div class="w20 ma ls3">팀명 </div>
                        <div class="w80"><input type="text" value={pjtName} class="pjtNo" name="pjtNo"/></div>
                    </div>
                    <div class="divflex w15">
                        <div class="w40 ma ld2">팀원명 </div>
                        <div class="w60"><input type="text" value={empName} class="empNo" name="empNo"/></div>
                    </div>

                    <div class="divflex w33">
                        <div class="w30 ma">팀원투입일 </div>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                          <DesktopDatePicker
                            value={selectedSDate}
                            sx={{ width: '180px'}}
                            renderInput={(params) => <TextField {...params} label="날짜" />}
                            format="YYYY-MM-DD"
                            onChange={(date)=> setSelectedSDate(date)}
                          />
                        </LocalizationProvider>
                    </div>
                    <div class="w12">
                        <button class="btn01" type="button" onClick={modalController}>팀원등록</button>
                    </div>
                </div>
                {/*!-- 결제 모달 Start --> targetUrl: axios로 보낼 url aprvType: 결제 타입 설정  successUrl :결제 성공후 이동할 url  */   }
                    { isOn ? <> <ApprovalModal data={teamMemberInfo}
                                              targetUrl={"/teammember/post"}
                                              aprvType={15}
                                              successUrl={"/teamproject/teammember/write"}
                                              modalControll={modalController}>
                    </ApprovalModal></> : <> </> }
            </div>
            <hr class="hr00"/>
            {/* 프로젝트팀 출력 공간 */}
            <div className="contentBox2 divflex">
                <div className="cont">
                    <TableContainer
                        sx={{
                            width: 430,
                            height: 500,
                            'table':{
                                borderRadius: '5px'
                            },
                            'td': {
                                textAlign: 'center',
                                fontSize: '0.8rem',
                                paddingTop: '9px',
                                paddingBottom: '9px',
                                paddingLeft: '3px',
                                paddingRight: '3px',
                                border:'solid 1px var(--lgray)'
                            }
                        }}
                        component={Paper}>
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
                          <TableRow>
                            <TableCell  align="center" className="th_cell">번호</TableCell>
                            <TableCell className="th_cell" align="center">프로젝트명</TableCell>
                            <TableCell className="th_cell" align="center">PM</TableCell>
                          </TableRow>
                        {/* 테이블 제목 구역 */}
                        </TableHead>
                        {/* 테이블 내용 구역 */}
                        <TableBody>
                          {projectTeam.map((row) => (
                            <TableRow

                              onClick={() => {
                                setPjtName(row.pjtName);
                                setPjtNumber(row.pjtNo);
                              }}
                              key={row.name}
                            >
                              <TableCell align="center">{row.pjtNo}</TableCell>
                              <TableCell align="center">{row.pjtName}</TableCell>
                              <TableCell align="center">{row.empName}</TableCell>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>
                </div>

                <div className="cont">
                    <TableContainer
                        sx={{
                            width: 450,
                            height: 500,
                            'table':{
                                borderRadius:'5px'
                            },
                            'td': {
                                textAlign: 'center',
                                fontSize: '0.8rem',
                                paddingTop: '9px',
                                paddingBottom: '9px',
                                paddingLeft: '3px',
                                paddingRight: '3px',
                                border:'solid 1px var(--lgray)'
                            }
                        }}
                        component={Paper}>
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
                          <TableRow>
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

        </div>
    </>)
}