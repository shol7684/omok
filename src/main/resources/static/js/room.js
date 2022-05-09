
import { stomp, contextPath, main, errorMsg } from "./main.js";
import { timer } from "./timer.js";
import { Board } from "./board.js";
import { MyInfo, Room, GameStatus, status } from "./status.js";
import { stone } from "./stone.js";

export { roomStatus, setRoomStatus, setMyInfo, setGameStatus, userList,
		init, gameover, gameStart, exit,
		putStone, stoneWhite, stoneBlack};

const myInfo = {
	myUserNumber : '',
	myNickname : '',
}

const roomStatus = {
	roomNumber : '',
	roomName : '',
	users : [],
};

const gameStatus = {
	run : false,
	currentOrder : '',
	firstOrder : '',	
	lastStone : '',
}


function setMyInfo(data){
	myInfo.myNickname = data.nickname;
	myInfo.myUserNumber = data.userNumber;
}

function setRoomStatus(room){
	roomStatus.roomNumber = room.roomNumber;
	roomStatus.roomName = room.roomName;
	roomStatus.users = room.users;
}

function setGameStatus(data){
	gameStatus.run = data.run;
	gameStatus.currentOrder = data.currentOrder;
	gameStatus.firstOrder = data.firstOrder;
	gameStatus.lastStone = data.lastStone;
}


function resetStatus(){
	roomStatus.roomNumber = '';
	roomStatus.roomName = '';
	roomStatus.users = [];
	
	myInfo.myUserNumber = '';
	myInfo.myNickname = '';
}

function resetGameStatus(){
	gameStatus.run = false;
	gameStatus.currentOrder = '';
	gameStatus.firstOrder = '';
	gameStatus.lastStone = '';	
}



function isMaster(){
	if(status.room.users[0].userNumber == status.info.userNumber) {
		return true;
	}
	return false;
}



function userList(users){
	// roomStatus.users = users;
	console.log(users);
	status.room.users = users;
	
	const button = $(".status button").eq(0); 
	if(isMaster()) {
		button.text("시작");
	} else {
		button.text("준비");
	}
	
	
	let usersHtml = "";
	for(let i=0;i<users.length;i++) {
		let status = "";
		
		if(i == 0) {
			status = " 방장";
		}
		
		if(i != 0 && users[i].ready) {
			status = " ready";
		}
		
		usersHtml += `
			<div><span>${users[i].nickname}<span><span>${status}<span></div>
		`;
	}
	$(".room_info .users").html(usersHtml);
}





$(".exit").click(function(){
	$.ajax({
		url: contextPath + "/exit",
		type: "POST",
	})
	.done(function(result){
		if(!result) {
			const message = myInfo.myNickname + "님이 나갔습니다"
			stomp.send("/room/exit/" + roomStatus.roomNumber, {}, message);
			status.info = {};
			status.room = {};
			
			//resetStatus();
			//resetGameStatus();
			timer.stop();
		}
		
		main();
		stomp.send("/roomList");
		
	})	
	.fail(function(result){
		errorMsg(result);
	})
})


function exit(){
	if(gameStatus.run) {
		gameover();
	}
}



function sendMessage(){
	const textarea = $(".chat_input textarea");
	const message = status.info.nickname + ": " + textarea.val();
	
	stomp.send("/room/chat/" + status.room.roomNumber, {}, message);
	textarea.val("");
}



$(".chat_input textarea").keypress(function(event){
	if (event.keyCode == 13) {
		if (!event.shiftKey) {
			event.preventDefault();
			sendMessage();
		}
	}
	
})


	
// 게임 시작하기
$(".status button").eq(0).off().click(function(){
	console.log("버튼클릭");
	
	console.log(myInfo);
	console.log(roomStatus);
	console.log(gameStatus);
	
	if(status.room.gameStatus != null) {
		return;
	}
	const roomNumber = status.room.roomNumber;
	const userNumber = status.info.userNumber;
	
	const data = {
		roomNumber : roomNumber
	}
	
	if(isMaster()) {
		$.ajax({
			url: contextPath +  "/start",
			type: "POST",
			data: data
		})
		.done(function(result){
			if(result) {
				stomp.send("/game/init/" + roomNumber, {}, result.currentOrder);
			} else {
				alert("준비안됨");
			}
		})
		.fail(function(result){
			errorMsg(result);
		})
		
	} else {
		console.log("래디");
		stomp.send("/game/ready/" + roomNumber, {}, userNumber);
	}
})






