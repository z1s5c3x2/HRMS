import React , { useState , useEffect } from 'react';
import { useParams  } from "react-router-dom";
import axios from "axios";

export default function LeaveRequestList( props ){
    const params = useParams(); //  useParams() 훅 : 경로[URL]상의 매개변수 가져올때

    return(
        <div>
           <h3>연차목록</h3>
        </div>
    )
}