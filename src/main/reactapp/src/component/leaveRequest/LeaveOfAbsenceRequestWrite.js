import axios from 'axios';
import React, { useState , useEffect  } from 'react';
import styles from '../../css/leaveRequest/leaveRequestWrite.css';

// --------- mui ---------//
import dayjs from 'dayjs';
import 'dayjs/locale/ko';
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DesktopDatePicker } from '@mui/x-date-pickers/DesktopDatePicker';
import RegisterEmp from "../employee/RegisterEmp";
import ApprovalModal from "../approval/ApprovalModal";


// -----------------------//
dayjs.locale('ko');

export default function LeaveOfAbsenceRequestWrite(props) {
 const [isOn, setIsOn] = useState(false);

   // 3. 현재 로그인된 회원의 번호
       const login = JSON.parse(sessionStorage.getItem('login_token'));
       const login_empNo = login != null ? login.empNo : null;
       const login_empName = login != null ? login.empName : null;



  const modalController = () => {
    setIsOn(!isOn);
  };
  const [selectedDate1, setSelectedDate1] = useState(null); // 초기 값 null로 설정
  const [selectedDate2, setSelectedDate2] = useState(null); // 초기 값 null로 설정
  const [leaveNumber, setLeaveNumber] = useState('');
  const [paymentType, setPaymentType] = useState("");
const data = {
      lrqSt: selectedDate1,
      lrqEnd: selectedDate2,
      lrqType: 1,
      lrqSrtype: Number(paymentType),
      empNo: login_empNo // 추후에 세션 구현하면 접속한 본인 사번 대입
    };
    console.log(data);


  const handleSubmit = () => {
    // 서버로 보낼 데이터 준비

    console.log(selectedDate1);  console.log(selectedDate2);


    // Axios를 사용하여 서버로 데이터 전송
    axios.post("/leaveRequest/post", data)
      .then((response) => {
        console.log("데이터가 성공적으로 전송되었습니다.", response);
        // 성공 시 어떤 작업을 수행하거나 리다이렉트할 수 있습니다.
      })
      .catch((error) => {
        console.error("데이터 전송 중 오류가 발생했습니다.", error);
        // 오류 처리 로직을 추가할 수 있습니다.
      });





  };
    const rkList = ["대기", "사원", "주임", "대리", "과장", "팀장", "부장"];
     const dptList = ['인사팀', '기획팀(PM)', '개발팀', 'DBA팀'];
      function createData(name, calories, fat, carbs, protein) {
         return { name, calories, fat, carbs, protein };
       }
  return (<>
    <div className="contentBox">
    <div class="pageinfo"><span class="lv0">인사관리</span> > <span class="lv1">휴직 신청</span></div>


      <form className="boardForm">
      <div className="emp_regs_content">
          <div className="eregInputBox">
              <div class="input_title ">휴직 신청 날짜</div>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DesktopDatePicker
                    value={selectedDate1}
                    sx={{ width: '70%'}}
                    onChange={(date1) => setSelectedDate1(date1)}
                    renderInput={(params) => <TextField {...params} label="날짜" />}
                     format="YYYY-MM-DD"
                  />
                </LocalizationProvider>
          </div>
          <div className="eregInputBox">
                <div class="input_title ">휴직 종료 날짜</div>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                          <DesktopDatePicker
                            value={selectedDate2}
                            onChange={(date2) => setSelectedDate2(date2)}
                            sx={{ width: '70%'}}
                            renderInput={(params) => <TextField {...params} label="날짜" />}
                             format="YYYY-MM-DD"
                          />
                        </LocalizationProvider>
          </div>

        <div className="eregInputBox pmBox">
            <div class="input_title ">급여 유무</div>
            <TextField
              label="( 0: 무급 / 1: 유급 )"
              type="number"
              value={paymentType}
              onChange={(e) => setPaymentType(e.target.value)}
            />
        </div>
        <div className="eregBtnBox">
            <Button className="btn01" variant="contained" color="primary" onClick={modalController}>
              결재요청
            </Button>
        </div>
      </div>
      </form>
      {isOn ? (
                <ApprovalModal
                  data={data}
                  aprvType={ 6 }
                  targetUrl="/leaveRequest/post"
                  successUrl="/leaveRequest/write"
                  modalControll={modalController}
                />
              ) : null}
    </div>
  </>);
}
