package smartspace.dao.rdb;

import java.util.ArrayList;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//import smartspace.dao.ElementDao;
import smartspace.dao.AdvancedElementDao;
import smartspace.data.ElementEntity;

@Repository
public class RdbElementDao implements AdvancedElementDao<String>{
	private ElementCrud elementCrud;
	private IdCreatorCrud idCreatorCrud; 
	private String elementSmartspace;
	
	public String getElementSmartSpace() {
		return elementSmartspace;
	}
	@Value("${smartspace.name}")
	public void setElementSmartSpace(String elementSmartSpace) {
		this.elementSmartspace = elementSmartSpace;
	}
	@Autowired
	public RdbElementDao(ElementCrud elementCrud, IdCreatorCrud idCreatorCrud) {
		this.elementCrud = elementCrud;
		this.idCreatorCrud = idCreatorCrud;
		
	}
	@Override
	@Transactional
	public ElementEntity create(ElementEntity element) {
		IdCreator idCreator = this.idCreatorCrud.save(new IdCreator());
		if (element.getElementSmartspace() == null)
			element.setKey(elementSmartspace + "#" + idCreator.getId());
		
		// SQL: INSERT
		if (!this.elementCrud.existsById(element.getKey())) {
			ElementEntity rv = this.elementCrud.save(element);
			this.idCreatorCrud.delete(idCreator);
			return rv;
		} else {
			throw new RuntimeException("element already exists with id: " + element.getKey());
		}

	}

	@Override
	@Transactional(readOnly=true)
	public Optional<ElementEntity> readById(String key) {
		
		return this.elementCrud.findById(key);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll() {
		// SQL: SELECT
		List<ElementEntity> elements = new ArrayList<>();
		this.elementCrud.findAll().forEach(elements::add);
		return elements;
	}

	@Override
	@Transactional(readOnly=false)
	public void update(ElementEntity element) {
		ElementEntity found = readById(element.getKey())
				.orElseThrow
				(()->new RuntimeException("invalid element key " + element.getKey()));
		if (element.getName() != null)
			found.setName(element.getName());
		
		found.setExpired(element.isExpired());
		if(element.getLocation() != null)
			found.setLocation(element.getLocation());
		if(element.getMoreAttributes() != null)
			found.setMoreAttributes(element.getMoreAttributes());
		if (element.getCreatorEmail() != null)
			found.setCreatorEmail(element.getCreatorEmail());
		if (element.getCreatorSmartspace() != null)
			found.setCreatorSmartspace(element.getCreatorSmartspace());
		if(element.getType() != null)
			found.setType(element.getType());
		
		// SQL: UPDATE
		elementCrud.save(found);
		
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteByKey(String key) {
		this.elementCrud.delete(readById(key).get());
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(ElementEntity element) {
		this.elementCrud.delete(element);
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteAll() {
		// SQL: DELETE
		this.elementCrud.deleteAll();
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(int size, int page) {
		return this.elementCrud
				.findAll(PageRequest.of(page, size))
				.getContent();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(int size, int page, String sortAttr) {
		return this.elementCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, sortAttr))
				.getContent();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAllByValidity(boolean expired, int size, int page) {
		return 
			this.elementCrud
			.findAllByExpired(expired, PageRequest.of(page, size));
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public Optional<ElementEntity> readByIdAndValidity(String key, boolean expired) {
		//String[] keySplit = key.split("#");
		return this.elementCrud.findByIdAndExpired(key, expired);
	}

	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readByName(String name, int size, int page) {
		return this.elementCrud
				.findByName(name, PageRequest.of(page, size));
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readByNameAndValidity(String name, boolean expired,
			int size, int page) {
		return this.elementCrud
				.findByNameAndExpired(name, expired, PageRequest.of(page, size));
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readByType(String type, int size, int page) {
		return this.elementCrud
				.findByType(type, PageRequest.of(page, size));
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readByTypeAndValidity(String type, boolean expired,
			int size, int page) {
		return this.elementCrud
				.findByTypeAndExpired(type, expired, PageRequest.of(page, size));
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readByDistance(double x, double y, double distance, 
			int size, int page) {
		if (distance < 0)
			throw new RuntimeException("ERROR NEGATIVE DISTANCE!! " + distance);
		return this.elementCrud
				.findAllByXBetweenAndYBetween(x - distance, x + distance, y - distance,
						y + distance, PageRequest.of(page, size));
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readByDistanceAndValidity(double x, double y, double distance, 
			boolean expired, int size, int page) {
		if (distance < 0)
			throw new RuntimeException("ERROR NEGATIVE DISTANCE!! " + distance);
		return this.elementCrud
				.findAllByXBetweenAndYBetweenAndExpired(x - distance, x + distance, y - distance,
						y + distance, expired, PageRequest.of(page, size));
	}
	

}
