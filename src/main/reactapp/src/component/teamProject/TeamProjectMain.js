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
// -------페이지네이션--------
import * as React from 'react';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
// -----------------------//
dayjs.locale('ko');



export default function TeamProjectMain( props ){
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
        empNo : null
     });
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
                console.log(r.data);
                setEmpList( r.data.someList )
                setPageInfo( {...pageInfo,totalCount: r.data.totalCount,totalPages:r.data.totalPages} )
            })
            .catch( (e) =>{
                console.log( e )
            })
    }



    return(<>
        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">프로젝트팀관리</span> > <span class="lv1">프로젝트팀등록</span></div>
            <div class="emp_regs_content divflex w100">
                <div class="div_1st">
                    <div class="eregInputBox">

                        <div class="w40 ls9 ma" > 프로젝트명</div>
                        <div class="w60"><input
                            type="text"
                            className=""
                            name="pjtName"
                            onChange={updateApprovalInfo}
                         /></div>
                    </div>
                    <div class="eregInputBox pmBox">
                        <div class="w40 ma" >프로젝트매니저</div>
                        <div class="w60">
                            <input value={empName} type="text" class="empNo" name="empNo"/>
                        </div>
                    </div>
                </div>
                <div class="div_2nd">
                    <div class="eregInputBox">
                        <div class="w40 ma">프로젝트시작날짜</div>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                          <DesktopDatePicker
                            value={selectedSDate}
                            sx={{ width: '200px'}}
                            renderInput={(params) => <TextField {...params} label="날짜" />}
                            format="YYYY-MM-DD"
                            onChange={(date)=> setSelectedSDate(date)}
                          />
                        </LocalizationProvider>
                    </div>
                    <div class="eregInputBox">
                        <div class="w40 ma">프로젝트완료날짜</div>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                          <DesktopDatePicker
                            value={selectedEDate}
                            sx={{ width: '200px'}}
                            renderInput={(params) => <TextField {...params} label="날짜" />}
                            format="YYYY-MM-DD"
                            onChange={(date)=> setSelectedEDate(date)}
                          />
                        </LocalizationProvider>
                    </div>
                </div>
                <div class="div_3th">
                        <button class="btn01 lh18 pd15_0" type="button" onClick={modalController}>프로젝트팀<br/>등록</button>
                </div>
            </div>
            {/*!-- 결제 모달 Start --> targetUrl: axios로 보낼 url aprvType: 결제 타입 설정  successUrl :결제 성공후 이동할 url  */   }
                { isOn ? <> <ApprovalModal data={projectInfo}
                                          targetUrl={"/teamproject/post"}
                                          aprvType={12}
                                          successUrl={"/teamproject"}
                                          modalControll={modalController}>
                </ApprovalModal></> : <> </> }
        </div>
        {/* 사원리스트 출력 공간 */}
         <div className="contentBox2 ">
            <div className="searchBox w20 pd5_0">
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

             </div>
             <TableContainer
                 sx={{
                     width: 900,
                     height: 400,
                     'td': {
                         textAlign: 'center',
                         fontSize: '0.8rem',
                         paddingTop: '10px',
                         paddingBottom: '10px',
                         paddingLeft: '3px',
                         paddingRight: '3px',
                         border:'solid 1px var(--lgray)'
                     }
                 }}

                      component={Paper}>
                 <Table sx={{ minWidth: 650 }} aria-label="simple table">
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
                             <TableRow
                                onClick={() => {
                                    setEmpName(emp.empName);
                                    setEmpNumber(emp.empNo);
                                  }}
                                >
                                     <TableCell align="center">{emp.empName}</TableCell>
                                     <TableCell align="center"> {emp.empPhone} </TableCell>
                                     <TableCell align="center">{emp.empSex}</TableCell>
                                     <TableCell align="center">{emp.empSta ? '재직' : '휴직'}</TableCell>
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


    </>)
}