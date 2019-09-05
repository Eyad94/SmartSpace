package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.AdvancedActionDao;
import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactoryImplementation;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class BoundaryClassesTests {
	
	private AdvancedElementDao<String> elements;
	private AdvancedActionDao actions;
	private AdvancedUserDao<String> users;

	public AdvancedActionDao getActions() {
		return actions;
	}
	
	@Autowired
	public void setActions(AdvancedActionDao actions) {
		this.actions = actions;
	}

	public AdvancedUserDao<String> getUsers() {
		return users;
	}
	
	@Autowired
	public void setUsers(AdvancedUserDao<String> users) {
		this.users = users;
	}

	public AdvancedElementDao<String> getElements() {
		return elements;
	}
	
	@Autowired
	public void setElements(AdvancedElementDao<String> elements) {
		this.elements = elements;
	}
	
	@After
	public void teardown() {
		this.elements.deleteAll();
	}
	
	@Test
	public void testCreateElementBoudaryHasAll() throws Exception{
		
		//GIVEN there is one ElementEntity in the database
		ElementEntity element = new  EntityFactoryImplementation().createNewElement(
				"MYNAME",
				"Computer",
				new Location(0,0),
				new Date(),
				"hi@mail.net",
				"home",
				false,
				new HashMap<String, Object>());
		element = elements.create(element);
		
		//WHEN I create ElementBoundary
		ElementBoundary boundary = new ElementBoundary(element);
		
		//THEN all the attributes are there
		assertThat(boundary.getKey())
		.extracting("smartspace")
		.containsExactly("2019b.gavrield");
		
		assertThat(boundary.getCreator())
		.extracting("smartspace", "email")
		.containsExactly(element.getCreatorSmartspace(), element.getCreatorEmail());
	}
	
	@Test
	public void testCreateActionBoudaryHasAll() throws Exception{
		//GIVEN there is an ActionEntity in the database
		ActionEntity entity = new EntityFactoryImplementation().createNewAction(
				"602",
				"Other",
				"Learn",
				new Date(),
				"mymail@gmail.com",
				"2019b.gavrield",
				new HashMap<String, Object>());
		actions.create(entity);
		//WHEN I create ActionBoundary
		ActionBoundary boundary = new ActionBoundary(entity);
		
		//THEN all the attributes are there
		assertThat(boundary.getActionKey())
		.extracting("smartspace")
		.containsExactly("2019b.gavrield");
	}
	
	@Test
	public void testCreateUserBoudaryHasAll() throws Exception{
		//GIVEN there is an UserEntity in the database
		UserEntity entity = new EntityFactoryImplementation().createNewUser(
				"my.mail@o12.net.il",
				"Oren",
				"%%D",
				UserRole.MANAGER,
				990);
		users.create(entity);
		//WHEN I create UserBoundary
		UserBoundary boundary = new UserBoundary(entity);
		
		//THEN all the attributes are there
		assertThat(boundary.getKey())
		.extracting("smartspace")
		.containsExactly("2019b.gavrield");
	}
}
