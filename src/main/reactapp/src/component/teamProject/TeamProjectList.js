import axios from 'axios';
import { useState , useEffect } from 'react';
import {BrowserRouter, Routes, Route, Link, useSearchParams} from 'react-router-dom';

// -------------- mui table ----------------
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

// ------------ page nation -----------------
import * as React from 'react';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
// -------------------------------------------
import ApprovalModal from "../approval/ApprovalModal";
import styles from '../../css/Table.css';

export default function TeamProjectList(props){

    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)

    // 결재 모달창 여는 함수
    function onModal(e) {
        //document.querySelector('.approv_modal').style.display = 'flex';
        setIsOn(!isOn)
    }


    // 컴포넌트 상태변수 (스프링에서 전달받은 객체)
    let [rows, setRows] = useState( {
        someList : [],
        totalPages : 0,
        totalCount : 0
    } )
    // 체크박스 상태변수
    let [check, setCheck] = useState( [] )
    // 페이지 상태변수 관리
    const [page, setPage] = useState( 1 )
    // 프로젝트팀번호 상태변수 관리
    const [projectNum, setProjectNum] = useState( [] );

    useEffect( () => {

        // 전체보기 체크시
        if(check == 0){
            axios
                .get('/teamproject/getAll', { params : {page : page} } )
                .then(r => {
                    console.log(r.data);
                    setRows(r.data);    // 응답받은 값들을 상태변수에 저장
                })
        }
        // 승인, 반려, 검토중 체크시
        else if(check > 0){
            axios
                .get('/teamproject/getPermitAll', { params : { approval : check , page : page} })
                .then(r => {
                    console.log(r.data);
                    setRows(r.data);    // 응답받은 값들을 상태변수에 저장
                })
        }

    }, [check,page]);

    // 체크박스는 하나만 체크될수 있게하는 함수
    const checkOnlyOne = (element) => {
        const checkboxes = document.getElementsByName("cb");

        checkboxes.forEach((c) => {
            c.checked = false;
        });
        console.log(check)
        setCheck(element.value);
        setPage(1);
        element.checked = true;
    };

    // 페이지 번호를 클릭했을때
    const onPageSelect = (e, value) => {
        console.log(value);
        setPage(value)
    }

    // 삭제 버튼을 누르면 실행되는 함수
    const projectDelete = (e)=>{
        setProjectNum(e);
        console.log(projectNum);
        onModal();
    }



    return(<>
     <div className="contentBox">
        <div className="pageinfo"><span className="lv0">프로젝트팀관리</span> > <span className="lv1">팀조회</span></div>
        <div className="cont">
            <div className="checkboxList">
                <span>전체보기<input type="checkbox"
                name="cb"
                value={0}
                className="cb1"
                onClick={(e) => checkOnlyOne(e.target)}
                defaultChecked /></span>

                <span>승인완료<input type="checkbox"
                name="cb"
                value={1}
                className="cb2"
                onClick={(e) => checkOnlyOne(e.target)} /></span>

                <span>반려됨<input type="checkbox"
                name="cb"
                value={2}
                className="cb3"
                onClick={(e) => checkOnlyOne(e.target)} /></span>

                <span>검토중<input type="checkbox"
                name="cb"
                value={3}
                className="cb4"
                onClick={(e) => checkOnlyOne(e.target)} /></span>
            </div>
            <TableContainer
                sx={{
                    width: 900,
                    height:500,
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
                  <TableRow >
                    <TableCell  align="center" className="th_cell">번호</TableCell>
                    <TableCell className="th_cell" align="center">프로젝트명</TableCell>
                    <TableCell className="th_cell" align="center">PM</TableCell>
                    <TableCell className="th_cell" align="center">프로젝트시작일</TableCell>
                    <TableCell className="th_cell" align="center">프로젝트기한</TableCell>
                    <TableCell className="th_cell" align="center">프로젝트상태</TableCell>
                    <TableCell className="th_cell" align="center"></TableCell>
                  </TableRow>
                {/* 테이블 제목 구역 */}
                </TableHead>
                {/* 테이블 내용 구역 */}
                <TableBody>
                  {rows.someList.map((row) => (
                    <TableRow
                      key={row.name}
                    >
                      <TableCell align="center">{row.pjtNo}</TableCell>
                      <TableCell align="center">
                        <a href={'/teamProject/teammember/print?pjtNo=' + row.pjtNo}>
                            {row.pjtName}
                        </a>
                      </TableCell>
                      <TableCell align="center">{row.empName}</TableCell>
                      <TableCell align="center">{row.pjtSt}</TableCell>
                      <TableCell align="center">{row.pjtEND}</TableCell>
                      <TableCell align="center">{row.pjtSta == 1 ? '완료' : '진행중'}</TableCell>
                      <TableCell align="center">
                        <button type="button" class="searchbtn" style={{marginRight:'5px'}}>
                            <Link to={'/teamProject/update?pjtNo=' + row.pjtNo}>
                                수정
                            </Link>
                        </button>
                        <button onClick={ () => {projectDelete(row.pjtNo)} } class="searchbtn" type="button" > 삭제 </button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
            <div style = { {display: 'flex', justifyContent : 'center', margin: '10px'} }>
                {/* count = 전체 페이지수, onChange : 페이지를 클릭했을때 생기는 이벤트 */}
                <Pagination count={rows.totalPages} page={page} onChange={onPageSelect}/>
                </div>
            </div>
        </div>
        {/*!-- 결제 모달 Start --> targetUrl: axios로 보낼 url aprvType: 결제 타입 설정  successUrl :결제 성공후 이동할 url  */   }
        { isOn ? <> <ApprovalModal data={projectNum}
                                  targetUrl={"/approval/deleteAproval"}
                                  aprvType={14}
                                  successUrl={"/teamProject/listAll"}>
        </ApprovalModal></> : <> </> }
    </>)
}