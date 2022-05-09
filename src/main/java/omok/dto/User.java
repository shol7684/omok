package omok.dto;

import java.util.Objects;
import java.util.UUID;

import lombok.Data;

@Data
public class User {
	private String roomNumber;
	private String userNumber;
	private String nickname;
	
	private boolean ready;
	
	public User() {}

	public User(String roomNumber, String userNumber, String nickname) {
		this.roomNumber = roomNumber;
		this.userNumber = userNumber;
		this.nickname = nickname;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(userNumber, other.userNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userNumber);
	}

	
	
	
}
