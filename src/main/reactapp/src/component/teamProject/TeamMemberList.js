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
// --------------------------------------------
import ApprovalModal from "../approval/ApprovalModal";

export default function TeamMemberList(props){

    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)

    // 결재 모달창 여는 함수
    function onModal(e) {
        //document.querySelector('.approv_modal').style.display = 'flex';
        setIsOn(!isOn)
    }

    const [ teamMemberNum, setTeamMemberNum ] = useState( [] );

    // url param 값 가져오기
    const [ searchParams, setSearchParams ] = useSearchParams();
    const pjtNo = searchParams.get('pjtNo');

    const [ teamMember, setTeamMember ] = useState( [] );

    // 컴포넌트 불러올때 한번만 실행
    useEffect( ()=>{
        axios
            .get("/teammember/getAll",{ params: {pjtNo: pjtNo} })
            .then( (r) => {
                console.log(r.data);
                setTeamMember(r.data);
            })
    }, [] )

    // 삭제 버튼을 누르면 실행되는 함수
    const teamMemberDelete = (e)=>{
        setTeamMemberNum(e);
        console.log(teamMemberNum);
        onModal();
    }

    return(<>
        <div className="contentBox">
            <div className="pageinfo"><span className="lv0">프로젝트팀관리</span> > <span className="lv1">팀조회</span> > <span className="lv1">팀원조회</span></div>
            <div className="cont">
                <TableContainer component={Paper}>
                  <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                      <TableRow className="table_head th01">
                        <TableCell  align="center" className="th_cell">번호</TableCell>
                        <TableCell className="th_cell" align="center">사원명</TableCell>
                        <TableCell className="th_cell" align="center">투입된날짜</TableCell>
                        <TableCell className="th_cell" align="center">상태</TableCell>
                        <TableCell className="th_cell" align="center"></TableCell>
                      </TableRow>
                    {/* 테이블 제목 구역 */}
                    </TableHead>
                    {/* 테이블 내용 구역 */}
                    <TableBody>
                      {teamMember.map((row) => (
                        <TableRow
                          className="tbody_tr"
                          key={row.name}
                        >
                          <TableCell align="center">{row.tmNo}</TableCell>
                          <TableCell align="center">{row.empName}</TableCell>
                          <TableCell align="center">{row.tmSt}</TableCell>
                          <TableCell align="center">{row.aprvSta == 1 ? '승인' : row.aprvSta == 2 ? '반려' : '검토중'}</TableCell>
                          <TableCell align="center">
                            <button type="button" >
                                <Link to={'/teamproject/teammember/update?tmNo=' + row.tmNo}>
                                    수정
                                </Link>
                            </button>
                            <button onClick={ () => {teamMemberDelete(row.tmNo)} } type="button" > 삭제 </button>
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
            </div>
        </div>
        {/*!-- 결제 모달 Start --> targetUrl: axios로 보낼 url aprvType: 결제 타입 설정  successUrl :결제 성공후 이동할 url  */   }
        { isOn ? <> <ApprovalModal data={teamMemberNum}
                                  targetUrl={"/approval/deleteAproval"}
                                  aprvType={17}
                                  successUrl={"/teamProject/listAll"}>
        </ApprovalModal></> : <> </> }
    </>)
}