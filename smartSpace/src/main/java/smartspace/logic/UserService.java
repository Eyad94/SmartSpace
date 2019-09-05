package smartspace.logic;

import java.util.List;


import smartspace.data.UserEntity;

public interface UserService {

	public UserEntity publishNewUser (UserEntity user, String adminSmartspace,
			String adminEmail);
	public List<UserEntity> getUsers (String adminSmartspace, String adminEmail,
			int size, int page);
	public UserEntity createNewUser(UserEntity entity);
	public UserEntity validateUser(String userSmartspace, String userEmail);
	public void update(UserEntity entity, String smartspace, String email);
	
}