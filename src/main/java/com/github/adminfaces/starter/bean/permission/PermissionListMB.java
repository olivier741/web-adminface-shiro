/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.permission;

import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import com.github.adminfaces.starter.model.Permission;
import com.github.adminfaces.starter.service.PermissionService;
import com.github.adminfaces.starter.service.RolePermissionRelService;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import com.github.adminfaces.template.exception.BusinessException;
import java.io.Serializable;
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
public class PermissionListMB extends CrudMB<Permission> implements Serializable{
    
    @Inject
    PermissionService permissionService;
    
    @Inject
    RolePermissionRelService rolepermissionRelService;

    @Inject
    @Service
    CrudService<Permission, Integer> crudService; //generic injection
    
    
    Integer id;

    LazyDataModel<Permission> permissions;

    Filter<Permission> filter = new Filter<>(new Permission());

    List<Permission> selectedPermissions; //cars selected in checkbox column

    List<Permission> filteredValue;// datatable filteredValue attribute (column filters)

    @Inject
    public void initService() {
        setCrudService(permissionService);
    }
    
    @PostConstruct
    public void initDataModel() {
        permissions = new LazyDataModel<Permission>() {
            @Override
            public List<Permission> load(int first, int pageSize,
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
                List<Permission> list = permissionService.paginate(filter);
                setRowCount(permissionService.count(filter).intValue());
                return list;
            }

            @Override
            public int getRowCount() {
                return super.getRowCount();
            }

            @Override
            public Permission getRowData(String key) {
                return permissionService.findById(new Integer(key));
            }
        };
    }

       
    
     public void clear() {
        filter = new Filter<Permission>(new Permission());
    }

    public List<String> completePermission(String query) {
        List<String> result = permissionService.getPermisionStr(query);
        return result;
    }

    public void findPermisiton(String id) {
        if (id == null) {
           addDetailMessage("Provide Permission name to load");
        }if ("".equals(id)) {
           addDetailMessage("Provide Permission name to load");
        }else {
             selectedPermissions.addAll(permissionService.getFindByPermission(id));
        }
    }
    
    public void delete() {
        int numCars = 0;
        for (Permission permission : selectedPermissions) {     
            if (rolepermissionRelService.deleteRolePermissionRelByPermission(permission.getId())){
                 permissionService.remove(permission);
                 numCars++;
            }
        }
        selectedPermissions.clear();
        if (numCars != 0)  addDetailMessage(numCars + " permission deleted successfully!");
        else addDetailMessage(numCars + " permission cannot deleted!");

    }
   

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LazyDataModel<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(LazyDataModel<Permission> permissions) {
        this.permissions = permissions;
    }

    public Filter<Permission> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Permission> filter) {
        this.filter = filter;
    }

    public List<Permission> getSelectedPermissions() {
        return selectedPermissions;
    }

    public void setSelectedPermissions(List<Permission> selectedPermissions) {
        this.selectedPermissions = selectedPermissions;
    }

    public List<Permission> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Permission> filteredValue) {
        this.filteredValue = filteredValue;
    }

   
  
}