const stoneWhite = "<div class='stone white'><div></div></div>";
const stoneBlack = "<div class='stone black'><div></div></div>";


let put = false;
let stoneDelete = true;

function myturn(){
	const gameStatus = status.room.gameStatus; 
	if(gameStatus == null) return;
	
	const co = gameStatus.currentOrder;
	if(status.room.users[co].userNumber  == status.info.userNumber) {
		return true;
	}
	
	return false;
}

function myStone(){
	const gameStatus = status.room.gameStatus; 
	const fo = gameStatus.firstOrder;
	
	if(status.room.users[fo].userNumber == status.info.userNumber) {
		return stone.black;
	} else {
		return stone.white;
	}
}

	
function init(data) {
	// setGameStatus(data.gameStatus);
	status.room.gameStatus = new GameStatus(data.gameStatus);
	
	let startId = "";   
    let start = 3;
	const target = $(".start");
	
	target.text("");
	target.show();
	
    const s = setInterval(function(){
	
		if(start > 0) {
			target.text(start);
		}
	
        if(start == 0) {
            target.text("start");
        }
        
        if(start < 0) {
		 	clearInterval(startId);
            target.hide();
            gameStart();
            return;
		}
        
        start--;

    }, 1000);
    startId = s;
}



function gameStart(){
	const gameStatus = status.room.gameStatus;
	timerStart(gameStatus.lastStone);
	
	$(".board td").hover(function(){
		if(!gameStatus || !myturn()) return;
		
	    const target = $(this);
	    const stone = target.html();
		
		if(stone) {
			stoneDelete = false;
			put = true;
			return;
		}
		
		stoneDelete = true;
	    target.html(myStone());
	}
	,function(){
		if(!gameStatus || !myturn()) return;
		
	    const target = $(this);
	    
	    if(!stoneDelete) {
			stoneDelete = true;
			put = false;
			return;
		}
	    
	    target.html("");
	})


	$(".board td").click(function(){
		console.log(gameStatus);
		console.log(myturn());
		console.log(!myturn());
		console.log(gameStatus == null);
		
		if(gameStatus == null || !myturn()) return;
		console.log("돌놓기");
		const target = $(this);
		if(put) {
			return;
		}
		console.log("돌놓기2");
	    put = true;
	    const x = target.parent("tr").index();
	    const y = target.index();
	    
	    let color;
	    if(myStone() == stone.black) {
			color = 1;
		} else {
			color = 2;
		}
	    
	    const data = {
	    	coordinate : [x, y],
	    	color :color,
	    	userNumber : status.info.userNumber,
	    	nickname : status.info.nickname,
		} 
	    
	    timer.stop();
	    stomp.send("/game/stone/" + status.room.roomNumber, {}, JSON.stringify(data));
	    
	})	
}





function putStone(data){
	// setGameStatus(data.gameStatus);
	status.room.gameStatus = new GameStatus(data.gameStatus);
	const s = data.stone;
	const coordinate = s.coordinate;
	
	const x = coordinate[0];
	const y = coordinate[1];
	
	const target = $(".board td").parent("tr").eq(x).find("td").eq(y);
	
	if(s.color == 1) {
		target.html(stone.black);
	} else {
		target.html(stone.white);
	}
	
	
	if(data.win) {
		alert(data.message);
		gameover();
		return;
	}
	
	
	if(s.userNumber == status.info.userNumber) {
		put = true;
	} else {
		put = false;
	}
	
	timerStart(data.gameStatus.lastStone);
	
}


function timerStart(time) {
	if(status.room.gameStatus && myturn()) {
		timer.start(function(){
			stomp.send("/game/timeout/" + status.room.roomNumber, {}, status.info.nickname);
		}, time);
	}
}



function gameover(){
	put = false;
	
	$.ajax({
		url: contextPath + "/gameover",
		type: "POST",
		data: {roomNumber : status.room.roomNumber}
	})
	.done(function(result){
		timer.stop();
		status.room.gameStatus = null;
		status.room.users = result.uesr;
		userList(result.users);
	})
	.fail(function(result){
		errorMsg(result);
	})
}







