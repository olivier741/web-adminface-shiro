/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.model;

/**
 *
 * @author olivier
 */
import com.github.adminfaces.persistence.model.BaseEntity;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private Set<UserRoleRel> userRoleRels = new HashSet<>(0);

   
    @OneToMany(mappedBy = "role")
    private Set<RolePermissionRel> rolePermissionRels = new HashSet<>(0);

    public Role() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<UserRoleRel> getUserRoleRels() {
        return userRoleRels;
    }

    public void setUserRoleRels(Set<UserRoleRel> userRoleRels) {
        this.userRoleRels = userRoleRels;
    }

    public Set<RolePermissionRel> getRolePermissionRels() {
        return rolePermissionRels;
    }

    public void setRolePermissionRels(Set<RolePermissionRel> rolePermissionRels) {
        this.rolePermissionRels = rolePermissionRels;
    }
    
     public boolean hasRoleName() {
        return roleName != null && !"".equals(roleName.trim());
    }


}
