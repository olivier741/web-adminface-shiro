/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service.repository;

import com.github.adminfaces.starter.model.User;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author olivier
 */
@Repository
public interface UserRepository extends EntityRepository<User,Integer> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getOneByUsername(@QueryParam("username") String username);
  
}
