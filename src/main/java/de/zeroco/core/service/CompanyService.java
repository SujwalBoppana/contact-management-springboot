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

import de.zeroco.core.base.ControlKit;
import de.zeroco.core.pojo.Company;
import de.zeroco.core.pojo.Contact;
import de.zeroco.core.repository.CompanyRepository;
import de.zeroco.core.repository.ContactRepository;
import de.zeroco.core.util.FieldValidation;
import de.zeroco.core.util.Utility;

@Component
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ContactRepository contactRepository;

	public Map<String, Object> createCompany(Map<String, Object> reqData) {
		ControlKit controlKit = new ControlKit(reqData);
		if (FieldValidation.required(reqData, "name", "email", "phone", "website", "code") == null) {
			if (Utility.isValidEmail(controlKit.param("email")) && Utility.isValidMoblieNo(controlKit.param("phone"))) {
				if (companyRepository.findByCode(controlKit.param("code")) == null) {
					Company company = new Company(controlKit.param("name"), controlKit.param("code"),
							controlKit.param("email"), controlKit.param("phone"), controlKit.param("website"),
							controlKit.param("address"));
					return Utility.objectToMap(companyRepository.save(company));
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
			if (FieldValidation.required(reqData, "name", "email", "phone", "website") == null) {
				ControlKit controlKit = new ControlKit(reqData);
				if (Utility.isValidEmail(controlKit.param("email"))
						&& Utility.isValidMoblieNo(controlKit.param("phone"))) {
					Company existingCompany = optionalCompany.get();
					existingCompany.setName(controlKit.param("name"));
					existingCompany.setEmail(controlKit.param("email"));
					existingCompany.setPhone(controlKit.param("phone"));
					existingCompany.setWebsite(controlKit.param("website"));
					existingCompany.setAddress(controlKit.param("address"));
					return Utility.objectToMap(companyRepository.save(existingCompany));
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
			resData.add(Utility.objectToMap(company));
		}
		return resData;
	}

	public Map<String, Object> getCompanyById(Integer id) {
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

	public void deleteCompany(Integer id) {
		if (companyRepository.findById(id).isPresent()) {
			companyRepository.delete(companyRepository.findById(id).get());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
		}
	}

	public Map<String, Object> findByCode(String code) {
		return Utility.objectToMap(companyRepository.findByCode(code));
	}

	public Map<String, Object> getCompanyContacts(String code) {
		Company company = companyRepository.findByCode(code);
		List<Contact> contants = contactRepository.findByCompanyId(company.getId());
		Map<String, Object> resData = Utility.objectToMap(company);
		resData.put("contacts", contants);
		return resData;
	}

	public List<Map<String, Object>> getAllCompanyContacts() {
		List<Company> companys = companyRepository.findAll();
		List<Map<String, Object>> resData = new LinkedList<Map<String, Object>>();
		for (Company company : companys) {
			List<Contact> contacts = contactRepository.findByCompanyId(company.getId());
			Map<String, Object> companyData = Utility.objectToMap(company);
			companyData.put("contacts", contacts);
			resData.add(companyData);
		}
		return resData;
	}

	public List<Map<String, Object>> getCompanyContacts() {
		List<Company> companys = companyRepository.findAll();
		List<Map<String, Object>> resData = new LinkedList<Map<String, Object>>();
		for (Company company : companys) {
			List<Map<String, Object>> contacts = contactRepository.getMapByCompanyId(company.getId());
			Map<String, Object> companyData = Utility.objectToMap(company);
			companyData.put("contacts", contacts);
			resData.add(companyData);
		}
		return resData;
	}
}
