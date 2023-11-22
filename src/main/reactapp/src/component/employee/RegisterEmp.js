import styles from '../../css/employee.css';

import {useImperativeHandle, useEffect, useState} from "react";
import axios from "axios";
import css1 from "../../css/employee.css"
import ApprovalModal from "../approval/ApprovalModal";

export default function RegisterEmp(props) {
    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)
    const modalController = (e)=> {
        setIsOn(!isOn)
    }
    /*모달 호출 선언 필요*/

    // 사원 데이터
    const [aprovalInfo, setAprovalInfo] = useState({    })
    //이벤트 발생시 이벤트인자를 받아서 이벤트를 일으킨 name과 value로 approvalInfo에 값 수정
    const updateApprovalInfo = (e) =>{
        setAprovalInfo( {...aprovalInfo,[e.target.name] :e.target.value})
    }




    return (<>
        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">인사관리</span> > <span class="lv1">사원등록</span></div>
            <div class="emp_regs_content">

                <div class="eregInputBox">
                    <div class="input_title ls23"> 사원명</div>
                    <div class="input_box"><input type="text" className="" name="empName" value={aprovalInfo.empName} onChange={ updateApprovalInfo} /></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ws55"> 성 별</div>
                    <div class="input_box">
                        <input type="radio" name="empSex" value="남자" onChange={ updateApprovalInfo } checked = {"남자" === aprovalInfo.empSex} /> 남
                        <input type="radio" name="empSex" value="여자" onChange={ updateApprovalInfo } checked = {"여자" === aprovalInfo.empSex} /> 여
                    </div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ">사 원 연 락 처</div>
                    <div class="input_box"><input type="text" class="" name="empPhone" value={aprovalInfo.empPhone} onChange={ updateApprovalInfo}/></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title">사원 비밀번호</div>
                    <div class="input_box"><input type="text" class="" name="empPwd" value={aprovalInfo.empPwd} onChange={ updateApprovalInfo}/></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title"> 사원 계좌번호</div>
                    <div class="input_box"><input type="text" class="" name="empAcn" value={aprovalInfo.empAcn} onChange={ updateApprovalInfo} /></div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ls3"> 근 무 상 태</div>
                    <div class="input_box">
                            <input type="radio" name="empSta" value={true} onChange={ updateApprovalInfo } checked = {aprovalInfo.empSta == 'true'}/> 재 직
                        <input type="radio" name="empSta" value={false} onChange={ updateApprovalInfo} checked = {aprovalInfo.empSta == 'false'}/> 휴 직
                    </div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ws55">부 서</div>
                    <select name="dptmNo" onChange={ updateApprovalInfo} >
                        <option value="">부서를 선택하세요</option>
                        <option value={1}>인사팀</option>
                        <option value={2}>기획팀(PM)</option>
                        <option value={3}>개발팀</option>
                        <option value={3}>DBA팀</option>
                    </select>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ws55">직 급</div>
                    <select name="empRk" onChange={ updateApprovalInfo} >
                        <option value={-1}>직급을 선택하세요</option>
                        <option value={6}>부장 </option>
                        <option value={5}>팀장 </option>
                        <option value={4}>과장</option>
                        <option value={3}>대리 </option>
                        <option value={2}>주임 </option>
                        <option value={1}>사원 </option>
                        <option value={0}>인턴  </option>

                    </select>
                </div>
                <div class="eregBtnBox">
                    <button onClick={modalController} class="btn01" type="button">사 원 정 보 저 장</button>
                </div>
            </div>

        </div>
        {/*!-- 결재 모달 Start --> targetUrl: axios로 보낼 url aprvType: 결재 타입 설정  successUrl :결재 성공후 이동할 url  */   }
        { isOn ? <> <ApprovalModal data={aprovalInfo}
                                   aprvType={1}
                                   targetUrl={"/employee/postEmp"}
                                   successUrl={"/approval/reconsiderview"}
                                   modalControll={modalController}>
        </ApprovalModal></> : <> </> }

    </>)
}