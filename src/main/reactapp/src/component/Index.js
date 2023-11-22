/*
    Index : 여러 컴포넌트들을 연결하는 최상위 컴포넌트
        - 가상URL 정의해서 컴포넌트를 연결하는 공간/컴포넌트
*/
import{ BrowserRouter , Routes , Route , Link   } from 'react-router-dom';
/* 라우터에 적용할 컴포넌트 호출  */
import Header from './Header';
import Aside from './Aside';
import Main from './Main';
import styles from '../css/Index.css';

/* Approval import */
import ApprovalList from "./approval/ApprovalList";
import ReconsiderView from "./approval/ReconsiderView";
import ApprovalView from "./approval/ApprovalView";

/* Attendance import */
import AttendanceList from "./attendance/AttendanceList";
import AttendancePCalendar from "./attendance/AttendancePCalendar";
import AttendanceDailyCal from "./attendance/AttendanceDailyCal";
import AttendanceDailyAll from "./attendance/AttendanceDailyAll";

/* Employee import */
import RegisterEmp from "./employee/RegisterEmp";
import EmployeeList from "./employee/EmployeeList";

/* Salary import */
import SalaryMain from "./salary/SalaryMain";
import SalaryWrite from "./salary/SalaryWrite";
import SalaryView from "./salary/SalaryView";
import SalaryList from "./salary/SalaryList";

/* LeaveRequest */
import LeaveRequestMain from "./leaveRequest/LeaveRequestMain";
import LeaveRequestWrite from "./leaveRequest/LeaveRequestWrite";
import LeaveRequestView from "./leaveRequest/LeaveRequestView";
import LeaveRequestList from "./leaveRequest/LeaveRequestList";
import SickLeaveRequestWrite from "./leaveRequest/SickLeaveRequestWrite";
import LeaveOfAbsenceRequestWrite from "./leaveRequest/LeaveOfAbsenceRequestWrite";

/* Teamproject import */
import TeamProjectList from "./teamProject/TeamProjectList";
import TeamProjectMain from "./teamProject/TeamProjectMain";
import TeamMemberWrite from "./teamProject/TeamMemberWrite";
import TeamMemberList from "./teamProject/TeamMemberList";
import TeamMemberUpdate from "./teamProject/TeamMemberUpdate";
import TeamProjectUpdate from "./teamProject/TeamProjectUpdate";


/* Login import */
import Login from "./member/Login";
import EmployeeUpdate from "./employee/EmployeeUpdate";
import EmployeeDetails from "./employee/EmployeeDetails";
import EmployeeSearch from "./employee/EmployeeSearch";
import LeaveRequestUpdate from "./leaveRequest/LeaveRequestUpdate";

/* error */
import AccessDenied from "./error/AccessDenied";

export default function Index( props ){
    return(<>
        <div class="wrapContainer scrollbox" >

            <div class="wrap">
                <BrowserRouter >
                    <Aside/>
                       <Header />
                        <div className="content">
                        <Routes>

                            {/* MAIN*/}
                            <Route path='/' element = { <Login />} />
                            {/* Approval*/}
                                <Route path='/approval' element = { <ApprovalList/> } />
                                <Route path='/approval/reconsiderview' element = { <ReconsiderView/> } />
                                <Route path='/approval/approvalview' element = { <ApprovalView/> } />

                            {/* Attendance*/}
                                <Route path='/attendance' element={<AttendanceList/>} />
                                <Route path='/attendance/pcalendar' element={<AttendancePCalendar/>} />
                                <Route path='/attendance/dcalendar' element={<AttendanceDailyCal/>} />
                                <Route path='/attendance/dall' element={<AttendanceDailyAll/>} />
                            {/* Employee*/}
                                <Route path='/employee/list' element={<EmployeeList/>} />
                                <Route path='/employee/register' element={<RegisterEmp/>} />
                                <Route path='/employee/update' element={<EmployeeUpdate/>} />
                                <Route path='/employee/details' element={<EmployeeDetails/>} />
                                <Route path='/employee/searchemp' element={<EmployeeSearch/>} />
                                <Route path='/employee/retemplist' element={<EmployeeSearch/>} />

                            {/* Salary*/}
                                <Route path='/salary' element={<SalaryMain/>} />
                                <Route path='/salary/write' element={<SalaryWrite/>} />
                                <Route path='/salary/view' element={<SalaryView/>} />
                                <Route path='/salary/list' element={<SalaryList/>} />

                            { /* LeaveRequest 근태안에 종속 */ }
                                <Route path='attendance/leaveRequestMe' element={<LeaveRequestMain/>} />
                                <Route path='/attendance/leaveRequestwrite' element={<LeaveRequestWrite/>} />
                                <Route path='/attendance/leaveRequestview' element={<LeaveRequestView/>} />
                                <Route path='/attendance/leaveRequestlist' element={<LeaveRequestList/>} />
                                {/* 연차 수정 */}
                                <Route path='/attendance/leaveRequestupdate' element={<LeaveRequestUpdate/>} />
                                {/* 병가신청 */}
                                <Route path='/attendance/sickleaveRequestwrite' element={<SickLeaveRequestWrite/>} />
                                {/* 휴직신청 */}
                                <Route path='/employee/leaveofabsenceRequestwrite' element={<LeaveOfAbsenceRequestWrite/>} />

                            {/* TeamProject */}
                                <Route path='/teamProject' element={<TeamProjectMain />} />
                                <Route path='/teamProject/listAll' element={<TeamProjectList />} />
                                <Route path='/teamProject/update' element={<TeamProjectUpdate />} />
                                <Route path='/teamProject/teammember/write' element={<TeamMemberWrite />} />
                                <Route path='/teamProject/teammember/print' element={<TeamMemberList />} />
                                <Route path='/teamProject/teammember/update' element={<TeamMemberUpdate />} />

                            {/* Login */}
                                <Route path='/member/login' element={<Login/>} />

                            {/* Error */}
                                <Route path='/accessdenied' element={<AccessDenied/>} />
                        </Routes >
                        </div>
                </BrowserRouter >
            </div>
        </div>
    </>)
}
/*

    <Route path='//' element = { } />

    # 기본코드
    export default function AttendanceList(props){
        return (<>

            <div class="contentBox">
                <div class="pageinfo"><span class="lv0">근태관리</span>  > <span class="lv1">근태캘린더</span> </div>
            </div>
        </>)
    }

*/