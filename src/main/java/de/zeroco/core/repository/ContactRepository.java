package de.zeroco.core.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.zeroco.core.pojo.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	List<Contact> findByCompanyId(Integer companyId);
	
	@Query(value = "SELECT c.pk_id,c.name,c.email,c.phone,c.dob,c.age,c.gender,c.address FROM Contact c WHERE c.company_id=?1",nativeQuery = true)
	List<Map<String, Object>> getMapByCompanyId(Integer companyId);
	
	@Query(value = "SELECT * FROM Contact c WHERE c.age <=?1",nativeQuery = true)
	List<Contact> findByAgeAndSort(Integer age);

	List<Contact> findByAgeLessThanEqualOrderByAgeDesc(int age);

	List<Contact> findByAgeLessThanOrderByAgeDesc(int age);

	List<Contact> findByAgeGreaterThanEqualOrderByAgeDesc(int age);

	List<Contact> findByAgeGreaterThanOrderByAgeDesc(int age);

	List<Contact> findByAgeGreaterThanEqualOrderByAgeAsc(int age);

	List<Contact> findByAgeGreaterThanOrderByAgeAsc(int age);

	List<Contact> findByAge(int age);

	List<Contact> findByAgeLessThanOrderByAgeAsc(int age);

	List<Contact> findByAgeLessThanEqualOrderByAgeAsc(int age);

}
