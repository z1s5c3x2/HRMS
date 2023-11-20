/*
    mui : 리액트 전용 라이브러리
        1. 설치
            npm install @mui/material @emotion/react @emotion/styled
            npm install @mui/material @mui/styled-engine-sc styled-components
        2. 예제
           1. 호출을 컴포넌트 상단에 import 하기
              import Button from '@mui/material/Button';
           2. 호출된 mui 컴포넌트를 사용
              <Button variant="contained">Hello world</Button>;

*/
import axios from 'axios';
import { useState , useEffect } from 'react';
// ------------------- mui table 관련 컴포넌트 import------------ //
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Pagination from "@mui/material/Pagination";
import dayjs from 'dayjs';
import 'dayjs/locale/ko';
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';

//----------------------------------------------------------------------//
dayjs.locale('ko');



export default function BoardList(props){
    // 0. 컴포넌트 상태변수 관리
    const [selectedDate1, setSelectedDate1] = useState(null); // 초기 값 null로 설정
    const [selectedDate2, setSelectedDate2] = useState(null); // 초기 값 null로 설정
    let DateSt = document.querySelector('.periodStart')
    let DateEnd = document.querySelector('.periodEnd')
     console.log(DateEnd); console.log(DateSt);
    let [ pageDto , setPageDto ] = useState( {
            someList : [] , totalPages : 0 , totalCount : 0
        } );

    // 0. 스프링에게 전달할 객체
        const [ pageInfo , setPageInfo ] = useState( {
            page : 1 , key : 'empNo' , keyword : '' , view : 5 , empRk : 0 , dptmNo : 0 , slryType : 0 , DateSt : '' , DateEnd : ''
        }); console.log( pageInfo );

    // 1. 급여목록에서 특정 레코드 클릭시 해당 레코드 상세보기
     const loadView = ( slryNo ) => {
            window.location.href = '/salary/view?slryNo='+slryNo

        }
    // 1-1.  axios를 이용한 스프링의 컨트롤과 통신
    const getSalary = (e) => {
        axios.get('/salary/getAll' , { params : pageInfo } )
              .then( r =>{  // r.data : PageDto  // r.data.boardDtos : PageDto 안에 있는 boardDtos
                   setPageDto( r.data ); // 응답받은 모든 게시물을 상태변수에 저장 // setState : 해당 컴포넌트가 업데이트(새로고침/재랜더링/return재실행)
                });
    }
    // 1-2. 컴포넌트가 생성 될떄 // + 의존성배열 : page (주소값) 변경될때 //+의존성배열 : view (주소값) 변경될때
    useEffect( ()=>{   getSalary();  } , [ pageInfo.page  , pageInfo.view , pageInfo.empRk , pageInfo.dptmNo , pageInfo.slryType , pageInfo.DateSt , pageInfo.DateEnd ] );

    // 2. 페이지 번호를 클릭했을떄.
    const onPageSelect = ( e , value )=>{  console.log( value );
        pageInfo.page = value; // 클릭한 페이지번호 로 변경
        setPageInfo( { ...pageInfo } ); // 새로고침 [ 상태변수의 주소값이 바뀌면 재랜더링 ]
    }
    // 3. 검색 버튼을 눌렀을때. // 첫페이지 1페이지 초기화.
    const onSearch = ( e ) =>{
        setPageInfo( { ...pageInfo  , page : 1 } ); // 첫페이지 1페이지 초기화.
        getSalary();
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

    function getdptmLabel(dptmNo) {
            switch (dptmNo) {
              case 1:
                return "인사";
              case 2:
                return "기획";
              case 3:
                return "개발";
              case 4:
                return "경영지원";
              case 5:
                return "마케팅";
              default:
                return "부서";
            }
          }

    return(<>
    <div className="contentBox">
        <div className="pageinfo"><span className="lv0">급여관리</span> > <span className="lv1">전체 급여내역</span></div>

        {/*<p> page : { pageInfo.page  } totalCount : { pageDto.totalCount  } </p>*/}
        <div class="searchBoxB divflex pd10_0">
            <div class="divflex w90">
                <div>
                    <input type="date" className="periodStart" onChange={ (e)=> { setPageInfo( { ...pageInfo , DateSt : e.target.value} ); } } style={{width:'120px'}}/> ~
                    <input type="date" className="periodEnd" onChange={ (e)=> { setPageInfo( { ...pageInfo , DateEnd : e.target.value} ); } } style={{width:'120px'}}/>
                </div>
                <select
                    value = { pageInfo.dptmNo }
                    onChange={ (e)=>{  setPageInfo( { ...pageInfo , dptmNo : e.target.value} );  } }
                    >
                    <option  value="0"> 전체부서 </option>
                    <option  value="1"> 인사 </option>
                    <option  value="2"> 기획 </option>
                    <option  value="3"> 개발 </option>
                    <option  value="4"> 경영지원 </option>
                    <option  value="5"> 마케팅 </option>
                </select>
                <select
                    value = { pageInfo.empRk }
                    onChange={ (e)=>{  setPageInfo( { ...pageInfo , empRk : e.target.value} );  } }
                    >
                    <option  value="0"> 전체직급 </option>
                    <option  value="1"> 사원 </option>
                    <option  value="2"> 주임 </option>
                    <option  value="3"> 대리 </option>
                    <option  value="4"> 과장 </option>
                    <option  value="5"> 팀장 </option>
                    <option  value="6"> 부장 </option>
                </select>
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
                { /* 검색 */}
                <select
                   value={ pageInfo.key}
                   onChange = {
                       (e)=>{ setPageInfo( { ...pageInfo , key : e.target.value } )  }
                       }
                   >
                   <option value="empNo"> 사번 </option>
                </select>

                <input type="text"
                   style={{width:'80px'}}
                   placeholder="사번을 입력하세요"
                   value={ pageInfo.keyword }
                   onChange = {
                       (e)=>{ setPageInfo( { ...pageInfo , keyword : e.target.value } )  }
                   }
                />
               <button type="button" class="searchbtn" onClick={ onSearch }>검색 </button>
            </div>
            { /* 삼항연산자 를 이용한 조건부 랜더링 */}
            {
                pageInfo.keyword == '' ?
                (<> </>)
                :
                (<> <button class="searchbtn po_abtr0" type="button" onClick = { (e)=> { window.location.href="/salary/list"; }  } > 검색제거 </button>  </>)
            }
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


        <hr class="hr00"/>

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
             {/* 테이블 제목 구역 */}
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
                   <TableCell style={{ width : '8%' }} align="right">번호</TableCell>
                   <TableCell style={{ width : '15%' }} align="right">지급날짜</TableCell>
                   <TableCell style={{ width : '15%' }} align="right">지급금액</TableCell>
                   <TableCell style={{ width : '9%' }} align="right">지급유형</TableCell>
                    <TableCell style={{ width : '10%' }} align="right">결재번호</TableCell>
                    <TableCell style={{ width : '10%' }} align="right">이름<br/>(사원번호)</TableCell>
                    <TableCell style={{ width : '8%' }} align="right">직급</TableCell>
                    <TableCell style={{ width : '8%' }} align="right">부서</TableCell>
                 </TableRow>
               </TableHead>
               {/* 테이블 내용 구역 */}
               <TableBody>
                 {pageDto.someList.map((row) => (
                   <TableRow
                     key={row.name}
                    >

                     <TableCell style={{ width : '5%' }} onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.slryNo}</TableCell>
                     <TableCell style={{ width : '13%' }} onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.slryDate}</TableCell>
                     <TableCell style={{ width : '13%' }} onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.slryPay}</TableCell>
                     <TableCell style={{ width : '10%' }} onClick={ ( ) => loadView( row.slryNo ) } align="right">{getSlryTypeLabel(row.slryType)}</TableCell>
                     <TableCell style={{ width : '5%' }} onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.aprvNo}</TableCell>
                     <TableCell style={{ width : '15%' }} onClick={ ( ) => loadView( row.slryNo ) } align="right">{row.empName + "(" + row.empNo + ")"}</TableCell>
                    <TableCell style={{ width : '8%' }} onClick={ ( ) => loadView( row.slryNo ) } align="right">{getrankLabel(row.empRk)}</TableCell>
                    <TableCell style={{ width : '10%' }} onClick={ ( ) => loadView( row.slryNo ) } align="right">{getdptmLabel(row.dptmNo)}</TableCell>
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
