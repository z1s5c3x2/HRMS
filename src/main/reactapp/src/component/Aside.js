import { Link }from "react-router-dom" //
import styles from '../css/aside.css';
import axios from 'axios';
import {useState, useEffect} from 'react'
import { useLocation } from 'react-router-dom';

export default function Aside( props ){
//useLocation을 이용해서 현재 페이지의 pathname을 가져와서 / 문자를 제거한 1번인덱스를 추출
 const location = useLocation().pathname.split('/')[1];
 console.log(location)

//location이 널이아니면  location값을 대입, 널이면 employee대입
let currentMenu = location != null ? location : 'employee';
    console.log(currentMenu)
    return(<>
        <aside className="maside">
        	<div className="logobox"><img src="../../logo_is.png"/></div>
        		{/*-- 사이드 메뉴 --*/}


    {/*
          switch (currentMenu){
             case 'employee' :
                return(
                 <ul className="nav">
                    <li className="tmenu">인사관리</li>
                    <li><Link to='/employee/list'>사원 조회</Link></li>
                    <li><Link to='/employee/register'>사원등록</Link></li>
                    <li className="smenu">A메뉴서브3</li>
                    <li>A메뉴서브4</li>
                </ul>
               )
             case 'teamproject' :
                return(
                 <ul className="nav">
                    <li className="tmenu">프로젝트팀관리</li>
                    <li><Link to='/employee/list'>사원 조회</Link></li>
                    <li><Link to='/employee/register'>사원등록</Link></li>
                    <li className="smenu">A메뉴서브3</li>
                    <li>A메뉴서브4</li>
                </ul>
               )
             case 'attendance' :
                return(
                 <ul className="nav">
                     <li className="tmenu">근태관리</li>
                     <li><Link to='/employee/list'>사원 조회</Link></li>
                     <li><Link to='/employee/register'>사원등록</Link></li>
                     <li className="smenu">A메뉴서브3</li>
                     <li>A메뉴서브4</li>
                 </ul>
                )
             case 'salary' :
                 return(<>
                  <ul className="nav">
                     <li className="tmenu">급여관리</li>
                     <li><Link to='/employee/list'>사원 조회</Link></li>
                     <li><Link to='/employee/register'>사원등록</Link></li>
                     <li className="smenu">A메뉴서브3</li>
                     <li>A메뉴서브4</li>
                 </ul>
                </>)
             case 'leaveRequest' :
                return(
                 <ul className="nav">
                     <li className="tmenu">연차관리</li>
                     <li><Link to='/employee/list'>사원 조회</Link></li>
                     <li><Link to='/employee/register'>사원등록</Link></li>
                     <li className="smenu">A메뉴서브3</li>
                     <li>A메뉴서브4</li>
                  </ul>
                )
             case 'approval' :
                return(
                 <ul className="nav">
                    <li className="tmenu">결재관리</li>
                    <li><Link to='/employee/list'>사원 조회</Link></li>
                    <li><Link to='/employee/register'>사원등록</Link></li>
                    <li className="smenu">A메뉴서브3</li>
                    <li>A메뉴서브4</li>
                 </ul>
               )

           }

    */}

        	    <ul className="nav">
        			<li className="tmenu">인사관리</li>
					<li><Link to='/employee/list'>사원 조회</Link></li>
        			<li><Link to='/employee/register'>사원등록</Link></li>
					<li><Link to='/department/update'>사원 부서 변경</Link></li>
					<li><Link to='/employee/register'>사원 직급 변경</Link></li>
					<li><Link to='/employee/register'>사원 연차 조회</Link></li>
					<li><Link to='/employee/register'>연차 등록</Link></li>
					<li><Link to='/employee/register'>연차 수정</Link></li>
					<li><Link to='/employee/register'>사원 정보 조회</Link></li>
					<li><Link to='/employee/register'>사원 수정</Link></li>
					<li><Link to='/employee/register'>사원 퇴사</Link></li>
					<li><Link to='/employee/register'>휴직 사원 조회</Link></li>
					<li><Link to='/employee/register'>사원 휴직기간 등록</Link></li>
					<li><Link to='/employee/register'>사원 휴직기간 수정</Link></li>
        			<li className="smenu">A메뉴서브3</li>
        			<li>A메뉴서브4</li>
        		</ul>



        		<div className="templogin"><Link to='/member/Login'> 로그인</Link></div>
        		<div className="attendence" onClick="changeState()"><i class="fa-solid fa-arrow-right-to-bracket"></i> 출 근</div>
        	</aside>

    </>)
}