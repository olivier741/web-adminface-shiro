/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.car;

import com.github.adminfaces.persistence.bean.BeanService;
import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.starter.infra.security.realm.ShiroSecured;
import com.github.adminfaces.starter.model.Car;
import com.github.adminfaces.starter.service.CarService;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Inject;
import org.apache.shiro.authz.annotation.RequiresRoles;

/**
 * @author rmpestano
 */
@Named
@ViewScoped  
@BeanService(CarService.class)//use annotation instead of setter
public class CarFormMB extends CrudMB<Car> implements Serializable {

    private Integer id;
    private Car car;

     
    @Inject
    CarService carService;

    public void afterRemove() {
        try {
            addDetailMsg("Car " + entity.getModel()
                    + " removed successfully");
            Faces.redirect("car_list.xhtml");
            clear(); 
            sessionFilter.clear(CarListMB.class.getName());//removes filter saved in session for CarListMB.
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterInsert() {
         addDetailMsg("Car " + entity.getModel() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("Car " + entity.getModel() + " updated successfully");
    }
    
  

    public void init() {
        
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            car = carService.findById(id);
        } else {
            car = new Car();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }


    public void remove()  {
        if (has(car) && has(car.getId())) {
            carService.remove(car);
            addDetailMessage("Car " + car.getModel()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            
            try {
                 Faces.redirect("car_list.xhtml");
            } catch (Exception e) {
                
            }
            
        }
    }

    public Car save() {
        String msg;
        if (car.getId() == null) {
            carService.insert(car);
            msg = "Car " + car.getModel() + " created successfully";
        } else {
            carService.update(car);
            msg = "Car " + car.getModel() + " updated successfully";
        }
        addDetailMessage(msg);
        return car;
    }

    public void clear() {
        car = new Car();
        id = null;
    }

    public boolean isNew() {
        return car == null || car.getId() == null;
    }

    

}
