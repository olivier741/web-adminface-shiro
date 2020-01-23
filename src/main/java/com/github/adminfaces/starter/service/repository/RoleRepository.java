/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service.repository;

import com.github.adminfaces.starter.model.Permission;
import com.github.adminfaces.starter.model.Role;
import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author olivier
 */
@Repository
public interface RoleRepository extends EntityRepository<Role,Integer>{
    
     @Query("SELECT u FROM Role u WHERE u.roleName = :rolename")
     Role getOneByRoleName(@QueryParam("rolename") String rolename);
     
     @Query("SELECT r FROM Role r ")
     List<Role> getRole();
     
    
    
}
