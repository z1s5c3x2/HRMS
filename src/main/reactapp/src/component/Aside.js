import { Link }from "react-router-dom" //
import styles from '../css/aside.css';
import axios from 'axios';
import {useState, useEffect} from 'react'

export default function Aside( props ){

    return(<>
        <aside className="maside">
        		<div className="logobox"><img src="logo_is.png"/></div>
        		{/*-- 사이드 메뉴 --*/}

        		<ul className="nav">
        			<li className="tmenu">인사관리</li>
					<li><Link to='/employee/list'>사원 조회</Link></li>
        			<li><Link to='/employee/register'>사원등록</Link></li>
        			<li className="smenu">A메뉴서브3</li>
        			<li>A메뉴서브4</li>
        		</ul>
        		<div className="attendence" onClick="changeState()"><i class="fa-solid fa-arrow-right-to-bracket"></i> 출 근</div>
        	</aside>

    </>)
}