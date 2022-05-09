package omok.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

import org.junit.jupiter.api.Test;

public class victoryTest {

	@Test
	public void victory() throws IOException {
		
		int[][] arr = new int[10][10];
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int count =0;
		
		boolean myturn = true;
		
		while(count != 5) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			
			if(myturn == true) {
				arr[x][y] = 1;
			} else {
				arr[x][y] = 2;
			}
			
			
			for(int i=0;i<arr.length;i++) {
				System.out.println(Arrays.toString(arr[i]));
			}
			
			가로세로검사(arr, x, y);
			대각선검사(arr, x, y);
			
		}
		
	}
	
	
	public boolean 가로세로검사(int[][] arr, int column, int row) {
		
		boolean[][] vis = new boolean[arr.length][arr.length];
		
//		0 ~ 1 가로
//		2 ~ 3 세로
		int[] vx = {0, 0, -1, 0};
		int[] vy = {1, -1, 0, 0};
		
		for(int k=0;k<2;k++) {
			
			Queue<int[]> q = new LinkedList<>();
			int count = 1;
			q.add(new int[] {column, row});
			vis[column][row] = true;
			
			while(!q.isEmpty()) {
				int[] poll = q.poll();
				
				int px = poll[0]; 
				int py = poll[1];
				
				for(int i=k*2;i<(k+1)*2;i++) {
					int x = px + vx[i];
					int y = py + vy[i];
					
					if(0 <= x && x < arr.length && 0 <= y && y < arr.length) {
						if(arr[x][y] == 1 && vis[x][y] == false) {
							q.add(new int[] {x, y});
							vis[x][y] = true;
							count++;
						}
					}
				}
			}
			
			System.out.println("가로세로 체크 :" + count);
			
			if(count == 5) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean 대각선검사(int[][] arr, int column, int row) {
		
//		0 ~ 1 : ↗
//		2 ~ 3 : ↖
		int[] vx = {1, -1, 1, -1};
		int[] vy = {-1, 1, 1, -1};
		
		
		for(int k=0;k<2;k++) {
			int count = 1;
			
			boolean[][] vis = new boolean[arr.length][arr.length];
			
			Queue<int[]> q = new LinkedList<>();
			
			q.add(new int[] {column, row});
			vis[column][row] = true;
			
			while(!q.isEmpty()) {
				int[] poll = q.poll();
				int px = poll[0];
				int py = poll[1];
				
				for(int i=k*2;i<(k+1)*2;i++) {
					int x = py + vy[i];
					int y = px + vx[i];
					
					if(0 <= x && x < arr.length && 0 <= y && y < arr.length) {
						if(arr[x][y] == 1 && vis[x][y] == false) {
							q.add(new int[] {x, y});
							vis[x][y] = true;
							count++;
						}
					}
				}
			}
			
			System.out.println("대각선 체크 :" + count);
			
			if(count == 5) {
				return true;
			}
		}
		
		return false;
	}
	
}
