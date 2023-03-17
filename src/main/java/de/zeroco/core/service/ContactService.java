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

import de.zeroco.core.base.ControlKit;
import de.zeroco.core.pojo.Company;
import de.zeroco.core.pojo.Contact;
import de.zeroco.core.repository.CompanyRepository;
import de.zeroco.core.repository.ContactRepository;
import de.zeroco.core.util.FieldValidation;
import de.zeroco.core.util.Utility;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public Map<String, Object> createContact(Map<String, Object> reqData) {
		ControlKit controlKit = new ControlKit(reqData);
		Company company = companyRepository.findByCode(controlKit.param("code"));
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
		else {
			if (FieldValidation.required(reqData, "name", "email", "phone", "dob", "gender", "code") == null) {
				if (Utility.isValidEmail(controlKit.param("email"))
						&& Utility.isValidMoblieNo(controlKit.param("phone"))) {
					Contact contact = new Contact(controlKit.param("name"), controlKit.param("email"),
							controlKit.param("phone"), Utility.stringToDate(controlKit.param("dob")),
							Utility.ageCalculator(controlKit.param("dob")), controlKit.param("address"),
							controlKit.param("gender"), company);
					return Utility.objectToMap(contactRepository.save(contact));
				} else
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Enter a valid Email or phone Number");
			} else
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Required fields are missing");
		}
	}

	public Map<String, Object> getContactById(Integer id) {
		Optional<Contact> optionalContact = contactRepository.findById(id);
		if (optionalContact.isPresent()) {
			Contact contact = optionalContact.get();
			return Utility.objectToMap(contact);
		}
		throw new EntityNotFoundException("Contact with id " + id + " not found");
	}

	public List<Map<String, Object>> getAllContacts() {
		List<Map<String, Object>> resData = new LinkedList<Map<String, Object>>();
		for (Contact contact : contactRepository.findAll()) {
			resData.add(Utility.objectToMap(contact));
		}
		return resData;
	}

	public Map<String, Object> getContactsByCompanyId(Integer id) {
		Optional<Company> optionalCompany = companyRepository.findById(id);
		if (optionalCompany.isPresent()) {
			Company company = optionalCompany.get();
			List<Contact> contacts = contactRepository.findByCompanyId(id);
			Map<String, Object> resData = new LinkedHashMap<String, Object>();
			resData.put("company", Utility.objectToMap(company));
			resData.put("contacts", contacts);
			return resData;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company id is not present!. please try again");
		}
	}

	public Map<String, Object> updateContact(Integer id, Map<String, Object> reqData) {
		Optional<Contact> optionalContact = contactRepository.findById(id);
		ControlKit controlKit = new ControlKit(reqData);
		if (optionalContact.isPresent()) {
			if (FieldValidation.required(reqData, "name", "email", "phone", "dob", "gender", "code") == null) {
				if (Utility.isValidEmail(controlKit.param("email"))
						&& Utility.isValidMoblieNo(controlKit.param("phone"))) {
					Contact contact = optionalContact.get();
					Company company = companyRepository.findByCode(controlKit.param("code"));
					contact.setName(controlKit.param("name"));
					contact.setEmail(controlKit.param("email"));
					contact.setPhone(controlKit.param("phone"));
					contact.setDob(Utility.stringToDate(controlKit.param("dob")));
					contact.setAddress(controlKit.param("address"));
					contact.setGender(controlKit.param("gender"));
					contact.setCompany(company);
					contact.setAge(Utility.ageCalculator(controlKit.param("dob")));
					return Utility.objectToMap(contactRepository.save(contact));
				} else
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Enter a valid Email or phone Number");
			} else
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
		ControlKit controlKit = new ControlKit(reqData);
		int age = Integer.parseInt(controlKit.param("age"));
		String type = controlKit.param("type");
		String order = controlKit.param("order");
		if (type.equalsIgnoreCase("equal")) {
			return contactRepository.findByAge(age);
		} else if (order.equalsIgnoreCase("desc")) {
			if (type.equalsIgnoreCase("greaterthanorequal"))
				return contactRepository.findByAgeGreaterThanEqualOrderByAgeDesc(age);
			else if (type.equalsIgnoreCase("greaterthan"))
				return contactRepository.findByAgeGreaterThanOrderByAgeDesc(age);
			else if (type.equalsIgnoreCase("lessthanorequal"))
				return contactRepository.findByAgeLessThanEqualOrderByAgeDesc(age);
			else if (type.equalsIgnoreCase("lessthan"))
				return contactRepository.findByAgeLessThanOrderByAgeDesc(age);
		} else if (order.equalsIgnoreCase("asc")) {
			if (type.equalsIgnoreCase("lessthanorequal"))
				return contactRepository.findByAgeLessThanEqualOrderByAgeAsc(age);
			else if (type.equalsIgnoreCase("lessthan"))
				return contactRepository.findByAgeLessThanOrderByAgeAsc(age);
			else if (type.equalsIgnoreCase("greaterthanorequal"))
				return contactRepository.findByAgeGreaterThanEqualOrderByAgeAsc(age);
			else if (type.equalsIgnoreCase("greaterthan"))
				return contactRepository.findByAgeGreaterThanOrderByAgeAsc(age);
		}
		return null;
	}

	public List<Map<String, Object>> sorting(Map<String, Object> reqData) {
		List<Map<String, Object>> resData = new LinkedList<Map<String, Object>>();
		for (Contact contact : sortList(reqData)) {
			resData.add(Utility.objectToMap(contact));
		}
		return resData;
	}
}
