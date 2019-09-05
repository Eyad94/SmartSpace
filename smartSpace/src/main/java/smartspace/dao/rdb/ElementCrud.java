package smartspace.dao.rdb;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ElementEntity;

public interface ElementCrud extends PagingAndSortingRepository<ElementEntity, String> {
	
	public Optional<ElementEntity> findById(
			@Param("elementId") String key);
	
	public List<ElementEntity> findAllByExpired(
			@Param("expired") boolean expired, 
			Pageable pageable);
	
	
//	@Query("select e from ElementEntity"
//			+ " e where e.elementSmartspace = ?1 and  "
//			+ "e.elementId = ?2 and"
//			+ " e.expired = ?3")
//	public Optional<ElementEntity> findByIdAndExpired(
//			@Param("elementSmartspace") String smartspace,
//			@Param("elementId") String id,
//			@Param("expired") boolean expired);
	
	@Query(value="SELECT * FROM ELEMENTS WHERE ELEMENT_KEY = ?1 AND EXPIRED = ?2", nativeQuery =true)
	public Optional<ElementEntity> findByIdAndExpired(
			String key,
			boolean expired);
	
	
	
	public List<ElementEntity> findByName(
			@Param("name") String name, 
			Pageable pageable);
	
	public List<ElementEntity> findByNameAndExpired(
			@Param("name") String name,
			@Param("expired") boolean expired,
			Pageable pageable);
	
	public List<ElementEntity> findByType(
			@Param("type") String type, 
			Pageable pageable);
	
	public List<ElementEntity> findByTypeAndExpired(
			@Param("type") String type,
			@Param("expired") boolean expired,
			Pageable pageable);
	
	
	@Query("select e from ElementEntity e where e.location.x between ?1 and ?2 and e.location.y between ?3 and ?4")
	public List<ElementEntity> findAllByXBetweenAndYBetween(
			@Param("x1") double x1,
			@Param("x2") double x2,
			@Param("y1") double y1,
			@Param("y2") double y2,
			Pageable pageable);
	
	@Query("select e from ElementEntity e where e.location.x between ?1 and ?2 and e.location.y between ?3 and ?4 and e.expired = ?5")
	public List<ElementEntity> findAllByXBetweenAndYBetweenAndExpired(
			@Param("x1") double x1,
			@Param("x2") double x2,
			@Param("y1") double y1,
			@Param("y2") double y2,
			@Param("expired") boolean expired,
			Pageable pageable);
	
}
