package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.AdvancedActionDao;
import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactoryImplementation;
import smartspace.layout.ElementBoundary;
import smartspace.layout.Key;
import smartspace.layout.Latlng;
import smartspace.layout.Player;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class ControllersIntegrationTests {

	private int port;
	private RestTemplate rest;
	private String baseUrl;
	
	private AdvancedElementDao<String> elements;
	private AdvancedActionDao actions;
	private AdvancedUserDao<String> users;
	

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@Autowired
	public void setElements(AdvancedElementDao<String> elements) {
		this.elements = elements;
	}
	@Autowired
	public void setActions(AdvancedActionDao actions) {
		this.actions = actions;
	}
	@Autowired
	public void setUsers(AdvancedUserDao<String> users) {
		this.users = users;
	}
	@PostConstruct
	public void init() {
		this.rest = new RestTemplate();
		this.baseUrl = "http://localhost:" + port ;
	}
	@After
	public void teardown() {
		this.actions.deleteAll();
		this.elements.deleteAll();
		this.users.deleteAll();
	}
	
	@Test
	public void testPublishForignElement() throws Exception{
		//GIVEN the admin that posts the user is in the database
		UserEntity admin = new EntityFactoryImplementation().createNewUser(
				"my.mail@012.net.il",
				"Oren",
				":-D",
				UserRole.ADMIN,
				0);
		admin.setUserSmartspace("other.project");
		admin = users.create(admin);
		
		//WHEN admin posts element
		ElementBoundary boundary = new ElementBoundary();
		boundary.setCreated(new Date());
		Player player = new Player();
		player.setEmail("o12@mail.co.il");
		player.setSmartspace("other.project");
		boundary.setCreator(player);
		boundary.setName("book1");
		boundary.setExpired(false);
		Key key = new Key();
		key.setId("90"); key.setSmartspace("other.project");
		boundary.setKey(key);
		Latlng ll = new Latlng();
		ll.setLat(2.4); ll.setLng(2.11);
		boundary.setLatlng(ll);
		boundary.setElementType("book");
		boundary.setElementProperties(new HashMap<String, Object>());
		
		ElementBoundary[] boundaries = {boundary};
		ElementBoundary[] actual =
				this.rest.postForObject(
						this.baseUrl + "/smartspace/admin/elements/{adminSmartspace}/{adminEmail}",
						boundaries,
						ElementBoundary[].class,
						"other.project", "my.mail@012.net.il");
		
		//THEN the element is registered in the database
		assertThat(
				this.elements.
				readAll()).
				hasSize(1).
				usingElementComparatorOnFields("elementSmartspace", "elementId").
				containsExactly(actual[0].toEntity());
	}
	
	@Test
	public void playerSearchElementsInRadiusTest() throws Exception{
		
		//GIVEN there are 5 elements in the database 
		//3 located in the requested area, 1 is of them has 'expired = true'
		//AND player is registered in database
		ElementEntity element1 = new EntityFactoryImplementation().createNewElement(
				"computer1",
				"computer",
				new Location(4.4, 2.1),
				new Date(),
				"manager@mail.com",
				null,
				true,
				new HashMap<String, Object>());
		element1 = elements.create(element1);
		ElementEntity element2 = new EntityFactoryImplementation().createNewElement(
				"computer2",
				"computer",
				new Location(5.6, 2.5),
				new Date(),
				"manager@mail.com",
				null,
				false,
				new HashMap<String, Object>());
		element2 = elements.create(element2);
		ElementEntity element3 = new EntityFactoryImplementation().createNewElement(
				"computer3",
				"computer",
				new Location(5.9, 2.25),
				new Date(),
				"manager@mail.com",
				null,
				false,
				new HashMap<String, Object>());
		element3 = elements.create(element3);
		ElementEntity element4 = new EntityFactoryImplementation().createNewElement(
				"computer4",
				"computer",
				new Location(6.6, 2.5),
				new Date(),
				"manager@mail.com",
				null,
				false,
				new HashMap<String, Object>());
		element4 = elements.create(element4);
		ElementEntity element5 = new EntityFactoryImplementation().createNewElement(
				"computer5",
				"computer",
				new Location(4.1, 7.0),
				new Date(),
				"manager@mail.com",
				null,
				false,
				new HashMap<String, Object>());
		element5 = elements.create(element5);
		UserEntity player = new EntityFactoryImplementation().createNewUser(
				"my.mail@012.net.il",
				"Oren",
				":-D",
				UserRole.PLAYER,
				0);
		player = users.create(player);
		
		//WHEN player wants to see elements located in 4<x<6 and 1<y<3
		ElementBoundary[] reqBoundaries =
				this.rest.getForObject(
						this.baseUrl + "/smartspace/elements/{userSmartspace}/{userEmail}"
						+ "?search=location&x={x}&y={y}&distance={distance}"
						+ "&page={page}&size={size}",
						ElementBoundary[].class,
						"2019b.gavrield", "my.mail@012.net.il",
						5.0, 2.0, 1.0, 0, 5);
		
		//THEN we will get a list consisting elements in that area
		//AND all elements 'expired' field is false
		assertThat(Stream.of(reqBoundaries)
				.collect(Collectors.toList()))
				.hasSize(2);
		
				
	}
	
	
	@Test
	public void testGetElementByName() throws Exception{
		// GIVEN the database contains 3 elements
		ElementEntity element1 = new EntityFactoryImplementation().createNewElement(
				"computer1",
				"computer",
				new Location(4.4, 2.1),
				new Date(),
				"manager@mail.com",
				null,
				true,
				new HashMap<String, Object>());
		element1 = elements.create(element1);
		
		ElementEntity element2 = new EntityFactoryImplementation().createNewElement(
				"computer2",
				"computer",
				new Location(5.6, 2.5),
				new Date(),
				"manager@mail.com",
				null,
				false,
				new HashMap<String, Object>());
		element2 = elements.create(element2);
	
		ElementEntity element3 = new EntityFactoryImplementation().createNewElement(
				"computer3",
				"computer",
				new Location(5.9, 2.25),
				new Date(),
				"manager@mail.com",
				null,
				false,
				new HashMap<String, Object>());
		element3 = elements.create(element3);
		
		UserEntity player = new EntityFactoryImplementation().createNewUser(
				"my.mail@012.net.il",
				"Oren",
				":-D",
				UserRole.PLAYER,
				0);
		player = users.create(player);
		
		
		//WHEN a player wants to get elements by name
		ElementBoundary[] reqBoundaries =
				this.rest.getForObject(
						this.baseUrl + "/smartspace/elements/{userSmartspace}/{userEmail}"
						+ "?search=name&value={value}"
						+ "&page={page}&size={size}",
						ElementBoundary[].class,
						"2019b.gavrield", "my.mail@012.net.il",
						"computer2", 0, 5);
		
		//THEN we will get a elements that has same name
		//AND all elements 'expired' field is false
		assertThat(Stream.of(reqBoundaries)
				.collect(Collectors.toList()))
				.hasSize(1);
	}

	

	@Test
	public void testGetElementByType() throws Exception{
		// GIVEN the database contains 2 elements
		ElementEntity element1 = new EntityFactoryImplementation().createNewElement(
				"computer1",
				"computer",
				new Location(4.4, 2.1),
				new Date(),
				"manager@mail.com",
				null,
				false,
				new HashMap<String, Object>());
		element1 = elements.create(element1);
		
		ElementEntity element2 = new EntityFactoryImplementation().createNewElement(
				"computer2",
				"computer",
				new Location(5.6, 2.5),
				new Date(),
				"manager@mail.com",
				null,
				false,
				new HashMap<String, Object>());
		element2 = elements.create(element2);
	
		
		UserEntity player = new EntityFactoryImplementation().createNewUser(
				"my.mail@012.net.il",
				"Oren",
				":-D",
				UserRole.PLAYER,
				0);
		player = users.create(player);
		
		
		//WHEN a player wants to get elements by type
		ElementBoundary[] reqBoundaries =
				this.rest.getForObject(
						this.baseUrl + "/smartspace/elements/{userSmartspace}/{userEmail}"
						+ "?search=type&value={value}"
						+ "&page={page}&size={size}",
						ElementBoundary[].class,
						"2019b.gavrield", "my.mail@012.net.il",
						"computer", 0, 5);
		
		//THEN we will get a elements that has same type
		//AND all elements 'expired' field is false
		assertThat(Stream.of(reqBoundaries)
				.collect(Collectors.toList()))
				.hasSize(2);
	}
}
