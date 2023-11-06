import axios from 'axios';
import { useState , useEffect } from 'react';
import {BrowserRouter, Routes, Route, Link, useSearchParams} from 'react-router-dom';


export default function TeamProjectList(props){

    // 컴포넌트 상태변수 관리
    let [rows, setRows] = useState( [] )

    useEffect( () => {
        axios
            .get('/teamproject/getAll')
            .then(r => {
                setRows(r.data);    // 응답받은 값들을 상태변수에 저장
            })
    }, []);


    return(<>
        <div>
            <div>
            </div>
        </div>

    </>)
}