/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.login;

import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.UserService;
import com.github.adminfaces.starter.util.PasswordGenerators;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 *
 * @author olivier.tatsinkou
 */
@Named
@SessionScoped
public class ResetPassword implements Serializable{
  
    private String username;
    private String password;
    private String current_password;
    private String changed_password;
    private UIInput changed_UIpassword;
    private String conf_password;
    private String captchaUserInput;
    
    
    @Inject
    UserService userService;

    private Logger log = Logger.getLogger(ResetPassword.class.getName());

    public void reset_login(String login) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        User user = userService.getOneByUsername(login);
        user.setPassword(changed_password);
        user.setIsReset(true);
        try {
             userService.changePassword(user);
             FacesMessage facesMessage = new FacesMessage( login + " : You have Success Change password");
             facesContext.addMessage(null, facesMessage);
        } catch (Exception e) {
            
            FacesMessage facesMessage = new FacesMessage( login + " : You have failed to change Default password");
            facesContext.addMessage(null, facesMessage);
        }
         SecurityUtils.getSubject().logout();
         Faces.getExternalContext().getFlash().setKeepMessages(true);
         Faces.redirect("login.xhtml");
        
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

  
    public String getCurrent_password() {
        return current_password;
    }

    public void setCurrent_password(String current_password) {
        this.current_password = current_password;
    }

    public String getChanged_password() {
        return changed_password;
    }

    public void setChanged_password(String changed_password) {
        this.changed_password = changed_password;
    }

    public UIInput getChanged_UIpassword() {
        return changed_UIpassword;
    }

    public void setChanged_UIpassword(UIInput changed_UIpassword) {
        this.changed_UIpassword = changed_UIpassword;
    }

    public String getConf_password() {
        return conf_password;
    }

    public void setConf_password(String conf_password) {
        this.conf_password = conf_password;
    }

    public String getCaptchaUserInput() {
        return captchaUserInput;
    }

    public void setCaptchaUserInput(String captchaUserInput) {
        this.captchaUserInput = captchaUserInput;
    }
    
    

    public String getCurrentUser() {
       
        return SecurityUtils.getSubject().getPrincipal().toString();
    }
    
    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }
    
    
    public void validateCurPass(FacesContext context, UIComponent component, Object value)  throws ValidatorException{ 
        String curPass = value.toString();
        String id = (String) component.getAttributes().get("username");

        if (id !=null){
             User user = userService.getOneByUsername(id);

            if (user!=null){

                String curPassEncrypt = new Sha256Hash(curPass, Base64.decode(user.getSalt()), 1024).toBase64();

                if (!user.getPassword().equals(curPassEncrypt)) {
                    System.out.println("encrypt current pass :"+curPass + "database value ="+user.getPassword());
                    FacesMessage errorMessage = new FacesMessage("Current password is not correct.");
                    throw new ValidatorException(errorMessage);
                }
            }else {
                String message = context.getApplication().evaluateExpressionGet(context, "Don't have User with loging "+id, String.class);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
                throw new ValidatorException(msg);
            }
        }else{
             FacesMessage errorMessage = new FacesMessage("Login provide to system is Null");
             throw new ValidatorException(errorMessage);
        }
       
       
    }
    
   public void validateNewPass(FacesContext context, UIComponent component, Object value) throws ValidatorException{

        PasswordGenerators passGen = new PasswordGenerators();
        
        changed_password = changed_UIpassword.getLocalValue().toString();
        conf_password = value.toString();
        System.out.println("1 password : "+ changed_password +" confirmation : "+conf_password);
        if (!changed_password.equals(conf_password)) {
            System.out.println("2 password : "+ changed_password +" confirmation : "+conf_password);
            FacesMessage errorMessage = new FacesMessage("New passwords and Confirmation password must match.");
            throw new ValidatorException(errorMessage);
        }else if (!passGen.isPasswordValid(changed_password) && !passGen.isPasswordValid(conf_password)){
           System.out.println("3 password : "+ changed_password +" confirmation : "+conf_password);
           FacesMessage errorMessage = new FacesMessage(passGen.getPassword_Message().toString());
           throw new ValidatorException(errorMessage); 
        }
    }


}
