package smartspace.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ElementEntity;
import smartspace.logic.ElementService;

@RestController
public class ElementController {

	private ElementService elementService;
	
	
	@Autowired
	public ElementController(ElementService elementService) {
		super();
		this.elementService = elementService;
	}
	
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/admin/elements/{adminSmartspace}/{adminEmail}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] publish(
			@RequestBody ElementBoundary[] elements,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail) {
		
		List<ElementEntity> entityList = new ArrayList<>();
		for (ElementBoundary boundary : elements) {
			entityList.add(boundary.toEntity());
		}
		
		entityList = this.elementService.publishNewElements(entityList, adminSmartspace, adminEmail);
		ElementBoundary[] boundaries = new ElementBoundary[entityList.size()];
		int i = 0;
		for(ElementEntity ent: entityList) {
			boundaries[i++] = new ElementBoundary(ent);
		}
		
		return boundaries;
	}
	
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/admin/elements/{adminSmartspace}/{adminEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElements(
			@PathVariable("adminSmartspace") String smartspace,
			@PathVariable("adminEmail") String email,
			@RequestParam(name="size", required=false, defaultValue="10") int size, 
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
				return this.elementService.
						getElements(smartspace, email, size, page).
						stream().
						map(ElementBoundary::new).
						collect(Collectors.toList()).
						toArray(new ElementBoundary[0]);
	}
	
	
	//--------------- Milestone 4 ---------------
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/elements/{managerSmartspace}/{managerEmail}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary create(
			@RequestBody ElementBoundary boundary,
			@PathVariable("managerSmartspace") String managerSmartspace,
			@PathVariable("managerEmail") String managerEmail){
			
		return new ElementBoundary(
				this.elementService.createNewElement(
						boundary.toEntity(),
						managerSmartspace,
						managerEmail));
	}
	
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path = "/smartspace/elements/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void update(
			@RequestBody ElementBoundary boundary,
			@PathVariable("managerSmartspace") String managerSmartspace,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId) {
		
		if (boundary.getKey() == null)
			this.elementService.update(
					boundary.toEntity(), managerSmartspace, managerEmail, elementSmartspace, elementId);
		else throw new RuntimeException("Illegal key value!");
	}
	
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getElement(
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId) {
				return new ElementBoundary(
						this.elementService
						.getElementById(
								userSmartspace, userEmail, elementSmartspace, elementId));
	}
	
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/smartspace/elements/{userSmartspace}/{userEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	
	public ElementBoundary[] getAllElementsByAttribute(
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="search", required=false, defaultValue="") String search,
			@RequestParam(name="value", required=false, defaultValue="") String value,
			@RequestParam(name="x", required=false, defaultValue="0") double x,
			@RequestParam(name="y", required=false, defaultValue="0") double y,
			@RequestParam(name="distance", required=false, defaultValue="0") double distance, 
			@RequestParam(name="size", required=false, defaultValue="10") int size, 
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		
		List<ElementEntity> elementsEntities;
		
		switch(search) {
		case "name": 
			elementsEntities = this.elementService
			.getAllElementsByName(userSmartspace, userEmail, value, size, page);
		break;
		
		case "type": 
			elementsEntities = this.elementService
			.getAllElementsByType(userSmartspace, userEmail, value, size, page);
		break;
		
		case "location": 
			elementsEntities = this.elementService
			.getAllElementsInRadius(userSmartspace, userEmail, x, y, distance, size, page);
		break;

		case "":
			elementsEntities = this.elementService
			.getAllElements(userSmartspace, userEmail, size, page);
		break;
		
		default:
			return null;
		}
		
		return 	elementsEntities
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
	}
	
}
