package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.ElementEntity;

public interface AdvancedElementDao<ElementKey> extends ElementDao<ElementKey>{
	public List<ElementEntity> readAll(int size, int page);
	
	public List<ElementEntity> readAll(int size, int page, String sortAttr);
	
	public List<ElementEntity> readAllByValidity(boolean expired, int size, int page);
	
	public Optional<ElementEntity> readByIdAndValidity(String key, boolean expired);
	
	public List<ElementEntity> readByName(String name, int size, int page);
	
	public List<ElementEntity> readByNameAndValidity(String name, boolean expired,
			int size, int page);
	
	public List<ElementEntity> readByType(String type, int size, int page);
	
	public List<ElementEntity> readByTypeAndValidity(String type, boolean expired,
			int size, int page);
	
	public List<ElementEntity> readByDistance(double x, double y, double distance,
			int size, int page);
	
	public List<ElementEntity> readByDistanceAndValidity(double x, double y, double distance,
			boolean expired, int size, int page);

}
