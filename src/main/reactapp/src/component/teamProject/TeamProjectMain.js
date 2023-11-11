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



export default function TeamProjectMain( props ){
    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)

    // 사원 직급 배열
    const rkList = ["대기","사원","주임","대리","과장","팀장","부장"]
    // 결재 모달창 여는 함수
    function onModal(e) {
        //document.querySelector('.approv_modal').style.display = 'flex';
        setIsOn(!isOn)
    }

    function onModal2(e){
        document.querySelector('.approv_modal').style.display = 'flex';
    }

    // 인사팀 전체 리스트
    const [aprvList, setAprvList] = useState([])

    // 프로젝트팀 데이터
    const [projectInfo, setProjectInfo] = useState({
        pjtName : null,
        pjtSt : null,
        pjtEND : null,
        empNo : "2311002"
     });
    // 날짜 데이터
    const [selectedSDate, setSelectedSDate] = useState(null);
    const [selectedEDate, setSelectedEDate] = useState(null);


    // 결제 요청받을 사원 리스트 저장
    const [selectList, setSelectList] = useState([])

    //최초로 인사팀 전부 호출
    useEffect(() => {
        axios
            .get("/employee/getaprvlist")
            .then( (r) => {
                setAprvList(r.data)
             })
            .catch( (e) =>{
                console.log( e )
            })
    }, []);

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




    return(<>
        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">프로젝트팀관리</span> > <span class="lv1">프로젝트팀등록</span></div>
            <div class="emp_regs_content">

                <div class="eregInputBox">
                    <div class="input_title ls23" className="inputPname"> 프로젝트명</div>
                    <div class="input_box"><input
                        type="text"
                        className=""
                        name="pjtName"
                        onChange={updateApprovalInfo}
                     /></div>
                </div>
                <div class="eregInputBox pmBox">
                    <div class="input_title " className="inputPm">프로젝트매니저</div>
                    <div class="input_box">
                        <input type="text" class="pmNo" name="empNo"/>
                        <span><button onClick={onModal2} class="pmBtn" type="button">사원찾기</button></span>
                    </div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ">프로젝트시작날짜</div>
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
                <div class="eregInputBox">
                    <div class="input_title ">프로젝트완료날짜</div>
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
                <div class="eregBtnBox">
                    <button class="btn01" type="button" onClick={onModal}>프로젝트팀등록</button>
                </div>
            </div>
            {/*!-- 결제 모달 Start --> targetUrl: axios로 보낼 url aprvType: 결제 타입 설정  successUrl :결제 성공후 이동할 url  */   }
                { isOn ? <> <ApprovalModal data={projectInfo}
                                          targetUrl={"/teamproject/post"}
                                          aprvType={12}
                                          successUrl={"/teamproject"}>
                </ApprovalModal></> : <> </> }



        </div>

        {/*!-- 사원리스트 모달 Start -->*/
        <div class="approv_modal">

            <form class="Approv_form">
                <div class="modal">
                    {
                       <EmployeeList />
                    }

                    {/* 삭제된 곳 */}
                </div>
            </form>
        </div>
        }

    </>)
}