import React, { useState , useEffect  } from 'react';
import { useParams } from "react-router-dom";
import axios from "axios";

import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';

import Paper from '@mui/material/Paper';
import Pagination from "@mui/material/Pagination";

export default function SalaryMain(props) {
     // 0. 컴포넌트 상태변수 관리
       let [ pageDto , setPageDto ] = useState( {
                      someList : [] , totalPages : 0 , totalCount : 0
        } );
    // 3. 현재 로그인된 회원의 번호
    const login = JSON.parse(sessionStorage.getItem('login_token'));
    const login_empNo = login != null ? login.empNo : null;
    const login_empName = login != null ? login.empName : null;
     // 0. 스프링에게 전달할 객체
               const [ pageInfo , setPageInfo ] = useState( {
                   page : 1 ,  view : 5 , empNo : login_empNo ,  slryType : 0 , DateSt : '' , DateEnd : ''
    }); console.log( pageInfo );




    // 특정 레코드 클릭시 해당 레코드 상세보기
        const loadView = ( slryNo ) => {
                        window.location.href = '/salary/view?slryNo='+slryNo
                    }

    // 1-1.  axios를 이용한 스프링의 컨트롤과 통신
          const getslryMe = (e) => {
                axios.get('/salary/getMe' , { params : pageInfo } )
                        .then( r =>{  // r.data : PageDto  // r.data.boardDtos : PageDto 안에 있는 boardDtos
                             setPageDto( r.data ); // 응답받은 모든 게시물을 상태변수에 저장 // setState : 해당 컴포넌트가 업데이트(새로고침/재랜더링/return재실행)
                          });
                     }
     // 1-2 컴포넌트가 생성될 때 // + 의존성 배열 : page 변경될떄 // + 의존성 배열 : view 변경될 때
                    useEffect( () => { getslryMe(); }, [ pageInfo.page , pageInfo.view , pageInfo.slryType , pageInfo.DateSt , pageInfo.DateEnd  ] );
     // 2. 페이지 번호를 클릭했을 때.
                         const onPageSelect = ( e , value )=>{
                                 pageInfo.page = value; // 클릭한 페이지번호로 변경
                                  setPageInfo( { ...pageInfo } ); // 새로고침 [ 상태변수의 주소 값이 바뀌면 재랜더링 ]
                        }

    function getSlryTypeLabel(slryType) {
        switch (slryType) {
            case 1:
                return "기본급";
            case 2:
                return "정기상여";
            case 3:
                return "특별상여";
            case 4:
                return "성과금";
            case 5:
                return "명절휴가비";
            case 6:
                return "퇴직금";
            default:
                return "알 수 없는 타입";
        }
    }

    return (<>
    <div className="contentBox">
        <div className="pageinfo"><span className="lv0">급여관리</span> > <span className="lv1">개인 급여내역</span></div>
            <h3>{ /*row.empNo*/ } {login_empName}({login_empNo})님 급여 내역보기</h3>
        {/*<p> page : { pageInfo.page  } totalCount : { pageDto.totalCount  } </p>*/}
               <div class="searchBoxB divflex pd10_0">
                        <div class="divflex w54">
                            <div>
                                  조회기간 : <input type="date" className="periodStart" onChange={ (e)=> { setPageInfo( { ...pageInfo , DateSt : e.target.value} ); } }/> ~
                                  <input type="date" className="periodEnd" onChange={ (e)=> { setPageInfo( { ...pageInfo , DateEnd : e.target.value} ); } } />
                            </div>

                            <select
                              value = { pageInfo.slryType }
                              onChange={ (e)=>{  setPageInfo( { ...pageInfo ,  slryType : e.target.value} );  } }
                              >
                              <option  value="0"> 전체유형 </option>
                              <option  value="1"> 기본급 </option>
                              <option  value="2"> 정기상여 </option>
                              <option  value="3"> 특별상여 </option>
                              <option  value="4"> 성과금 </option>
                              <option  value="5"> 명절휴가비 </option>
                              <option  value="6"> 퇴직금 </option>
                              <option  value="7"> 경조사비 </option>
                              <option  value="8"> 연가보상비 </option>
                          </select>
                    </div>
                    <div>
                        <select
                            value = { pageInfo.view }
                            onChange={ (e)=>{  setPageInfo( { ...pageInfo , view : e.target.value} );  } }
                        >
                            <option value="5"> 5 </option>
                            <option value="10"> 10 </option>
                            <option value="20"> 20 </option>
                        </select>
                    </div>
                </div>
                <TableContainer
                 sx={{
                     width: 900,
                     height: 500,
                     'td': {
                         textAlign: 'center',
                         fontSize: '0.8rem',
                         paddingTop: '9px',
                         paddingBottom: '9px',
                         paddingLeft: '3px',
                         paddingRight: '3px',
                         border:'solid 1px var(--lgray)'
                     }
                 }}
                 component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead
                        sx={{
                            'th':{
                                textAlign: 'center',
                                fontSize: '0.9rem',
                                bgcolor: 'var(--main04)',
                                color: '#fff',
                                paddingTop: '10px' ,
                                paddingBottom: '10px',
                            }
                        }}

                    >
                        <TableRow>
                            <TableCell align="right">번호</TableCell>
                            <TableCell align="right">지급날짜</TableCell>
                            <TableCell align="right">지급금액</TableCell>
                            <TableCell align="right">지급유형</TableCell>
                            <TableCell align="right">결재번호</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {pageDto.someList.map((row) => (
                            <TableRow
                                key={row.name}
                                >
                                <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.slryNo}</TableCell>
                                <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.slryDate}</TableCell>
                                <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.slryPay}</TableCell>
                                <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{getSlryTypeLabel(row.slryType)}</TableCell>
                                <TableCell onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.aprvNo}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <div style = {{ display : 'flex' , flexDirection : 'column' , alignItems : 'center' , margin : '10px' }} >
             { /* page : 현재페이지    count : 전체페이지수   onChange : 페이지번호를 클릭/변경 했을떄 이벤트 */}
              <Pagination page = { pageInfo.page }  count={ pageDto.totalPages } onChange={ onPageSelect } />
             </div>
        </div>
        </>)
}
