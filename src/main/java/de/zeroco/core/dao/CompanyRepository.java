package de.zeroco.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.zeroco.core.pojo.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
	
	Company findByCode(String code);
}
