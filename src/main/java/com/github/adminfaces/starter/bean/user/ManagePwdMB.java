/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.user;


import com.github.adminfaces.persistence.bean.BeanService;
import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.UserService;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author olivier.tatsinkou
 */
@Named
@ViewScoped
@BeanService(UserService.class)//use annotation instead of setter
public class ManagePwdMB extends CrudMB<User> implements Serializable {
    
    private String id;
    private String curPass;
    private String newPass1;
    private String newPass2;
    private UIInput newPass1UI;
    
   
    @Inject
    UserService userService;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurPass() {
        return curPass;
    }

    public void setCurPass(String curPass) {
        this.curPass = curPass;
    }

    public String getNewPass1() {
        return newPass1;
    }

    public void setNewPass1(String newPass1) {
        this.newPass1 = newPass1;
    }

    public String getNewPass2() {
        return newPass2;
    }

    public void setNewPass2(String newPass2) {
        this.newPass2 = newPass2;
    }

    public UIInput getNewPass1UI() {
        return newPass1UI;
    }

    public void setNewPass1UI(UIInput newPass1UI) {
        this.newPass1UI = newPass1UI;
    }

    
    @Override
    public void afterInsert() {
         addDetailMsg("User " + entity.getUsername() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("User " + entity.getUsername() + " updated successfully");
    }
    
    
    
    public void validateNewPass(FacesContext context, UIComponent component, Object value) throws ValidatorException{

        newPass1 = newPass1UI.getLocalValue().toString();
        newPass2 = value.toString();
        if (!newPass1.equals(newPass2)) {
            FacesMessage errorMessage = new FacesMessage("New passwords must match.");
            throw new ValidatorException(errorMessage);
        }
    }

    public void validateCurPass(FacesContext context, UIComponent component, Object value)  throws ValidatorException{ 
        curPass = value.toString();
        id = (String) component.getAttributes().get("login");

        User user = userService.getOneByUsername(id);
        
        String curPassEncrypt = new Sha256Hash(curPass, Base64.decode(user.getSalt()), 1024).toBase64();
        
        if (!user.getPassword().equals(curPassEncrypt)) {
            System.out.println("encrypt current pass :"+curPass + "database value ="+user.getPassword());
            FacesMessage errorMessage = new FacesMessage("Current password is not correct.");
            throw new ValidatorException(errorMessage);
        }
    }
    
     public String changePwd() {
        String msg="cannot change password";
        String page = "/index.xhtml?faces-redirect=true";
        
        User user = userService.getOneByUsername(id);
        user.setPassword(newPass1);
        
         System.out.println("change pass :"+newPass1);
         System.out.println("syste current pass :"+user.getPassword());
         try {
                userService.changePassword(user);
                msg = "User " + user.getUsername() + " as change Password";
                 System.out.println(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        addDetailMessage(msg);
        return page;
    }
}
