package omok;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import omok.controller.MainController;
import omok.dto.Room;


@EnableScheduling
@SpringBootApplication
public class OmokApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmokApplication.class, args);
		
//		List<String> users = new ArrayList<>();
//		
//		users.add("유저1");
//		
//		String roomNumber = UUID.randomUUID().toString();
//		Room room = Room.builder()
//				.roomNumber(roomNumber)
//				.roomName("테스트방")
//				.users(users)
//				.build();
//		
//		MainController.roomList.put(roomNumber, room);
		
	}
}
