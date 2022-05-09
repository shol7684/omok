package omok.controller;

import java.util.Iterator;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import omok.dto.Room;

@Component
public class Sche {
	// 2시간
//	private final int CHECK_TIME = 1000 * 60 * 60 * 2; 
	private final int CHECK_TIME = 5000; 
	
//	@Scheduled(fixedDelay = 502202220)
//	public void sc() {
//		System.out.println("스케쥴");
//		
//		Iterator<String> it = MainController.roomList.keySet().iterator();
//		
//		while(it.hasNext()) {
//			String roomNumber = it.next();
//			Room room = MainController.roomList.get(roomNumber);
//			// 방만들지 2시간, 게임 안한지 2시간
//			if(room.getStartTime() + CHECK_TIME <= System.currentTimeMillis()) {
//				MainController.roomList.remove(roomNumber);
//				System.out.println("방 삭제");
//			}
//			
//		}
//	}

}
