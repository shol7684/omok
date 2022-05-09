
export { stomp, contextPath, main, errorMsg };
import { Board } from "./board.js";

import { roomStatus, userList,
		gameStart, gameover, init, exit,  
		putStone } from "./room.js";

import { util } from "./util.js";
import { MyInfo, Room, GameStatus, status } from "./status.js";








$(document).ready(function(){
	$(".loadingBox").remove();
})	
	
const contextPath = location.pathname.substring(0, location.pathname.indexOf("/", 2));

const socket = new SockJS(contextPath + '/socket');
const stomp = Stomp.over(socket);
// stomp.debug = null;
	



stomp.connect({}, function(){
	main();
});

// 구독을 취소하기위해 구독 시 아이디 저장
const subscribeList = [];

// 모든 구독 취소하기
function subscribeCancle() {
	const length = subscribeList.length;
	for(let i=0;i<length;i++) {
		const sid = subscribeList.pop();
		stomp.unsubscribe(sid.id);
	}
}
	
	
	
// 	방목록 가져오기
function roomList(){
	$.ajax({
		url: contextPath + "/roomList",
		type: "GET",
	})
	.done(function(result){
		let roomHtml = "";
		for(let i=0;i<result.length;i++) {
			roomHtml +=`
				<li data-room_number=${result[i].roomNumber}> 
					<div>${result[i].users[0].nickname}</div>
	                <div>${result[i].roomName}</div>
	                <div class="users">${result[i].users.length}/2</div>
                </li>`;
		}
		
		$(".roomList ul").html(roomHtml);
	})
	.fail(function(){
		alert("에러");
	})
}	
	

// 대기실
function main(){
	const run = running();
	
	if(run) return; 
	
	$(".roomList").show();
	$(".room").hide();
	
	subscribeCancle();
	
	const roomListSub = stomp.subscribe("/topic/roomList", function(){
		// "/topic/roomList"에서 메세지가 왔을때 실행할 함수
		roomList();
	});
	
	subscribeList.push(roomListSub);
	
	roomList();
}
	
	

function roomConnect(){
	const roomNumber = status.room.roomNumber;
	
	// 참가 메세지
	const enterSub = stomp.subscribe("/topic/room/enter/" + roomNumber, function(result){
		const data = JSON.parse(result.body);
		
		userList(data.users);
		
		const messageHtml = `<li>${data.message}</li>`;
		$(".content ul").append(messageHtml);
		
	});
	
	// 퇴장 메세지
	const exitSub = stomp.subscribe("/topic/room/exit/" + roomNumber, function(result){
		const data = JSON.parse(result.body);
		
		userList(data.users);
		
		const messageHtml = `<li>${data.message}</li>`;
		$(".content ul").append(messageHtml);
		
		exit();
		
	});
	
	
	// 채팅
	const chatSub = stomp.subscribe("/topic/room/chat/" + roomNumber, function(result){
		const data = result.body;
		const messageHtml = `<li>${data}</li>`;
		$(".content ul").append(messageHtml);
	});
	
	
	// 돌 놓기
	const stoneSub = stomp.subscribe("/topic/stone/" + roomNumber, function(result){
		const data = JSON.parse(result.body);
		putStone(data);
	});
	
	// 게임 초기화
	const initSub = stomp.subscribe("/topic/init/" + roomNumber, function(result){
		const data = JSON.parse(result.body);
		// settingBoard();
		Board.init();
		init(data);
	});
	
	// 레디
	const readySub = stomp.subscribe("/topic/ready/" + roomNumber, function(result){
		const data = JSON.parse(result.body);
		userList(data);
	});
	
	
	// 시간초과
	const timeoutSub = stomp.subscribe("/topic/timeout/" + roomNumber, function(result){
		const data = result.body;
		alert(data);
		gameover();
	});	
	
	
	
	subscribeList.push(enterSub);
	subscribeList.push(exitSub);
	subscribeList.push(chatSub);
	subscribeList.push(stoneSub);
	subscribeList.push(initSub);
	subscribeList.push(readySub);
	subscribeList.push(timeoutSub);
}	

	
	
