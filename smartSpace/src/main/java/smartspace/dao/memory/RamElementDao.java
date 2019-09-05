package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
//import java.util.concurrent.atomic.AtomicInteger;

//import org.springframework.stereotype.Repository;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;


//@Repository
public class RamElementDao<ElementKey> implements ElementDao<ElementKey> {
	private Map<String, ElementEntity> elements;
	//private AtomicInteger nextId;

	public RamElementDao() {
		elements = Collections.synchronizedMap(new HashMap<String,ElementEntity>());
		//nextId = new AtomicInteger(1);
	}
	@Override
	public ElementEntity create(ElementEntity element) {
		
		elements.put(element.getKey(), element);
		return element;
	}

	public Optional<ElementEntity> readById(ElementKey key) {
		Optional<ElementEntity> op = Optional.empty();
		if (elements.containsKey(key)) {
			op = Optional.of(elements.get(key));
		}
		return op;
	}

	@Override
	public List<ElementEntity> readAll() {
		return new ArrayList<ElementEntity>(elements.values());
	}

	@Override
	public void update(ElementEntity element) {
		@SuppressWarnings("unchecked")
		ElementEntity found = readById((ElementKey)element.getKey())
				.orElseThrow
				(()->new RuntimeException("invalid element key " + element.getKey()));
		found.setName(element.getName());
		found.setExpired(element.isExpired());
		found.setLocation(element.getLocation());
		found.setMoreAttributes(element.getMoreAttributes());
		found.setCreatorEmail(element.getCreatorEmail());
		found.setCreatorSmartspace(element.getCreatorSmartspace());
		found.setType(element.getType());
		
	}

	public void deleteByKey(ElementKey key) {
		elements.remove(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(ElementEntity element) {
		if (element.getKey() != null)
			deleteByKey((ElementKey)element.getKey());
		else if (element.getName() != null)
			elements.values().forEach((e)->{ 
				if (e.getName().equals(element.getName()))
					deleteByKey((ElementKey)e.getKey());
			});
		else
			throw new  RuntimeException("invalid element to delete!");
	}

	@Override
	public void deleteAll() {
		elements.clear();
		
}



}
