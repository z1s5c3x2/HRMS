import React, { useState , useEffect  } from 'react';


import {useSearchParams} from "react-router-dom";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {DesktopDatePicker} from "@mui/x-date-pickers/DesktopDatePicker";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import ApprovalModal from "../approval/ApprovalModal";

import axios from "axios";
import dayjs from "dayjs";


export default function LeaveRequestUpdate (props){

    // 3. 현재 로그인된 회원의 번호
           const login = JSON.parse(sessionStorage.getItem('login_token'));
           const login_empNo = login != null ? login.empNo : null;
           const login_empName = login != null ? login.empName : null;



    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)
    const modalController = (e)=> {
        setIsOn(!isOn)
    }
    /*모달 호출 선언 필요*/
    // url 파라미터 서치
    const [ searchParams , setSearchParams ]  = useSearchParams()
    const [lrqInfo, setLrqInfo] = useState({}) //휴가 정보와 남은 연차 수 가져오기
    useEffect(() => {
        axios
            .get("/leaveRequest/findone",{params:{lrqNo:searchParams.get('lrqNo')}})
            .then( (r) => {
                r.data.lrqEnd = dayjs(r.data.lrqEnd)
                r.data.lrqSt = dayjs(r.data.lrqSt)
                setLrqInfo(r.data)
             })
            .catch( (e) =>{
                console.log( e )
            })
    }, []);
    console.log( lrqInfo )

    return( <>
        <div className="contentBox">
            <div className="pageinfo"><span className="lv0">근태관리</span> > <span className="lv1">연차 수정</span></div>
            <h3>{login_empName}({login_empNo})님 사용 가능한 연차 : {lrqInfo.leaveCnt}일</h3>

            <form className="boardForm">
                <div className="emp_regs_content">
                    <div className="eregInputBox">
                        <div className="input_title ">연차 시작 날짜</div>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DesktopDatePicker
                                value={lrqInfo.lrqSt}
                                sx={{ width: '70%'}}
                                onChange={(date1) => setLrqInfo({...lrqInfo,lrqSt:date1})}
                                renderInput={(params) => <TextField {...params} label="날짜" />}
                                format="YYYY-MM-DD"
                            />
                        </LocalizationProvider>
                    </div>
                    <div className="eregInputBox">
                        <div className="input_title ">연차 종료 날짜</div>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DesktopDatePicker
                                value={lrqInfo.lrqEnd}
                                onChange={(date2) => {
                                    console.log( typeof date2 )
                                    setLrqInfo({...lrqInfo,lrqEnd:date2})}}
                                sx={{ width: '70%'}}
                                renderInput={(params) => <TextField {...params} label="날짜" />}
                                format="YYYY-MM-DD"
                            />
                        </LocalizationProvider>
                    </div>

                    <div className="eregInputBox pmBox">
                        <div className="input_title ">급여 유무</div>
                        <TextField
                            label="( 0: 무급 / 1: 유급 )"
                            type="number"
                            value={lrqInfo.lrqSrtype}
                            onChange={(e) => setLrqInfo({...lrqInfo,lrqSrtype:e.target.value})}
                        />
                    </div>
                    <div className="eregBtnBox">
                        <Button className="btn01" variant="contained" color="primary" onClick={modalController}>
                            결재요청
                        </Button>
                    </div>
                </div>
            </form>
            { isOn ? <> <ApprovalModal data={lrqInfo}
                                       aprvType={ 11  }
                                       targetUrl={"/leaveRequest/updateyearleave"}
                                       successUrl={"/leaveRequest/list"}
                                       modalControll={modalController}>
            </ApprovalModal> </> : <> </> }
        </div>
    </>)

}