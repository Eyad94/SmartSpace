package smartspace.layout;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.logic.UserService;

@RestController
public class UserController {

	
	private UserService service;

	@Autowired
	public UserController(UserService service) {
		super();
		this.service = service;
	}
	
	
	

	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/admin/users/{adminSmartspace}/{adminEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getUsers(
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size, 
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
				
		
		return this.service.
						getUsers(adminSmartspace, adminEmail, size, page)
						.stream()
						.map(UserBoundary::new)
						.collect(Collectors.toList())
						.toArray(new UserBoundary[0]);
	}
	
	
	
	@RequestMapping(
			method = RequestMethod.POST,
			path = "/smartspace/admin/users/{adminSmartspace}/{adminEmail}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary publish(@RequestBody UserBoundary userBoundary,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail) {
	
		
		return new UserBoundary(
				this.service  
				.publishNewUser(userBoundary.toEntity(), adminSmartspace, adminEmail));
	
	
	}
	
	@RequestMapping(
			method = RequestMethod.POST,
			path = "/smartspace/users",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser(@RequestBody NewUserForm form) {
		
		UserBoundary newUser = new UserBoundary();
		
		if (!emailValidation(form.getEmail()))
			throw new RuntimeException("Invalid email string!");
		
		UserKey key = new UserKey();
		key.setEmail(form.getEmail());
		newUser.setKey(key);
		newUser.setUsername(form.getUsername());
		newUser.setAvatar(form.getAvatar());
		newUser.setRole(form.getRole());
		newUser.setPoints(0l);
		
		return new UserBoundary(service.createNewUser(newUser.toEntity()));
	}

	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/users/login/{userSmartspace}/{userEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login(
			@PathVariable("userSmartspace") String smartspace,
			@PathVariable("userEmail") String email) {
		return new UserBoundary(service.validateUser(smartspace, email));
	}
	
	@RequestMapping(
			method = RequestMethod.PUT,
			path = "/smartspace/users/login/{userSmartspace}/{userEmail}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void update(
			@RequestBody UserBoundary boundary,
			@PathVariable("userSmartspace") String smartspace,
			@PathVariable("userEmail") String email) {
		if(boundary.getPoints() != null)
			boundary.setPoints(null);
		service.update(boundary.toEntity(), smartspace, email);
	}

	private boolean emailValidation(String email) {
		//regex provided from: https://www.geeksforgeeks.org/check-email-address-valid-not-java/
		String regex = 
				"^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{2,7}$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(email).matches();
		
	}
	
	
	
	
}