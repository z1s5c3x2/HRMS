
import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

export default function ReconsiderView(props){

    // 상신내역 요청
    useEffect( () => {
        const getReconsidering = r => {

        }

axios.get( '/approval/getApprovalHistory' )
            .then( result => {
                console.log(111)
                console.log( result )

            })
    }, [])


    return (<>

        <h3> 상신관리 </h3>



    </>)

}