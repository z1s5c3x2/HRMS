import * as React from "react";
import {useEffect, useState} from "react";
import axios from "axios";
import Pagination from "@mui/material/Pagination";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import {Link} from "react-router-dom";
import TableContainer from "@mui/material/TableContainer";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {DesktopDatePicker} from "@mui/x-date-pickers/DesktopDatePicker";
import TextField from "@mui/material/TextField";
import ApprovalModal from "../approval/ApprovalModal";

export default function EmployeeSearch(props) {
    const rkList = ["대기", "사원", "주임", "대리", "과장", "팀장", "부장"]
    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)
    /* 모달 키고 끄기 */
    const modalController = (e)=> {
        setIsOn(!isOn)
    }
    //검색 정보
    const [searchOption, setSearchOption] = useState({searchNameOrEmpNo: 0, page: 1})
    // 페이지 정보 저장
    const [pageInfo, setPageInfo] = useState({totalCount: 0, totalPages: 0})
    //검색 조건 재설정
    const changeData = (e) => {
        setSearchOption({...searchOption, [e.target.className]: e.target.value})
    }
    //사원 리스트
    const [empList, setEmpList] = useState([])
    // 페이지 번호 바뀔때마다 설정한 검색 정보로 검색
    useEffect(() => {
        getList()
    }, [searchOption.page]);
    //검색버튼 클릭시 검색한 정보와 1페이지로 초기화 하여 다시 요청한다
    const searchEmp = (e) => {
        setSearchOption({...searchOption, page: typeof searchOption.page == 'string' ? 1 : '1'})
    }

    // 사원 리스트 불러오기
    const getList = (e) => {
        axios
            .get("/employee/findoneoption", {params: searchOption})
            .then((r) => {
                setPageInfo({...pageInfo, totalCount: r.data.totalCount, totalPages: r.data.totalPages}) // 페이지 정보 초가화
                setEmpList(r.data.someList) // 불러온 사원 목록 저장
            })
            .catch((err) => {
                console.log(err)
            })
    }
    console.log(searchOption)

    // 사원 퇴사
    const retiredEmployee = (empNo) => {
        axios
            .post("/employee/retired", {empNo: empNo})
            .then((r) => {
                console.log(r.data)
            })
            .catch((e) => {
                console.log(e)
            })
    }
    // 클릭한 사원의 상세 정보
    const [empInfo, setEmpInfo] = useState(null)
    //  클릭된 사원 상세 정보 가져오기
    const getInfo = (empNo) => {
        axios
            .get("/employee/searchempinfo", {params: {empNo: empNo}})
            .then((r) => {
                setEmpInfo(r.data)
            })
            .catch((e) => {
                console.log(e)
            })
    }
    console.log(empInfo)
    // 퇴사 사유 작성 버튼 클릭시 퇴사정보 기입하는 구역 출력
    const [textIsOn, setTextIsOn] = useState(false)
    // 퇴사 정보 객체
    const [retiredInfo, setRetiredInfo] = useState({})
    return (<>
        <div className="contentBox">
            <div className="pageinfo"><span className="lv0">인사관리</span> > <span className="lv1">사원 정보 검색</span></div>
            <div className="emp_regs_content">
                {/* 필터 , 검색 구역*/}
                <div style={{display: "flex"}}>
                    <div>
                        <select value={searchOption.searchNameOrEmpNo}
                                onChange={changeData} className={"searchNameOrEmpNo"}>
                            <option value={"0"}> 사번</option>
                            <option value={"1"}> 이름</option>
                        </select>
                        <input type={"text"} className={"searchValue"} value={searchOption.searchValue}
                               onChange={changeData}/>
                        <button type={"button"} onClick={searchEmp}> 검색</button>
                        {/* 필터 , 검색 구역*/}
                        {/*리스트 출력*/}
                        <TableContainer component={Paper} sx={{
                            width: 400,
                            height: 600,
                            'td': {
                                fontSize: 9,
                                paddingTop: '10px',
                                paddingBottom: '10px',
                                paddingLeft: '3px',
                                paddingRight: '3px'
                            }
                        }}>
                            <Table aria-label="simple table">
                                <TableHead>
                                    <TableRow>
                                        <TableCell align="center">사번</TableCell>
                                        <TableCell align="center">이름 </TableCell>
                                        <TableCell align="center">번호</TableCell>
                                        <TableCell align="center">상태</TableCell>
                                        <TableCell align="center">직급</TableCell>
                                        <TableCell align="center">부서</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {empList.map((emp) => (
                                        <TableRow onClick={(e) => {
                                            getInfo(emp.empNo)
                                        }} sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                                            <TableCell align="center">{emp.empNo}</TableCell>
                                            <TableCell align="center">{emp.empName}</TableCell>
                                            <TableCell align="center"> {emp.empPhone} </TableCell>
                                            <TableCell align="center">{emp.empSta ? '재직' : '휴직'}</TableCell>
                                            <TableCell align="center">{rkList[emp.empRk]}</TableCell>
                                            <TableCell align="center">{emp.dptmName}</TableCell>
                                        </TableRow>

                                    ))}
                                </TableBody>
                            </Table>
                            {/* 페이지네이션 */}
                            <div style={{
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                                margin: '10px'
                            }}>
                                <Pagination page={searchOption.page} count={pageInfo.totalPages}
                                            onChange={(e, value) => {
                                                setSearchOption({...searchOption, page: value})
                                            }
                                            }/>
                            </div>
                        </TableContainer>
                        {/* 페이지네이션 */}
                        {/*리스트 출력*/}
                    </div>
                    <div>
                        <h3> 사원 상세 정보</h3>
                        {empInfo != null && // 가져온 사원 정보가 존재한다면 사원 정보 출력
                            <div>
                                사번 : <input value={empInfo.empNo} disabled={true}/> <br/>
                                이름 : <input value={empInfo.empName} disabled={true}/> <br/>
                                성별: <input value={empInfo.empSex} disabled={true}/> <br/>
                                계좌 : <input value={empInfo.empAcn} disabled={true}/> <br/>
                                근무 상태 : <input value={empInfo.empSta ? '재직' : '휴직'} disabled={true}/> <br/>
                                직급 : <input value={rkList[empInfo.empRk]} disabled={true}/> <br/>
                                부서 : <input value={empInfo.dptmName} disabled={true}/> <br/>
                                진행중인 결제 수 : <input value={empInfo.aprvCount} disabled={true}/> <br/>
                                처리 필요한 결제 수 : <input value={empInfo.apLogCount} disabled={true}/> <br/>
                                <button onClick={(e) => {
                                    setRetiredInfo({...retiredInfo,empNo:empInfo.empNo})
                                    setTextIsOn(!textIsOn)
                                }}> 퇴사 정보 작성
                                </button>
                                {textIsOn && <button onClick={modalController}> 결제</button>}
                            </div>
                        }
                        {textIsOn && <div>
                            <textarea style={{width: '400px', height: '150px'}} placeholder={"퇴사 사유 작성"}
                                      value={retiredInfo.rtempCont}
                            onChange={(e)=>{
                                setRetiredInfo({...retiredInfo,rtempCont:e.target.value})
                            }}> </textarea>
                            퇴사 날짜 : <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DesktopDatePicker
                                value={retiredInfo.rtempDate}
                                sx={{width: '30%', height: "70px"}}
                                renderInput={(params) => <TextField {...params} label="날짜"/>}
                                format="YYYY-MM-DD"
                                onChange={(date) => setRetiredInfo({...retiredInfo, rtempDate: date})}
                            />
                        </LocalizationProvider>

                        </div>}
                    </div>
                </div>
            </div>
        </div>
        {/*결제 모달 구역*/}
        { isOn ? <> <ApprovalModal data={retiredInfo}
                                   aprvType={3}
                                   targetUrl={'/employee/retired'}
                                   successUrl={"/employee/list"}
                                   modalControll={modalController}>
        </ApprovalModal></> : <> </> }
    </>)
}