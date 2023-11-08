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
// -----------------------//
dayjs.locale('ko');
export default function SalaryWrite(props) {
  const [selectedDate, setSelectedDate] = useState(null); // 초기 값 null로 설정
  const [amount, setAmount] = useState("");
  const [paymentType, setPaymentType] = useState("");

  const handleSubmit = () => {
    // 서버로 보낼 데이터 준비
    const data = {
      slryDate: selectedDate,
      slryPay: Number(amount),
      slryType: Number(paymentType),
    };
    console.log(data);

    // Axios를 사용하여 서버로 데이터 전송
    axios.post("/salary/post", data)
      .then((response) => {
        console.log("데이터가 성공적으로 전송되었습니다.", response);
        // 성공 시 어떤 작업을 수행하거나 리다이렉트할 수 있습니다.
      })
      .catch((error) => {
        console.error("데이터 전송 중 오류가 발생했습니다.", error);
        // 오류 처리 로직을 추가할 수 있습니다.
      });
  };

  return (
    <div>
        {/* 급여지급 관련 입력구역  */}
      <form className="boardForm">
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DesktopDatePicker
            value={selectedDate}
            onChange={(date) => setSelectedDate(date)}
            renderInput={(params) => <TextField {...params} label="날짜" />}
             format="YYYY-MM-DD"
          />
        </LocalizationProvider>
        <TextField
          label="금액"
          type="number"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />
        <TextField
          label="지급 타입( 1:기본급/2:정기상여/3:특별상여/4:성과금/5:명절휴가비/6:퇴직금)"
          type="number"
          value={paymentType}
          onChange={(e) => setPaymentType(e.target.value)}
        />
        <Button variant="contained" color="primary" onClick={handleSubmit}>
          제출
        </Button>
      </form>
      {/* 사원 목록 출력구역 */}







    </div>
  );
}
