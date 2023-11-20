import styles from '../../css/aside.css';
import {Link} from 'react-router-dom'
import axios from 'axios';

export default function Login(props){

    const onLogin = (e)=>{

        // 로그인폼 데이터 가져오기
        let loginForm = document.querySelectorAll('.loginForm')[0];
        let loginFormData = new FormData(loginForm);

        axios
            .post('/member/login', loginFormData)   // 스프링 시큐리티로 객체 전달
            .then(r => {
                console.log(r);
                if(r.data){
                    alert('로그인 성공');
                    window.location.href = '/attendance/pcalendar';
                }else{
                    alert('로그인 실패');
                }
            })

    }


    return(<>
        <div className="loginWrap">
            <div className="loginBox">
            <div className="logo "><img src="../../logo_is.png"/></div>
                <form className='loginForm'>
                    <input class="empNo" name="empNo" type="text" placeholder="사번를 입력하세요"/>
                    <input class="empPwd" name="empPwd" type="text" placeholder="비밀번호를 입력하세요"/>
                    <button onClick={onLogin} className="btn btypeW100H50" type="button">로그인</button>
                </form>
            </div>
        </div>

    </>)

}