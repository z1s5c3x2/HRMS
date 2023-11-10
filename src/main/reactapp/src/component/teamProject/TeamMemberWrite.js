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

    // 사원찾기 모달창 여는 함수
    function onModal2(e){
        document.querySelector('.approv_modal').style.display = 'flex';
    }

    // 프로젝트팀 팀원 데이터
    const [teamMemberInfo, setteamMemberInfo] = useState({
        tmSt : null,
        pjtNo : 1,
        empNo : "2311002"
     });
    // 날짜 데이터
    const [selectedSDate, setSelectedSDate] = useState(null);

    // 날짜값을 입력할때마다 프로젝트팀 팀원 데이터에 값 넣어줌
    useEffect( () =>{
        teamMemberInfo.tmSt = selectedSDate;
        console.log(teamMemberInfo);
    }, [selectedSDate])

    return(<>
        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">프로젝트팀관리</span> > <span class="lv1">프로젝트팀 팀원등록</span></div>
            <div class="emp_regs_content">

                <div class="eregInputBox pmBox">
                    <div class="input_title " className="inputPm">프로젝트팀원</div>
                    <div class="input_box">
                        <input type="text" class="empNo" name="empNo"/>
                        <span><button onClick={onModal2} class="empBtn" type="button">사원찾기</button></span>
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