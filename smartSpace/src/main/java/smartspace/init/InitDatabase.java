package smartspace.init;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;


@Component
public class InitDatabase implements CommandLineRunner{
	
	private Log log = LogFactory.getLog(InitDatabase.class);
	private AdvancedUserDao<String> users;
	private AdvancedElementDao<String> elements;
	private EntityFactory factory;

	
	@Autowired
	public InitDatabase(
			AdvancedUserDao<String> users,
			AdvancedElementDao<String> elements,
			EntityFactory factory) {
		this.users = users;
		this.elements = elements;
		this.factory = factory;
	}

	@Override
	public void run(String... args) throws Exception {
		
		UserEntity manager = factory.createNewUser("manager@gmail.com", "JOHN", ":-D", UserRole.MANAGER, 0);
		UserEntity player = factory.createNewUser("mymail@mail.afeka.ac.il", "Dan", ";}", UserRole.PLAYER, 100);
		UserEntity admin = factory.createNewUser("admin.other.project@gmail.com", "Avi Simchon", "**", UserRole.ADMIN, 0);
		
		admin.setUserSmartspace("otherSmartspace");
		
		manager = users.create(manager); 
		player = users.create(player); 
		admin = users.create(admin);
		
		log.info("-------> " + manager);
		log.info("-------> " + player);
		log.info("-------> " + admin);
		
		
		//Add 3 Labs
		List<ElementEntity> elementList_Labs =
		IntStream.range(1, 4)
		.mapToObj(i -> factory.createNewElement(
				"lab"+i, "lab", null,
				new Date(), "manager@gmail.com", "2019b.gavrield",
				false, new HashMap<String, Object>()))
		.collect(Collectors.toList());
		
		List<Location> locations_Labs = new ArrayList<>();
		for(double x = 0.0; x <= 40.0; x += 20.0)
			locations_Labs.add(new Location(x,0));
			
		int index = 0;
		for (ElementEntity element: elementList_Labs) {
			element.setLocation(locations_Labs.get(index++));
			elements.create(element);
			log.info("=========>> " + element);
		}		
		
		
		//Add 9 computers to Lab 1
		List<ElementEntity> elementList_Lab1 =
		IntStream.range(1, 10)
		.mapToObj(i -> factory.createNewElement(
				"computer"+i, "computer", null,
				new Date(), "manager@gmail.com", "2019b.gavrield",
				false, new HashMap<String, Object>()))
		.collect(Collectors.toList());
		
		List<Location> locations_Lab1 = new ArrayList<>();
		for (double y = 1.0; y <= 9.0; y += 4.0)
			for(double x = 1.0; x <= 9.0; x += 4.0)
				locations_Lab1.add(new Location(x,y));
		
		index = 0;
		for (ElementEntity element: elementList_Lab1) {
			element.setLocation(locations_Lab1.get(index++));
			elements.create(element);
			log.info("=========>> " + element);
		}
		
		
		//Add 9 computers to Lab 2
		List<ElementEntity> elementList_Lab2 =
		IntStream.range(10, 19)
		.mapToObj(i -> factory.createNewElement(
				"computer"+i, "computer", null,
				new Date(), "manager@gmail.com", "2019b.gavrield",
				false, new HashMap<String, Object>()))
		.collect(Collectors.toList());
		
		List<Location> locations_Lab2 = new ArrayList<>();
		for (double y = 1.0; y <= 9; y += 4.0)
			for(double x = 21.0; x <= 29; x += 4.0)
				locations_Lab2.add(new Location(x,y));
		
		index = 0;
		for (ElementEntity element: elementList_Lab2) {
			element.setLocation(locations_Lab2.get(index++));
			elements.create(element);
			log.info("=========>> " + element);
		}
		
		
		//Add 9 computers to Lab 3
				List<ElementEntity> elementList_Lab3 =
				IntStream.range(19, 28)
				.mapToObj(i -> factory.createNewElement(
						"computer"+i, "computer", null,
						new Date(), "manager@gmail.com", "2019b.gavrield",
						false, new HashMap<String, Object>()))
				.collect(Collectors.toList());
				
				List<Location> locations_Lab3 = new ArrayList<>();
				for (double y = 1.0; y <= 9.0; y += 4.0)
					for(double x = 41.0; x <= 49.0; x += 4.0)
						locations_Lab3.add(new Location(x,y));
				
				index = 0;
				for (ElementEntity element: elementList_Lab3) {
					element.setLocation(locations_Lab3.get(index++));
					elements.create(element);
					log.info("=========>> " + element);
				}
				
	}

}
