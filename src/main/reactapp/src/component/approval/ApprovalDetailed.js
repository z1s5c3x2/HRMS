import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import styles from '../../css/ApprovalDetailed.css';

export default function ApprovalDetailed( { isOpen, closeModal, aprvNo } ){

    // 상세 결제건 저장 상태변수
    const [moreInformation, setMoreInformation] = useState({});

    useEffect( () => {

        getApprovaDetailed()

    }, [ aprvNo ])

    // 결재건 상세정보 호출
    const getApprovaDetailed = () => {

        axios.get( '/approval/getDetailedApproval', { params: { aprvNo: aprvNo } } )
            .then( result => {
                setMoreInformation( result.data );
        })

    }

    return (<>

        {isOpen && (

            <div className="modal-overlay" onClick={closeModal}>
                <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                    <h2>Modal Content</h2>
                    <p>This is the content of the modal.</p>
                    <button onClick={closeModal}>Close Modal</button>
                </div>
            </div>

        )}
    </>)




}