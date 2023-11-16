import {useEffect, useState} from "react";
import axios from "axios";
import {BrowserRouter, Routes, Route, Link, useSearchParams} from 'react-router-dom';
import ApprovalModal from "../approval/ApprovalModal";

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


export default function TeamMemberUpdate( props ){
    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)
    // 모달창 열기/닫기 함수
    const modalController = (e)=> {
        setIsOn(!isOn)
    }

    // url param 값 가져오기
    const [ searchParams, setSearchParams ] = useSearchParams();
    const tmNo = searchParams.get('tmNo');

    // 프로젝트팀 팀원 데이터
    const [teamMemberInfo, setteamMemberInfo] = useState({
        tmSt : null,
        tmEnd : null,
        tmNo : 0,
     });

     useEffect( () => {
        teamMemberInfo.tmNo = tmNo;
        console.log(teamMemberInfo);
     }, [searchParams])

    // 날짜 데이터
    const [selectedSDate, setSelectedSDate] = useState(null);
    const [selectedEDate, setSelectedEDate] = useState(null);

    // 날짜값을 입력할때마다 프로젝트팀 팀원 데이터에 값 넣어줌
    useEffect( () =>{
        teamMemberInfo.tmSt = selectedSDate;
        teamMemberInfo.tmEnd = selectedEDate;
        console.log(teamMemberInfo);
    }, [selectedSDate, selectedEDate])


    return(<>
        <div className="teamMemberWrap">
            <div class="contentBox">
                <div class="pageinfo"><span class="lv0">프로젝트팀관리</span> > <span class="lv1">프로젝트팀 팀원등록</span></div>
                <div class="emp_regs_content">
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
                    <div class="eregInputBox">
                        <div class="input_title ">투입종료날짜</div>
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
                        <button class="btn01" type="button" onClick={modalController}>수정</button>
                    </div>
                </div>
                {/*!-- 결제 모달 Start --> targetUrl: axios로 보낼 url aprvType: 결제 타입 설정  successUrl :결제 성공후 이동할 url  */   }
                    { isOn ? <> <ApprovalModal data={teamMemberInfo}
                                              targetUrl={"/approval/putAproval"}
                                              aprvType={16}
                                              successUrl={"/teamProject/listAll"}
                                              modalControll={modalController}>
                    </ApprovalModal></> : <> </> }
            </div>

        </div>
    </>)
}