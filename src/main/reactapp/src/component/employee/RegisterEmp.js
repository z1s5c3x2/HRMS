
import styles from '../../css/registerEmp.css';
export default function RegisterEmp( props ){

function onModal(e){
	document.querySelector('.approv_modal').style.display = 'flex';
}


    return(<>
        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">인사관리</span>  > <span class="lv1">사원등록</span> </div>
            <div class="emp_regs_content">

            				<div class="eregInputBox">
            					<div class="input_title ls23"> 사원명</div>
            					<div class="input_box"><input type="text" class="" name="empName" /></div>
            				</div>
            				<div  class="eregInputBox">
            					<div class="input_title ws55"> 성 별</div>
            					<div class="input_box">
            						<input type="radio" name="empSex" value="M" checked /> 남
            						<input type="radio" name="empSex" value="M" checked /> 여
            					</div>
            				</div>
            				<div  class="eregInputBox">
            					<div class="input_title ">사 원  연 락 처</div>
            					<div class="input_box"><input type="text" class="" name="empPhone" /></div>
            				</div>
            				<div  class="eregInputBox">
            					<div class="input_title">사원 비밀번호</div>
            					<div class="input_box"><input type="text" class="" name="empPwd" /></div>
            				</div>
            				<div  class="eregInputBox">
            					<div class="input_title"> 사원 계좌번호</div>
            					<div class="input_box"><input type="text" class="" name="empAcn" /></div>
            				</div>
            				<div  class="eregInputBox">
            					<div class="input_title ls3"> 근 무 상 태</div>
            					<div class="input_box">
            						<input type="radio" name="empSta" value="0" checked /> 재 직
            						<input type="radio" name="empSta" value="1" checked /> 휴 직
            					</div>
            				</div>
            				<div  class="eregInputBox">
            					<div class="input_title ws55">부 서</div>
            					<select name="">
            						<option value="">부서를 선택하세요</option>
            						<option value="">뚱뚱</option>
            						<option value="">삥삥</option>
            						<option value="">꽁꽁</option>
            					</select>
            				</div>
            				<div  class="eregInputBox">
            					<div class="input_title ws55">직 급</div>
            					<select name="">
            						<option value="">직급을 선택하세요</option>
            						<option value="">대리</option>
            						<option value="">과장</option>
            						<option value="">기타</option>
            					</select>
            				</div>
            				<div class="eregBtnBox"><button onClick={onModal} class="btn01" type="button">사 원 정 보 저 장</button></div>



            		</div>

            </div>
            {/*!-- 결제 모달 Start -->*/}
                                		<div class="approv_modal">

                                				<form class="Approv_form">
                                				<div class="modal">
                                				{/*!-- 1 -->*/}
                                					<div class="section">
                                						<div class="amodalTitle">결제요청내용</div>
                                						<textarea class="aprv_cont"></textarea>

                                					</div>
                                				{/*!-- 2 -->*/}
                                					<div class="section">
                                						<div class="amodalTitle">전체사원리스트</div>
                                							<div class="aprvList">
                                								<div class="aprvListHeader">
                                									<span class="apDept">부서</span>
                                									<span class="apLv">직급</span>
                                									<span class="apDeptEmp">이름</span>
                                								</div>
                                								{/*!-- 사원목록 구역 Start -->*/}
                                								<div class="aprvListContentBox">
                                								{/*!-- 반복 Start -->*/}
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                									<div class="aprvListContent">
                                										<span class="apDept">인사</span>
                                										<span class="apLv">부장</span>
                                										<span class="apDeptEmp">김아무개</span>
                                									</div>
                                								{/*<!-- 반복구간 e -->*/}
                                								</div>
                                							</div>
                                						</div>
                                					{/*!-- 3-->*/}
                                						<div class="section">
                                							<div class="amodalTitle">결제요청리스트</div>
                                								<div class="aprvList reqlist">
                                									<div class="aprvListHeader">
                                										<span class="apDept">부서</span>
                                										<span class="apLv">직급</span>
                                										<span class="apDeptEmp">이름</span>
                                									</div>
                                									{/*<!-- 사원목록  -->*/}
                                									<div class="aprvListContentBox">
                                									    {/*<!-- 선택사원 표시 구역 -->*/}
                                										<div class="aprvListContent">
                                											<span class="apDept">인사</span>
                                											<span class="apLv">부장</span>
                                											<span class="apDeptEmp">김아무개</span>
                                										</div>
                                										<div class="aprvListContent">
                                											<span class="apDept">인사</span>
                                											<span class="apLv">부장</span>
                                											<span class="apDeptEmp">김아무개</span>
                                										</div>
                                										<div class="aprvListContent">
                                											<span class="apDept">인사</span>
                                											<span class="apLv">부장</span>
                                											<span class="apDeptEmp">김아무개</span>
                                										</div>

                                									    {/*<!-- 선택사원 표시 구역 e -->*/}
                                									</div>
                                								</div>
                                								<div class="aprvBtnBox">
                                									<button onClick=""  class="btn02" type="button">취 소</button>
                                									<button onclick="" class="btn02"type="button">결제요청하기</button>
                                								</div>
                                							</div>


                                				</div>
                                				</form>

                                		</div>
        </>)
    }