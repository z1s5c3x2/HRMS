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

export default function LeaveRequestList( props ){
    // 0. 컴포넌트 상태변수 관리 [ 스프링으로 부터 전달받은 객체  ]
        let [ pageDto , setPageDto ] = useState( {
            lrqDtos : [] ,
            totalPages : 0 ,
            totalCount : 0
        } ); // 스프링에서 전달해주는 DTO와 일치화

        // 0. 스프링에게 전달할 객체
            const [ pageInfo , setPageInfo ] = useState( {
                page : 1 , key : 'btitle' , keyword : '' , view : 5
            }); console.log( pageInfo );


    // 0. 컴포넌트 상태변수 관리
        let [ rows , setRows ] = useState( [ ] )


        // 1. axios를 이용한 스프링의 컨트롤과 통신
         const loadView = ( lrqNo ) => {
                window.location.href = '/leaveRequest/view?lrqNo='+lrqNo
                // 조회수 처리 할 예정
            }

         const getlrq = (e) =>{
                axios.get('/leaveRequest/getAll').then( r =>{ console.log(r)
                        setPageDto(r.data); // 응답받은 모든 게시물을 상태변수에 저장
                        // setState : 해당 컴포넌트가 업데이트 (새로고침/재랜더링/return재실행)
                     });
            }

            // 1-2 컴포넌트가 생성될 때 // + 의존성 배열 : page 변경될떄 // + 의존성 배열 : view 변경될 때
                useEffect( () => { getlrq(); }, [ pageInfo.page , pageInfo.view ] );

                // 2. 페이지 번호를 클릭했을 때.
                const onPageSelect = ( e , value )=>{
                    pageInfo.page = value; // 클릭한 페이지번호로 변경
                    setPageInfo( { ...pageInfo } ); // 새로고침 [ 상태변수의 주소 값이 바뀌면 재랜더링 ]


                }
                // 3. 검색 버튼을 눌렀을 때.
                const onSearch = ( e ) => {
                    setPageInfo({ ...pageInfo , page :1 } ); // 첫페이지 1페이지 초기화
                //getBoard();
                }
                const DeleteSearch = ( e ) => {
                     setPageInfo({ ...pageInfo,page :1 ,key : '' ,keyword : '' ,view : 5} ); // 첫페이지 1페이지 초기화
                     //getBoard();
                }
                // 날짜 출력 함수 (오늘이면 시간 , 오늘이 아니면 날짜)
                function formatDateTime(dateTime) {
                    const now = new Date();
                    const date = new Date(dateTime);

                    if (isToday(now, date)) {
                        // 오늘이면 시간만 표시
                        return date.toLocaleTimeString();
                    } else {
                        // 오늘이 아니면 날짜만 표시
                        return date.toLocaleDateString();
                    }
                }

                function isToday(now, date) {
                    // 현재 날짜와 비교 대상 날짜가 같은 날인지 확인
                    return (
                        now.getDate() === date.getDate() &&
                        now.getMonth() === date.getMonth() &&
                        now.getFullYear() === date.getFullYear()
                    );
                }

function getlrqTypeLabel(lrqType) {
              switch (lrqType) {
                case 1:
                  return "휴직";
                case 2:
                  return "연차";
                case 3:
                  return "병가";
                default:
                  return "알 수 없는 타입";
              }
            }
    function getlrqSrtypeLabel(lrqSrtype) {
                  switch (lrqSrtype) {
                    case 0:
                      return "무급";
                    case 1:
                      return "유급";
                    default:
                      return "알 수 없는 타입";
                  }
                }

    return(<>
        <h3> 연차 목록 </h3>
                <a href="/leaveRequest/write">연차신청</a>

    </>)
}
