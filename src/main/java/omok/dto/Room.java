package omok.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Room {
	
	private String roomNumber;
	private String roomName;
	
	@Builder.Default
	private List<User> users = new ArrayList<>();
	
	private GameStatus gameStatus;
	
	
	// 방 만든시간, 게임 시작시간
	@Builder.Default
	private long startTime = System.currentTimeMillis();
}
