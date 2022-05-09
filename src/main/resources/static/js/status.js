
export class GameStatus {
	constructor(data) {
		this.run = data.run;
		this.currentOrder = data.currentOrder;
		this.firstOrder = data.firstOrder;	
		this.lastStone = data.lastStone; 
		this.board = data.board;
	}
}


export class Room {
	constructor(data){
		this.roomNumber = data.roomNumber;
		this.roomName = data.roomName;
		this.users = data.users;
		this.startTime = data.startTime;
		this.gameStatus = null;
	}
	
}

export class MyInfo {
	constructor(userNumber, nickname) {
		this.userNumber = userNumber;
		this.nickname = nickname;
	}
	
}




export const status = {
	room : {},
	info : {},
};


export const get = {
	nickname : function(){
		return status.info.nickname;
	}
}



