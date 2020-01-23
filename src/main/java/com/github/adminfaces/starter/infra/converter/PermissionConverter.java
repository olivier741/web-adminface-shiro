/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.infra.converter;

import com.github.adminfaces.starter.model.Permission;
import com.github.adminfaces.starter.service.PermissionService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 *
 * @author olivier.tatsinkou
 */
@FacesConverter(value = "permissionConverter")
public class PermissionConverter implements Converter{
    
    @Inject
    PermissionService permissionService;
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
            String result = null;
            if (object == null || "".equals(object)) {
                    result = null;
            }

            if (object instanceof Permission) {
                Permission permission = (Permission)object;
                result  = permission.getPermissionStr();
            } 
            return result;
    }

	@Override
	public Permission getAsObject(FacesContext context, UIComponent component, String uuid) {
             Permission perm = permissionService.getOneByPermission(uuid);
            return perm;
	}
}
