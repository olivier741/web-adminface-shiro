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
import com.github.adminfaces.starter.model.RolePermissionRel;
import com.github.adminfaces.starter.model.RolePermissionRel_;
import com.github.adminfaces.starter.service.repository.RolePermissionRelRepository;
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
public class RolePermissionRelService extends CrudService<RolePermissionRel, Integer> implements Serializable{
    
    @Inject
    private RolePermissionRelRepository rolepermRepository;
    
    @Inject
    @Service
    private CrudService<RolePermissionRel,Integer> crudRolePermService;
      
    
    
     public RolePermissionRel getRolePermissionRel(String rolename, String permName){ 
        List<RolePermissionRel> listRolePermissionRel = rolepermRepository.getbyRolePermission(rolename, permName);
        if (!listRolePermissionRel.isEmpty()){
            return listRolePermissionRel.get(0);
        }else return null;
    }
    
    
     public void beforeInsert(RolePermissionRel perm) {
        validate(perm);
    }

    public void beforeUpdate(RolePermissionRel perm) {
        validate(perm);
    }
    
    public boolean deleteRolePermissionRelByPermission(Long id){
        boolean result = false;
        List<RolePermissionRel> listRolePermissionRel = rolepermRepository.getRolePermissionRelbyPermission(id);
        
        if (listRolePermissionRel!=null){
            crudRolePermService.remove(listRolePermissionRel);        
            result = true;
        }
        
        return result;
    }    
    
    
    public boolean deleteRolePermissionRelByRole(Long id){
        boolean result = false;
        List<RolePermissionRel> listRolePermissionRel = rolepermRepository.getRolePermissionRelbyRole(id);
        
        if (listRolePermissionRel!=null){
            crudRolePermService.remove(listRolePermissionRel);        
            result = true;
        }
        
        return result;
    }    
    
    public void validate(RolePermissionRel perm) {
        BusinessException be = new BusinessException();
        if (!perm.hasvalue()) {
            be.addException(new BusinessException("RolePermissionRel Value cannot be empty"));
        }
        

        if (count(criteria().eq(RolePermissionRel_.role, perm.getRole())
                            .eq(RolePermissionRel_.permission, perm.getPermission())
                ) > 0) {

            be.addException(new BusinessException("RolePermissionRel name must be unique"));
        }

        if (has(be.getExceptionList())) {
            throw be;
        }
    }
    
    
     public void addRolePermissionRel(RolePermissionRel roleperm) throws EntityExistedException {
        
       if (roleperm == null) {
            throw new IllegalArgumentException("RolePermissionRel must not be null");
        }

        try {
            
            List <RolePermissionRel> duplicaterolePerm = rolepermRepository.getbyRolePermission(roleperm.getRole().getRoleName(), roleperm.getPermission().getPermissionStr());
            if (!duplicaterolePerm.isEmpty() ) {
                throw new EntityExistedException();
            }
        } catch (NoResultException e) {
        } catch (NonUniqueResultException nure) {
        // Code for handling NonUniqueResultException
       }

       
        
        crudRolePermService.insert(roleperm);
    }
   
   
   public void UpdateUserRoleRel(RolePermissionRel roleperm) throws EntityNotExistedException {
        if (roleperm == null) {
            throw new IllegalArgumentException("RolePermissionRel must not be null");
        }

        List <RolePermissionRel> entitys = null;
        try {
            
             entitys =  rolepermRepository.getbyRolePermission(roleperm.getRole().getRoleName(), roleperm.getPermission().getPermissionStr());
            
            if (!entitys.isEmpty()) {
                throw new EntityNotExistedException();
            }
        } catch (NoResultException e) {
        } catch (NonUniqueResultException nure) {
        // Code for handling NonUniqueResultException
       }
       
        if (!entitys.isEmpty()) {
            crudRolePermService.update(roleperm);
        }
       
    }
    
}
