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
import com.github.adminfaces.starter.model.Permission;
import com.github.adminfaces.starter.model.Permission_;
import com.github.adminfaces.starter.service.repository.PermissionRepository;
import com.github.adminfaces.starter.service.repository.RolePermissionRelRepository;
import com.github.adminfaces.template.exception.BusinessException;
import static com.github.adminfaces.template.util.Assert.has;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author olivier.tatsinkou
 */
@Stateless
public class PermissionService extends CrudService<Permission, Integer> implements Serializable {
    
    @Inject
    private PermissionRepository permRepository;
    
    @Inject
    private RolePermissionRelRepository rolepermRepository;
    
    @Inject
    @Service
    private CrudService<Permission,Integer> crudPermService;
     
     
     public void beforeInsert(Permission perm) {
        validate(perm);
    }

    public void beforeUpdate(Permission perm) {
        validate(perm);
    }
    
    
     public Permission getOneByPermission(String permission) {
        if (StringUtils.isEmpty(permission)) {
            throw new IllegalArgumentException("permission must not be null");
        }
        try {
            return permRepository.getOneByPermission(permission);
        } catch (NoResultException e) {
            return null;
        }
    }
    
    
    public List<Permission> getFindByPermission(String permission) {
        if (StringUtils.isEmpty(permission)) {
            throw new IllegalArgumentException("permission must not be null");
        }

        try {
            return permRepository.getFindByPermission(permission);
        } catch (NoResultException e) {
            return null;
        }
    }
    
    
    public List<Permission> listPermision() {
        return permRepository.getPermission();
    }
    
     public List<Permission> listPermisionByRole(String role) {
        return rolepermRepository.getPermissionByRole(role);
    }
    
    
   public List<Permission> listByPermisionStr(String permision_name) {
        return criteria()
                .likeIgnoreCase(Permission_.permissionStr, permision_name)
                .getResultList();
    }
   

    public List<String> getPermisionStr(String query) {
        return criteria()
                .select(String.class, attribute(Permission_.permissionStr))
                .likeIgnoreCase(Permission_.permissionStr, "%" + query + "%")
                .getResultList();
    }
    
    public void validate(Permission perm) {
        BusinessException be = new BusinessException();
        if (!perm.hasPermissionStr()) {
            be.addException(new BusinessException("Permission Value cannot be empty"));
        }
        

        if (count(criteria()
                .eqIgnoreCase(Permission_.permissionStr, perm.getPermissionStr())
                .notEq(Permission_.id, perm.getId())) > 0) {

            be.addException(new BusinessException("Permission name must be unique"));
        }

        if (has(be.getExceptionList())) {
            throw be;
        }
    }
   
    
       
   public void addPermission(Permission perm) throws EntityExistedException {
        
       if (perm == null) {
            throw new IllegalArgumentException("Permission must not be null");
        }

        try {
            
            Permission duplicatePerm = permRepository.getOneByPermission(perm.getPermissionStr());
            if (duplicatePerm != null) {
                throw new EntityExistedException();
            }
        } catch (NoResultException e) {
            
        } catch (NonUniqueResultException nure) {
        // Code for handling NonUniqueResultException
       }

       
        
        crudPermService.insert(perm);
    }
   
   
   public void UpdatePermission(Permission perm) throws EntityNotExistedException {
        if (perm == null) {
            throw new IllegalArgumentException("Permission must not be null");
        }

        Permission entity = null;
        try {
            
             entity = permRepository.getOneByPermission(perm.getPermissionStr());
            
            if (entity == null) {
                throw new EntityNotExistedException();
            }
        } catch (NoResultException e) {
            
         } catch (NonUniqueResultException nure) {
        // Code for handling NonUniqueResultException
       }
       
        if (entity!=null) {
            crudPermService.update(perm);
        }
       
    }
    

}
