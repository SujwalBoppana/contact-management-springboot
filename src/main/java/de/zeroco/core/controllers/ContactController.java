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

import de.zeroco.core.pojo.Contact;
import de.zeroco.core.service.ContactService;

@RestController
public class ContactController {

	@Autowired
	private ContactService contactService;

	@PostMapping("/contact/save")
	public ResponseEntity<Map<String, Object>> insert(@RequestBody Map<String, Object> reqData) {
		return ResponseEntity.ok(contactService.createContact(reqData));
	}

	@GetMapping("/contact/list")
	public ResponseEntity<List<Map<String, Object>>> getAll() {
		List<Map<String, Object>> contacts = contactService.getAllContacts();
		return ResponseEntity.ok(contacts);
	}

	@PutMapping("/contact/update/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable int id, @RequestBody Map<String, Object> reqData) {
		Map<String, Object> updatedContact = contactService.updateContact(id, reqData);
		return new ResponseEntity<>(updatedContact, HttpStatus.OK);
	}

	@DeleteMapping("/contact/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		contactService.deleteContact(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/contact/companyid/{id}")
	public ResponseEntity<List<Map<String, Object>>> getContactsByCompanyId(@PathVariable int id) {
		List<Map<String, Object>> contacts = contactService.getContactsByCompanyId(id);
		return new ResponseEntity<>(contacts, HttpStatus.OK);
	}

	@PutMapping("/contact/sort")
	public ResponseEntity<List<Map<String, Object>>> sorting(@RequestBody Map<String, Object> reqData) {
		List<Map<String, Object>> contacts = contactService.sorting(reqData);
		return new ResponseEntity<>(contacts, HttpStatus.OK);
	}
}
