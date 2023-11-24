import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import { getTypeName } from './ListOutputConverter';
import styles from '../../css/ApprovalDetailed.css';

export default function ApprovalDetailed( { isOpen, closeModal, aprvNo } ){

    // 사원 직급
    const rkList = ["대기","사원","주임","대리","과장","팀장","부장"]

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
                    <div className="basicApprovalInfoBox">
                        <div> 결재번호 <span> 제 { moreInformation.aprvNo }호 </span> </div>
                        <div> 결재유형 <span> { getTypeName( moreInformation.aprvType ) } </span> </div>
                        <div> 진행상태
                            <span>
                                {
                                    moreInformation.apState == 1 ? <> 결재완료 </> : moreInformation.apState == 2 ? <> 반려 </> : <> 검토중 </>
                                }
                            </span>
                        </div>
                        <div> 상신자   <span> {rkList[moreInformation.empRk]} </span><span> {moreInformation.empName} </span> </div>
                        <div> 상신일시
                            {moreInformation.cdate && (
                                <>
                                    <span> {(moreInformation.cdate.split("T"))[0]} </span>
                                    <span> {((moreInformation.cdate.split("T"))[1].split("."))[0].substring(0, 5)} </span>
                                </>
                            )}
                         </div>
                    </div>

                    <h3> 내용 </h3>
                    <div className="detailedContentBox">
                        <div> 내용 </div>
                    </div>

                    <h3> 상세진행현황 </h3>
                    <div></div>
                </div>
            </div>

        )}
    </>)




}