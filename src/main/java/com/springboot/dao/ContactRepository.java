package com.springboot.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springboot.ContactManager.entities.Contact;
import com.springboot.ContactManager.entities.Userr;

public interface ContactRepository extends JpaRepository<Contact,Integer>{

	
	//Pageable contain two parameters,
	//pageno and total to display in page
		@Query("from Contact as c where c.user.id=:userid")
		public Page<Contact> findContactsByUser(@Param("userid") int id, Pageable pageable);
		
		
		public List<Contact> findByFirstnameContainingAndUser(String firstname, Userr user);
		                     

	 
}
 