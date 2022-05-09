package omok.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import omok.controller.MainController;
import omok.dto.User;

public class Util {
	
	private static int cookieMaxage = 60 * 60 * 10;
	
	public static User createUser(String roomNumber, String userNumber, String nickname) {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletResponse response = attr.getResponse();
		
		Cookie cookie1 = new Cookie("roomNumber", roomNumber);
		Cookie cookie2 = new Cookie("userNumber", userNumber);
		Cookie cookie3 = new Cookie("nickname", nickname);
		
		cookie1.setMaxAge(cookieMaxage);
		cookie2.setMaxAge(cookieMaxage);
		cookie3.setMaxAge(cookieMaxage);
		
		response.addCookie(cookie1);
		response.addCookie(cookie2);
		response.addCookie(cookie3);
		
		User user = new User(roomNumber, userNumber, nickname);
		
		return user;
	}
	

	public static User myInfo() {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		Cookie[] cookies = request.getCookies();
		
		String roomNumber = "";
		String userNumber = "";
		String nickname = "";
		
		
		if(cookies != null) {
			for(int i=0;i<cookies.length;i++) {
				if(cookies[i].getName().equals("roomNumber")) {
					roomNumber = cookies[i].getValue();
				}
				if(cookies[i].getName().equals("userNumber")) {
					userNumber = cookies[i].getValue();
				}
				if(cookies[i].getName().equals("nickname")) {
					nickname = cookies[i].getValue();
				}
			}
		}
		
		
		if(!"".equals(roomNumber) && !"".equals(userNumber) && !"".equals(nickname)) {
			User user = new User();
			user.setRoomNumber(roomNumber);
			user.setUserNumber(userNumber);
			user.setNickname(nickname);
			
			return user;
		}
		
		return null;
		
	}
	
	
	
	public static void deleteCookie() {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletResponse response = attr.getResponse();
		
		Cookie cookie1 = new Cookie("roomNumber", null);
		Cookie cookie2 = new Cookie("userNumber", null);
		Cookie cookie3 = new Cookie("nickname", null);
		
		cookie1.setMaxAge(0);
		cookie2.setMaxAge(0);
		cookie3.setMaxAge(0);
		
		response.addCookie(cookie1);
		response.addCookie(cookie2);
		response.addCookie(cookie3);
	}
	
	
	
	public static boolean isWin(int[][] board, int x, int y, int color) {
		if(가로세로검사(board, x, y, color)) {
			return true;
		}
		
		if(대각선검사(board, x, y, color)) {
			return true;
		}
		
		return false;
	}
	
	private static boolean 가로세로검사(int[][] arr, int column, int row, int color) {
		for(int i=0;i<arr.length;i++) {
			System.out.println(Arrays.toString(arr[i]));
		}
		
		System.out.println("색깔 : " + color);
		
//		0 ~ 1 가로
//		2 ~ 3 세로
		int[] vx = {0, 0, -1, 1};
		int[] vy = {1, -1, 0, 0};
		
		for(int k=0;k<2;k++) {
			boolean[][] vis = new boolean[arr.length][arr.length];
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
						if(arr[x][y] == color && vis[x][y] == false) {
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
	
	
	private static boolean 대각선검사(int[][] arr, int column, int row, int color) {
		
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
					int x = px + vx[i];
					int y = py + vy[i];
					
					if(0 <= x && x < arr.length && 0 <= y && y < arr.length) {
						if(arr[x][y] == color && vis[x][y] == false) {
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
