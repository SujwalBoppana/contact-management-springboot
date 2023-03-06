package de.zeroco.core.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.zeroco.core.pojo.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	List<Contact> findByCompanyId(Integer companyId);

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
