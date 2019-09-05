package smartspace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.rdb.RdbActionDao;
import smartspace.dao.rdb.RdbElementDao;
import smartspace.dao.rdb.RdbUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class DBTests {


	public EntityFactory fact;
	public RdbActionDao rdbActionDao;
	public RdbElementDao rdbElementDao;
	public RdbUserDao rdbUserDao;
	
	
	@Autowired
	public void setFact(EntityFactory fact) {
		this.fact = fact;
	}

	@Autowired
	public void setRdbActionDao(RdbActionDao rdbActionDao) {
		this.rdbActionDao = rdbActionDao;
	}

	@Autowired
	public void setRdbElementDao(RdbElementDao rdbElementDao) {
		this.rdbElementDao = rdbElementDao;
	}

	@Autowired
	public void setRdbUserDao(RdbUserDao rdbUserDao) {
		this.rdbUserDao = rdbUserDao;
	}
	
	
	@After
	public void teardown() {
		this.rdbActionDao.deleteAll();
		this.rdbElementDao.deleteAll();
		this.rdbUserDao.deleteAll();
	}
	
	

	@Test
	public void testFactoryActionCreation() throws Exception {
	
		IntStream.range(1, 8)
		.mapToObj(i->this.fact.createNewAction("Activity #"+i,"lab" , "play",new Date() ,"Leonidvishniakov@gmail.com","lab",new HashMap<>() ))
		.forEach(this.rdbActionDao::create);

		List<ActionEntity> actions = this.rdbActionDao.readAll();
		
		assertThat(actions)
		.hasSize(7);
		
		assertTrue(actions.get(0).getElementId().equals("Activity #1"));
		
		assertTrue(actions.get(0).getPlayerEmail().equals("Leonidvishniakov@gmail.com"));
		
		actions = this.rdbActionDao.readAll();
		
		this.rdbActionDao.deleteAll();
		actions = this.rdbActionDao.readAll();
		
		assertThat(actions.isEmpty()); 
	}
	
	@Test
	public void testFactoryElementCreation() throws Exception {
	
		smartspace.data.Location loc = new  smartspace.data.Location();
		
		
		IntStream.range(1, 8)
		.mapToObj(i ->this.fact.createNewElement("computer"+i, "computer", loc, new Date(), "Leonidvishniakov@gmail.com", "lab", false, new HashMap<>()))
		.forEach(this.rdbElementDao::create);

		List<ElementEntity> elements = this.rdbElementDao.readAll();
		
		assertThat(elements)
		.hasSize(7);
		
		assertTrue(elements.get(0).getCreatorEmail().equals("Leonidvishniakov@gmail.com"));
		
		
		this.rdbElementDao.deleteAll();
		elements = this.rdbElementDao.readAll();
		
		assertThat(elements.isEmpty()); 
		
	}
	
	@Test
	public void testFactoryUserCreation() throws Exception {
	
		
		
		IntStream.range(1, 8)
		.mapToObj(i ->this.fact.createNewUser("Leonidvishniakov"+i+"@gmail.com", "User"+i, "Avatar"+i,UserRole.PLAYER,(long) i*i))
		.forEach(this.rdbUserDao::create);

		List<UserEntity> users = this.rdbUserDao.readAll();
		
		
		assertThat(users)
		.hasSize(7);
		
			
		this.rdbUserDao.deleteAll();
		users = this.rdbUserDao.readAll();
		
		assertThat(users.isEmpty()); 
		
	}
	
	
	
	
	
	
	
}
