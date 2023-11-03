import React , { useState , useEffect } from 'react';
import { useParams  } from "react-router-dom";
import axios from "axios";

export default function SalaryView( props ){
    const params = useParams(); //  useParams() 훅 : 경로[URL]상의 매개변수 가져올때

    const [ board , setBoard ] = useState( { } ); // *게시물 메모리


    useEffect( // 1. 서버로 부터 해당 게시물번호의 게시물정보 -> useState[board]  요청
        ()=>axios.get( "/salary/get" , { params : { slryNo: params.slryNo } } )
        .then( res => { setBoard( res.data)} )
    , [] )

    /*
    const [ login , setLogin ] = useState( { } ); // *로그인정보 메모리
    useEffect( // 2. 서버로 부터 해당 로그인된 회원의 아이디 [ MemberService : getloginMno() return 반환 [ 1. null[공백] or 2.[아이디] ]
        ()=>axios.get("/member/getloginMno").then( res => { setLogin( res.data ); } )
    ,[] )
    */

    return(
        <div>

        </div>
    )
}
/*
    [JS] innerHTML 사용방법
        0. <태그명 class="클래스명"></태그명>
        1. document.querySelector('.클래스명').innerHTML = 데이터
    [JSX] innerHTML 사용방법
        1. <태그명  dangerouslySetInnerHTML = {{ __html : 데이터 }} > </태그명>

    조건부 렌더링
        1. 조건 && HTML
        2. 조건 ? 참HTML : 거짓HTML
            [중첩]3. 조건1 ? 참1HTML : 조건2 ? 참2HTML : 조건3 ? 참3HTML : 거짓HTML
*/