package omok.dto;

import lombok.Data;

@Data
public class GameStatus {

	private boolean run;
	private int[][] board;
	private int currentOrder; // 현재 순서
	private int firstOrder; // 첫번째 시작한 사람
	private long lastStone;
	
	public GameStatus(int order) {
		this.run = true;
		this.board = new int[20][20];
		this.currentOrder = order;
		this.firstOrder = order;
	}
}
