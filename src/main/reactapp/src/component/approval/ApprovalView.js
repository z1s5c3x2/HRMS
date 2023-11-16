

import axios from 'axios';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import { getTypeName } from './ListOutputConverter';

/* MUI TABLE 관련 컴포넌트 import */
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

/* MUI 페이지네이션 */
import Pagination from '@mui/material/Pagination';

export default function ApprovalMain(props){

    // 미 로그인 시 접근 불가
    if( sessionStorage.getItem('login_token') == null ){
        alert('개별 결재 목록은 로그인 후 사용가능합니다');
        window.location.href = '/approval'
    }

    // 출력할 결재 리스트 상태변수
    const [ approvalList, setApprovalList ] = useState( [] )
    const [ approbateResult, setApprobateResult ] = useState( false )

    // 페이징처리 및 검색필터;
    const [pageInfo, setPageInfo] = useState({
        page : 1 ,
        key : '' ,      // 결재번호[aprvNo], 내용[aprvCont] , 상신자명[empName]
        keyword : '',   // key에 대한 탐색명
        aprvType : 0,   // 결재의 유형
        apState : '',   // 결재 진행현황 [1:완료  2:반려  3:검토중]
        cdate : ''      // 상신일자
    });

    // 페이지 번호를 클릭했을 때
    const onPageSelect = (e, value) =>{

        pageInfo.page = value;          // 클릭한 페이지번호로 변경
        setPageInfo( {...pageInfo} )    // 새로고침 [상태변수의 주소값이 바뀌면 재랜더링]

    }

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
        <div class="contentBox">
            <div class="searchBox">

                <span>
                    상신일자
                    조회기간 : <input type="date" className="periodStart" /> ~  <input type="date" className="periodEnd" />
                </span>

                <span>
                    결재상태
                    <select>
                        <option value="1"> 완료 </option>
                        <option value="2"> 반려 </option>
                        <option value="3"> 검토중 </option>
                    </select>
                </span>

                <span>
                    <select>
                        <option value="aprvNo"> 결재번호 </option>
                        <option value="aprvCont"> 내용 </option>
                        <option value="empName"> 상신자 </option>
                    </select>
                    <input type="text" />
                    <button type="button" className=""> 검색 </button>
                </span>

            </div>

            <hr class="hr01"/>
            <table className="tableTypeA">

                <tr>
                    <th width="12%"> 결재번호 </th>
                    <th width="20%"> 상신유형 </th>
                    <th width="29%"> 내용 </th>
                    <th width="17%"> 상신일시 </th>
                    <th width="10%"> 상신자 </th>
                    <th width="12%"> 진행상태 </th>
                </tr>


                { approvalList.map( r =>(
                    <tr className="outputTd">
                        <td> 제 { r.aprvNo }호 </td>
                        <td> { getTypeName( r.aprvType ) } </td>
                        <td> { r.aprvCont !== "" ? r.aprvCont : "-" } </td>
                        <td>
                            <span> { (r.cdate.split("T"))[0] } </span>
                            <span> { ((r.cdate.split("T"))[1].split("."))[0].substring(0, 5) } </span>
                        </td>
                        <td> { r.empName } </td>
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

            <div style={{ display : 'flex' , flexDirection : 'column' , alignItems : 'center' , margin : '10px' }}>

                {/* count : 전체 페이지 수   onChange : 페이지번호를 클릭/변경 했을 때 이벤트 */}
                <Pagination page={ pageInfo.page } count={ 5 } variant="outlined" onChange={ onPageSelect } />

            </div>


        </div>

    </>)

}