/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.user;

/**
 *
 * @author olivier.tatsinkou
 */



import com.github.adminfaces.persistence.model.Filter;
import java.util.List;
import org.primefaces.model.LazyDataModel;
import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import com.github.adminfaces.starter.model.Role;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.RoleService;
import com.github.adminfaces.starter.service.UserService;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import com.github.adminfaces.template.exception.BusinessException;
import static com.github.adminfaces.template.util.Assert.has;
import java.util.ArrayList;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.primefaces.model.SortOrder;

@Named
@ViewScoped
public class UserListMB extends CrudMB<User> implements Serializable  {
    
    @Inject
    UserService userService;
    
    @Inject
    RoleService roleService;
    
    @Inject
    @Service
    CrudService<User, Integer> crudService; //generic injection
    
    Integer id;

    LazyDataModel<User> users;

    Filter<User> filter = new Filter<>(new User());

    List<User> selectedUsers; //user selected in checkbox column

    List<User> filteredValue;// datatable filteredValue attribute (column filters)
    
    
    @Inject
    public void initService() {
        setCrudService(userService);
    }
    
    @PostConstruct
    public void initDataModel() {
        users = new LazyDataModel<User>() {
            @Override
            public List<User> load(int first, int pageSize,
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
                List<User> list = userService.paginate(filter);
                setRowCount(userService.count(filter).intValue());
                return list;
            }

            @Override
            public int getRowCount() {
                return super.getRowCount();
            }

            @Override
            public User getRowData(String key) {
                return userService.findById(new Integer(key));
            }
        };
    }
    
    
    
      
     public void clear() {
        filter = new Filter<User>(new User());
    }

    public List<String> completeUserName(String query) {
        List<String> result = userService.getUsers(query);
        return result;
    }
    
    public List<String> completeislock(String query) {
        List<String> result = userService.getUsers(query);
        return result;
    }

    public void findUserByUsername(String username) {
       if (username == null) {
           addDetailMessage("Provide User username to load");
        }if ("".equals(username)) {
           addDetailMessage("Provide User username to load");
        }else {
              selectedUsers.add(userService.getOneByUsername(username));
        }

    }
    
    
     public String getRoleByUser(String username){
       String result = "";
         
       List<Role>  listRole = roleService.listRoleByUser(username);
       
       if (listRole !=null){
            for (Role role : listRole){
               if (result.equals(""))
                  result=role.getRoleName();
               else result=result+" | "+role.getRoleName();
            }
       }
      
      return result;  
    }
    
     public List<String> getRole(){
       List<String> result = null;
         
       List<Role>  listRole = roleService.listRole();
       
       if (listRole !=null){
            result = new ArrayList<>();
            for (Role role : listRole){
               result.add(role.getRoleName());
            }
       }
      
      return result;  
    }
    

    public void delete() {
        int numCars = 0;
        for (User user : selectedUsers) {
            numCars++;
            userService.remove(user);
        }
        selectedUsers.clear();
        addDetailMessage(numCars + " cars deleted successfully!");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LazyDataModel<User> getUsers() {
        return users;
    }

    public void setUsers(LazyDataModel<User> users) {
        this.users = users;
    }

    public Filter<User> getFilter() {
        return filter;
    }

    public void setFilter(Filter<User> filter) {
        this.filter = filter;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public List<User> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<User> filteredValue) {
        this.filteredValue = filteredValue;
    }


    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);

        String usernameParam = null;
        User userFilter = filter.getEntity();

        Integer idParam = null;
        if (filter.hasParam("id")) {
            idParam = filter.getIntParam("id");
        }

        if (has(idParam)) {
            sb.append("<b>id</b>: ").append(idParam).append(", ");
        }

        if (filter.hasParam("username")) {
            usernameParam = filter.getStringParam("username");
        } else if (has(userFilter) && userFilter.getUsername() != null) {
            usernameParam = userFilter.getUsername();
        }

        if (has(usernameParam)) {
            sb.append("<b>name</b>: ").append(usernameParam).append(", ");
        }

        int commaIndex = sb.lastIndexOf(",");

        if (commaIndex != -1) {
            sb.deleteCharAt(commaIndex);
        }

        if (sb.toString().trim().isEmpty()) {
            return "No search criteria";
        }

        return sb.toString();
    }
    
}
