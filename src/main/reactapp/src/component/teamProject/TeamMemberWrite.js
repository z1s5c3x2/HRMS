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
    function createData(name, calories, fat, carbs, protein) {
        return { name, calories, fat, carbs, protein };
    }
    const [empList, setEmpList] = useState([]) // 사원 리스트 저장
    const [pageInfo, setPageInfo] = useState({ page:1, dptmNo:0,sta:'0',totalCount:0,totalPages:0 })  // 페이지 정보 저장

    useEffect(() => {
        console.log( pageInfo )
        getList()
    }, [pageInfo.page,pageInfo.sta,pageInfo.dptmNo]); //페이지가 바뀌거나 카테고리가 바뀌면 호출
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


    return(<>
        <div className="teamMemberWrap">
            <div class="contentBox">
                <div class="pageinfo"><span class="lv0">프로젝트팀관리</span> > <span class="lv1">프로젝트팀 팀원등록</span></div>
                <div class="emp_regs_content">

                    <div class="eregInputBox pmBox">
                        <div class="input_title " className="inputPm">프로젝트팀원</div>
                        <div class="input_box">
                            <input type="text" value={empName} class="empNo" name="empNo"/>
                        </div>
                    </div>
                    <div class="eregInputBox pmBox">
                        <div class="input_title " className="inputPm">프로젝트팀</div>
                        <div class="input_box">
                            <input type="text" value={pjtName} class="pjtNo" name="pjtNo"/>
                        </div>
                    </div>
                    <div class="eregInputBox">
                        <div class="input_title ">팀원투입날짜</div>
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
                    <div class="eregBtnBox">
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
            <div className="selectBox">   {/* 프로젝트팀 출력 공간 */}
                <div className="contentBox2">
                    <div className="cont">
                        <TableContainer component={Paper}>
                          <Table sx={{ minWidth: 200 }} aria-label="simple table">
                            <TableHead>
                              <TableRow className="table_head th01">
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
                                  className="tbody_tr"
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
                </div>
                <div>
                    {/* 사원리스트 출력 공간 */}
                     <div className="contentBox2">
                         <select
                             value = { pageInfo.sta }
                             onChange={ (e)=>{  setPageInfo( { ...pageInfo , sta : e.target.value,page:"1"} );  } }
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
                             <option value="1"> 인사팀 </option>
                             <option value="2"> 기획팀(PM) </option>
                             <option value="3"> 개발팀 </option>
                             <option value="4"> DBA팀 </option>

                         </select>

                         <div className="emp_regs_content"></div>
                         <TableContainer component={Paper}>
                             <Table sx={{ minWidth: 650 }} aria-label="simple table">
                                 <TableHead>
                                     <TableRow>
                                         <TableCell align="center">이름</TableCell>
                                         <TableCell align="center">직급</TableCell>
                                         <TableCell align="center">부서</TableCell>
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
                         <div style = {{ display : 'flex' , flexDirection : 'column' , alignItems : 'center' , margin : '10px' }} >
                             <Pagination page = { pageInfo.page }  count={ pageInfo.totalPages }  onChange = { (e,value) => {
                                 setPageInfo( { ...pageInfo , page : value })
                             }
                             } />
                         </div>
                     </div>
                </div>
            </div>
        </div>
    </>)
}