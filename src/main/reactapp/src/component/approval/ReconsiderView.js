/*import styles from '../../css/approval.css';
import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import ApprovalList from './ApprovalList';

export default function ReconsiderView(props){

    const [ reconsiderList, setReconsiderList ] = useState( [] )

    // 상신내역 요청
    useEffect( () => {

        axios.get( '/approval/getReconsiderHistory' )
            .then( result => {
                console.log( result.data );
                setReconsiderList( result.data );
        })

    }, [])




    return (<>

        <h3> 상신관리 </h3>

        <table className="reconsiderTable">

            <tr>
                <th> 결재번호 </th>
                <th> 상신유형 </th>
                <th> 내용 </th>
                <th> 상신일시 </th>
                <th> 진행상태 </th>
            </tr>
            { reconsiderList.map( r =>(
                <tr>
                    <td> 제 {r.apState}호 </td>
                    <td> { props.getTypeName( r.aprvType ) } </td>
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

}*/