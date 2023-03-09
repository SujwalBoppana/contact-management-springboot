package de.zeroco.core.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.zeroco.core.service.CompanyService;

@RestController
public class CompanyController {

	@Autowired
	private CompanyService service;

	@PostMapping("/company/save")
	public ResponseEntity<Map<String, Object>> insert(@RequestBody Map<String, Object> reqData) {
		return ResponseEntity.ok(service.createCompany(reqData));
	}

	@PutMapping("/company/update/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable int id, @RequestBody Map<String, Object> reqData) {
		return ResponseEntity.ok(service.updateCompany(id, reqData));
	}

	@GetMapping("/company/get/{id}")
	public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.getCompanyById(id));
	}
	
	@GetMapping("/company/list")
	public ResponseEntity<List<Map<String, Object>>> list() {
		return ResponseEntity.ok(service.getAllCompanyContacts());
	}
	
	@DeleteMapping("/company/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		service.deleteCompany(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}


	@GetMapping("/company/details")
	public ResponseEntity<List<Map<String, Object>>> companyList() {
		return ResponseEntity.ok(service.getAllCompanies());
	}

	

	@GetMapping("/company/contacts/{code}")
	public ResponseEntity<Map<String, Object>> getAllContantsOfCompany(@PathVariable String code) {
		return ResponseEntity.ok(service.getCompanyContacts(code));
	}

	@GetMapping("/company/getbycode/{code}")
	public ResponseEntity<Map<String, Object>> getByCode(@PathVariable String code) {
		return ResponseEntity.ok(service.findByCode(code));
	}
	
	@GetMapping("/company/test")
	public ResponseEntity<List<Map<String, Object>>> test() {
		return ResponseEntity.ok(service.getCompanyContacts());
	}
}
