/*import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';


export default function ApprovalMain(props){


    const [ approvalList, setApprovalList ] = useState( [] )

    // 검토대상 내역 및 검토완료 내역 요청
    useEffect( () => {
        axios.get( '/approval/getApprovalHistory' ).then( result => {

            console.log( result.data );
            setApprovalList( result.data )

        })
    })

    return (<>

        <h3> 결재관리 </h3>


    </>)

}*/