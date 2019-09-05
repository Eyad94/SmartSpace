package smartspace;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImplementation;
import smartspace.dao.ActionDao;
import smartspace.dao.ElementDao;
import smartspace.dao.UserDao;
import smartspace.dao.memory.RamActionDao;
import smartspace.dao.memory.RamElementDao;
import smartspace.dao.memory.RamUserDao;
import smartspace.data.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class UinitTests {

	public EntityFactory fact;
	
	public EntityFactory getFact() {
		return fact;
	}
	
	@Autowired
	public void setFact(EntityFactory fact) {
		this.fact = fact;
	}
	

	
	@Test
	public void testFactoryActionElement() throws Exception {
	
		EntityFactory fact = this.fact;
		String email ="leonidvishniakov@gmail.com";
		Date now = new Date();
		System.out.println(fact);
		ActionEntity entity = fact.createNewAction("1", "labs", "play", now,email, "lab", new HashMap<>());
		
		assert entity.getElementId().equals("1");
		
		assert entity.getPlayerEmail().equals(email);
		assert entity.getCreationTimestamp().equals(now);
		assert entity.getMoreAttributes().isEmpty();
		assert entity.getActionType().equals("play");
	
	}
	@Test
	public void testFactoryEntityElement() throws Exception {
		
		EntityFactory fact = new EntityFactoryImplementation();
		String name = "computer";
		String type = "computer";
		String smartSpace = "lab";
		smartspace.data.Location loc = new  smartspace.data.Location();
		loc.setX(1.2);
		loc.setY(12.2);
		Date now = new Date();
		String email ="leonidvishniakov@gmail.com";
		boolean expired = false;
		
		ElementEntity entity = fact.createNewElement(name, type,loc, now,email,smartSpace, expired, new HashMap<String,Object>());
		
		assert entity.isExpired() == expired;
		assert entity.getCreationTimestamp().equals(now);
		assert entity.getCreatorEmail().equals(email);
		assert entity.getLocation().equals(loc);
		assert entity.getCreatorSmartspace().equals(smartSpace);
		assert entity.getName().equals(name);
		assert entity.getType().equals(type);
		
		
	}

	@Test
	public void testFactoryUserElement() throws Exception{
		
		EntityFactory fact = new EntityFactoryImplementation();
		String name = "computer";
		String email ="leonidvishniakov@gmail.com";
		String avatar = "Guts";
		long points = 10;
		UserRole role = UserRole.PLAYER;
		
		UserEntity user =  fact.createNewUser(email,name, avatar, role, points);
		
		
		
		assert user.getUsername().equals(name);
		assert user.getAvatar().equals(avatar);
		assert user.getPoints() == points;
		assert user.getRole().equals(role);
	}
	
	@Test
	public void TestElementDao()  throws Exception{
		
		EntityFactory fact = new EntityFactoryImplementation();
		String name = "computer";
		String type = "computer";
		String smartSpace = "lab";
		smartspace.data.Location loc = new  smartspace.data.Location();
		loc.setX(1.2);
		loc.setY(12.2);
		Date now = new Date();
		String email ="leonidvishniakov@gmail.com";
		boolean expired = false;
		
		ElementEntity entity = fact.createNewElement(name, type,loc, now,email,smartSpace, expired, new HashMap<String,Object>());
		
		ElementDao<String> dao = new RamElementDao<>();
		dao.create(entity);
		assert dao.readById(entity.getKey()).get().equals(entity);
		assert !dao.readById(email).isPresent();
		assert dao.readAll().size() == 1;
		dao.deleteByKey(entity.getKey());
		dao.create(entity);
		dao.delete(entity);
		
		List<ElementEntity> lis = dao.readAll();
		assert lis.isEmpty();
		System.out.println();
	}
	
	
	@Test
	public void TestActionDao()  throws Exception{
		
		EntityFactory fact = new EntityFactoryImplementation();
		String email ="leonidvishniakov@gmail.com";
		Date now = new Date();
		ActionEntity entity = fact.createNewAction("1", "labs", "play", now,email, "lab", new HashMap<>());
		
		
		ActionDao dao = new RamActionDao();
		
		dao.create(entity);
		
		
		List<ActionEntity> lis = dao.readAll();
		assert !lis.isEmpty();
		dao.deleteAll();
		assert dao.readAll().isEmpty();
	
			
	}
	
	@Test
	public void TestUserDao()  throws Exception{
		
		EntityFactory fact = new EntityFactoryImplementation();
		String name = "computer";
		String email ="leonidvishniakov@gmail.com";
		String avatar = "Guts";
		long points = 10;
		UserRole role = UserRole.PLAYER;
		
		UserEntity user =  fact.createNewUser(email, name, avatar, role, points);
		
		UserDao<String> dao = new RamUserDao<>();  
		
		dao.create(user);
		
		UserEntity user2 =  fact.createNewUser(email,  name, avatar, role, points);
		user2.setRole(UserRole.MANAGER);
		
		dao.update(user2);
		
		assert dao.readById(user2.getKey()).get().getRole().equals(UserRole.MANAGER);
		assert !dao.readById("ss").isPresent();
		System.out.println(dao.readById(user2.getKey()));
		
		
	}
	
	



}
