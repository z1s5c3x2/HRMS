import axios from 'axios';

export default function BoardWrite(props){

    const boardWrite = (e) => {
        // 1. 폼 가져오기 [ 첨부파일 ]
        let boardForm = document.querySelectorAll('.boardForm')[0];
        let boardFormData = new FormData( boardForm );

        // 2. axios 전송
        axios.post("/board" , boardFormData)
            .then( result =>{
                if( result.data){
                    alert('글등록 성공');
                    window.location.href= '/board/list';
                }else{ alert('글등록 실패')}
             } );

    }

    return(<>
        <div>
            <h3> 게시물 쓰기 </h3>
            <form class="boardForm">
                <input type="text" placeholder='제목' name="btitle"/> <br/>
                <textarea type="text" placeholder='내용' name="bcontent"></textarea> <br/>
                <button type="button" onClick={ boardWrite }>등록</button>
            </form>
        </div>
    </>)
}