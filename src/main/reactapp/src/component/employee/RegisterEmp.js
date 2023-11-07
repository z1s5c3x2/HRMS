import {useEffect, useState} from "react";
import axios from "axios";


export default function RegisterEmp(props) {
    const rkList = ["대기","사원","주임","대리","과장","팀장","부장"]
    function onModal(e) {
        console.log( aprovalInfo )
        document.querySelector('.approv_modal').style.display = 'flex';
    }

    const [aprvList, setAprvList] = useState([]) // 인사팀 전체 리스트
    const [selectList, setSelectList] = useState([]) // 결제 요청받을 리스트 저장
    // 실제 axios로 보낼 데이터
    const [approvalRequest, setApprovalRequest] = useState({
        aprvType:1,
        empNo:"2311001",
    })
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

    //결제받을 사원  클릭시 리스트에서 제외
    const removeItem = (getIndex) => {
        // filter 함수를 사용하여 특정 인덱스의 요소를 제거
        const updateList = selectList.filter((item, index) => (
            index !== getIndex
        ));
        setSelectList(updateList);
    };
    // 사원 데이터
    const [aprovalInfo, setAprovalInfo] = useState({    })
    //이벤트 발생시 이벤트인자를 받아서 이벤트를 일으킨 name과 value로 approvalInfo에 값 수정
    const updateApprovalInfo = (e) =>{
        setAprovalInfo( {...aprovalInfo,[e.target.name] :e.target.value})
    }
    //결제 요청 버튼
    const submitApproval = (e)=>{
        // 결제받을 사원 리스트 id만 추출
        approvalRequest.approvers = selectList.map( s =>{
            return s.empNo
        })
        // axios로 보낼 사원 데이터 key:data로 저장
        approvalRequest.data = aprovalInfo
        // 필드에서 정수로 저장해야 하는 값은 정수로 변환
        approvalRequest.data.dtpmNo = Number(approvalRequest.data.dtpmNo)
        approvalRequest.data.empRk = Number(approvalRequest.data.empRk)
        approvalRequest.data.empSta = approvalRequest.data.empSta == "0"
        //console.log( approvalRequest )
        axios
            .post("/employee/register",approvalRequest)
            .then( (r) => {
                console.log( r )
             })
            .catch( (e) =>{
                console.log( e )
            })
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
                            <input type="radio" name="empSta" value="0" onChange={ updateApprovalInfo } checked = {"0" === aprovalInfo.empSta}/> 재 직
                        <input type="radio" name="empSta" value="1" onChange={ updateApprovalInfo} checked = {"1" === aprovalInfo.empSta}/> 휴 직
                    </div>
                </div>
                <div class="eregInputBox">
                    <div class="input_title ws55">부 서</div>
                    <select name="dtpmNo" onChange={ updateApprovalInfo} >
                        <option value="">부서를 선택하세요</option>
                        <option value={1}>인사팀</option>
                        <option value={2}>기획팀(PM)</option>
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
                    <button onClick={onModal} class="btn01" type="button">사 원 정 보 저 장</button>
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
                        <textarea class="aprv_cont" value={approvalRequest.aprvCont} onChange={(e)=>{
                            setApprovalRequest( {...approvalRequest,aprvCont:e.target.value} )
                        }}></textarea>

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
                                {
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
                                }
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
                                {
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
                                }

                                {/*<!-- 선택사원 표시 구역 e -->*/}
                            </div>
                        </div>
                        <div class="aprvBtnBox">
                            <button onClick="" class="btn02" type="button">취 소</button>
                            <button onClick={submitApproval} class="btn02" type="button">결제요청하기</button>
                        </div>
                    </div>


                </div>
            </form>

        </div>
    </>)
}