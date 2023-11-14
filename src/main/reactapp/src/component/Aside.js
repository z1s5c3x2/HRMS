import { Link }from "react-router-dom" //
import styles from '../css/aside.css';
import axios from 'axios';
import {useState, useEffect} from 'react'
import { useLocation } from 'react-router-dom';

export default function Aside( props ){
//useLocation을 이용해서 현재 페이지의 pathname을 가져와서 / 문자를 제거한 1번인덱스를 추출
 const location = useLocation().pathname.split('/')[1];
 console.log(location)

//location이 null이면 employee를 기본값으로 셋팅, !null이면 location값
let currentMenu = location == '' ? 'employee' : location;
 console.log(currentMenu)


    return(<>
        <aside className="maside">
        	<div className="logobox"><Link to='/'><img src="../../logo_is.png"/></Link></div>
        		{/*-- 사이드 메뉴 --*/}

                <ul className="nav">
                {/*user*/}
                {currentMenu === 'employee' && (
                <>
                    <li className="tmenu">인사관리</li>
                    <li className="smenu"><Link to='/employee/list'>사원 조회</Link></li>
                    <li><Link to='/employee/register'>사원등록</Link></li>
                    <li><Link to='/employee/details'>사원 수정</Link></li>
                    <li><Link to='/employee/searchemp'>사원 정보 조회</Link></li>
                </>
                )}

                {currentMenu === 'teamProject' && (
                <>
                   <li className="tmenu">프로젝트팀관리</li>
                   <li className="smenu"><Link to='/teamProject'>프로젝트팀등록</Link></li>
                   <li><Link to='/teamProject/listAll'>프로젝트팀 조회/삭제</Link></li>
                   <li><Link to='/'>프로젝트팀원조회/삭제</Link></li>
                   <li><Link to='/teamProject/'>프로젝트팀원수정</Link></li>
                   <li><Link to='/teamproject/teammember/write'>프로젝트팀원등록</Link></li>

                </>)}

                {currentMenu === 'attendance' && (
                <>
                   <li className="tmenu">근태관리</li>
                   <li className="smenu"><Link to='/attendance'>전사원 근태리스트</Link></li>
                   <li><Link to='/attendance/pcalendar'>나의근무현황캘린더</Link></li>
                   <li><Link to='/attendance'>나의출결캘린더</Link></li>
                   <li><Link to='/attendance'>출결리스트</Link></li>
                   <li><Link to='/attendance'>병가신청</Link></li>
                   <li><Link to='/attendance'>휴직신청</Link></li>
                   <li><Link to='/attendance'>개인연차내역</Link></li>
                   <li><Link to='/attendance'>전사원연차내역 </Link></li>
                   <li><Link to='/attendance'>개인연차신청</Link></li>
                </>)}

                {currentMenu === 'salary' && (
                <>
                   <li className="tmenu">급여관리</li>
                   <li className="smenu"><Link to='/salary'>개인급여내역</Link></li>
                   <li><Link to='/salary/list'>전사원급여내역</Link></li>
                   <li><Link to='/salary/write'>급여등록</Link></li>
                </>)}


                {currentMenu === 'approval' && (
                <>
                   <li className="tmenu">결재관리</li>
                   <li className="smenu"><Link to='/approval'>전사원결재 조회</Link></li>
                   <li><Link to='/reconsider'>개별 상신한 리스트 조회</Link></li>
                   <li><Link to='/approvalview'>개별 결재한 리스트 조회</Link></li>
                </>)}

            </ul>
            <div className="templogin"><Link to='/member/Login'> 로그인</Link></div>
            <div className="attendence" onClick="changeState()"><i class="fa-solid fa-arrow-right-to-bracket"></i> 출 근</div>
        </aside>

    </>)
}