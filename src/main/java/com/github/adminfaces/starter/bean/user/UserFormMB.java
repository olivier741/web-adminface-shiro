/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.user;

import com.github.adminfaces.persistence.bean.BeanService;
import com.github.adminfaces.persistence.bean.CrudMB;
import static com.github.adminfaces.persistence.bean.CrudMB.addDetailMsg;
import com.github.adminfaces.starter.model.Email;
import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.model.UserRoleRel;
import com.github.adminfaces.starter.service.RoleService;
import com.github.adminfaces.starter.service.UserRoleRelService;
import com.github.adminfaces.starter.service.UserService;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author olivier.tatsinkou
 */
@Named
@ViewScoped
@BeanService(UserService.class)//use annotation instead of setter
public class UserFormMB extends CrudMB<User> implements Serializable{
    
    private Integer id;
    private User user;
    private boolean skip;
    private String email;
    private String selectedEmail;
    private List<Email> listEmail;
    private List<Role> listrole;
    private List<Role> selectedRole ;
    private String displaySelectedRole;
    
    @Inject
    UserService userService;
    
    @Inject
    RoleService roleService;
    
    @Inject
    UserRoleRelService userroleservice;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
     public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public List<Email> getListEmail() {
        return listEmail;
    }

    public void setListEmail(List<Email> listEmail) {
        this.listEmail = listEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSelectedEmail() {
        return selectedEmail;
    }

    public void setSelectedEmail(String selectedEmail) {
        this.selectedEmail = selectedEmail;
    }
    
    

     
    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }
    
