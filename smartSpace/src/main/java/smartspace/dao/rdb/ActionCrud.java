package smartspace.dao.rdb;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ActionEntity;

public interface ActionCrud extends PagingAndSortingRepository<ActionEntity, String> {
	
	public Optional<ActionEntity> findById(
			@Param("Id") String key);
}
