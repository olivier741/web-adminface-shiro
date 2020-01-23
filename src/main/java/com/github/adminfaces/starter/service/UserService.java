/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service;

/**
 *
 * @author olivier
 */
import com.github.adminfaces.starter.service.repository.UserRepository;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import com.github.adminfaces.starter.infra.security.exception.EntityExistedException;
import com.github.adminfaces.starter.infra.security.exception.EntityNotExistedException;
import com.github.adminfaces.starter.model.RolePermissionRel;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.model.UserRoleRel;
import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.model.User_;
import com.github.adminfaces.starter.service.repository.UserRoleRelRepository;
import com.github.adminfaces.starter.util.CredentialEncrypter;
import com.github.adminfaces.starter.util.PasswordGenerators;
import com.github.adminfaces.starter.util.TwoTuple;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;



@Stateless
public class UserService extends CrudService<User, Integer> implements Serializable {

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private UserRoleRelRepository userRoleRepository;
    
    @Inject
    @Service
    private CrudService<User,Integer> crudUserService;

    public User getOneByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username must not be null");
        }

        try {
            return userRepository.getOneByUsername(username);
        } catch (NoResultException e) {
            return null;
        }
    }
    

//    public Set<String> listRolesByUsername(String username) {
//        if (StringUtils.isEmpty(username)) {
//            throw new IllegalArgumentException("username must not be null");
//        }
//
//        User user = userRepository.getOneByUsername(username);
//        Set<String> roles = new HashSet<>();
//        if (user != null && user.getUserRoleRels() != null) {
//            for (UserRoleRel rel : user.getUserRoleRels()) {
//                roles.add(rel.getRole().getRoleName());
//            }
//        }
//
//        return roles;
//    }
    
    
    
     public Set<String> listRolesByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username must not be null");
        }

        Set<String> roles = new HashSet<>();
        List<Role> listrole = userRoleRepository.getRoleByUser(username);
        if (!listrole.isEmpty()) {
            for (Role rel : listrole) {
                roles.add(rel.getRoleName());
            }
        }

        return roles;
    }

    public Set<String> listPermissionsByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username must not be null");
        }

        User user = userRepository.getOneByUsername(username);
        Set<String> permissions = new HashSet<>();
        if (user != null && user.getUserRoleRels() != null) {
            for (UserRoleRel rel : user.getUserRoleRels()) {
                for (RolePermissionRel permRel : rel.getRole().getRolePermissionRels()) {
                      permissions.add(permRel.getPermission().getPermissionStr().trim());                  
                }
            }
        }

        return permissions;
    }
    
    
     public void addUser(User user) throws EntityExistedException {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        try {
            
            User duplicateUser = userRepository.getOneByUsername(user.getUsername());
            if (duplicateUser != null) {
                throw new EntityExistedException();
            }
        } catch (NoResultException e) {}

        if (user.getPassword()== null){
            String passwod = PasswordGenerators.generateRandomPassword();
            user.setPassword(passwod);
            System.out.println("generated password of user = "+user.getUsername()+" is :  password = "+passwod);
        }
        
        TwoTuple<String, String> hashAndSalt = CredentialEncrypter.saltedHash(user.getPassword());
        java.sql.Timestamp date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

        user.setPassword(hashAndSalt.t1);
        user.setSalt(hashAndSalt.t2);        
        user.setCreate_date(date);
        
        crudUserService.insert(user);
    }
    
    public void UpdateUser(User user) throws EntityNotExistedException {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        User entity = null;
        try {
            
             entity = userRepository.getOneByUsername(user.getUsername());
            
            if (entity == null) {
                throw new EntityNotExistedException();
            }
        } catch (NoResultException e) {}
       
        if (entity!=null) {
            if (!user.isIsReset()){
                 String passwod = PasswordGenerators.generateRandomPassword();
               
                 TwoTuple<String, String> hashAndSalt = CredentialEncrypter.saltedHash(passwod);
                 user.setPassword(hashAndSalt.t1);
                 user.setSalt(hashAndSalt.t2);   
                 System.out.println("generated password of user = "+user.getUsername()+" is :  password = "+passwod);
            }
            java.sql.Timestamp date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            user.setLast_update(date);
            crudUserService.update(user);
        }
       
    }
    
    
    public void changePassword(User user) throws EntityNotExistedException {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        User entity = null;
        try {
            
             entity = userRepository.getOneByUsername(user.getUsername());
            
            if (entity == null) {
                throw new EntityNotExistedException();
            }
        } catch (NoResultException e) {}
       
        if (entity!=null) {
            TwoTuple<String, String> hashAndSalt = CredentialEncrypter.saltedHash(user.getPassword());
            java.sql.Timestamp date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            user.setPassword(hashAndSalt.t1);
            user.setSalt(hashAndSalt.t2);
            user.setLast_update(date);
            crudUserService.update(user);
        }
       
    }
    
     public List<String> getUsers(String query) {
        return criteria()
                .select(String.class, attribute(User_.username))
                .likeIgnoreCase(User_.username, "%" + query + "%")
                .getResultList();
    }
     
     

}
