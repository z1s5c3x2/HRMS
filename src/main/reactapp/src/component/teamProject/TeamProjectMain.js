import {useEffect, useState} from "react";
import axios from "axios";

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



export default function TeamProjectMain( props ){

    // 프로젝트팀 데이터
    const [projectInfo, setProjectInfo] = useState({
        pjtName : null,
        pjtSt : null,
        pjtEND : null
     });
    // 날짜 데이터
    const [selectedSDate, setSelectedSDate] = useState(null);
    const [selectedEDate, setSelectedEDate] = useState(null);

    // input 값이 입력되면 그 값을 appovalInfo에 저장하는 함수
    const updateApprovalInfo = (e) =>{
        projectInfo.pjtName = e.target.value;
        setProjectInfo({...projectInfo})
        console.log(projectInfo);
    }

    useEffect( () =>{
        projectInfo.pjtSt = selectedSDate;
        projectInfo.pjtEND = selectedEDate;
    }, [selectedSDate, selectedEDate])



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
                <div class="eregInputBox">
                    <div class="input_title " className="inputPm">프로젝트매니저</div>
                    <div class="input_box"><input type="text" class="" name="empNo"/></div>
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
                    <button class="btn01" type="button">프로젝트팀등록</button>
                </div>


            </div>

        </div>

        {/*!-- 결제 모달 Start -->*/}
        <div class="approv_modal">

            <form class="Approv_form">
                <div class="modal">
                    {/*!-- 1 -->*/}
                    <div class="section">
                        <div class="amodalTitle">결제요청내용</div>
                        <textarea class="aprv_cont"></textarea>

                    </div>
                    {/*!-- 2 -->*/}
                    <div class="section">
                        <div class="amodalTitle">전체사원리스트</div>
                        <div class="aprvList">
                            <div class="aprvListHeader">
                                <span class="apDept">부서</span>
                                <span class="apLv">직급</span>
                                <span class="apDeptEmp">이름</span>
                            </div>
                            {/*!-- 사원목록 구역 Start -->*/}
                            <div class="aprvListContentBox">
                                {/*!-- 반복 Start -->*/}
                                {/*<div class="aprvListContent">
                                    <span class="apDept">인사</span>
                                    <span class="apLv">부장</span>
                                    <span class="apDeptEmp">김아무개</span>
                                </div>*/}
                                {/*
                                    aprvList.map((emp) => {
                                        let findEle = selectList.find((em) => emp.empNo === em.empNo);
                                        return (
                                            <div className= {"aprvListContent"+( findEle ? " selectEmployee": "")}
                                                 onClick={(e) => {
                                                if (!findEle) {
                                                    setSelectList([...selectList, emp]);
                                                }
                                            }}>
                                                <span className="apDept">인사</span>
                                                <span className="apLv">{rkList[emp.empRk]}</span>
                                                <span className="apDeptEmp">{emp.empName}</span>
                                            </div>
                                        );
                                    })
                                */}
                                {/*<!-- 반복구간 e -->*/}
                            </div>
                        </div>
                    </div>
                    {/*!-- 3-->*/}
                    <div class="section">
                        <div class="amodalTitle">결제요청리스트</div>
                        <div class="aprvList reqlist">
                            <div class="aprvListHeader">
                                <span class="apDept">부서</span>
                                <span class="apLv">직급</span>
                                <span class="apDeptEmp">이름</span>
                            </div>
                            {/*<!-- 사원목록  -->*/}
                            <div class="aprvListContentBox">
                                {/*<!-- 선택사원 표시 구역 -->*/}
                                {/*<div class="aprvListContent">
                                    <span class="apDept">인사</span>
                                    <span class="apLv">부장</span>
                                    <span class="apDeptEmp">김아무개</span>
                                </div>*/}
                                {/*
                                    selectList.map( (emp,index)=>(
                                        <div className="aprvListContent" onClick={
                                            (e)=>{
                                                removeItem(index)
                                            }
                                        }>
                                            <span className="apDept">인사</span>
                                            <span className="apLv"> {rkList[emp.empRk]} </span>
                                            <span className="apDeptEmp">{emp.empName} </span>
                                        </div>
                                    ) )
                                */}

                                {/*<!-- 선택사원 표시 구역 e -->*/}
                            </div>
                        </div>
                        <div class="aprvBtnBox">
                            <button onClick="" class="btn02" type="button">취 소</button>
                            <button class="btn02" type="button">결제요청하기</button>
                        </div>
                    </div>


                </div>
            </form>

        </div>
    </>)
}