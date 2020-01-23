package com.github.adminfaces.starter.infra.persistence;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@ApplicationScoped
public class EntityManagerProducer {

    @PersistenceContext(unitName = "DB_PU1")
    EntityManager em;

    @Produces
    public EntityManager produce() {
        return em;
    }
}
