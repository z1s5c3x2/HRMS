import styles from '../../css/aside.css';
import axios from 'axios';

export default function Login(props){
    return(<>
        <div className="loginWrap">
            <div className="loginBox">
            <div className="logo "><img src="../../logo_is.png"/></div>
                <input class="empNo" type="text" placeholder="사번를 입력하세요"/>
                <input class="empPwd" type="text" placeholder="비밀번호를 입력하세요"/>
                <button onclick="onLogin()" className="btn btypeW100H50" type="button">로그인</button>
            </div>
        </div>

    </>)

}