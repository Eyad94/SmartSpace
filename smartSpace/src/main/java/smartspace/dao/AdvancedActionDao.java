package smartspace.dao;

import java.util.List;

import smartspace.data.ActionEntity;

public interface AdvancedActionDao extends ActionDao{
	public List<ActionEntity> readAll(int size, int page);
	
	public List<ActionEntity> readAll(int size, int page, String sortAttr);
}