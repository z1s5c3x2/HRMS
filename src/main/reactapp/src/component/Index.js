/*
    Index : 여러 컴포넌트들을 연결하는 최상위 컴포넌트
        - 가상URL 정의해서 컴포넌트를 연결하는 공간/컴포넌트
*/
import{ BrowserRouter , Routes , Route , Link   } from 'react-router-dom';
/* 라우터에 적용할 컴포넌트 호출  */
import Header from './Header';
import Aside from './Aside';
import Main from './Main';

/* Approval import */
import ApprovalList from "./approval/ApprovalList";

/* Attendance import */
import AttendanceList from "./attendance/AttendanceList";

/* Employee import */
import RegisterEmp from "./employee/RegisterEmp";

/* Salary import */
//import RegisterEmp from "./employee/RegisterEmp";

/* Teamproject import */
//import RegisterEmp from "./employee/RegisterEmp";

export default function Index( props ){
    return(<>
        <div class="wrapContainer scrollbox" >

            <div class="wrap">
                <BrowserRouter >
                    <Aside/>
                       <Header />
                        <Routes >
                            {/* MAIN*/}
                            <Route path='/' element = { <Main />} />
                            {/* Approval*/}
                                <Route path='/approval' element = { ApprovalList } />
                            {/* Attendance*/}
                                <Route path='/attendance' element={<AttendanceList/>} />
                            {/* Employee*/}
                                <Route path='/employee' element={<RegisterEmp/>} />

                            {/* Salary*/}


                            {/* TeamProject */}


                        </Routes >

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