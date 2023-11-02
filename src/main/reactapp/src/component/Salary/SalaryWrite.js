import axios from 'axios';

export default function SalaryWrite(props){

    const SalaryWrite = (e) => {
        // 1. 폼 가져오기 [ 첨부파일 ]
        let boardForm = document.querySelectorAll('.boardForm')[0];
        let boardFormData = new FormData( boardForm );

        // 2. axios 전송
        axios.post("http://localhost:80/salary/post" , boardFormData)
            .then( result =>{
                if( result.data){
                    alert('글등록 성공');
                    window.location.href= '/salary/list';
                }else{ alert('글등록 실패')}
             } );

    }

    return(<>
        <div>
            <h3> 급여 작성 </h3>
            <form className="boardForm">
                <input type="date" placeholder='지급날짜' className="slryDate" name="slryDate"/> <br/>
                <input type="text" placeholder='지급금액' name="slryPay"/> <br/>
               <input type="text" placeholder='지급유형' name="slryType"/> <br/>
               <button type="button" onClick={ SalaryWrite }>등록</button>
            </form>
        </div>
    </>)
}