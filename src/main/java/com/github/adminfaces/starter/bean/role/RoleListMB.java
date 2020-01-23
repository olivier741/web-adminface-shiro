/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.role;

import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import com.github.adminfaces.starter.model.Permission;
import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.service.PermissionService;
import com.github.adminfaces.starter.service.RolePermissionRelService;
import com.github.adminfaces.starter.service.RoleService;
import com.github.adminfaces.starter.service.UserRoleRelService;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author olivier.tatsinkou
 */
@Named
@ViewScoped
public class RoleListMB extends CrudMB<Role> implements Serializable{
    
    @Inject
    RoleService roleService;
    
    @Inject
    PermissionService permissionService;
    
    @Inject
    RolePermissionRelService rolepermissionRelService;
    
    @Inject
    UserRoleRelService userroleservice;

    @Inject
    @Service
    CrudService<Role, Integer> crudService; //generic injection
    
    
    Integer id;

    LazyDataModel<Role> roles;
    

    Filter<Role> filter = new Filter<>(new Role());

    List<Role> selectedRoles; //cars selected in checkbox column

    List<Role> filteredValue;// datatable filteredValue attribute (column filters)
    
    

    @Inject
    public void initService() {
        setCrudService(roleService);
    }
    
    @PostConstruct
    public void initDataModel() {
        roles = new LazyDataModel<Role>() {
            @Override
            public List<Role> load(int first, int pageSize,
                                  String sortField, SortOrder sortOrder,
                                  Map<String, Object> filters) {
                com.github.adminfaces.persistence.model.AdminSort order = null;
                if (sortOrder != null) {
                    order = sortOrder.equals(SortOrder.ASCENDING) ? com.github.adminfaces.persistence.model.AdminSort.ASCENDING
                            : sortOrder.equals(SortOrder.DESCENDING) ? com.github.adminfaces.persistence.model.AdminSort.DESCENDING
                            : com.github.adminfaces.persistence.model.AdminSort.UNSORTED;
                }
                filter.setFirst(first)
                      .setPageSize(pageSize)
                      .setSortField(sortField)
                      .setAdminSort(order)
                      .setParams(filters);
                List<Role> list = roleService.paginate(filter);
                setRowCount(roleService.count(filter).intValue());
                return list;
            }

            @Override
            public int getRowCount() {
                return super.getRowCount();
            }

            @Override
            public Role getRowData(String key) {
                return roleService.findById(new Integer(key));
            }
        };
        
    }

   
    
     public String getPermisssionByRole(String role){
       String result = "";
         
       List<Permission>  listPermission = permissionService.listPermisionByRole(role);
       
       if (listPermission !=null){
            for (Permission perm : listPermission){
               if (result.equals(""))
                  result=perm.getPermissionStr();
               else result=result+" | "+perm.getPermissionStr();
               
            }
       }
      
      return result;  
    }
    
    
    
     public List<String> getPermisssion(){
       List<String> result = null;
         
       List<Permission>  listPermission = permissionService.listPermision();
       
       if (listPermission !=null){
            result = new ArrayList<>();
            for (Permission perm : listPermission){
               result.add(perm.getPermissionStr());
            }
       }
      
      return result;  
    }
    
     public void clear() {
        filter = new Filter<Role>(new Role());
    }

    public List<String> completeRole (String query) {
        List<String> result = roleService.getRole(query);
        return result;
    }

    public void findRole(String id) {
        if (id == null) {
           addDetailMessage("Provide Role name to load");
        }if ("".equals(id)) {
           addDetailMessage("Provide Role name to load");
        }else {
           selectedRoles.add(roleService.getByRole(id)); 
        }
        
    }
    
     public void delete() {
        int numCars = 0;
        for (Role role : selectedRoles) {  
            
          if (!role.getRoleName().equals("root")){
            rolepermissionRelService.deleteRolePermissionRelByRole(role.getId());
            userroleservice.deleteUserRoleRelByRole(role.getId());
            
            roleService.remove(role);
            numCars++;
            
          }else {
             addDetailMessage("Cannot delete Role : "+role.getRoleName()); 
             break;
          }
            
        }
        selectedRoles.clear();
        if (numCars != 0)  addDetailMessage(numCars + " Role deleted successfully!");
        else addDetailMessage(numCars + " Role cannot be deleted!");

    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LazyDataModel<Role> getRoles() {
        return roles;
    }

    public void setRoles(LazyDataModel<Role> roles) {
        this.roles = roles;
    }

    public Filter<Role> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Role> filter) {
        this.filter = filter;
    }

    public List<Role> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public List<Role> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Role> filteredValue) {
        this.filteredValue = filteredValue;
    }
    
    
   

}
