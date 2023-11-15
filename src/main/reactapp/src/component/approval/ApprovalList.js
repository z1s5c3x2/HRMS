import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import { getTypeName } from './ListOutputConverter';

export default function ApprovalList(props){

    const [ allApprovalList, setAllApprovalList ] = useState( [] )

    // 상신내역 요청
    useEffect( () => {

        axios.get( '/approval/getAllEmployeesApproval' )
            .then( result => {
                console.log( result.data );
                setAllApprovalList( result.data );
        })

    }, [])

    return (<>

        <h3> 결재 메인페이지 </h3>

        <table className="reconsiderTable">

            <tr>
                <th> 결재번호 </th>
                <th> 상신유형 </th>
                <th> 내용 </th>
                <th> 상신일시 </th>
                <th> 진행상태 </th>
            </tr>
            { allApprovalList.map( r =>(
                <tr>
                    <td> 제 {r.apState}호 </td>
                    <td> { getTypeName( r.aprvType ) } </td>
                    <td> {r.aprvCont} </td>
                    <td>
                        <span> { (r.cdate.split("T"))[0] } </span>
                        <span> { ((r.cdate.split("T"))[1].split("."))[0].substring(0, 5) } </span>
                    </td>
                    <td> {
                        r.apState == 1 ? <> 결재완료 </> :  r.apState == 2 ? <> 반려 </> : <> 검토중 </>
                    }
                    </td>
                </tr>
            ))}

        </table>

    </>)


}