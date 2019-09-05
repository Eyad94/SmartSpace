package smartspace.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.logic.ActionService;

@RestController
public class ActionController {
	
	private ActionService actionService;

	@Autowired
	public ActionController(ActionService actionService) {
		this.actionService = actionService;
	}
	
	
	@RequestMapping(
			method = RequestMethod.POST,
			path = "/smartspace/admin/actions/{adminSmartspace}/{adminEmail}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] publish(
			@RequestBody ActionBoundary[] actions,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail) {
		
		
		List<ActionEntity> entityList = new ArrayList<>();
		for (ActionBoundary actionBoundary : actions) {
			entityList.add(actionBoundary.toEntity());
		}
		
		entityList = this.actionService.publishNewActions(entityList, adminSmartspace, adminEmail);
		ActionBoundary[] boundaries = new ActionBoundary[entityList.size()];
		int i = 0;
		for(ActionEntity actionEntity: entityList) {
			boundaries[i++] = new ActionBoundary(actionEntity);
		}
		
		return boundaries;
	}
	
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/admin/actions/{adminSmartspace}/{adminEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] getActions(
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size, 
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
				return this.actionService
						.getActions(adminSmartspace, adminEmail, size, page)
						.stream()
						.map(ActionBoundary::new)
						.collect(Collectors.toList())
						.toArray(new ActionBoundary[0]);
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
					path="/smartspace/actions",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin("http://localhost:8000")
	public Object doAction(@RequestBody ActionBoundary actionBoundary) {
		
		if (actionBoundary.getActionKey() != null)
			throw new RuntimeException("Ilegal action!");
		
		String actionType = actionBoundary.getType();
		Map<String, Object> map;

		switch (actionType) {
			
		case "echo":
			return new ActionBoundary(this.actionService.createNewAction(actionBoundary.toEntity()));
		case "checkin":
			return new ActionBoundary(this.actionService.checkin(actionBoundary.toEntity()));
		case "checkout":
			return new ActionBoundary(this.actionService.checkout(actionBoundary.toEntity()));
		case "storeFile":
			map = this.actionService.storeFile(actionBoundary.toEntity());
			return returnedMap(map);
		case "deleteFile":
			map = this.actionService.deleteFile(actionBoundary.toEntity());
			return returnedMap(map);
		case "install":
			map = this.actionService.installProgram(actionBoundary.toEntity());
			return returnedMap(map);
		case "uninstall":
			map = this.actionService.uninstallProgram(actionBoundary.toEntity());
			return returnedMap(map);
		case "study":
			ActionEntity action = this.actionService.createNewAction(actionBoundary.toEntity());
			Map<String, String> rval = new HashMap<>();
			if (action != null)
				rval.put("Study", "Approved");
			else rval.put("Study", "Disapproved");
			return rval;
		default:
			throw new RuntimeException("Illegal action type!");
		}

	}
	
	private Map<String, Object> returnedMap(Map<String, Object> map){
		Map<String, Object> rmap = new HashMap<>();
		rmap.put("action", new ActionBoundary((ActionEntity) map.get("actionEntity")));
		rmap.put("element", new ElementBoundary((ElementEntity) map.get("elementEntity")));
		rmap.put("user", new UserBoundary((UserEntity) map.get("userEntity")));
		return rmap;
	}
}
