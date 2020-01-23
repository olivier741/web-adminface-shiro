package com.github.adminfaces.starter.bean.login;

import com.github.adminfaces.starter.model.Permission;
import com.github.adminfaces.starter.model.RolePermissionRel;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.model.UserRoleRel;
import com.github.adminfaces.starter.service.UserService;
import com.github.adminfaces.starter.service.repository.RolePermissionRelRepository;
import com.github.adminfaces.starter.service.repository.UserRoleRelRepository;
import com.github.adminfaces.template.session.AdminSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by rmpestano on 12/20/14.
 * <p>
 * This is just a login example.
 * <p>
 * AdminSession uses isLoggedIn to determine if user must be redirect to login page or not.
 * By default AdminSession isLoggedIn always resolves to true so it will not try to redirect user.
 * <p>
 * If you already have your authorization mechanism which controls when user must be redirect to initial page or logon
 * you can skip this class.
 */
@Named
@SessionScoped
@Specializes
public class LogonMB extends AdminSession implements Serializable {

   
    private String email;
    private String password;
    private String current_password;
    private String changed_password;
    private UIInput changed_UIpassword;
    private String conf_password;
    private String captchaUserInput;
    private boolean remember;
    
    @Inject
    private UserRoleRelRepository userroleRepository;
    
    @Inject
    private RolePermissionRelRepository rolepermRepository;
    
    @Inject
    UserService userService;

    private Logger log = Logger.getLogger(LogonMB.class.getName());


    public void login() throws IOException {
        
        UsernamePasswordToken token = new UsernamePasswordToken(email, password);
        
        // "Remember Me" built-in:
        token.setRememberMe(remember);

        Subject currentUser = SecurityUtils.getSubject();
        FacesContext facesContext = FacesContext.getCurrentInstance();

        log.log(Level.INFO, "Submitting login with username of {0} and password of {1}", new Object[]{email, password});
        try {
            currentUser.login(token);
            
            User user = userService.getOneByUsername(email);
            if (!user.isIsReset()){
              
                FacesMessage facesMessage = new FacesMessage("YOU MUST RESET PASSWORD OF : " + email );
                facesContext.addMessage(null, facesMessage);
                
                Faces.getExternalContext().getFlash().setKeepMessages(true);
                Faces.redirect("reset_password.xhtml");
            }else{
                addDetailMessage("Logged in successfully as <b>" + email + "</b>");
                Faces.getExternalContext().getFlash().setKeepMessages(true);
                Faces.redirect("index.xhtml");
            }
           

           
       /* 
            you can handle specific exceptions
        
            
        } catch (UnknownAccountException uae) {
            //username wasn't in the system, show them an error message?
            throw uae;*/
        } catch (IncorrectCredentialsException ice) {
            currentUser.logout();
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest)context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();
            System.out.println("****************************** redirection of "+ contextPath + "/login.xhtml *********************************");
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/login.xhtml");    
        } catch (LockedAccountException e2) {
            //account for that username is locked - can't login.  Show them a message?
            log.log(Level.SEVERE, e2.getMessage(), e2);
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Messages.addFatal(null,"LOGING IS LOCK:" + e2.getMessage());
        } catch (Exception e) {
            // Could catch a subclass of AuthenticationException if you like
            log.log(Level.SEVERE, e.getMessage(), e);
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Messages.addFatal(null,"Login Failed:" + e.getMessage());
        }
    }
    
    
    public String getRoleByUserAndPermission(String username, String permName){
        
         if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("username must not be null");
        }

        String roleName = "";
        
        List<UserRoleRel> listuserrole = userroleRepository.getUserRolebyUser(username);
        boolean isok = false;
        if (listuserrole != null && !listuserrole.isEmpty()){
           User user = listuserrole.get(0).getUser();
              if (user.isIsReset()){
                for (UserRoleRel rel : listuserrole){
                    roleName = rel.getRole().getRoleName();
                    if (roleName.equals("root")){
                       isok = true;
                       break; 
                    }else{
                        List<Permission> lisperm = rolepermRepository.getPermissionByRole(roleName);

                        if (lisperm !=null && !lisperm.isEmpty()){


                            for(Permission perm : lisperm){
                                if (perm.getPermissionStr().trim().equals(permName.trim())){
                                   isok = true;
                                   break; 
                                }
                            }

                            if (isok) break;
                        }

                     }
                }
              }
          
        }
        
        if (isok) return roleName;
        else return "";
    }
    
  

    @Override
    public boolean isLoggedIn() {
        return SecurityUtils.getSubject().isAuthenticated();
    }
    
    
  
  
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
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
    
    
  


  

}
