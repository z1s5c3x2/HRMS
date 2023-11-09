import axios from 'axios';
import React, { useState ,useEffect } from 'react';
import styles from '../../css/SalaryWrite.css';
// --------- mui ---------//
import dayjs from 'dayjs';
import 'dayjs/locale/ko';
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DesktopDatePicker } from '@mui/x-date-pickers/DesktopDatePicker';
// ----------사원 테이블 ----//
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
// -----------------------//
dayjs.locale('ko');
export default function SalaryWrite(props) {
  const [selectedDate, setSelectedDate] = useState(null); // 초기 값 null로 설정
  const [amount, setAmount] = useState("");
  const [paymentType, setPaymentType] = useState("");
  const [accountNumber, setAccountNumber] = useState('');
  const [empNumber, setEmpNumber] = useState('');
   const [empName, setEmpName] = useState('');
  const [rankNumber, setRankNumber] = useState('');
  const handleSubmit = () => {
    // 서버로 보낼 데이터 준비
    const data = {
      empNo: empNumber ,
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

  {/* 사원 목록 출력 관련 */}

  function getrankLabel(empRk) {
                switch (empRk) {
                  case 1:
                    return "사원";
                  case 2:
                    return "주임";
                  case 3:
                    return "대리";
                  case 4:
                    return "과장";
                  case 5:
                    return "팀장";
                  case 6:
                    return "부장";
                  default:
                    return "직급";
                }
              }
    // 0. 컴포넌트 상태변수 관리
        let [ rows , setRows ] = useState( [ ] )
  useEffect( ()=>{ // 컴포넌트가 생성될때 1번 실행되는 axios
          axios.get('/employee/findAll').then( r =>{
                    console.log(r);
                  setRows(r.data); // 응답받은 모든 게시물을 상태변수에 저장
                  // setState : 해당 컴포넌트가 업데이트 (새로고침/재랜더링/return재실행)
               });
      } , []);

  return (
    <div>
        {/* 급여지급 관련 입력구역  */}
      <form className="boardForm">
      <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DesktopDatePicker className="valueBox"
                   size="small"
                  value={selectedDate}
                  onChange={(date) => setSelectedDate(date)}
                  renderInput={(params) => <TextField {...params} label="날짜" />}
                   format="YYYY-MM-DD"
                />
              </LocalizationProvider>

      <TextField className="valueBox customDisabled"
                disabled={true}
                size="small"
                label="이름(사번)"
                type="text"
                value={ empName != '' ?  empName +"("+ empNumber+")" : ''  }
              />
       <TextField className="valueBox customDisabled"
                  disabled={true}
                  size="small"
                  label="직급"
                  type="text"
                  value={getrankLabel(rankNumber)}
         />
         <TextField className="valueBox customDisabled"
                    disabled={true}
                    size="small"
                    label="계좌번호"
                    type="text"
                    value={accountNumber}
                  />

        <TextField className="valueBox"
         size="small"
          label="금액"
          type="number"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />

        <TextField className="valueBox"
           size="small"
          label="지급 타입"
          type="number"
          value={paymentType}
          onChange={(e) => setPaymentType(e.target.value)}
        />

        <Button className="btn" variant="contained" color="primary" onClick={handleSubmit}>
          제출
        </Button>

      </form>
      {/* 사원 목록 출력구역 */}
        <h3> 사원 목록 </h3>
                <TableContainer component={Paper}>
                         <Table sx={{ minWidth: 650 }} aria-label="simple table">
                         {/* 테이블 제목 구역 */}
                           <TableHead>
                             <TableRow>
                               <TableCell align="right">사원번호</TableCell>
                               <TableCell align="right">이름</TableCell>
                               <TableCell align="right">전화번호</TableCell>
                               <TableCell align="right">사원성별</TableCell>
                                <TableCell align="right">계좌번호</TableCell>
                                <TableCell align="right">직급</TableCell>
                             </TableRow>
                           </TableHead>
                           {/* 테이블 내용 구역 */}
                           <TableBody>
                             {rows.map((row) => (
                               <TableRow
                                 onClick={() =>{
                                 setEmpName(row.empName);
                                 setAccountNumber(row.empAcn);
                                 setEmpNumber(row.empNo);
                                 setRankNumber(row.empRk);
                                 }}
                                 key={row.name}
                                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>

                                 <TableCell  align="right">{row.empNo}</TableCell>
                                 <TableCell  align="right">{row.empName}</TableCell>
                                 <TableCell  align="right">{row.empPhone}</TableCell>
                                 <TableCell  align="right">{row.empSex}</TableCell>
                                 <TableCell  align="right">{row.empAcn}</TableCell>
                                 <TableCell  align="right">{getrankLabel(row.empRk)}</TableCell>

                               </TableRow>
                             ))}
                           </TableBody>
                         </Table>
                       </TableContainer>
    </div>
  );
}
