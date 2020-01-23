/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.infra.filter;

import com.github.adminfaces.starter.bean.login.LogonMB;
import com.github.adminfaces.starter.model.User;
import com.github.adminfaces.starter.service.UserService;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author olivier.tatsinkou
 */
public class AuthenticationFilter implements Filter{
    private FilterConfig customedFilterConfig;
    
    @Inject
    UserService userService;
    
    public void doFilter(ServletRequest req, ServletResponse resp,FilterChain chain) throws IOException, ServletException {
        
         HttpServletRequest httpRequest = (HttpServletRequest) req;
         HttpServletResponse httpResponse = (HttpServletResponse) resp;

         String newPath = httpRequest.getContextPath(); 
         String Url_link = httpRequest.getRequestURI();
         String URL = Url_link.replace(newPath, "");
         LogonMB logon = (LogonMB) httpRequest.getSession().getAttribute("logonMB");
                   
            if (logon!=null){
               if (SecurityUtils.getSubject().getPrincipal() != null){

                   String username = SecurityUtils.getSubject().getPrincipal().toString();
                   User user = userService.getOneByUsername(username);
                   
                   if (!user.isIsReset()) {
                       ((HttpServletResponse)resp).sendRedirect(newPath + "/reset_password.xhtml");
                   }else if (SecurityUtils.getSubject().hasRole("root") || SecurityUtils.getSubject().isPermitted(URL) || Url_link.equals(newPath + "/index.xhtml") ){
                        chain.doFilter(req, resp);
                   }else {
                        ((HttpServletResponse)resp).sendRedirect(newPath + "/index.xhtml");
                   }
              } else {
                 chain.doFilter(req, resp);
              }
           }else{
              if (SecurityUtils.getSubject().getPrincipal() != null){
                   String username = SecurityUtils.getSubject().getPrincipal().toString();
                   User user = userService.getOneByUsername(username);
                   
                   if (!user.isIsReset()){
                        SecurityUtils.getSubject().logout();
                        ((HttpServletResponse)resp).sendRedirect(newPath + "/login.xhtml");
                   }else if (SecurityUtils.getSubject().hasRole("root") || SecurityUtils.getSubject().isPermitted(URL) || Url_link.equals(newPath + "/index.xhtml")){
                        chain.doFilter(req, resp);
                   }else {
                        ((HttpServletResponse)resp).sendRedirect(newPath + "/index.xhtml");
                   }
               
              }else{
                  chain.doFilter(req, resp);
              }
           }

    }
    
    public void init(FilterConfig customedFilterConfig) throws ServletException {
        this.customedFilterConfig = customedFilterConfig;
    }
    
    public void destroy() {
        customedFilterConfig = null;
    }
}
