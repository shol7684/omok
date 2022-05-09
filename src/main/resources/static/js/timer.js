
export { timer }

const time = 6;


let timerId = "";
const target = $(".timer");
// 	10 70 
function start(func, start){
	target.css("color", "black");
	
	let startTime;
	if(start) {
		let s = start + (time * 1000);
		let e = +new Date();
		startTime = Math.floor((s - e)/1000);
	} else {
		startTime = time;
	}
	
	console.log(startTime);
	
	target.show();
	clearTimeout(timerId);
	
	(function timer() {
		const m = Math.floor(startTime / 60);
		const s = startTime % 60;
		
		const time = `
			<span class="timer_text">
				${String(m).padStart(2, 0) }:${String(s).padStart(2, 0)}	
			</span>`;
		target.html(time);
		
		if(startTime <= 10) {
			target.css("color", "red");
		}
		
		if(startTime <= 0 ) {
			clearTimeout(timerId);
			
			if(func instanceof Function) {
				func();
			}
			return;
		}
		
		const tid = setTimeout(function(){
			timer();
		}, 1000);
			
		timerId = tid;
		startTime--;
	})();
}



function stop(){
	clearTimeout(timerId);
	target.hide();
}



const timer = {
	start, stop
}




	
	