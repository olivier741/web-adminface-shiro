/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service.repository;

import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.model.UserRoleRel;
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
public interface UserRoleRelRepository extends EntityRepository<UserRoleRel,Integer>{
    
    @Query("SELECT ur FROM UserRoleRel ur WHERE ur.role.id = :roleid")
    List <UserRoleRel> getUserRolebyRole(@QueryParam("roleid") Long roleid);
    
    @Query("SELECT ur FROM UserRoleRel ur WHERE ur.user.id = :userid")
    List <UserRoleRel> getUserRolebyUser(@QueryParam("userid") Long userid);
    
    @Query("SELECT ur FROM UserRoleRel ur WHERE ur.user.username = :username and ur.role.roleName = :roleName")
    List <UserRoleRel> getUserRolebyRoleUser(@QueryParam("username") String username,@QueryParam("roleName") String roleName);
    
    @Query("SELECT ur FROM UserRoleRel ur WHERE ur.user.username = :username ")
    List <UserRoleRel> getUserRolebyUser(@QueryParam("username") String username);
    
    @Query(" SELECT ur.role FROM UserRoleRel ur \n" +
           " INNER JOIN ur.role r \n" +
           " WHERE ur.user.username = :username ")
     List<Role> getRoleByUser(@QueryParam("username") String username);
     
     @Query(" SELECT ur.user FROM UserRoleRel ur \n" +
           " INNER JOIN ur.user u \n" +
           " WHERE ur.role.roleName = :rolename")
     List<User> getUserByRole(@QueryParam("rolename") String rolename);
    
}
