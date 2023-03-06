package de.zeroco.core.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import de.zeroco.core.dao.CompanyRepository;
import de.zeroco.core.dao.ContactRepository;
import de.zeroco.core.pojo.Company;
import de.zeroco.core.pojo.Contact;
import de.zeroco.core.util.FieldValidation;
import de.zeroco.core.util.Utility;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private CompanyRepository companyRepository;

	private Map<String, Object> reqData;

	public Map<String, Object> createContact(Map<String, Object> reqData) {
		this.reqData = reqData;
		Company company = companyRepository.findByCode(param("code"));
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
		else {
			if (FieldValidation.required(reqData, "name", "email", "phone", "dob","gender","code") == null) {
				if (Utility.isValidEmail(param("email")) && Utility.isValidMoblieNo(param("phone"))) {
			Contact contact = new Contact(param("name"), param("email"), param("phone"),
					Utility.stringToDate(param("dob")), Utility.ageCalculator(param("dob")), param("address"),
					param("gender"), company);
			return objectToMap(contactRepository.save(contact));
				}else 
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Enter a valid Email or phone Number");
			}else
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Required fields are missing");
		}
	}

	public Map<String, Object> getContactById(Integer id) {
		Optional<Contact> optionalContact = contactRepository.findById(id);
		if (optionalContact.isPresent()) {
			Contact contact = optionalContact.get();
			return objectToMap(contact);
		}
		throw new EntityNotFoundException("Contact with id " + id + " not found");
	}

	public List<Map<String, Object>> getAllContacts() {
		List<Map<String, Object>> resData = new LinkedList<Map<String, Object>>();
		for (Contact contact : contactRepository.findAll()) {
			resData.add(objectToMap(contact));
		}
		return resData;
	}

	public List<Map<String, Object>> getContactsByCompanyId(Integer id) {
		List<Contact> contacts = contactRepository.findByCompanyId(id);
		List<Map<String, Object>> resData = new LinkedList<Map<String, Object>>();
		for (Contact contact : contacts) {
			Company company = contact.getCompany();
			if (company != null) {
				company = companyRepository.findById(company.getId()).orElse(null);
				contact.setCompany(company);
				resData.add(objectToMap(contact));
			}
		}
		return resData;
	}

	public Map<String, Object> updateContact(Integer id, Map<String, Object> reqData) {
		Optional<Contact> optionalContact = contactRepository.findById(id);
		if (optionalContact.isPresent()) {
			this.reqData = reqData;
			if (FieldValidation.required(reqData, "name", "email", "phone", "dob", "gender","code") == null) {
				if (Utility.isValidEmail(param("email")) && Utility.isValidMoblieNo(param("phone"))) {
					Contact contact = optionalContact.get();
					Company company = companyRepository.findByCode(param("code"));
					contact.setName(param("name"));
					contact.setEmail(param("email"));
					contact.setPhone(param("phone"));
					contact.setDob(Utility.stringToDate(param("dob")));
					contact.setAddress(param("address"));
					contact.setGender(param("gender"));
					contact.setCompany(company);
					contact.setAge(Utility.ageCalculator(param("dob")));
					return objectToMap(contactRepository.save(contact));
				}else 
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Enter a valid Email or phone Number");
			}else 
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Required fields are missing");
		}
		throw new EntityNotFoundException("Contact with id " + id + " not found");
	}

	public void deleteContact(Integer id) {
		Optional<Contact> optionalContact = contactRepository.findById(id);
		if (optionalContact.isPresent()) {
			contactRepository.delete(optionalContact.get());
		} else {
			throw new EntityNotFoundException("Contact with id " + id + " not found");
		}
	}

	public List<Contact> sortList(Map<String, Object> reqData) {
		this.reqData = reqData;
		int age = Integer.parseInt(param("age"));
		if (param("type").equalsIgnoreCase("equal")) {
			return contactRepository.findByAge(age);
		} else if (param("order").equalsIgnoreCase("desc")) {
			if (param("type").equalsIgnoreCase("greaterthanorequal")) return contactRepository.findByAgeGreaterThanEqualOrderByAgeDesc(age);
			else if (param("type").equalsIgnoreCase("greaterthan")) return contactRepository.findByAgeGreaterThanOrderByAgeDesc(age);
			else if (param("type").equalsIgnoreCase("lessthanorequal")) return contactRepository.findByAgeLessThanEqualOrderByAgeDesc(age);
			else if (param("type").equalsIgnoreCase("lessthan")) return contactRepository.findByAgeLessThanOrderByAgeDesc(age);
		} else if (param("order").equalsIgnoreCase("asc")) {
			if (param("type").equalsIgnoreCase("lessthanorequal")) return contactRepository.findByAgeLessThanEqualOrderByAgeAsc(age);
			else if (param("type").equalsIgnoreCase("lessthan")) return contactRepository.findByAgeLessThanOrderByAgeAsc(age);
			else if (param("type").equalsIgnoreCase("greaterthanorequal")) return contactRepository.findByAgeGreaterThanEqualOrderByAgeAsc(age);
			else if (param("type").equalsIgnoreCase("greaterthan")) return contactRepository.findByAgeGreaterThanOrderByAgeAsc(age);
		}
		return null;
	}

	public List<Map<String, Object>> sorting(Map<String, Object> reqData) {
		List<Map<String, Object>> resData = new LinkedList<Map<String, Object>>();
		for (Contact contact : sortList(reqData)) {
			resData.add(objectToMap(contact));
		}
		return resData;
	}

	public String param(String key) {
		Object object = reqData.get(key);
		return object == null ? null : object.toString().trim();
	}

	public Map<String, Object> objectToMap(Contact contact) {
		if (contact == null)
			return null;
		Map<String, Object> resData = new LinkedHashMap<String, Object>();
		resData.put("company", contact.getCompany());
		resData.put("id", contact.getId());
		resData.put("name", contact.getName());
		resData.put("email", contact.getEmail());
		resData.put("phone", contact.getPhone());
		resData.put("address", contact.getAddress());
		resData.put("dob", contact.getDob());
		resData.put("age", contact.getAge());
		resData.put("gender", contact.getGender());
		return resData;
	}

}
