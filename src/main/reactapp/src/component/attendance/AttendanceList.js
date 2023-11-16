import styles from '../../css/Table.css';
import axios from 'axios';
import {useState, useEffect} from 'react'

export default function AttendanceList(props){
    const [searchOption, setsearchOption] = useState({})
    const optionChange = (e) =>{ setsearchOption( {...searchOption,[e.target.className]: e.target.value} )}
    let date = new Date();
    useEffect(() => {
        setsearchOption( {...searchOption,periodStart: new Date(date.getFullYear(), date.getMonth(), 1).toISOString().split('T')[0] ,
            periodEnd:new Date(date.getFullYear(), date.getMonth() + 1, 0).toISOString().split('T')[0]})
    }, []);
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
                    <select className="">
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
                <select className="">
                    <option value="">선택</option>
                    <option value="">이름</option>
                    <option value="">사번</option>
                </select>
                <input type="text" className="keyword" placeholder="검색어"/>
                <button type="button" className=""> 검색 </button>
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
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>휴직</td>
                            <td>출근</td>
                        </tr>
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>병가</td>
                            <td>출근</td>
                        </tr>
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>병가</td>
                            <td>출근</td>
                        </tr>
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>병가</td>
                            <td>출근</td>
                        </tr>
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>병가</td>
                            <td>출근</td>
                        </tr>
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>휴직</td>
                            <td>출근</td>
                        </tr>
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>병가</td>
                            <td>출근</td>
                        </tr>
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>병가</td>
                            <td>출근</td>
                        </tr>
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>병가</td>
                            <td>출근</td>
                        </tr>
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>병가</td>
                            <td>출근</td>
                        </tr>
                        <tr>
                            <td>2023-11-07</td>
                            <td>개발팀</td>
                            <td>김개발</td>
                            <td>대리</td>
                            <td>병가</td>
                            <td>출근</td>
                        </tr>


                    </table>

                </td>
                </tr>
            </table>


        </div>
    </>)
}