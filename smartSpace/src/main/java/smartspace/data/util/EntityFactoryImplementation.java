package smartspace.data.util;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;


@Component
public class EntityFactoryImplementation implements EntityFactory{
	
	
	public EntityFactoryImplementation() {};

	public UserEntity createNewUser(String userEmail,
			String username, String avatar, UserRole role, long points){
		
		UserEntity user = new UserEntity();
		user.setUserEmail(userEmail);
		user.setUsername(username);
		user.setAvatar(avatar);
		user.setRole(role);
		user.setPoints(points);
		
		return user;
	}
	
	
	public ElementEntity createNewElement(String name, String type, Location location,
			Date creationTimestamp, String creatorEmail, String creatorSmartspace,
			boolean expired, Map<String,Object> moreAttributes){
		
		ElementEntity element = new ElementEntity();
		element.setName(name);
		element.setType(type);
		element.setLocation(location);
		element.setCreationTimestamp(creationTimestamp);
		element.setCreatorEmail(creatorEmail);
		element.setCreatorSmartspace(creatorSmartspace);
		element.setExpired(expired);
		element.setMoreAttributes(moreAttributes);
		
		return element;
	}
	
	
	public ActionEntity createNewAction(String elementId, String elementSmartspace, String actionType,
			Date creationTimestamp, String playerEmail, String playerSmartspace,
			Map<String,Object> moreAttributes){
				
			ActionEntity action = new ActionEntity();
			action.setElementId(elementId);
			action.setElementSmartspace(elementSmartspace);
			action.setActionType(actionType);
			action.setCreationTimestamp(creationTimestamp);
			action.setPlayerEmail(playerEmail);
			action.setPlayerSmartspace(playerSmartspace);
			action.setMoreAttributes(moreAttributes);
			
			return action;
			}
}
