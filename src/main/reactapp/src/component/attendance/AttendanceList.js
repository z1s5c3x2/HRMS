export default function AttendanceList(props){  
    return (<>

        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">근태관리</span>  > <span class="lv1">월별근태조회리스트</span> </div>
            <div>
                조회기간 : <input type="date" className="periodStart" /> ~  <input type="date" className="periodEnd" />
                부서별조회
                    <select className="">
                        <option value=""></option>
                        <option value=""></option>
                        <option value=""></option>
                        <option value=""></option>
                        <option value=""></option>
                    </select>
            </div>
            <table className="tableTypeA">
                <tr>
                    <th>Date</th>
                    <th>부서</th>
                    <th>사원명</th>
                    <th>직급</th>
                    <th>근태상태</th>

                </tr>

                 <tr>
                    <td>2023-11-07</td>
                    <td>개발팀</td>
                    <td>김개발</td>
                    <td>대리</td>
                    <td>출근</td>
                </tr>
                <tr>
                    <td>2023-11-07</td>
                    <td>개발팀</td>
                    <td>김개발</td>
                    <td>대리</td>
                    <td>출근</td>
                </tr>
                <tr>
                    <td>2023-11-07</td>
                    <td>개발팀</td>
                    <td>김개발</td>
                    <td>대리</td>
                    <td>출근</td>
                </tr>
            </table>


        </div>
    </>)
}