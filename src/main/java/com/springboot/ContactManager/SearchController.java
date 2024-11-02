package com.springboot.ContactManager;

import java.security.Principal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ContactManager.entities.Contact;
import com.springboot.ContactManager.entities.Userr;
import com.springboot.dao.ContactRepository;
import com.springboot.dao.UserRepository;

@RestController
public class SearchController {
	
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
   @GetMapping("/search/{query}")
	public ResponseEntity<?>  search(@PathVariable("query") String query,Principal principal)
    {
	System.out.println(query);
	Userr user=this.userRepository.getUserByUsername(principal.getName());
	List<Contact> contacts=this.contactRepository.findByFirstnameContainingAndUser(query, user);
	return ResponseEntity.ok(contacts);
   }  

}   
   
