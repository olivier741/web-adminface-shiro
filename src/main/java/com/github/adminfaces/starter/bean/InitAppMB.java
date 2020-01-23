package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.model.Permission;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.UserService;
import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.model.RolePermissionRel;
import com.github.adminfaces.starter.model.UserRoleRel;
import com.github.adminfaces.starter.service.PermissionService;
import com.github.adminfaces.starter.service.RolePermissionRelService;
import com.github.adminfaces.starter.service.RoleService;
import com.github.adminfaces.starter.service.UserRoleRelService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.Serializable;

@Singleton
@Startup
public class InitAppMB implements Serializable {

   
    @Inject
    private UserService userService;
    
    @Inject
    private RoleService roleService;
    
    @Inject
    private PermissionService permService;
    
    @Inject
    private RolePermissionRelService rolepermService;
     
    @Inject
    private UserRoleRelService userRoleService;

    @PostConstruct
    public void init() {
            // create default user

            User user = new User();
            user.setUsername("admin");
            user.setPassword("admin");
            user.setIslock(true);
            user.setIsReset(true);
           
            Role  role = new Role();
            
            role.setRoleName("root");
                       
            UserRoleRel userRole = new UserRoleRel();
            userRole.setRole(role);
            userRole.setUser(user);
            
            try {
                Role tempRole = roleService.getOneByRole("root");
                User tempUser = userService.getOneByUsername("admin");
                UserRoleRel tempUserRoleRel = userRoleService.getUserRoleRel("admin", "root");
             
                
                if (tempRole==null) {
                   roleService.addRole(role);
                }
            
                if (tempUser==null) {
                    userService.addUser(user);
                }
 
                if (tempUserRoleRel==null){                  
                    userRoleService.addUserRoleRel(userRole);
                }

            } catch (Exception e) {
             
                e.printStackTrace();
            }

    }   

}
