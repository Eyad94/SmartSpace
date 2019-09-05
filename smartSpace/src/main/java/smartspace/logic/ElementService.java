package smartspace.logic;

import java.util.List;

import smartspace.data.ElementEntity;

public interface ElementService {
	public ElementEntity publishNewElement(ElementEntity elementEntity, String adminSmartspace,
			String adminEmail);
	
	public List<ElementEntity> publishNewElements(List<ElementEntity> elementEntities,
			String adminSmartspace, String adminEmail);
	
	public List<ElementEntity> getElements (String adminSmartspace, String adminEmail, int size, int page);

	public ElementEntity createNewElement(ElementEntity entity, String managerSmartspace,
			String managerEmail);
	
	public List<ElementEntity> getAllElements(String userSmartspace, String userEmail,
			int size, int page);
	
	public ElementEntity getElementById(String userSmartspace, String userEmail,
			String elementSmartspace, String elementId);

	public void update(ElementEntity entity, String managerSmartspace, String managerEmail, String elementSmartspace,
			String elementId);
	
	public List<ElementEntity> getAllElementsByName(String userSmartspace, String userEmail,
			String name, int size, int page);
	
	public List<ElementEntity> getAllElementsByType(String userSmartspace, String userEmail,
			String type, int size, int page);
	
	public List<ElementEntity> getAllElementsInRadius(String userSmartspace, String userEmail,
			double x, double y, double distance, int size, int page);

}
