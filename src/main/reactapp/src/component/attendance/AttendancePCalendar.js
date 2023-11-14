import styles from '../../css/calendar.css';
import axios from 'axios';
import {useState, useEffect} from 'react'

export default function AttendancePCalendar(props){
const[month, setMonth] = useState(
     new Date().getMonth()+1  // 현재 월[0~11] +1
)
console.log("month :: "+month)
const year = new Date().getFullYear(); //현재연도
const date = new Date().getDate();// 현재 날
const day = new Date().getDay();// 현재 날
console.log(year, month, date, day)

useEffect( ()=>{ calPrint() },[month])
//달력 출력
const calPrint =(e)=>{
	//1. 현재 연도와 월을 출력
	document.querySelector('.caldate').innerHTML = `${year}년 ${month}월`;
	console.log('현재 날짜의 getDate()  :  '+new Date().getDate());

	let now = new Date( year, month-1, 1); //month의 경우 +1했던 값이므로 원상태로 돌려줌. 2023년의 1일을 반환
	let prevdate = new Date(year,month-1,0).getDate();
	console.log("prevdate:::"+prevdate)
	//console.log('현재 날짜의 now  :  '+now);
 	//2. 요일과 일 출력
 	//현재의 달이 시작되는 요일
 	let sWeek = now.getDay(); // 현재 월의 시작 요일/
	console.log('요일 :(0일요일~6토요일)  :'+sWeek)
	//현재달에 해당하는 마지막 날 구하기 ( 30? 28? 31 ? 29??)
	let now2= new Date(year, month, 0); //  현재달 +1 (8월)	,  이전달의  뒤로(0)
	let eDay = now2.getDate();
    let blankDate = (sWeek+eDay >35)? 42-eDay-sWeek : 35-eDay-sWeek;
	let calendar = document.querySelector('.calDate');
	console.log('prevdate  :: '+prevdate)
	let html ='';

		for (let b = 1 ; b <= sWeek ; b++  ){ // 1일이 시작되는 요일이 되기 전까지 공란으로 만들어주기
			console.log("sWeek  :: "+sWeek);

			html +=`<div class="invalidDate"> ${prevdate-sWeek+b} </div>`;
		}
		// 일 출력.

		for(let i = 1 ; i <=eDay ;  i++){
			console.log('eDay :: '+ day);
			if(date == i) {
					html +=
					`<div class="today"> ${i} </div>`
			} else{
					html += `<div> ${i} </div>`;
			}
			//
		}
		for (let b = 1 ; b <= blankDate ; b++  ){ // 1일이 시작되는 요일이 되기 전까지 공란으로 만들어주기
			console.log("blankDate  :: "+blankDate);
			html +=`<div class="invalidDate">${b}  </div>`;
		}//
	calendar.innerHTML = html;
}

const onNext = (check)=>{
	console.log("check ::: " + check);
	if(check == -1){
		//만약에 월을 차감했는데 1보다 작아지면 12월로 변경하되 연도 1 차람
		//23년도 1월	인데 1차감되면 2022년 12월로
		if(month-1 < 1 ){ setMonth(12); year--;}
		setMonth(month--)
	} else if (check == 1) {
		if(month+1>12){setMonth(1); year++;}
		month ++;
	}

	//setMonth(month+check);


	calPrint();
}
    return(<>
        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">근태관리</span>  > <span class="lv1">나의근무현황캘린더</span> </div>
                {/*<!--근태 달력 -->*/}
                <div class="attd_cal">
                    <div class="calYM">
                        <button onClick={ ()=>onNext(-1)} type="button"> &lt; </button>
                        <span class="caldate">2023년 10월</span>
                        <button onClick={ ()=>onNext(1) } type="button"> &gt; </button>
                    </div>
                    <div class="calBox">
                        <ul class="calDay">
                            <li class="sun" >SUN</li>
                            <li >MON</li>
                            <li >TUE</li>
                            <li >WEN</li>
                            <li >TUR</li>
                            <li >FRI</li>
                            <li class="sat">SAT</li>
                        </ul>
                        <div class="calDate">
                        {/* 날짜 출력구역*/}
                        </div>

                    </div>

                {/*<-- calendar end -->*/}


            </div>
        </div>

    </>)
}