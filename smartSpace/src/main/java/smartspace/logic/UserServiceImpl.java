package smartspace.logic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.aop.PerformanceMonitor;
import smartspace.dao.AdvancedUserDao;

import smartspace.data.UserEntity;

@Service
public class UserServiceImpl implements UserService {

	
	private AdvancedUserDao<String> users;
	private String smartspaceName;
	private Validator validator;
	
	
	@Autowired
	public void setusers(AdvancedUserDao<String> users, Validator validator) {
		this.users = users;
		this.validator = validator;
	}

	@Value("${smartspace.name}")
	public void setSmartspaceName(String smartspaceName) {
		this.smartspaceName = smartspaceName;
	}

	public AdvancedUserDao<String> getUsers() {
		return users;
	}
	
	public String getSmartspaceName() {
		return smartspaceName;
	}
	
	@Override
	@Transactional
	@PerformanceMonitor
	public UserEntity publishNewUser(UserEntity user, String adminSmartspace, String adminEmail) {

		if(validator.isAdmin(adminSmartspace + "#" + adminEmail,users)) {
			
			if (user.getUserSmartspace().equals(smartspaceName))
				throw new RuntimeException("illegal smartspace name!");
			
			return users.create(user);
		
		}else
			throw new RuntimeException("No admin in the system");	
	}
		


	@Override
	@PerformanceMonitor
	public List<UserEntity> getUsers(String adminSmartspace, String adminEmail, int size, int page) {
		
		
		if(validator.isAdmin(adminSmartspace + "#" + adminEmail,users)) {
			return this.users.readAll(size, page);
		}	
		else throw new RuntimeException("Admin does not exist in the system!");

		}

	@Override
	@PerformanceMonitor
	public UserEntity createNewUser(UserEntity entity) {
		
		return users.create(entity);
	}

	@Override
	@PerformanceMonitor
	public UserEntity validateUser(String userSmartspace, String userEmail) {
		
		Optional<UserEntity> userEntity = users.readById(userSmartspace+"#"+userEmail);
		if (userEntity.isPresent()) 
			return userEntity.get();
		else 
			throw new RuntimeException("User doesn't exist in the system!");
	}

	@Override
	@PerformanceMonitor
	public void update(UserEntity entity, String smartspace, String email) {
		entity.setKey(smartspace + "#" + email);
		
		users.update(entity);
	}
}
