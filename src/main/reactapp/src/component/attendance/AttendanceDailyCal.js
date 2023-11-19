import styles from '../../css/calendar.css';
import axios from 'axios';
import {useState, useEffect} from 'react'

export default function AttendancePCalendar(props){
const now = new Date();
const [calOption, setCalOption] = useState({
     month: now.getMonth()+1,  // 현재 월[0~11] +1
     year: now.getFullYear() //현재연도

})
console.log("month :: "+calOption.month)
console.log("year :: "+calOption.year)
const date = now.getDate();// 현재 날
const day = now.getDay();// 현재 날
console.log(calOption.year, calOption.month, date, day)

useEffect( ()=>{ getDataList() },[calOption.month, calOption.year])
//달력 출력
const calPrint =(e)=>{

	//1. 현재 연도와 월을 출력
	document.querySelector('.caldate').innerHTML = `${calOption.year}년 ${calOption.month}월`;
	console.log('현재 날짜의 getDate()  :  '+new Date().getDate());

	let now = new Date( calOption.year, calOption.month-1, 1); //month의 경우 +1했던 값이므로 원상태로 돌려줌. 2023년의 1일을 반환
	let prevdate = new Date(calOption.year,calOption.month-1,0).getDate();
	console.log("prevdate:::"+prevdate)
	//console.log('현재 날짜의 now  :  '+now);
 	//2. 요일과 일 출력
 	//현재의 달이 시작되는 요일
 	let sWeek = now.getDay(); // 현재 월의 시작 요일/
	console.log('요일 :(0일요일~6토요일)  :'+sWeek)
	//현재달에 해당하는 마지막 날 구하기 ( 30? 28? 31 ? 29??)
	let now2= new Date(calOption.year, calOption.month, 0); //  현재달 +1 (8월)	,  이전달의  뒤로(0)
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
			let checkDef = checkDateNow(i)
			if(date == i) {
					html +=
					`<div class="today"> ${i} <span> ${checkDef}</span> </div>`
			} else{
					html += `<div> ${i}  <span> ${checkDef}</span> </div>`;
			}
			//
		}
		for (let b = 1 ; b <= blankDate ; b++  ){ // 1일이 시작되는 요일이 되기 전까지 공란으로 만들어주기
			console.log("blankDate  :: "+blankDate);
			html +=`<div class="invalidDate">${b}  </div>`;
		}//
	calendar.innerHTML = html;
}
	const [monthDate, setMonthDate] = useState([])
	function getDataList(){
		console.log( calOption )
		axios
			.get("/attendance/getMonthChart",{params: {...calOption}})
			.then( (r) => {
				console.log( "나ㅏㅏㅏㅏㅏㅏㅏㅏㅏ" )
				console.log( r.data )
				setMonthDate(r.data)
			})
			.catch( (e) =>{
				console.log( e )
			})
	}

	useEffect(() => {
		calPrint()
	}, [monthDate]);

	function checkDateNow( _idx)
	{
		let idx = _idx < 10 ? '0'+_idx : _idx.toString()
		for(let i=0;i<monthDate.length;i++){
			if(monthDate[i].attdWrst.split(' ')[0].split('-')[2] == idx){
				return monthDate[i].attdRes
			}

		}
		return ''

	}
const onNext = (check)=>{
	console.log("check ::: " + check);
	if(check == - 1){
		//만약에 월을 차감했는데 1보다 작아지면 12월로 변경하되 연도 1차람
		//23년도 1월	인데 1차감되면 2022년 12월로
		if(calOption.month-1 < 1 ){ setCalOption( { month:12, year: calOption.year-- })}
		else{ setCalOption({ month : calOption.month-1, year:calOption.year }) }
	} else if (check == 1) {
    	if(calOption.month+1>12){setCalOption( { month:1, year: calOption.year++ })}
		else{ setCalOption({ month : calOption.month+1,year:calOption.year }) }
	}

}

    return(<>
        <div class="contentBox">
            <div class="pageinfo"><span class="lv0">근태관리</span>  > <span class="lv1">나의출결캘린더</span> </div>
                {/*<!--근태 달력 -->*/}
                <div class="attd_cal">
                    <div class="calYM">
                        <button onClick={ ()=>onNext(-1)} type="button"> &lt; </button>
                        <span class="caldate">2023년 11월</span>
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