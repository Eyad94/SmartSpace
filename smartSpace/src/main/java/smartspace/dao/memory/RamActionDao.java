package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//import org.springframework.stereotype.Repository;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;

//@Repository
public class RamActionDao implements ActionDao{
	
	private List<ActionEntity> actions;
	private AtomicInteger nextId;
	
	public RamActionDao() {
		actions = Collections.synchronizedList(new ArrayList<ActionEntity>());
		nextId = new AtomicInteger(1);
	}

	@Override
	public ActionEntity create(ActionEntity action) {
		action.setKey(action.getActionSmartspace() + "#" + nextId.getAndIncrement());
		actions.add(action);
		return action;
	}

	@Override
	public List<ActionEntity> readAll() {
		return actions;
	}

	@Override
	public void deleteAll() {
		actions.clear();
	}

}
