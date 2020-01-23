/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.infra.converter;

import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.service.RoleService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 *
 * @author olivier
 */
@FacesConverter(value = "roleConverter")
public class RoleConverter implements Converter{
    
    
    @Inject
    RoleService roleService;
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
            String result = null;
            if (object == null || "".equals(object)) {
                    result = null;
            }

            if (object instanceof Role) {
                Role role = (Role)object;
                result  = role.getRoleName();
            } 
            return result;
    }

	@Override
	public Role getAsObject(FacesContext context, UIComponent component, String uuid) {
            Role role = roleService.getOneByRole(uuid);
            return role;
	}
    
}
