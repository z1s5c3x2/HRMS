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

/* Employee import */
import RegisterEmp from "./employee/RegisterEmp";

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

/* Teamproject import */
import TeamProjectList from "./teamProject/TeamProjectList";
import TeamProjectMain from "./teamProject/TeamProjectMain";
import TeamMemberWrite from "./teamProject/TeamMemberWrite";
import EmployeeList from "./employee/EmployeeList";

/* Login import */
import Login from "./member/Login";

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
                            <Route path='/' element = { <Main />} />
                            {/* Approval*/}
                                <Route path='/approval' element = { <ApprovalList/> } />
                                <Route path='/reconsider' element = { <ReconsiderView/> } />
                                <Route path='/approvalview' element = { <ApprovalView/> } />
                            {/* Attendance*/}
                                <Route path='/attendance' element={<AttendanceList/>} />
                            {/* Employee*/}
                                <Route path='/employee/list' element={<EmployeeList/>} />
                                <Route path='/employee/register' element={<RegisterEmp/>} />


                            {/* Salary*/}
                                <Route path='/salary' element={<SalaryMain/>} />
                                <Route path='/salary/write' element={<SalaryWrite/>} />
                                <Route path='/salary/view' element={<SalaryView/>} />
                                <Route path='/salary/list' element={<SalaryList/>} />
                            {/* LeaveRequest*/}
                                <Route path='/leaveRequest' element={<LeaveRequestMain/>} />
                                <Route path='/leaveRequest/write' element={<LeaveRequestWrite/>} />
                                <Route path='/leaveRequest/view' element={<LeaveRequestView/>} />
                                <Route path='/leaveRequest/list' element={<LeaveRequestList/>} />
                            {/* TeamProject */}
                                <Route path='/teamproject' element={<TeamProjectMain />} />
                                <Route path='/teamproject/listAll' element={<TeamProjectList />} />
                                <Route path='/teamproject/teammember/write' element={<TeamMemberWrite />} />
                             {/* Login */}
                                <Route path='/member/login' element={<Login/>} />
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