function createRoom(data){
	if(!data) return;
	subscribeCancle();
	
	// setRoomStatus(data);
	
	$(".roomList").hide();
	$(".room").css("display", "flex");
	
	$(".room_info .title span").html(status.room.roomName);
	$(".content ul").html("");
	$(".chat_input textarea").val("");
	userList(status.room.users);
	
	Board.init();
	
	roomConnect();
}	
	
	

// 방 만들기
$(".create_room").click(function(){
	swal({
		text: "방 제목",
		content: "input",
		buttons: ["취소", "확인"],
		closeOnClickOutside : false 
	})
	.then(function(roomName){
		if(roomName) {
			swal({
				text: "사용하실 닉네임을 입력해주세요",
				content: "input",
				buttons: ["취소", "확인"],
				closeOnClickOutside : false 
			})
			.then(function(nickname){
				if(nickname) {
					
					const data = {
						roomName : roomName,
						nickname : nickname
					}
					
					$.ajax({
						url: contextPath + "/room",
						type: "POST",
						data: data,
					})
					.then(function(result){
						stomp.send("/roomList");
						status.info = new MyInfo(result.userNumber, nickname);
						status.room = new Room(result.room);
						
						createRoom(result.room);
					})
					.fail(function(result){
						errorMsg(result);
					})
				}
			})
		}
	})
})


// 다른사람이 만든 방 입장하기 
$(document).on("dblclick", ".roomList li", function(){
	if($(this).find(".users").text() == '2/2') {
		alert("풀방");
		return;
	}
	
	const roomNumber = $(this).data("room_number");
	
	swal({
		text: "사용하실 닉네임을 입력해주세요",
		content: "input",
		buttons: ["취소", "확인"],
		closeOnClickOutside : false 
	})
	.then(function(nickname){
		if(nickname) {
			$.ajax({
				url: contextPath + "/enterRoom",
				type: "GET",
				data: {
					roomNumber : roomNumber,
					nickname : nickname
				}
			})
			.then(function(result){
				stomp.send("/roomList");
				status.info = new MyInfo(result.userNumber, result.nickname);
				status.room = new Room(result.room);
				
				createRoom(result.room);
				
				const message = nickname + "님이 참가하셨습니다"
				stomp.send("/room/enter/" + roomNumber, {}, message);
			})
			.fail(function(result){
				errorMsg(result);
			})
		}
	})

})





// 새로고침시 참가중인방이 있었다면 방 유지
// 게임이 진행중이었다면 돌 다시 이어서 진행
function running(){
	const roomNumber = util.getCookie("roomNumber");
	const userNumber = util.getCookie("userNumber");
	const nickname = util.getCookie("nickname");
	
	if(roomNumber && userNumber && nickname) {
		console.log("방있음");
	 
		$.ajax({
			url: contextPath + "/running",
			type: "GET",
			data: {
				roomNumber : roomNumber,
				
			}
		})
		.then(function(result){
			
			// 방에 들어가 있었고 게임 시작 전  
			if(result) {
				
				status.info = new MyInfo(userNumber, nickname);
				const room = status.room = new Room(result.room);
				
				createRoom(result.room);
				
				// 게임 진행 중이었을 때
				if(room.gameStatus) {
					const gameStatus = 
						status.room.gameStatus = new GameStatus(room.gameStatus);
					
					Board.setting(gameStatus.board);
					gameStart();
				}
				
				
				
			}
		})
		.fail(function(result){
			errorMsg(result);
		})
		
		return true;
	}
	
	return false;
}



function errorMsg(result){
	if(result.status == 400) {
		alert("풀방");
		return;
	}
	
	if(result.status == 404) {
		alert("종료된 방");
		main();
		return;
	}
	 
	alert("에러");
	return;
}




