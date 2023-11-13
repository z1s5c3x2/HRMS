import {useEffect, useState} from "react";
import axios from "axios";
import {useSearchParams} from "react-router-dom";
import * as React from "react";
import ApprovalModal from "../approval/ApprovalModal";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {DesktopDatePicker} from "@mui/x-date-pickers/DesktopDatePicker";
import TextField from "@mui/material/TextField";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";

export default function UpdateEmployee(props)
{
    /*모달 호출 선언 필요*/
    const [isOn, setIsOn] = useState(false)
    /* 변경 데이터 재 지정 */
    const [changeData, setChangeData] = useState({})

    const modalController = (e)=> {
        if(!isOn && aprvType != 0)
        {
            setChangeData( {...changeData,empNo:empInfo.empNo,infoData: aprvType==3 ? empInfo.empRk:empInfo.dtpmNo})
        }
        setIsOn(!isOn)
    }
    // 정보 리스트
    const rkList = ["대기","사원","주임","대리","과장","팀장","부장"]
    const dptList = ['인사팀','기획팀(PM)','개발팀','DBA팀']

    // url 파라미터 서치
    const [ searchParams , setSearchParams ]  = useSearchParams()
    //최초 실행시 url에 있는 유저의 정보를 불러온다
    const [empInfo, setEmpInfo] = useState({})
    // 변경할 결재 타입 설정
    const [aprvType, setAprvType] = useState(0)
    const [saveDp, setSaveDp] = useState()
    const [saveRk, setSaveRk] = useState()
    const [startDate, setStartDate] = useState()
    useEffect(() => {
        axios
            .get("/employee/findemp?empNo="+searchParams.get('empNo'))
            .then( (r) => {
                setEmpInfo(r.data)
                setSaveDp(r.data.dtpmNo.toString())
                setSaveRk(r.data.empRk.toString())
             })
            .catch( (e) =>{
                console.log( e )
            })
    }, []);
    const changeInfo = (e) =>{
        // 부서 직급 변경의 결제타입이 다름, 한번에 하나씩 수정
        if( (e.target.name == "empRk" && saveDp != empInfo.dtpmNo) || (e.target.name == "dtpmNo" && saveRk != empInfo.empRk)  )
        {
            alert("한개씩 수정 해주세요")
        }else{
            setEmpInfo( {...empInfo,[e.target.name]:e.target.value})
            // 직급 수정시 3 부서 수정시 4
            setAprvType(e.target.name == "empRk" ? 5:4)
        }
    }



    return (<>
        <div className="contentBox">
            <div className="pageinfo"><span className="lv0">인사관리</span> > <span className="lv1">부서 및 직급 변경 </span></div>
            <div className="emp_regs_content"></div>
            <div>
                사번 : <input value={empInfo.empNo} disabled={true}/> <br/>
                이름 : <input value={empInfo.empName} disabled={true}/><br/>
                번호 : <input value={empInfo.empPhone} disabled={true}/><br/>
                성별 : <input value={empInfo.empSex} disabled={true}/><br/>
                이름 : <input value={empInfo.empSta ? "재직" : "휴직"} disabled={true}/><br/>
                부서 : <select name="dtpmNo" onChange={changeInfo} value={empInfo.dtpmNo}>
                    <option value={1}>인사팀</option>
                    <option value={2}>기획팀(PM)</option>
                    <option value={3}>DBA팀</option>
                    </select><br/>
                직급 : <select name="empRk" onChange={changeInfo}  value={empInfo.empRk} >
                        <option value={6}>부장 </option>
                        <option value={5}>팀장 </option>
                        <option value={4}>과장</option>
                        <option value={3}>대리 </option>
                        <option value={2}>주임 </option>
                        <option value={1}>사원 </option>
                        <option value={0}>인턴  </option>
                    </select>
                <br/>
            </div>
            {
                aprvType != 0 ? <>
                    변경 날짜 : <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DesktopDatePicker
                        value={startDate}
                        sx={{ width: '70%'}}
                        renderInput={(params) => <TextField {...params} label="날짜" />}
                        format="YYYY-MM-DD"
                        onChange={(date)=> setChangeData({...changeData,changeDate : date})}
                    />
                </LocalizationProvider>
                </> : <></>
            }
            <button onClick={e => window.location.reload()}> 새로고침 </button>
            <button onClick={modalController}> 결제</button>

        </div>
        { isOn ? <> <ApprovalModal data={changeData}
                                   aprvType={aprvType}
                                   targetUrl={aprvType == 5 ? "/employee/changernk ": "/employee/changedptm"}
                                   successUrl={"/employee/list"}
                                   modalControll={modalController}>

        </ApprovalModal></> : <> </> }
    </>)
}