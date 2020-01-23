/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.role;

import com.github.adminfaces.persistence.bean.BeanService;
import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.starter.model.Permission;
import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.model.RolePermissionRel;
import com.github.adminfaces.starter.service.PermissionService;
import com.github.adminfaces.starter.service.RolePermissionRelService;
import com.github.adminfaces.starter.service.RoleService;
import com.github.adminfaces.starter.service.UserRoleRelService;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 *
 * @author olivier.tatsinkou
 */
@Named
@ViewScoped
@BeanService(RoleService.class)//use annotation instead of setter
public class RoleFormMB extends CrudMB<Role> implements Serializable{
    
    private Integer id;
    private Role role;
    private List<Permission> listpermission ;
    private List<Permission> selectedPermission ;

     
    @Inject
    RoleService roleService;
    
    @Inject
    PermissionService permissionService;
    
    @Inject
    RolePermissionRelService rolePermissionservice;
    
    @Inject
    UserRoleRelService userroleservice;

    public void afterRemove() {
        try {
            addDetailMsg("Role " + entity.getRoleName()
                    + " removed successfully");
            Faces.redirect("/manageRole_page/role_list.xhtml");
            clear(); 
            sessionFilter.clear(RoleListMB.class.getName());//removes filter saved in session for RoleListMB.
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterInsert() {
         addDetailMsg("Role " + entity.getRoleName() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("Role " + entity.getRoleName() + " updated successfully");
    }
    
  
    @PostConstruct   
    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        listpermission = permissionService.listPermision();
        if (has(id)) {
            role = roleService.findById(id);
            selectedPermission = permissionService.listPermisionByRole(role.getRoleName());
        } else {
            role = new Role();
        }       
        
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Permission> getListpermission() {
        return listpermission;
    }

    public void setListpermission(List<Permission> listpermission) {
        this.listpermission = listpermission;
    }

    public List<Permission> getSelectedPermission() {
        return selectedPermission;
    }

    public void setSelectedPermission(List<Permission> selectedPermission) {
        
        System.out.println("liste permission : "+selectedPermission);
        this.selectedPermission = selectedPermission;
    }

   
 

    public void remove()  {
        if (has(role) && has(role.getId())) {
            if (role.getRoleName().equals("root")){
                 addDetailMessage("Cannot Remove Role : " + role.getRoleName());
                 Faces.getFlash().setKeepMessages(true);
            }else{
                roleService.remove(role);
                rolePermissionservice.deleteRolePermissionRelByRole(role.getId());
                userroleservice.deleteUserRoleRelByRole(role.getId());
                addDetailMessage("Role " + role.getRoleName()
                        + " removed successfully");
                Faces.getFlash().setKeepMessages(true);

                try {
                     Faces.redirect("/manageRole_page/role_list.xhtml");
                } catch (Exception e) {

                }
            }
           
            
        }
    }

    public Role save() {
        String msg;
        if (role.getId() == null) {
            roleService.insert(role);
            rolePermissionservice.deleteRolePermissionRelByRole(role.getId());
            for (Permission permission : selectedPermission){
                RolePermissionRel rolepermission = new RolePermissionRel();
                rolepermission.setRole(role);
                rolepermission.setPermission(permission);
                rolePermissionservice.insert(rolepermission);
            }
            msg = "Role " + role.getRoleName() + " created successfully";
            
            
        } else if (role.getRoleName().equals("browser")||role.getRoleName().equals("root")) {
         
            msg = "Role " + role.getRoleName() + " cannot been updated";
        } else {
            roleService.update(role);
            rolePermissionservice.deleteRolePermissionRelByRole(role.getId());
            for (Permission permission : selectedPermission){
                RolePermissionRel rolepermission = new RolePermissionRel();
                rolepermission.setRole(role);
                rolepermission.setPermission(permission);
                rolePermissionservice.insert(rolepermission);
            }
            msg = "Role " + role.getRoleName() + " updated successfully";
        }
        addDetailMessage(msg);
        return role;
    }

    public void clear() {
        role = new Role();
        id = null;
    }

    public boolean isNew() {
        return role == null || role.getId() == null;
    }

    
}
