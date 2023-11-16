import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import { getTypeName } from './ListOutputConverter';

export default function ApprovalMain(props){

    // 미 로그인 시 접근 불가
    if( sessionStorage.getItem('login_token') == null ){
        alert('개별 결재 목록은 로그인 후 사용가능합니다');
        window.location.href = '/approval'
    }

    // 출력할 결재 리스트 상태변수
    const [ approvalList, setApprovalList ] = useState( [] )
    const [ approbateResult, setApprobateResult ] = useState( false )

    // 결재 리스트 요청
    const getApprovalList = e => {

        axios.get( '/approval/getApprovalHistory' ).then( result => {

            console.log( result.data );
            setApprovalList( result.data )

        })

    }

    // 검토대상 내역 및 검토완료 내역 요청
    useEffect( () => {
        getApprovalList();
    }, [approbateResult])


    // 검토완료
    const approvate = ( aprvNo, apState ) => {

        axios.put( '/approval/approbate', { aprvNo:aprvNo, apState:apState } )
            .then( result => {

            if( result ) setApprobateResult( !approbateResult );

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
                    <td> 제 { r.aprvNo }호 </td>
                    <td> { getTypeName( r.aprvType ) } </td>
                    <td> { r.aprvCont !== "" ? r.aprvCont : "-" } </td>
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