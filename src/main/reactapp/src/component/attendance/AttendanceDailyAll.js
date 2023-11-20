import styles from '../../css/Table.css';
import axios from 'axios';
import {useState, useEffect} from 'react'
import * as React from "react";

export default function AttendanceList(props){
    const rkList = ["대기","사원","주임","대리","과장","팀장","부장"]
    let date = new Date();
    const [searchOption, setsearchOption] = useState({page:1,dptmNo:0,empRk:0,keywordType:0,keyword:'',
        periodStart: new Date(date.getFullYear(), date.getMonth(), 2).toISOString().split('T')[0] ,
        periodEnd:new Date(date.getFullYear(), date.getMonth() , date.getDate()).toISOString().split('T')[0]})
    const optionChange = (e) =>{ setsearchOption( {...searchOption,[e.target.className]: e.target.value} )}
    const [pageDto, setPageDto] = useState({someList:[]})

    useEffect(() => {
        if(setsearchOption.periodStart != null)
        {getsearch()}
    }, [setsearchOption.periodStart,setsearchOption.page]);
    const getsearch = (e) =>{
        console.log( searchOption )
        axios
            .get("/attendance/allempAttdList",{params:searchOption})
            .then( (r) => {
                setPageDto(r.data)
                console.log( r.data )
            })
            .catch( (e) =>{
                console.log( e )
            })
    }
    useEffect(() => {
        getsearch()
    }, []);
    function strToTime(_time){
        console.log( _time )
        let _res = _time.split(' ')[1].split(':')
        console.log( _res )
        return _res[0] + ':' + _res[1]
    }
    return (<>
        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">근태관리</span>  > <span class="lv1">전사원출결현황</span> </div>
            <div class="searchBox">
                <div>
                    조회기간 : <input type="date" className="periodStart" value={searchOption.periodStart} onChange={optionChange} /> ~
                    <input type="date" value={searchOption.periodEnd} onChange={optionChange} className="periodEnd" />
                </div>
                <div>
                부서
                    <select className="dptmNo" onChange={optionChange} value={searchOption.dptmNo}>
                        <option value="">선택</option>
                        <option value="">인사</option>
                        <option value="">개발</option>
                        <option value="">영업</option>
                        <option value="">기타</option>
                    </select>
                </div>
                <div>
                    직급
                    <select className="">
                        <option value="">선택</option>
                        <option value="">사원</option>
                        <option value="">대리</option>
                        <option value="">과장</option>
                        <option value="">차장</option>
                        <option value="">부장</option>
                    </select>
                </div>  
                <div>
                <select className="keywordType" onChange={optionChange} value={searchOption.keywordType}>
                    <option value="0">선택</option>
                    <option value="1">이름</option>
                    <option value="2">사번</option>
                </select>
                <input type="text" className="keyword" onChange={optionChange} value={searchOption.keyword} placeholder="검색어"/>
                    <button type="button" className="" onClick={getsearch}> 검색 </button>
                </div>

            </div>
            <hr class="hr01"/>
            <table className="tableTypeA">
                <tr>
                    <th className="th_w20">날짜</th>
                    <th className="th_w10">부서</th>
                    <th className="th_w15">사원명</th>
                    <th className="th_w10">직급</th>
                    <th className="th_w15">출결현황</th>
                    <th className="th_w15">출근시간</th>
                    <th className="th_w15">퇴근시간</th>


                </tr>
                <tr class="dataArea">
                    <td colspan="7" >

                    <table class="tableTypeB">
                        {/*<tr>
                            <td className="th_w20">2023-11-07</td>
                            <td className="th_w10">개발팀</td>
                            <td className="th_w15">김개발</td>
                            <td className="th_w10">대리</td>
                            <td className="th_w15">출근</td>
                            <td className="th_w15">09:00</td>
                            <td className="th_w15">18:00</td>

                        </tr>*/}
                        {pageDto.someList.map( (attd) =>(
                            <tr>
                                <td className="th_w20">{attd.attdWrst.split(' ')[0]}</td>
                                <td className="th_w10">{attd.dptmName}</td>
                                <td className="th_w15">{attd.empName}</td>
                                <td className="th_w10">{rkList[attd.empRk]}</td>
                                <td className="th_w15">{attd.attdRes}</td>
                                <td className="th_w15">{strToTime(attd.attdWrst) }</td>
                                <td className="th_w15">{strToTime(attd.attdWrend) }</td>


                            </tr>
                        ))}


                    </table>

                </td>
                </tr>
            </table>


        </div>
    </>)
}