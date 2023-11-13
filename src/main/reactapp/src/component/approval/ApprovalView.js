import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import { getTypeName } from './ListOutputConverter';

export default function ApprovalMain(props){


    const [ approvalList, setApprovalList ] = useState( [] )

    // 검토대상 내역 및 검토완료 내역 요청
    useEffect( () => {
        axios.get( '/approval/getApprovalHistory' ).then( result => {

            console.log( result.data );
            setApprovalList( result.data )

        })
    }, [])



    // 검토완료
    const approvate = ( aprvNo, aplogSta ) => {

        axios.put( '/approval/approbate', { aprvNo:aprvNo, aplogSta:aplogSta } )
            .then( result => {
                console.log( result.data );
        })
    }

    return (<>

        <h3> 결재관리 </h3>

        <table className="approvalTable">

            <tr>
                <th> 결재번호 </th>
                <th> 상신유형 </th>
                <th> 내용 </th>
                <th> 상신일시 </th>
                <th> 진행상태 </th>
            </tr>
            { approvalList.map( r =>(
                <tr>
                    <td> 제 {r.apState}호 </td>
                    <td> { getTypeName( r.aprvType ) } </td>
                    <td> {r.aprvCont} </td>
                    <td>
                        <span> { (r.cdate.split("T"))[0] } </span>
                        <span> { ((r.cdate.split("T"))[1].split("."))[0].substring(0, 5) } </span>
                    </td>
                    <td> {
                        r.apState == 1 ? <> 결재완료 </>
                        :  r.apState == 2 ? <> 반려 </>
                        : <>
                            <button onClick={ () => approvate( r.aprvNo, 1 ) } type="button"> 결재 </button>
                            <button onClick={ () => approvate( r.aprvNo, 2 ) } type="button"> 반려 </button>
                         </>
                    }
                    </td>
                </tr>
            ))}

        </table>

    </>)

}