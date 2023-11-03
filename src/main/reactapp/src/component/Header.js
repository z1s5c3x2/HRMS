import styles from '../css/aside.css';
import { Link }from "react-router-dom" //

export default function Header( props ){
    return(<>
        <header>
        			<div className="topmenu">
        				<ul className="topMenuList">
        					<li className=""><Link to='/employee'>인사관리</Link></li>
        					<li className=""><Link to='/teamproject'>프로젝트팀관리</Link></li>
        					<li className=""><Link to='/attendance'>근태관리</Link></li>
        					<li className=""><Link to='/salary'>급여관리</Link></li>
        					<li className=""><Link to='/approval'>결재관리</Link></li>
        				</ul>
        			</div>

        			<div className="userbox"><span className="emp_name">김이젠</span>님 <br/> 좋은 하루되세요!{/*-- <img src="./images/user_photo.jpeg"/>--*/}</div>

        	</header>
    </>)
}