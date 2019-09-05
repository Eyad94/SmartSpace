package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//import org.springframework.stereotype.Repository;

import smartspace.dao.UserDao;
import smartspace.data.UserEntity;

//@Repository
public class RamUserDao<UserKey> implements UserDao<UserKey>{
	
	private Map<String, UserEntity> users;
	
	public RamUserDao() {
		users = Collections.synchronizedMap(new HashMap<>());
	}

	@Override
	public UserEntity create(UserEntity userEntity) {
		users.put(userEntity.getKey(), userEntity);
		return userEntity;
	}

	@Override
	public Optional<UserEntity> readById(Object userKey) {
		Optional<UserEntity> op = Optional.empty();
		if (users.containsKey(userKey)) {
			op = Optional.of(users.get(userKey));
		}
		return op;
	}

	@Override
	public List<UserEntity> readAll() {	
		return new ArrayList<UserEntity>(users.values());
	}

	@Override
	public void update(UserEntity userEntity) {
		UserEntity found = readById(userEntity.getKey())
				.orElseThrow
				(()->new RuntimeException("invalid user key " + userEntity.getKey()));
		found.setAvatar(userEntity.getAvatar());
		found.setPoints(userEntity.getPoints());
		found.setRole(userEntity.getRole());
		found.setUsername(userEntity.getUsername());
		found.setUserSmartspace(userEntity.getUserSmartspace());
	}

	@Override
	public void deleteAll() {
		users.clear();	
	}

}