     public void afterRemove() {
        try {
            addDetailMsg("User " + entity.getUsername()
                    + " removed successfully");
            Faces.redirect("manageUser_page/user_list.xhtml");
            clear(); 
            sessionFilter.clear(UserListMB.class.getName());//removes filter saved in session for UserListMB.
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterInsert() {
         addDetailMsg("User " + entity.getUsername() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("User " + entity.getUsername() + " updated successfully");
    }
    
  

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        listrole = roleService.listRole();
        listEmail = new ArrayList<Email>();
        
        if (has(id)) {
            user = userService.findById(id);
            listEmail = user.getListEmail();
            if (listEmail== null) listEmail = new ArrayList<Email>();
            selectedRole = roleService.listRoleByUser(user.getUsername());
        } else {
            user = new User();
        }
    }

    public void remove()  {
        if (has(user) && has(user.getId())) {
            userService.remove(user);
            userroleservice.deleteUserRoleRelByUser(user.getId());
            addDetailMessage("User " + user.getUsername()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            
            try {
                 Faces.redirect("manageUser_page/user_list.xhtml");
            } catch (Exception e) {
                
            }
            
        }
    }

    public User save() {
        String msg;
        user.setListEmail(listEmail);
        if (user.getId() == null) {

            try {
                 user.setIsReset(false);               
                 userService.addUser(user);
                 userroleservice.deleteUserRoleRelByUser(user.getId());
                 
                 for (Role role : selectedRole){
                    UserRoleRel userRole = new UserRoleRel();
                    userRole.setRole(role);
                    userRole.setUser(user);
                    userroleservice.insert(userRole);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           
            
            msg = "User " + user.getUsername() + " created successfully";
        } else {
            try {
                 userService.UpdateUser(user);
                 userroleservice.deleteUserRoleRelByUser(user.getId());
                 
                 for (Role role : selectedRole){
                    UserRoleRel userRole = new UserRoleRel();
                    userRole.setRole(role);
                    userRole.setUser(user);
                    userroleservice.insert(userRole);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            msg = "User " + user.getUsername() + " updated successfully";
        }
        addDetailMessage(msg);
        return user;
    }
    
    
    
    public String getDisplaySelectedRole(){
      String result = "";
       if (selectedRole !=null){
            for (Role role : selectedRole){
               if (result.equals(""))
                  result=role.getRoleName();
               else result=result+" | "+role.getRoleName();
            }
       }
      return result; 
    }

    public List<Role> getListrole() {
        return listrole;
    }

    public void setListrole(List<Role> listrole) {
        this.listrole = listrole;
    }

    public List<Role> getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(List<Role> selectedRole) {
        this.selectedRole = selectedRole;
    }

    public void clear() {
        user = new User();
        id = null;
    }

    public boolean isNew() {
        return user == null || user.getId() == null;
    }
    
    
     public void onRowEditEmail(RowEditEvent event) {       
        FacesMessage msg = new FacesMessage("Email Edited", ((Email) event.getObject()).getEmail());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancelEmail(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Email) event.getObject()).getEmail());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        String newEmail = (String)newValue;
        int numberEmail = countEmail(newEmail);
        
        System.out.println("--------------------1 oncellEdit email : "+ listEmail);
        System.out.println("--------------------old email : "+ oldValue);
        System.out.println("--------------------new email : "+ newEmail);
        if(newValue != null && !newValue.equals(oldValue)) {
            if (numberEmail > 1){
                removeEmail(newEmail);
                FacesMessage msg = new FacesMessage("this Email already exist", newEmail);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                 System.out.println("--------------------2 oncellEdit email : "+ listEmail);
            }else{
                 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
                 FacesContext.getCurrentInstance().addMessage(null, msg);
                  System.out.println("--------------------3 oncellEdit email : "+ listEmail);
            }
        }
    }
 
    public void deleteEmail(){
        
      System.out.println("-------------------- delete email : "+ selectedEmail);
      if (selectedEmail != null ) {
          if (listEmail != null  && isContaintEmail(selectedEmail)){
              removeEmail(selectedEmail);
              FacesMessage msg = new FacesMessage("Success remove Row ", selectedEmail);
              FacesContext.getCurrentInstance().addMessage(null, msg);
              selectedEmail = null;
          } 
      };
      System.out.println("-------------------- new list email  : "+ listEmail);

    }
    
    public void onAddNewEmail() {
        // Add one new car to the table:
        Email mail = new Email();
        mail.setEmail("");

        System.out.println("--------------------1 list email : "+ listEmail);
        if (listEmail == null) listEmail = new ArrayList<Email>();

        if (isContaintEmail(mail.getEmail())){
            System.out.println("--------------------2 list email : "+ listEmail);
            FacesMessage msg = new FacesMessage("This Email already Exist", mail.getEmail());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else {
            listEmail.add(0,mail);
//            user.setListEmail(listEmail);
            
//            try {
//                userService.UpdateUser(user);
//            } catch (Exception e) {
//            }
//            listEmail = user.getListEmail();
            System.out.println("--------------------3 list email : "+ listEmail);
            FacesMessage msg = new FacesMessage("New Email added", mail.getEmail());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
    }
    
    private boolean isContaintEmail(String email){
        boolean result = false;
        
        if (listEmail != null && !listEmail.isEmpty()){
            for(Email val: listEmail){
                if (val.getEmail()== null){
                    if (email== null){
                        result = true;
                        break;
                    }
                         
                }else if (val.getEmail().equals(email)){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    
     private boolean removeEmail(String email){
        boolean result = false;
        int sizes =listEmail.size();
        if (listEmail != null && !listEmail.isEmpty()){
            for(int i = 0;i<sizes; i++){
                Email val = listEmail.get(i);
                if (val.getEmail()== null && email == null){
                        result = true;
                        listEmail.remove(val);
                        break;
                    
                         
                }else if ( val.getEmail()!= null && val.getEmail().equals(email)){
                    result = true;
                    listEmail.remove(val);
                    break;
                }
            }
        }
        
        return result;
    }
    
   private int countEmail(String email){
        int result = 0;
        int sizes =listEmail.size();
        if (listEmail != null && !listEmail.isEmpty()){
            for(int i = 0;i<sizes; i++){
                Email val = listEmail.get(i);
                if (val.getEmail()== null && email == null){
                        result ++;                                              
                }else if ( val.getEmail()!= null && val.getEmail().equals(email)){
                        result ++;
                }
            }
        }
        
        return result;
    }

}
