import axios from 'axios';
import React, { useState } from 'react';

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

export default function LeaveRequestWrite(props) {
 const [isOn, setIsOn] = useState(false);

  const modalController = () => {
    setIsOn(!isOn);
  };
  const [selectedDate1, setSelectedDate1] = useState(null); // 초기 값 null로 설정
  const [selectedDate2, setSelectedDate2] = useState(null); // 초기 값 null로 설정
  const [amount, setAmount] = useState("");
  const [paymentType, setPaymentType] = useState("");
const data = {
      lrqSt: selectedDate1,
      lrqEnd: selectedDate2,
      lrqType: Number(amount),
      lrqSrtype: Number(paymentType),
      empNo: '2311006' // 추후에 세션 구현하면 접속한 본인 사번 대입
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
  return (
  <>
    <div>
      <h3>시작날짜</h3>
      <form className="boardForm">
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DesktopDatePicker
            value={selectedDate1}
            onChange={(date1) => setSelectedDate1(date1)}
            renderInput={(params) => <TextField {...params} label="날짜" />}
             format="YYYY-MM-DD"
          />
        </LocalizationProvider>
        <h3>종료날짜</h3>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DesktopDatePicker
                    value={selectedDate2}
                    onChange={(date2) => setSelectedDate2(date2)}
                    renderInput={(params) => <TextField {...params} label="날짜" />}
                     format="YYYY-MM-DD"
                  />
                </LocalizationProvider>
        <TextField
          label="타입( 1:휴직 2:연차 3:병가 )"
          type="number"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />
        <TextField
          label="급여유무( 0: 무급 / 1: 유급 )"
          type="number"
          value={paymentType}
          onChange={(e) => setPaymentType(e.target.value)}
        />
        <Button variant="contained" color="primary" onClick={modalController}>
          제출
        </Button>
      </form>
      {isOn ? (
                <ApprovalModal
                  data={data}
                  aprvType={ data.lrqType === 1 ? 6 : data.lrqType === 2 ? 10
                                             : data.lrqType === 3 ? 8 : null }
                  targetUrl="/leaveRequest/post"
                  successUrl="/leaveRequest/write"
                  modalControll={modalController}
                />
              ) : null}

        </div>
    </>
  );
}
