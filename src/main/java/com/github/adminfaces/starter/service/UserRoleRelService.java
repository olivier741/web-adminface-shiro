/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service;

import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import com.github.adminfaces.starter.infra.security.exception.EntityExistedException;
import com.github.adminfaces.starter.infra.security.exception.EntityNotExistedException;
import com.github.adminfaces.starter.model.UserRoleRel;
import com.github.adminfaces.starter.model.UserRoleRel_;
import com.github.adminfaces.starter.service.repository.UserRoleRelRepository;
import com.github.adminfaces.template.exception.BusinessException;
import static com.github.adminfaces.template.util.Assert.has;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

/**
 *
 * @author olivier
 */
@Stateless
public class UserRoleRelService extends CrudService<UserRoleRel, Integer> implements Serializable{
    
    @Inject
    private UserRoleRelRepository userroleRepository;
    
    @Inject
    @Service
    private CrudService<UserRoleRel,Integer> crudUserRoleService;
    
    
     public void beforeInsert(UserRoleRel userRole) {
        validate(userRole);
    }

    public void beforeUpdate(UserRoleRel userRole) {
        validate(userRole);
    }
    
    
    public UserRoleRel getUserRoleRel(String username, String rolename){ 
        List<UserRoleRel> listUserRoleRel = userroleRepository.getUserRolebyRoleUser(username, rolename);
        if (!listUserRoleRel.isEmpty()){
            return listUserRoleRel.get(0);
        }else return null;
    }
    
     public boolean deleteUserRoleRelByRole(Long id){
        boolean result = false;
        List<UserRoleRel> listUserRoleRel = userroleRepository.getUserRolebyRole(id);
        
        if (listUserRoleRel!=null){
            crudUserRoleService.remove(listUserRoleRel);        
            result = true;
        }
        
        return result;
    }    
    
    
    public boolean deleteUserRoleRelByUser(Long id){
         boolean result = false;
        List<UserRoleRel> listUserRoleRel = userroleRepository.getUserRolebyUser(id);
        
        if (listUserRoleRel!=null){
            crudUserRoleService.remove(listUserRoleRel);        
            result = true;
        }
        
        return result;
    }   
    
    
    public void validate(UserRoleRel userRole) {
        BusinessException be = new BusinessException();
        if (!userRole.hasvalue()) {
            be.addException(new BusinessException("UserRoleRel Value cannot be empty"));
        }
        

        if (count(criteria().eq(UserRoleRel_.role, userRole.getRole())
                            .eq(UserRoleRel_.user, userRole.getUser())
                ) > 0) {

            be.addException(new BusinessException("UserRoleRel name must be unique"));
        }

        if (has(be.getExceptionList())) {
            throw be;
        }
    }
 
    
     public void addUserRoleRel(UserRoleRel userRole) throws EntityExistedException {
        
       if (userRole == null) {
            throw new IllegalArgumentException("userRole must not be null");
        }

        try {
            
            List <UserRoleRel> duplicatePerm = userroleRepository.getUserRolebyRoleUser(userRole.getUser().getUsername(), userRole.getRole().getRoleName());
            if (!duplicatePerm.isEmpty() ) {
                throw new EntityExistedException();
            }
        } catch (NoResultException e) {
        } catch (NonUniqueResultException nure) {
        // Code for handling NonUniqueResultException
       }

       
        
        crudUserRoleService.insert(userRole);
    }
   
   
   public void UpdateUserRoleRel(UserRoleRel userRole) throws EntityNotExistedException {
        if (userRole == null) {
            throw new IllegalArgumentException("Permission must not be null");
        }

        List <UserRoleRel> entitys = null;
        try {
            
             entitys = userroleRepository.getUserRolebyRoleUser(userRole.getUser().getUsername(), userRole.getRole().getRoleName());
            
            if (!entitys.isEmpty()) {
                throw new EntityNotExistedException();
            }
        } catch (NoResultException e) {
        } catch (NonUniqueResultException nure) {
        // Code for handling NonUniqueResultException
       }
       
        if (!entitys.isEmpty()) {
            crudUserRoleService.update(userRole);
        }
       
    }
    
    
    
}
