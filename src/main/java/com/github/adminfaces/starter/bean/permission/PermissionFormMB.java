/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.permission;

import com.github.adminfaces.persistence.bean.BeanService;
import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.starter.model.Permission;
import com.github.adminfaces.starter.service.PermissionService;
import com.github.adminfaces.starter.service.RolePermissionRelService;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 *
 * @author olivier.tatsinkou
 */
@Named
@ViewScoped
@BeanService(PermissionService.class)//use annotation instead of setter
public class PermissionFormMB extends CrudMB<Permission> implements Serializable{
     
    private Integer id;
    private Permission permission;

     
    @Inject
    PermissionService permissionService;
    
    @Inject
    RolePermissionRelService rolePermissionservice;

    public void afterRemove() {
        try {
            addDetailMsg("Permission " + entity.getPermissionStr()
                    + " removed successfully");
            Faces.redirect("/managePermission_page/permission_list.xhtml");
            clear(); 
            sessionFilter.clear(PermissionListMB.class.getName());//removes filter saved in session for CarListMB.
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterInsert() {
         addDetailMsg("Permission " + entity.getPermissionStr() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("Permission " + entity.getPermissionStr() + " updated successfully");
    }
    
  

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            permission = permissionService.findById(id);
        } else {
            permission = new Permission();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    


    public void remove()  {
        if (has(permission) && has(permission.getId())) {
            permissionService.remove(permission);
            rolePermissionservice.deleteRolePermissionRelByPermission(permission.getId());
            addDetailMessage("Permission " + permission.getPermissionStr()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            
            try {
                 Faces.redirect("/managePermission_page/permission_list.xhtml");
            } catch (Exception e) {
                
            }
            
        }
    }

    public Permission save() {
        String msg;
        if (permission.getId() == null) {
            permissionService.insert(permission);
            msg = "Permission " + permission.getPermissionStr() + " created successfully";
        } else {
            permissionService.update(permission);
            msg = "Permission " + permission.getPermissionStr() + " updated successfully";
        }
        addDetailMessage(msg);
        return permission;
    }

    public void clear() {
        permission = new Permission();
        id = null;
    }

    public boolean isNew() {
        return permission == null || permission.getId() == null;
    }

    
}
