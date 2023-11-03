import axios from 'axios';
import { useState , useEffect } from 'react';
export default function SalaryList(props){
    // 0. 컴포넌트 상태변수 관리
    let [ rows , setRows ] = useState( [ ] )
    // 1. axios를 이용한 스프링의 컨트롤과 통신

    useEffect( ()=>{ // 컴포넌트가 생성될때 1번 실행되는 axios
        axios.get('http://localhost:80/salary/getAll').then( r =>{
                setRows(r.data); // 응답받은 모든 게시물을 상태변수에 ㅈ ㅓ장
                // setState : 해당 컴포넌트가 업데이트 (새로고침/재랜더링/return재실행)
             });
    } , []);

    return(<>
        <div>
            <a href="/salary/write">글쓰기</a>
            <table>
                <tr>
                    <th>급여번호</th> <th>지급날짜</th> <th>지급금액</th>
                    <th>지급유형</th> <th>사원번호</th>
                </tr>
            {/* 게시물 내용 반복 */}
            {
                rows.map( (row)=> {
                return(<>
                    <tr>
                        <td>{ row.slryNo }</td> <td>{ row.slryDate }</td> <td>{ row.slryPay }</td>
                        <td>{ row.slryPay }</td> <td>{ row.empNo }</td>
                    </tr>
                </>)
                })
                }
            </table>
        </div>
    </>)
}