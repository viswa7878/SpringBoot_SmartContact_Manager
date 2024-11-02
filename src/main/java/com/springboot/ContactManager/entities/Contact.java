package com.springboot.ContactManager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int cid;
    private String firstname;
    private String secondname;
    private String work;
    private String email;
    private String phone;
    private String image;
    @Column(length=500000)
    private String description;
    
    @ManyToOne
    @JsonIgnore
    private Userr user;
    
    public Userr getUser() {
        return user;
    }
    public void setUser(Userr user) {
        this.user = user;
    }
    public Contact() {
        super();
        // TODO Auto-generated constructor stub
    }
    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }
    public String getFirstname() {  // Change this method name
        return firstname;
    }
    public void setFirstname(String firstname) {  // Change this method name
        this.firstname = firstname;
    }
    public String getSecondname() {
        return secondname;
    }
    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }
    @Override
    public String toString() {
        return "Contact [cid=" + cid + ", firstname=" + firstname + ", secondname=" + secondname + ", work=" + work
                + ", email=" + email + ", phone=" + phone + ", image=" + image + ", description=" + description
                + ", user=" + user + "]";
    }
    public String getWork() {
        return work;
    }
    public void setWork(String work) {
        this.work = work;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
 