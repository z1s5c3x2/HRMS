import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

export default function ApprovalDetailed( { isOpen, closeModal, aprvNo } ){

    useEffect( () => {
        console.log( aprvNo )
    })

    // 결재건 상세정보 호출
    const getApprovaDetailed = () => {

        axios.get( '/approval/getDetailedApproval', {params : aprvNo} )
            .then( result => {
                console.log( result.data );
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