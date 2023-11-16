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


export default function ApprovalList(props){

    const [ allApprovalList, setAllApprovalList ] = useState( [] )

    // 페이징처리 및 검색필터;
    const [pageInfo, setPageInfo] = useState({
        page : 1 ,
        key : '' ,      // 결재번호[aprvNo], 내용[aprvCont] , 상신자명[empName]
        keyword : '',   // key에 대한 탐색명
        aprvType : 0,
        apState : '',
        cdate : ''
    });

    // 2. 페이지 번호를 클릭했을 때
    const onPageSelect = (e, value) =>{

        pageInfo.page = value;          // 클릭한 페이지번호로 변경
        setPageInfo( {...pageInfo} )    // 새로고침 [상태변수의 주소값이 바뀌면 재랜더링]

    }







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


        <div class="searchBox">
            <div>
                <select>
                    <option value="aprvNo">
                    <option value="aprvCont">
                    <option value="empName">
                </select>
                <input type="text" />
            <div>

            <div>
                결재유형
                <select>
                    <option value="1"> test </option>

                </select>

            </div>

        </div>


        <table className="reconsiderTable">

            <tr>
                <th> 결재번호 </th>
                <th> 상신유형 </th>
                <th> 내용 </th>
                <th> 상신일시 </th>
                <th> 상신자 </th>
                <th> 진행상태 </th>
            </tr>
            { allApprovalList.map( r =>(
                <tr>
                    <td> 제 { r.apState }호 </td>
                    <td> { getTypeName( r.aprvType ) } </td>
                    <td> { r.aprvCont } </td>
                    <td>
                        <span> { (r.cdate.split("T"))[0] } </span>
                        <span> { ((r.cdate.split("T"))[1].split("."))[0].substring(0, 5) } </span>
                    </td>
                    <td> { r.empName } </td>
                    <td> {
                        r.apState == 1 ? <> 결재완료 </> :  r.apState == 2 ? <> 반려 </> : <> 검토중 </>
                    }
                    </td>
                </tr>
            ))}

        </table>

        <div style={{ display : 'flex' , flexDirection : 'column' , alignItems : 'center' , margin : '10px' }}>

            {/* count : 전체 페이지 수   onChange : 페이지번호를 클릭/변경 했을 때 이벤트 */}
            <Pagination page={ pageInfo.page } count={ 5 } variant="outlined" onChange={ onPageSelect } />

        </div>

    </>)


}