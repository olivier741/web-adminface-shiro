/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service.repository;

import com.github.adminfaces.starter.model.Permission;
import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.model.RolePermissionRel;
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
public interface RolePermissionRelRepository extends EntityRepository<RolePermissionRel,Integer>{
    
    @Query("SELECT u FROM RolePermissionRel u WHERE u.permission.id = :perid")
    List <RolePermissionRel> getRolePermissionRelbyPermission(@QueryParam("perid") Long perid);
    
    @Query("SELECT u FROM RolePermissionRel u WHERE u.role.id = :roleid")
    List <RolePermissionRel> getRolePermissionRelbyRole(@QueryParam("roleid") Long roleid);
    
    @Query("SELECT u FROM RolePermissionRel u WHERE u.role.roleName = :roleName AND u.permission.permissionStr = :permissionName")
    List <RolePermissionRel> getbyRolePermission(@QueryParam("roleName") String roleName,@QueryParam("permissionName") String permissionName);
    
    @Query("SELECT u FROM RolePermissionRel u WHERE u.role.roleName = :roleName")
    List <RolePermissionRel> getRolePermissionbyRoleName(@QueryParam("roleName") String roleName);
    
    @Query(" SELECT rp.permission FROM RolePermissionRel rp \n" +
           " INNER JOIN rp.permission p \n" +
           " WHERE rp.role.roleName = :rolename")
     List<Permission> getPermissionByRole(@QueryParam("rolename") String rolename);
     
     @Query(" SELECT rp.role FROM RolePermissionRel rp \n" +
           " INNER JOIN rp.role r \n" +
           " WHERE rp.permission.permissionStr = :permissionname")
     List<Role> getRoleByPermission(@QueryParam("permissionname") String permissionname);
    
}
