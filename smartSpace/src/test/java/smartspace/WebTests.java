package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;
import smartspace.layout.UserKey;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties="spring.profiles.active=default")
public class WebTests{
	private int port;
	
	private String baseUrl;
	
	private RestTemplate rest;
	
	private AdvancedUserDao<String> users;
	private AdvancedElementDao<String> elements;
	
	public EntityFactory fact;
	

	@Autowired
	public void setFact(EntityFactory fact) {
		this.fact = fact;
	}
	
	
	@Autowired
	public void setUsers(AdvancedUserDao<String> users) {
		this.users = users;
	}
	
	@Autowired
	public void setElements(AdvancedElementDao<String> elem) {
		this.elements = elem;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/users/2019b.gavrield/Leonidvishniakov@gmail.com";
		this.rest = new RestTemplate();
	}
	
	@Before
	public void setUp() {
		
		UserEntity user = this.fact.createNewUser("Leonidvishniakov@gmail.com", "User", "Avatar",UserRole.ADMIN,(long) 2);
		
		
		this.users.create(user);
	}
	

	@After
	public void teardown() {
		this.users
			.deleteAll();
	}
	
	@Test
	public void testPublish() throws Exception {
		// GIVEN the database is clean
		
		// WHEN I post a new message
		UserBoundary newUser = new UserBoundary();
		newUser.setUsername("Leo");
		UserKey key = new UserKey();
		key.setEmail("Leonid@gmail.com");
		key.setSmartspace("test");
		newUser.setKey(key);
		newUser.setPoints((long) 2);
		newUser.setRole(UserRole.PLAYER);
		
		
		UserBoundary actual =
		  this.rest
			.postForObject(
					this.baseUrl, 
					newUser, 
					UserBoundary.class);
		
		// THEN the database contains a single new message
		// AND the message in the database is similar to the newMessage posted
		assertThat(this.users
			.readAll())
			.hasSize(5);
			
	}
	
	
	@Test
	public void testGetWithPagination() throws Exception {
		// GIVEN the database contains 10 messages
		int inputSize = 10;
		
		
		// WHEN I get all messages using page size of 3 and page 1
		int page = 0;
		int size = 3;
		ElementBoundary[] actual = 
		  this.rest
			.getForObject(
					this.baseUrl + "?page={page}&size={size}", 
					ElementBoundary[].class, 
					page, size);
		
		// THEN I get 3 messages
		int expectedSize = 1;
		assertThat(actual)
			.hasSize(expectedSize);	
		
	}
}