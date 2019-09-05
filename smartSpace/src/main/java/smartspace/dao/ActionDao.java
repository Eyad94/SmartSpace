package smartspace.dao;

import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionDao{
	public ActionEntity create(ActionEntity action);
	public List<ActionEntity> readAll();
	public void deleteAll();
}
