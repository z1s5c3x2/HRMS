

import {useEffect, useState} from "react";
import axios from "axios";


export default function ApprovalModal(props)
{
    //저장이후 이동항 rul
    const rkList = ["대기","사원","주임","대리","과장","팀장","부장"]
    const [aprvList, setAprvList] = useState([]) // 인사팀 전체 리스트
    const [selectList, setSelectList] = useState([]) // 결제 요청받을 리스트 저장
    // 실제 axios로 보낼 데이터
    const [approvalRequest, setApprovalRequest] = useState({
        aprvType: props.aprvType,
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
    /*console.log( props.targetUrl )
    console.log( props.data )*/
    //결제 요청 버튼
    const submitApproval = (e)=>{


        // 결제받을 사원 리스트 id만 추출
        approvalRequest.approvers = selectList.map( s =>{
            return s.empNo
        })
        approvalRequest.data = props.data


        axios
            .post(props.targetUrl,approvalRequest)
            .then( (r) => {
                if(r.data)
                {
                    window.location.href=props.successUrl
                }else{
                    alert("결재 실패")
                }
            })
            .catch( (e) =>{
                console.log( e )
            })
    }
    return (<>
        <div className="approv_modal" style={ {display:"flex"}}>
            <form className="Approv_form">
                <div className="modal">
                    {/*!-- 1 -->*/}
                    <div className="section">
                        <div className="amodalTitle">결제요청내용</div>
                        <textarea className="aprv_cont" value={approvalRequest.aprvCont} onChange={(e)=>{
                            setApprovalRequest( {...approvalRequest,aprvCont:e.target.value} )
                        }}></textarea>

                    </div>
                    {/*!-- 2 -->*/}
                    <div className="section">
                        <div className="amodalTitle">전체사원리스트</div>
                        <div className="aprvList">
                            <div className="aprvListHeader">
                                <span className="apDept">부서</span>
                                <span className="apLv">직급</span>
                                <span className="apDeptEmp">이름</span>
                            </div>
                            {/*!-- 사원목록 구역 Start -->*/}
                            <div className="aprvListContentBox">
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
                    <div className="section">
                        <div className="amodalTitle">결제요청리스트</div>
                        <div className="aprvList reqlist">
                            <div className="aprvListHeader">
                                <span className="apDept">부서</span>
                                <span className="apLv">직급</span>
                                <span className="apDeptEmp">이름</span>
                            </div>
                            {/*<!-- 사원목록  -->*/}
                            <div className="aprvListContentBox">
                                {/*<!-- 선택사원 표시 구역 -->*/}
                                {/*<div class="aprvListContent">
                                    <span class="apDept">인사</span>
                                    <span class="apLv">부장</span>
                                    <span class="apDeptEmp">김아무개</span>
                                </div>*/}
                                {
                                    selectList.map( (emp,index) =>(
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
                        <div className="aprvBtnBox">
                            <button onClick={props.modalControll} className="btn02" type="button">취 소</button>
                            <button onClick={submitApproval} className="btn02" type="button">결제요청하기</button>
                        </div>
                    </div>


                </div>
            </form>

        </div>
    </>)
}