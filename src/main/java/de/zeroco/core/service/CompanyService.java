package de.zeroco.core.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import de.zeroco.core.dao.CompanyRepository;
import de.zeroco.core.dao.ContactRepository;
import de.zeroco.core.pojo.Company;
import de.zeroco.core.pojo.Contact;
import de.zeroco.core.util.FieldValidation;
import de.zeroco.core.util.Utility;

@Component
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ContactRepository contactRepository;

	private Map<String, Object> reqData;

	public Map<String, Object> createCompany(Map<String, Object> reqData) {
		this.reqData = reqData;
		if (FieldValidation.required(reqData, "name", "email", "phone", "website", "code") == null) {
			if (Utility.isValidEmail(param("email")) && Utility.isValidMoblieNo(param("phone"))) {
				if (companyRepository.findByCode(param("code")) == null) {
					Company company = new Company(param("name"), param("code"), param("email"), param("phone"),
							param("website"), param("address"));
					return objectToMap(companyRepository.save(company));
				} else {
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Company code already exists!");
				}
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Enter a valid Email or phone Number");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Required fields are missing");
		}
	}

	public Map<String, Object> updateCompany(int id, Map<String, Object> reqData) {
		Optional<Company> optionalCompany = companyRepository.findById(id);
		if (optionalCompany.isPresent()) {
			this.reqData = reqData;
			if (FieldValidation.required(reqData, "name", "email", "phone", "website") == null) {
				if (Utility.isValidEmail(param("email")) && Utility.isValidMoblieNo(param("phone"))) {
					Company existingCompany = optionalCompany.get();
					existingCompany.setName(param("name"));
					existingCompany.setEmail(param("email"));
					existingCompany.setPhone(param("phone"));
					existingCompany.setWebsite(param("website"));
					existingCompany.setAddress(param("address"));
					return objectToMap(companyRepository.save(existingCompany));
				} else {
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Enter a valid Email or phone Number");
				}
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Required fields are missing");
			}

		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
		}
	}

	public List<Map<String, Object>> getAllCompanies() {
		List<Map<String, Object>> resData = new LinkedList<Map<String, Object>>();
		for (Company company : companyRepository.findAll()) {
			resData.add(objectToMap(company));
		}
		return resData;
	}

	public Map<String, Object> getCompanyById(Integer id) {
		Optional<Company> optionalCompany = companyRepository.findById(id);
		if (optionalCompany.isPresent()) {
			return objectToMap(optionalCompany.get());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
		}
	}

	public void deleteCompany(Integer id) {
		if (companyRepository.findById(id).isPresent()) {
			companyRepository.delete(companyRepository.findById(id).get());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
		}
	}

	public Map<String, Object> findByCode(String code) {
		return objectToMap(companyRepository.findByCode(code));
	}

	public String param(String key) {
		Object object = reqData.get(key);
		return object == null ? null : object.toString().trim();
	}

	public Map<String, Object> objectToMap(Company company) {
		if (company == null)
			return null;
		Map<String, Object> resData = new LinkedHashMap<String, Object>();
		resData.put("id", company.getId());
		resData.put("name", company.getName());
		resData.put("code", company.getCode());
		resData.put("email", company.getEmail());
		resData.put("phone", company.getPhone());
		resData.put("website", company.getWebsite());
		resData.put("address", company.getAddress());
		return resData;
	}

	public Map<String, Object> getCompanyContacts(String code) {
		Company company = companyRepository.findByCode(code);
		List<Contact> contants = contactRepository.findByCompanyId(company.getId());
		Map<String, Object> resData = objectToMap(company);
		resData.put("contacts", contants);
		return resData;
	}
	
	public List<Map<String, Object>> getAllCompanyContacts() {
		List<Company> companys = companyRepository.findAll();
		List<Map<String, Object>> resData = new LinkedList<Map<String,Object>>();
		for (Company company : companys) {
			List<Contact> contacts = contactRepository.findByCompanyId(company.getId());
			Map<String, Object> companyData = objectToMap(company);
			companyData.put("contacts", contacts);
			resData.add(companyData);
		}
		return resData;
	}
	
	public List<Map<String, Object>> getCompanyContacts() {
		List<Company> companys = companyRepository.findAll();
		List<Map<String, Object>> resData = new LinkedList<Map<String,Object>>();
		for (Company company : companys) {
			List<Map<String, Object>> contacts = contactRepository.getMapByCompanyId(company.getId());
			Map<String, Object> companyData = objectToMap(company);
			companyData.put("contacts", contacts);
			resData.add(companyData);
		}
		return resData;
	}
}
