/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service.repository;

import com.github.adminfaces.starter.model.Permission;
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
public interface PermissionRepository extends EntityRepository<Permission,Integer>{
     
    @Query("SELECT u FROM Permission u WHERE u.permissionStr =:permname")
    Permission getOneByPermission(@QueryParam("permname") String perm_name);
     
     
     @Query("SELECT u FROM Permission u WHERE u.permissionStr LIKE  '%:permname%' ")
     List<Permission> getFindByPermission(@QueryParam("permname") String perm_name);
     
     
     
     @Query("SELECT p FROM Permission p")
     List<Permission>  getPermission();
     
    
}
