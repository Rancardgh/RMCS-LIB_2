package com.rancard.mobility.contentserver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by Mustee on 6/13/2014.
 */
public class EMF implements ServletContextListener {
    private static EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        emf = Persistence.createEntityManagerFactory("PersistenceUnit");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        emf.close();
    }

    public static EntityManager createEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("Context is not initialized yet.");
        }

        return emf.createEntityManager();
    }
}
