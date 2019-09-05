package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedUserDao;
import smartspace.data.UserEntity;

@Repository
public class RdbUserDao implements AdvancedUserDao<String>{
	private UserCrud userCrud;
	private String smartspace;
	
	public RdbUserDao(UserCrud userCrud) {
		this.userCrud = userCrud;
	}
	
	@Value("${smartspace.name}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	@Override
	@Transactional
	public UserEntity create(UserEntity user) {
		if (user.getUserSmartspace() != null) //outside project user
			user.setKey(user.getUserSmartspace() + "#" + user.getUserEmail());
		else user.setKey(smartspace + "#" + user.getUserEmail()); //this project user
		
		// SQL: INSERT
		if(!this.userCrud.existsById(user.getKey())) {
			UserEntity ue = this.userCrud.save(user);
			return ue;
		}
		else{
			throw new RuntimeException("User already exists: " + user.getKey());
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<UserEntity> readById(String key) {
		// SQL: SELECT
		return this.userCrud.findById(key);
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll() {
		List<UserEntity> list = new ArrayList<>();
		// SQL: SELECT
	    this.userCrud.findAll()
				.forEach(list::add);
	    return list;
	}

	@Override
	@Transactional
	public void update(UserEntity element) {
		// SQL: UPDATE
		UserEntity found = readById(element.getKey())
				.orElseThrow(() -> new RuntimeException("Invalid user key: " + element.getKey()));
		if(element.getUserSmartspace()!=null) {
			found.setUserSmartspace(element.getUserSmartspace());	
		}
		if(element.getUserEmail()!=null) {
			found.setUserEmail(element.getUserEmail());	
		}
		if(element.getUsername()!=null) {
			found.setUsername(element.getUsername());	

		}

		if(element.getAvatar()!=null) {
			found.setAvatar(element.getAvatar());	
		}
		if(found.getPoints()!= element.getPoints()){
			found.setPoints(element.getPoints());	
		}
		if(element.getRole()!=null) {
			found.setRole(element.getRole());
		}
		this.userCrud.save(found);
	}

	@Override
	public void deleteAll() {
		// SQL: DELETE
		this.userCrud.deleteAll();
	}

	@Override
	public List<UserEntity> readAll(int size, int page) {
		return this.userCrud
				.findAll(PageRequest.of(page, size))
				.getContent();
	}

	@Override
	public List<UserEntity> readAll(int size, int page, String sortAttr) {
		return this.userCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, sortAttr))
				.getContent();
	}

}