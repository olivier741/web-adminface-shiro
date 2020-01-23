/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.infra.security.exception;

/**
 *
 * @author olivier.tatsinkou
 */
public class EntityExistedException extends Exception{
    
    private static final long serialVersionUID = -6073718670508192198L;

    public EntityExistedException() {}

    public EntityExistedException(String msg) {
        super(msg);
    }
}
