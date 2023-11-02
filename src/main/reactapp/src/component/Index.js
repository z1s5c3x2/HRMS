/*
    Index : 여러 컴포넌트들을 연결하는 최상위 컴포넌트
        - 가상URL 정의해서 컴포넌트를 연결하는 공간/컴포넌트
*/
import{ BrowserRouter , Routes , Route , Link   } from 'react-router-dom';
/* 라우터에 적용할 컴포넌트 호출  */
import Header from './Header';
import Main from './Main';
import Footer from './Footer';

/* Member import */

/* Board import */

export default function Index( props ){
    return(<>
        <div className="webContainer">
            <BrowserRouter >
                <Header />
                    <Routes >
                        {/* MAIN*/}
                        <Route path='/' element = { <Main />} />

                        {/* EXAMPLE */}
                         <Route path='/example' element = { <ExampleList />} />
                            <Route path='/example/day01/컴포넌트1' element = { <컴포넌트1 />} />


                            <Route path='/example/day04/Axios컴포넌트' element = { <Axios컴포넌트 />} />

                        {/* MEMBER */}
                        <Route path='/login' element = { <Login />} />
                        <Route path='/signup' element = { <Signup />} />
                        <Route path='/info' element = { <Info />} />
                         {/* Board */}
                         <Route path='/board/list' element = { <BoardList />} />
                         <Route path='/board/write' element = { <BoardWrite />} />

                    </Routes >
                <Footer />
            </BrowserRouter >
        </div>
    </>)
}