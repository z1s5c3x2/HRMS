import React , { useState , useEffect } from 'react';
import { useParams  } from "react-router-dom";
import axios from "axios";

export default function LeaveRequestList( props ){
    const params = useParams(); //  useParams() 훅 : 경로[URL]상의 매개변수 가져올때

    return(
       <div class="contentBox">
                   <div class="pageinfo"><span class="lv0">퇴사직원</span>  > <span class="lv1">조회</span> </div>
                   <div class="list_content">
                        <Table class="tTpyeA">
                            <tr>
                                <th>No</th>
                                <th>부서명</th>
                                <th>사원명</th>
                                <th>직급</th>
                                <th>재직기간</th>
                            </tr>

                   		</Table>



                   		</div>

                   </div>
    )
}