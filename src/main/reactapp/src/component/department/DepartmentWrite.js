import {useEffect} from "react";
import axios from "axios";

export default function DepartmentWrite() {
    // 최초로 부서 목록을 불러온다
    useEffect(() => {
        axios
            .get("/department/findAll")
            .then( (r) => {
                console.log( r )
            })
            .catch( (e) =>{
                console.log( e )
            })
    }, []);

    return (<>
        <div className="contentBox">
            <div className="pageinfo"><span className="lv0">인사관리</span> > <span className="lv1">부서 변경</span></div>
            <div className="emp_regs_content">

                {/*사원의 부서 변경 내역*/}


            </div>
        </div>
    </>)
}