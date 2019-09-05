package smartspace.logic;

import java.util.Optional;

import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Component
public class Validator {
	
	public boolean isAdmin(String key, AdvancedUserDao<String> users) {
		Optional<UserEntity> user = users.readById(key);
		
		return 	   user.isPresent()
				&& user.get().getRole() == UserRole.ADMIN;
				
	}

	public boolean isManager(String key, AdvancedUserDao<String> users) {
		Optional<UserEntity> user = users.readById(key);
		
		return    user.isPresent()
				&& user.get().getRole() == UserRole.MANAGER;
	}
	
	
	public boolean isPlayer(String key, AdvancedUserDao<String> users) {
		Optional<UserEntity> user = users.readById(key);
		
		return    user.isPresent()
				&& user.get().getRole() == UserRole.PLAYER;
	}
	
}