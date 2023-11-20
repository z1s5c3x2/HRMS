import {useSearchParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import * as React from "react";
import ApprovalModal from "../approval/ApprovalModal";

export default function EmployeeDetails()
{
    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)
    const modalController = (e)=> {
        setIsOn(!isOn)
    }
    /*모달 호출 선언 필요*/
    // int to String
    const rkList = ["대기","사원","주임","대리","과장","팀장","부장"]
    const dptList = ['인사팀','기획팀(PM)','개발팀','DBA팀']
    const [empInfo, setEmpInfo] = useState({})
    //.get("/employee/findemp?empNo="+searchParams.get('empNo'))
    useEffect(() => {
        axios
            .get("/employee/myinfo")
            .then( (r) => {
                setEmpInfo(r.data)
            })
            .catch( (e) =>{
                console.log( e )
            })
    }, []);
    // 해당 함수를 호출한 className과 value로 값 수정
    const updateApprovalInfo = (e) =>{
        setEmpInfo( {...empInfo,[e.target.name] :e.target.value})
    }
    const checkPwd = (e) =>{
        if(empInfo.changePwd2 != empInfo.changePwd2)
        {
            alert("변경할 비밀번호를 확인해주세요")
        }else{
            if(empInfo.changePwd2 != null)
            {
                setEmpInfo( {...empInfo,empNewPwd:empInfo.changePwd2})
            }
            modalController();
        }

    }
    return( <>
        <div className="contentBox">
            <div className="pageinfo"><span className="lv0">인사관리</span> > <span className="lv1">사원 수정</span></div>
            <div className="emp_regs_content">
                <div class="eregInputBox">
                    <div class="input_title ls19"> 사원번호</div>
                    <div class="input_box"><input type={"text"} value={empInfo.empNo} name={"empNo"} onChange={updateApprovalInfo} disabled={true}/></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ls19"> 사원이름</div>
                    <div class="input_box"><input type={"text"} value={empInfo.empName} name={"empName"} onChange={updateApprovalInfo}  /></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ls19"> 전화번호</div>
                    <div class="input_box"><input type={"text"} value={empInfo.empPhone} name={"empPhone"} onChange={updateApprovalInfo} /></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ls19"> 사원성별</div>
                    <div class="input_box"><input type={"text"} value={empInfo.empSex} name={"empSex"} onChange={updateApprovalInfo} /></div>
                </div>
                <div class="eregInputBox">
                   <div class="input_title ls19"> 계좌번호</div>
                   <div class="input_box"><input type={"text"} value={empInfo.empAcn} name={"empAcn"} onChange={updateApprovalInfo} /></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ls19"> 비밀번호</div>
                    <div class="input_box"><input type={"password"} value={empInfo.empPwd} name={"empPwd"} onChange={updateApprovalInfo} /></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ls6"> 비밀번호변경</div>
                    <div class="input_box"><input type={"password"} value={empInfo.changePwd1} name={"changePwd1"} onChange={updateApprovalInfo} /></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title "> 비밀번호 변경확인</div>
                    <div class="input_box"><input type={"password"} value={empInfo.changePwd2} name={"changePwd2"} onChange={updateApprovalInfo} /></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ls12"> 현근무상태</div>
                    <div class="input_box"><input value={empInfo.empSta ? "재직" : "휴직"} disabled={true}/></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ls19"> 근무부서</div>
                    <div class="input_box"> <input value={rkList[empInfo.dptmNo-1]} disabled={true}/></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ls19"> 사원직급</div>
                    <div class="input_box"><input value={rkList[empInfo.empRk]} disabled={true}/></div>
                </div>
                 <div class="eregBtnBox">
                     <button onClick={checkPwd} class="btn01" type="button">사 원 정 보 수 정</button>
                 </div>
            </div>

            { isOn ? <> <ApprovalModal data={empInfo}
                                       aprvType={2}
                                       targetUrl={"/employee/changeinfo"}
                                       successUrl={"/approval/reconsiderview"}
                                       modalControll={modalController}>
            </ApprovalModal></> : <> </> }
        </div>

    </>)
}
