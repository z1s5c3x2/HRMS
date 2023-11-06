import React , { useState , useEffect } from 'react';
import { useParams  } from "react-router-dom";
import axios from "axios";

export default function SalaryMain( props ){
    const params = useParams(); //  useParams() 훅 : 경로[URL]상의 매개변수 가져올때

    return(
        <div>
           <h3>급여메인</h3>
           <a href="/salary/write">급여작성</a>
           <a href="/salary/view">급여상세보기</a>
           <a href="/salary/list">급여목록</a>
        </div>
    )
}