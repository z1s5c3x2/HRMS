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
// -----------------------//
dayjs.locale('ko');


export default function TeamMemberWrite( props ){
    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)

    // 결재 모달창 여는 함수
    function onModal(e) {
        //document.querySelector('.approv_modal').style.display = 'flex';
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

    /* 사원 직급설정 함수 */
    function getrankLabel(empRk) {
        switch (empRk) {
          case 1:
            return "사원";
          case 2:
            return "주임";
          case 3:
            return "대리";
          case 4:
            return "과장";
          case 5:
            return "팀장";
          case 6:
            return "부장";
          default:
            return "직급";
        }
    }

    // 사원데이터 상태변수 관리
    let [ rows , setRows ] = useState( [ ] )

    /*useEffect( ()=>{
          axios.get('/employee/findAll').then( r =>{
                    console.log(r);
                  setRows(r.data); // 응답받은 값을 rows에 저장
               });
    } , []);*/

    // 프로젝트팀 팀원이 변경될때마다 teamMemberInfo 값을 바꿔줌
    useEffect( () =>{
        teamMemberInfo.empNo = pjtNumber;
        console.log(teamMemberInfo);
    }, [pjtNumber])

    // 프로젝트팀 상태변수 관리
    let [ projectTeam , setProjectTeam ] = useState( [ ] )

    useEffect( ()=>{
          axios
            .get('/teamproject/getSelectAll', { params : { approval : 1 } })
            .then( r =>{
                  console.log(r.data);
                  setProjectTeam(r.data); // 응답받은 값을 projectTeam에 저장
               });
    } , []);

    // 프로젝트팀이 변경될때마다 teamMemberInfo 값을 바꿔줌
    useEffect( () =>{
        teamMemberInfo.pjtNo = empNumber;
        console.log(teamMemberInfo);
    }, [empNumber])

    return(<>
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
                    <button class="btn01" type="button" onClick={onModal}>팀원등록</button>
                </div>
            </div>
            {/*!-- 결제 모달 Start --> targetUrl: axios로 보낼 url aprvType: 결제 타입 설정  successUrl :결제 성공후 이동할 url  */   }
                { isOn ? <> <ApprovalModal data={teamMemberInfo}
                                          targetUrl={"/teammember/post"}
                                          aprvType={15}
                                          successUrl={"/teamproject/teammember/write"}>
                </ApprovalModal></> : <> </> }
        </div>
        <div>
            { /*사원리스트 출력 공간 */}
                <div>
                    <h3> 사원 목록 </h3>
                    <TableContainer component={Paper}>
                         <Table sx={{ minWidth: 650 }} aria-label="simple table">
                           <TableHead>
                             <TableRow>
                               <TableCell align="right">사원번호</TableCell>
                               <TableCell align="right">이름</TableCell>
                               <TableCell align="right">전화번호</TableCell>
                               <TableCell align="right">사원성별</TableCell>
                                <TableCell align="right">계좌번호</TableCell>
                                <TableCell align="right">직급</TableCell>
                             </TableRow>
                           </TableHead>
                           <TableBody>
                             {rows.map((row) => (
                               <TableRow
                                 onClick={() =>{
                                 setEmpName(row.empName);
                                 setEmpNumber(row.empNo);
                                 }}
                                 key={row.name}
                                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>

                                 <TableCell  align="right">{row.empNo}</TableCell>
                                 <TableCell  align="right">{row.empName}</TableCell>
                                 <TableCell  align="right">{row.empPhone}</TableCell>
                                 <TableCell  align="right">{row.empSex}</TableCell>
                                 <TableCell  align="right">{row.empAcn}</TableCell>
                                 <TableCell  align="right">{getrankLabel(row.empRk)}</TableCell>

                               </TableRow>
                             ))}
                           </TableBody>
                         </Table>
                    </TableContainer>

                </div>{/* 사원리스트 end */}

                {/* 프로젝트팀 리스트 출력공간 */}
                <div>
                    <h3> 프로젝트 팀 목록 </h3>
                    <TableContainer component={Paper}>
                         <Table sx={{ minWidth: 650 }} aria-label="simple table">
                         {/* 테이블 제목 구역 */}
                           <TableHead>
                             <TableRow>
                               <TableCell align="right">번호</TableCell>
                               <TableCell align="right">프로젝트명</TableCell>
                               <TableCell align="right">PM</TableCell>
                               <TableCell align="right">프로젝트시작일</TableCell>
                                <TableCell align="right">프로젝트기한</TableCell>
                             </TableRow>
                           </TableHead>
                           {/* 테이블 내용 구역 */}
                           <TableBody>
                             {projectTeam.map((row) => (
                               <TableRow
                                 onClick={() =>{
                                 setPjtName(row.pjtName);
                                 setPjtNumber(row.pjtNo);
                                 }}
                                 key={row.name}
                                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>

                                 <TableCell  align="right">{row.pjtNo}</TableCell>
                                 <TableCell  align="right">{row.pjtName}</TableCell>
                                 <TableCell  align="right">{row.empPhone}</TableCell>
                                 <TableCell  align="right">{row.empName}</TableCell>
                                 <TableCell  align="right">{row.pjtSt}</TableCell>
                                 <TableCell  align="right">{row.pjtEND}</TableCell>

                               </TableRow>
                             ))}
                           </TableBody>
                         </Table>
                    </TableContainer>

                </div>{/* 사원리스트 end */}

        </div>


    </>)
}