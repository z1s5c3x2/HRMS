import styles from '../css/header.css';
import { Link }from "react-router-dom" //
import { useState, useEffect } from "react"
import axios from 'axios';
import Aside from "./Aside"

export default function Header( props ){

   // let [menuType, setMenuType] = useState("HR");
   // console.log("header :: "+menuType)

   // 로그인 상태를 저장할 상태변수 선언
   let [login, setLogin] = useState( {
        empName : null,
        empNo : null
   } );

   // 회원정보 호출[로그인 여부 확인]
   useEffect( () => {
       axios.get('/employee/get')
               .then(r => { console.log('login get')
                   // 2. 만약에 로그인이 되어있으면
                   if(r.data != ''){
                       // 브라우저 세션/쿠키
                           // localstorage vs sessionstorage
                           // 모든 브라우저 탭/창 공유, 브라우저가 꺼져도 유지, 자동로그인 기능
                           //                      vs
                           // 탭/창 종료되면 사라짐, 로그인 여부확인
                           // 세션/쿠키 저장 : .setItem(key,value)
                           // 세션/쿠키 호출 : .getItem(key)
                           // 세션/쿠키 제거 : .removeItem(key)
                       sessionStorage.setItem('login_token', JSON.stringify(r.data));
                       setLogin(JSON.parse(sessionStorage.getItem('login_token') ) );
                       login.empName = r.data.empName;
                       login.empNo = r.data.empNo;
                       console.log(login);
                   }
               })

   }, [] )


    return(<>
        <header>
        			<div className="topmenu">
        				<ul className="topMenuList">
        					<li className=""><a href='/employee/list'>인사관리</a></li>
        					<li className=""><a href='/teamProject/listAll'>프로젝트팀관리</a></li>
        					<li className=""><Link to='/attendance/dcalendar'>근태관리</Link></li>
        					<li className=""><Link to='/salary'>급여관리</Link></li>
        					{/*<li className=""><Link to='/leaveRequest'>연차관리</Link></li>*/}
        					<li className=""><Link to='/approval'>결재관리</Link></li>
        				</ul>
        			</div>

            <div className="userbox"><span className="emp_name"> <Link to={'/employee/details'}> {login == null ? '김이젠' : login.empName} </Link></span>님 <br/> 좋은 하루되세요!{/*-- <img src="./images/user_photo.jpeg"/>--*/}</div>

        	</header>
    </>)
}