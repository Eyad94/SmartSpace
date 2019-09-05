package smartspace.layout;



import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class UserBoundary {

	private UserKey key;
	private UserRole role;
	private String username;
	private String avatar;
	private Long points;

	
	public UserBoundary() {
		
	}
	
	public UserBoundary(UserEntity user) {
		
		this.key = new UserKey();
		this.key.setSmartspace(user.getUserSmartspace());
		this.key.setEmail(user.getUserEmail());
		
		this.role = user.getRole();
		this.username = user.getUsername();
		this.avatar = user.getAvatar();
		this.points = user.getPoints();
					
	}

	public UserKey getKey() {
		return key;
	}

	public void setKey(UserKey key) {
		this.key = key;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	
	public UserEntity toEntity() {
		
		UserEntity user = new UserEntity();
		if(this.getKey() != null){
			user.setUserEmail(this.getKey().getEmail());
			user.setUserSmartspace(this.getKey().getSmartspace());
		}
		
		if(this.getPoints() != null)
			user.setPoints(points);
		user.setUsername(username);
		user.setAvatar(avatar);
		user.setRole(role);
		
		return user;
		
	}


	
	
	
	
	
	
}