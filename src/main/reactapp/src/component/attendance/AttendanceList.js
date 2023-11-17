import styles from '../../css/Table.css';
import axios from 'axios';
import {useState, useEffect} from 'react'
import Pagination from "@mui/material/Pagination";
import * as React from "react";

export default function AttendanceList(props){
    const [searchOption, setsearchOption] = useState({page:1,dptmNo:0,empRk:0})
    const optionChange = (e) =>{ setsearchOption( {...searchOption,[e.target.className]: e.target.value} )}
    const [pageDto, setPageDto] = useState({someList:[]})
    useEffect(() => {
        let date = new Date();
        setsearchOption( {...searchOption,
            periodStart: new Date(date.getFullYear(), date.getMonth(), 1).toISOString().split('T')[0] ,
            periodEnd:new Date(date.getFullYear(), date.getMonth() , date.getDate()).toISOString().split('T')[0]})
    }, []);
    const getsearch = (e) =>{
        axios
            .get("/attendance/allempAttdList",{params:searchOption})
            .then( (r) => {
                console.log( r.data )
             })
            .catch( (e) =>{
                console.log( e )
            })
    }
    useEffect(() => {
        console.log( searchOption )
        getsearch()
    }, [searchOption.periodStart,searchOption.periodEnd,searchOption.dptmNo,searchOption.empRk]);
    return (<>

        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">근태관리</span>  > <span class="lv1">전사원근무현황</span> </div>
            <div class="searchBox">
                <div>
                조회기간 : <input type="date" className="periodStart" value={searchOption.periodStart} onChange={optionChange} /> ~
                    <input type="date" value={searchOption.periodEnd} onChange={optionChange} className="periodEnd" />
                </div>
                <div>
                부서
                    <select className="dptmNo" onChange={optionChange} value={searchOption.dptmNo}>
                        <option value="0">전체</option>
                        <option value="1">인사</option>
                        <option value="2">개발</option>
                        <option value="3">영업</option>
                        <option value="4">dba</option>
                    </select>
                </div>
                <div>
                    직급
                    <select className="empRk" onChange={optionChange} value={searchOption.dptmNo}>
                        <option value="0">전체</option>
                        <option value="1">사원</option>
                        <option value="2">주임</option>
                        <option value="3">대리</option>
                        <option value="4">과장</option>
                        <option value="5">팀장</option>
                        <option value="6">부장</option>
                    </select>
                </div>
                <div>
                <select className="">
                    <option value="">선택</option>
                    <option value="">이름</option>
                    <option value="">사번</option>
                </select>
                <input type="text" className="keyword" placeholder="검색어"/>
                <button type="button" className="" onClick={getsearch}> 검색 </button>
                </div>

            </div>
            <hr class="hr01"/>
            <table className="tableTypeA">
                <tr>
                    <th className="th_w25">날짜</th>
                    <th className="th_w15">부서</th>
                    <th className="th_w15">사원명</th>
                    <th className="th_w15">직급</th>
                    <th className="th_w15">근무상태</th>
                    <th className="th_w15">근무현황</th>


                </tr>
                <tr>
                    <td colspan="6" class="dataArea">

                    <table class="tableTypeB">
                        <tr>
                            <td className="th_w25">2023-11-07</td>
                            <td className="th_w15">개발팀</td>
                            <td className="th_w15">김개발</td>
                            <td className="th_w15">대리</td>
                            <td className="th_w15">재직</td>
                            <td className="th_w15">근무</td>

                        </tr>
                        {pageDto.someList.map( (attd) =>(
                            <tr>
                                <td className="th_w25">2023-11-07</td>
                                <td className="th_w15">개발팀</td>
                                <td className="th_w15">김개발</td>
                                <td className="th_w15">대리</td>
                                <td className="th_w15">재직</td>
                                <td className="th_w15">근무</td>

                            </tr>
                        ))}

                    </table>

                </td>
                </tr>
            </table>
            {/* 페이지 버튼 */}
            <div style = {{ display : 'flex' , flexDirection : 'column' , alignItems : 'center' , margin : '10px' }} >
                <Pagination page = { searchOption.page }  count={ pageDto.totalPages }  onChange = { (e,value) => {
                    setsearchOption( { ...searchOption , page : value })
                }
                } />
            </div>
            {/*페이지 버튼*/}

        </div>
    </>)
}