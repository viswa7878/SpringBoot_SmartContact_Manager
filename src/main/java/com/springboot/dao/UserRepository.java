package com.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springboot.ContactManager.entities.Userr;

public interface UserRepository extends JpaRepository<Userr,Integer>{

	@Query("select u from Userr u where u.email=:email")
	public Userr getUserByUsername(@Param("email") String username);
}



