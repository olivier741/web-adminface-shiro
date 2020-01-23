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
import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.model.Role_;
import com.github.adminfaces.starter.service.repository.RoleRepository;
import com.github.adminfaces.starter.service.repository.UserRoleRelRepository;
import com.github.adminfaces.template.exception.BusinessException;
import static com.github.adminfaces.template.util.Assert.has;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author olivier.tatsinkou
 */
@Stateless
public class RoleService extends CrudService<Role, Integer> implements Serializable{
    
 
    @Inject
    private RoleRepository roleRepository;
    
    @Inject
    private UserRoleRelRepository userroleRepository;
    
    @Inject
    @Service
    private CrudService<Role,Integer> crudRoleService;
    
    
     public void beforeInsert(Role role) {
        validate(role);
    }

    public void beforeUpdate(Role role) {
        validate(role);
    }
    
    public List<Role> listRole() {
        return roleRepository.getRole();
    }
    
     public List<Role> listRoleByUser(String username) {
        return userroleRepository.getRoleByUser(username);
    }
    
    
     
     
    
   public List<Role> listByRole(String rolename) {
        return criteria()
                .likeIgnoreCase(Role_.roleName, rolename)
                .getResultList();
    }
   
   public Role getByRole(String username) {
        return criteria()
                .eq(Role_.roleName, username)
                .getSingleResult();
    }
   
    public Role getOneByRole(String rolename) {
        if (StringUtils.isEmpty(rolename)) {
            throw new IllegalArgumentException("Role must not be null");
        }

        try {
            return roleRepository.getOneByRoleName(rolename);
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<String> getRole(String query) {
        return criteria()
                .select(String.class, attribute(Role_.roleName))
                .likeIgnoreCase(Role_.roleName, "%" + query + "%")
                .getResultList();
    }
    
    
  
    public void validate(Role role) {
        BusinessException be = new BusinessException();
        if (!role.hasRoleName()) {
            be.addException(new BusinessException("Role Value cannot be empty"));
        }
        
        if (count(criteria()
                .eqIgnoreCase(Role_.roleName, role.getRoleName())
                .notEq(Role_.id, role.getId())) > 0) {
            be.addException(new BusinessException("Role name must be unique"));
        }

        if (has(be.getExceptionList())) {
            throw be;
        }
    }
    
    
    
   public void addRole(Role role) throws EntityExistedException {
        
       if (role == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        try {
            
            Role duplicateRole = roleRepository.getOneByRoleName(role.getRoleName());
            if (duplicateRole != null) {
                throw new EntityExistedException();
            }
        } catch (NoResultException e) {}

       
        
        crudRoleService.insert(role);
    }
   
   
   public void UpdateRole(Role role) throws EntityNotExistedException {
        if (role == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        Role entity = null;
        try {
            
             entity = roleRepository.getOneByRoleName(role.getRoleName());
            
            if (entity == null) {
                throw new EntityNotExistedException();
            }
        } catch (NoResultException e) {}
       
        if (entity!=null) {
            crudRoleService.update(role);
        }
       
    }
}
