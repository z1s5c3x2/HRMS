import React , { useState , useEffect } from 'react';
import { useParams  } from "react-router-dom";
import axios from "axios";

export default function SalaryView( props ){
    const params = useParams(); //  useParams() 훅 : 경로[URL]상의 매개변수 가져올때



    return(
        <div>
            <h3>급여 상세보기</h3>
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