import { Link } from 'react-router-dom';
import styles from '../css/header.css' // css파일 호출
import axios from 'axios';
import { useState , useEffect  } from 'react'
export default function Header( props ){

    /*
    // 1. 로그인 상태를 저장할 상태변수 선언
    let [ login , setLogin ] = useState( null );

    // 로그아웃
    //function 함수명(e) {}
    const logOut = (e) => {
        axios.get('/member/logout')
            .then( r => {   console.log('logOut get');
                if( r.data ){ //로그아웃을 성공했으면
                    //window.location.reload(); // 새로고침
                    // 세션 제거
                    sessionStorage.removeItem('login_token')
                    setLogin( null );
                }
            });
    }
    // - 회원정보 호출 [ 로그인 여부 확인 ]
    // --------------------------- 컴포넌트 생성될때 1번 --------------------------------//
    useEffect( ()=>{
        axios.get('').then( r => { console.log('login get');
            if( r.data != '' ){
                // 브라우저 세션/쿠키 // 브라우저 F12 -> 애플리케이션탭 -> Local storage / Session storage
                    // localstorage : 모든 브라우저 탭/창 공유 [페이지 전환 해도 유지 ], 브라우저가 꺼져도 유지 , 자동로그인 기능 , 로그인상태유지
                    //  vs
                    // sessionstorage : 페이지 전환 해도 유지 ,  탭/창 종료되면 사라짐 , 로그인 여부
                    // 세션/ 쿠키 저장 :  .setItem( key , value )
                    // 세션/ 쿠키 호출 :  .getItem( key )
                    // 세션/ 쿠키 제거 :  .removeItem( key )
                sessionStorage.setItem( 'login_token' , JSON.stringify(r.data) );
                setLogin( JSON.parse( sessionStorage.getItem('login_token') ) );
            }  // 2. 만약에 로그인이 되어 있으면
        } )
    } , [ ] )
    */
    // -------------------------------------------------------------------------------//
    return(<>
        <header>
            <h2> <Link to='/'> HRM </Link> </h2>
            <ul>
                <li> <Link to='/salary/list'>급여리스트</Link></li>


                {/* 삼항연산자     조건 ? 참 : 거짓 */}
                {
                    /*login == null
                    ?(<>
                        <li> <Link to='/'>로그인 </Link></li>
                        <li> <Link to='/'>회원가입 </Link></li>
                      </>)
                    :(<>
                        <li> 님 </li>
                        <li> <a href='/'> 내정보 </a></li>
                        <li> <div > 로그아웃 </div></li>
                      </>)
                      */
                }
            </ul>
        </header>
    </>)
}