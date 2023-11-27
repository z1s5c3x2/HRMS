import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import { getTypeName } from './ListOutputConverter';
import styles from '../../css/ApprovalDetailed.css';

export default function ApprovalDetailed( { isOpen, closeModal, aprvNo } ){

    // 사원 직급
    const rkList = ["인턴","사원","주임","대리","과장","팀장","부장"]

    // 상세 결제건 저장 상태변수
    const [moreInformation, setMoreInformation] = useState({});

    useEffect( () => {

        getApprovaDetailed()

    }, [ aprvNo ])

    // 결재건 상세정보 호출
    const getApprovaDetailed = () => {

        axios.get( '/approval/getDetailedApproval', { params: { aprvNo: aprvNo } } )
            .then( result => {
                console.log( result.data )
                setMoreInformation( result.data );
        })

    }

    return (<>

        {isOpen && (

            <div className="modal-overlay" onClick={closeModal}>
                <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                    <div className="close-button">
                        <button onClick={closeModal}> x </button>
                    </div>

                    <h3> 정보 </h3>
                    <table className="basicApprovalInfoBox">
                        <tbody>
                            <tr>
                                <td>결재번호</td>
                                <td>제 {moreInformation.aprvNo}호</td>
                            </tr>
                            <tr>
                                <td>결재유형</td>
                                <td>{getTypeName(moreInformation.aprvType)}</td>
                            </tr>
                            <tr>
                                <td>진행상태</td>
                                <td>
                                    <span>
                                        {moreInformation.apState === 1 ? <>결재완료</> : moreInformation.apState === 2 ? <>반려</> : <>검토중</>}
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td>상신자</td>
                                <td>
                                    <span>{rkList[moreInformation.empRk]} </span>
                                    <span>{moreInformation.empName}</span>
                                </td>
                            </tr>
                            <tr>
                                <td>상신일시</td>
                                <td>
                                    {moreInformation.cdate && (
                                        <>
                                            <span>{(moreInformation.cdate.split("T"))[0]} </span>
                                            <span>{((moreInformation.cdate.split("T"))[1].split("."))[0].substring(0, 5)}</span>
                                        </>
                                    )}
                                </td>
                            </tr>
                        </tbody>
                    </table>

                    <h3> 내용 </h3>
                    <div className="detailedContentBox">
                        <div> {moreInformation.aprvCont} </div>
                    </div>

                    <h3> 상세진행현황 </h3>
                    <div>
                        <table className="outputModalApprover">
                            <tr>
                                <th> 순번 </th>
                                <th> 검토자 </th>
                                <th> 검토결과 </th>
                                <th> 검토일시 </th>
                            </tr>
                            { moreInformation.approvers && moreInformation.approvers.map( (r, i) => (
                                <tr>
                                    <td> { i+1 } </td>
                                    <td> { rkList[r.empRk] } { r.empName } </td>
                                    <td> { r.apState == 1 ? <> 결재완료 </> :  r.apState == 2 ? <> 반려 </> : <> 검토중 </> } </td>
                                    <td> { r.apState==1 || r.apState==2
                                            ?   <>
                                                    <span> { (r.udate.split("T"))[0] } </span>
                                                    <span> { ((r.udate.split("T"))[1].split("."))[0].substring(0, 5) } </span>
                                                </>
                                            : <>-</> }
                                    </td>
                                </tr>
                            ))}


                        </table>
                    </div>
                </div>
            </div>

        )}
    </>)




}









