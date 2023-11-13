import * as React from "react";
import {useState} from "react";
import axios from "axios";

export default function EmployeeSearch(props)
{
    const [searchOption, setSearchOption] = useState({searchOptionSta:0,searchOptionNameOrEmpNo:1})

    const changeData = (e)=>{
        setSearchOption({...searchOption,[e.target.className]:e.target.value}) //검색 조건
    }

    const searchEmp = (e) =>{
        axios
            .get("/employee/findoneoption", {params:searchOption})
            .then( (r) => {
                console.log( r.data )
             })
            .catch( (e) =>{
                console.log( e )
            })
    }
    console.log( searchOption )

    const [empInfo, setEmpInfo] = useState({}) // 검색된 사원 정보 저장
    console.log( "asd" )
    return(<>
        <div className="contentBox">
            <div className="pageinfo"><span className="lv0">인사관리</span> > <span className="lv1">사원 정보 검색</span></div>
            <select value={searchOption.searchOptionNameOrEmpNo}
                    onChange={changeData} className={"searchOptionNameOrEmpNo"}>
                <option value="1"> 사번 </option>
                <option value="2"> 이름 </option>
            </select>
            <select className={"searchOptionSta"}
                value = { searchOption.searchOptionSta }
                onChange={changeData}>
                <option value="0"> 상태 전체 </option>
                <option value="1"> 재직 </option>
                <option value="2"> 휴직 </option>
            </select>
            <input type={"text"} className={"searchOptionSearchValue"} value={searchOption.searchValue} onChange={changeData}/>
            <button type={"button"} onClick={searchEmp}> 검색 </button>

        </div>
    </>)
}