/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.model;


import com.github.adminfaces.persistence.model.BaseEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CollectionTable;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private boolean islock;
    
    @Column(nullable = false)
    private boolean isReset;
   
    @Column(nullable = false)
    private String salt;
    
    @Column(nullable = true)
    private String firstname;
    
    @Column(nullable = true)
    private String lastname;
    
    @Column(nullable = true)
    private String CNI;

    @Column(nullable = true)
    private Integer age;

    @Column(nullable = true)
    private String street;

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String postalCode;

    @Column(nullable = true)
    private String info;

    @Column(nullable = true)
    private String sexe;
    
    @Column(nullable = true)
    private Timestamp  create_date;
    
    @Column(nullable = true)
    private Timestamp  last_update;
    
     @ElementCollection
        @CollectionTable(
        name = "LIST_PHONE_NUMBER",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "PHONE_NUMBER")
    protected List<Phone> listphone_number = new ArrayList<Phone>();
    
    @ElementCollection
        @CollectionTable(
        name = "LIST_EMAIL",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "EMAIL")
    protected List<Email> listEmail = new ArrayList<Email>();
    
   
    @OneToMany(mappedBy = "user")
    private Set<UserRoleRel> userRoleRels = new HashSet<>(0);
    

    public User() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public boolean isIslock() {
        return islock;
    }

    public boolean isIsReset() {
        return isReset;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCNI() {
        return CNI;
    }


    public Integer getAge() {
        return age;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getInfo() {
        return info;
    }

    public String getSexe() {
        return sexe;
    }

    public Timestamp getCreate_date() {
        return create_date;
    }

    public Timestamp getLast_update() {
        return last_update;
    }

    public List<Phone> getListphone_number() {
        return listphone_number;
    }

    public List<Email> getListEmail() {
        return listEmail;
    }

    public Set<UserRoleRel> getUserRoleRels() {
        return userRoleRels;
    }
    
    

  
    public void setIslock(boolean islock) {
        this.islock = islock;
    }

    public void setIsReset(boolean isReset) {
        this.isReset = isReset;
    }
    

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setCNI(String CNI) {
        this.CNI = CNI;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setCreate_date(Timestamp create_date) {
        this.create_date = create_date;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setListphone_number(List<Phone> listphone_number) {
        this.listphone_number = listphone_number;
    }

    public void setListEmail(List<Email> listEmail) {
        this.listEmail = listEmail;
    }

    public void setUserRoleRels(Set<UserRoleRel> userRoleRels) {
        this.userRoleRels = userRoleRels;
    }

    
 
    
}
