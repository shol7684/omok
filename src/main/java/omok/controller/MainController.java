package omok.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.RepaintManager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import omok.dto.GameStatus;
import omok.dto.Room;
import omok.dto.User;
import omok.util.Util;

@Controller
public class MainController {
	
//	public static List roomList = new ArrayList<>();
	public static Map<String, Room> roomList = new HashMap<>();
	
	@GetMapping("/")
	public String main() {
		return "main";
	}

	// 방 목록 가져오기
	@GetMapping("/roomList")
	ResponseEntity<?> board(){
		return new ResponseEntity<>(new ArrayList<>(roomList.values()), HttpStatus.OK);
	}
	
	// 방 만들기
	@PostMapping("/room")
	ResponseEntity<?> createRoom(String roomName, String nickname, HttpServletResponse response){
		
		String roomNumber = UUID.randomUUID().toString();
		String userNumber = UUID.randomUUID().toString();
		
		User user = Util.createUser(roomNumber, userNumber, nickname);
		
		List<User> users = new ArrayList<>();
		users.add(user);
		
		Room room = Room.builder()
				.roomNumber(roomNumber)
				.roomName(roomName)
				.users(users)
				.build();
		
		roomList.put(roomNumber, room);
		
		Map<String, Object> map = new HashMap<>();
		map.put("room", room);
		map.put("userNumber", userNumber);
		map.put("nickname", nickname);
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	}	
	
	
	// 진행 중이던 게임방 가져오기
	@GetMapping("/running")
	public ResponseEntity<?> getRoom(String roomNumber, String userNumber, String nickname) {
		Room room = roomList.get(roomNumber);
		
		if(room == null) {
			Util.deleteCookie();
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		} 
		
		Map<String, Object> map = new HashMap<>();
		map.put("room", room);
		map.put("userNumber", userNumber);
		map.put("nickname", nickname);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	
	
	
	// 방 나가기
	@PostMapping("/exit")
	public ResponseEntity<?> exit() {
		User user = Util.myInfo();
		String roomNumber = user.getRoomNumber();
		Room room = roomList.get(roomNumber);
		
		Util.deleteCookie();
		
		List<User> users = room.getUsers();
		
		if(users.size() <= 1) {
			roomList.remove(roomNumber);
			return new ResponseEntity<>(true, HttpStatus.OK);
			
		} else {
			int index = users.indexOf(user);
			users.remove(index);
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
	
		
	}	
	
	
	// 다른 방 들어가기
	@GetMapping("/enterRoom")
	public ResponseEntity<?> enterRoom(String roomNumber, String nickname, HttpServletResponse response) {
		Room room = roomList.get(roomNumber);
		
		if(room == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if(room.getUsers().size() == 2) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		String userNumber = UUID.randomUUID().toString();
		
		User user = Util.createUser(roomNumber, userNumber, nickname);
		
		room.getUsers().add(user);
		
		Map<String, Object> map = new HashMap<>();
		map.put("room", room);
		map.put("userNumber", userNumber);
		map.put("nickname", nickname);
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	}	
	
	
	@PostMapping("/start")
	public ResponseEntity<?> start(String roomNumber) {
		Room room = roomList.get(roomNumber);
		
		if(room == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		List<User> users = room.getUsers();
		
		// 사람이 2명일때만 시작
		if(users.size() == 2) {
			
			// 준비 됬는지 확인
			if(users.get(1).isReady()) {
				
				// 첫번째 순서 정하기
				int order = (int)(Math.random()*2);
				GameStatus gameStatus = new GameStatus(order);
				room.setGameStatus(gameStatus);
				room.setStartTime(System.currentTimeMillis());
				return new ResponseEntity<>(true, HttpStatus.OK);
			}
		}
		
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
	
	
	@PostMapping("/gameover")
	public ResponseEntity<?> gameover(String roomNumber) {
		Room room = roomList.get(roomNumber); 
		room.setGameStatus(null);
		
		List<User> users = room.getUsers();
		
		for(int i=0;i<users.size();i++) {
			users.get(i).setReady(false);
		}
		
		return new ResponseEntity<>(room, HttpStatus.OK);
	}
	
	
	
	
	
	
}
