package omok.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import omok.dto.GameStatus;
import omok.dto.Room;
import omok.dto.Stone;
import omok.dto.User;
import omok.util.Util;

@Controller
public class MessageController {
	
	// 방목록 업데이트
	@MessageMapping("/roomList")
	@SendTo("/topic/roomList")
	public String roomList() {
		return "";
	}
	
	
	// 다른사람 방 들어가기
	@MessageMapping("/room/enter/{roomNumber}")
	@SendTo("/topic/room/enter/{roomNumber}")
	public Map<String, Object> room(@DestinationVariable String roomNumber, String message) {
		Room room = MainController.roomList.get(roomNumber);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("users", room.getUsers());
		map.put("message", message);
		
		return map;
	}
	
	
	// 방 나가기
	@MessageMapping("/room/exit/{roomNumber}")
	@SendTo("/topic/room/exit/{roomNumber}")
	public Map<String, Object> exit(@DestinationVariable String roomNumber, String message) {
		Room room = MainController.roomList.get(roomNumber);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("users", room.getUsers());
		map.put("message", message);
		
		return map;
	}
	
	
	
	@MessageMapping("/room/chat/{roomNumber}")
	@SendTo("/topic/room/chat/{roomNumber}")
	public String chat(@DestinationVariable String roomNumber, String message) {
		return message;
	}
	
	
	
	// 돌 놓기
	@MessageMapping("/game/stone/{roomNumber}")
	@SendTo("/topic/stone/{roomNumber}")
	public Map<String, Object> stone(@DestinationVariable String roomNumber, Stone stone) {
		
		Room room = MainController.roomList.get(roomNumber);
		GameStatus gameStatus = room.getGameStatus();
		
		int[][] board = gameStatus.getBoard();
		int[] coordinate = stone.getCoordinate();
		int x = coordinate[0];
		int y = coordinate[1];
		
		board[x][y] = stone.getColor();
		
		// 순서 바꾸기
		if(gameStatus.getCurrentOrder() == 1) {
			gameStatus.setCurrentOrder(0);
		} else {
			gameStatus.setCurrentOrder(1);
		}
		
		// 돌 놓은 시간 저장
		// 새로고침 시 타이머 초기화 방지
		gameStatus.setLastStone(System.currentTimeMillis());
		
		boolean isWin = Util.isWin(board, x, y, stone.getColor());
		
		Map<String, Object> map = new HashMap<>();
		map.put("stone", stone);
		map.put("gameStatus", gameStatus);
		
		if(isWin) {
			map.put("win", true);
			map.put("message", stone.getNickname() + "님 승리");
		} else {
			map.put("win", false);
		}
		
		return map;
	}
	
	
	@MessageMapping("/game/init/{roomNumber}")
	@SendTo("/topic/init/{roomNumber}")
	public Room init(@DestinationVariable String roomNumber) {
		Room room = MainController.roomList.get(roomNumber);
		return room;
	}
	
	
	@MessageMapping("/game/ready/{roomNumber}")
	@SendTo("/topic/ready/{roomNumber}")
	public List<User> ready(@DestinationVariable String roomNumber, String userNumber) {
		System.out.println(userNumber);
		Room room = MainController.roomList.get(roomNumber);
		
		System.out.println(room);
		List<User> users = room.getUsers();
		
		if(users.size() == 2) {
			User user = users.get(1);
			
			if(user.isReady()) {
				user.setReady(false);
			} else {
				user.setReady(true);
			}
			return users;
		}
		
		return null;
	}
	
	@MessageMapping("/game/timeout/{roomNumber}")
	@SendTo("/topic/timeout/{roomNumber}")
	public String timeout(@DestinationVariable String roomNumber, String nickname) {
		return nickname + "님 시간초과";
	}
	
	
	
	

}
