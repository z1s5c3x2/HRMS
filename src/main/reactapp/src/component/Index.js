/*
    Index : 여러 컴포넌트들을 연결하는 최상위 컴포넌트
        - 가상URL 정의해서 컴포넌트를 연결하는 공간/컴포넌트
*/
import{ BrowserRouter , Routes , Route , Link   } from 'react-router-dom';
/* 라우터에 적용할 컴포넌트 호출  */
import Header from './Header';
import Main from './Main';
import Footer from './Footer';

/* LeaveRequest import */

/* Salary import */
import SalaryList from './Salary/SalaryList';
import SalaryView from './Salary/SalaryView';
import SalaryWrite from './Salary/SalaryWrite';
import App from './Salary/SalaryWrite';
export default function Index( props ){
    return(<>
        <div className="webContainer">
            <BrowserRouter >
                <Header />
                    <Routes >
                        {/* MAIN*/}
                        <Route path='/' element = { <Main />} />

                        {/* EXAMPLE */}

                        {/* MEMBER */}

                         {/* Salary */}
                         <Route path='/salary/list' element = { <SalaryList />} />
                         <Route path='/salary/view' element = { <SalaryView />} />
                         <Route path='/salary/write' element = { <SalaryWrite />} />

                    </Routes >
                <Footer />
            </BrowserRouter >
        </div>
    </>)
}