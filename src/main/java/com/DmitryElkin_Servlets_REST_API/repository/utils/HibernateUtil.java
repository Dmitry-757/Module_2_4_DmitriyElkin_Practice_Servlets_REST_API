package com.DmitryElkin_Servlets_REST_API.repository.utils;



import com.DmitryElkin_Servlets_REST_API.model.Event;
import com.DmitryElkin_Servlets_REST_API.model.File;
import com.DmitryElkin_Servlets_REST_API.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;


public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Properties properties= new Properties();
            properties.setProperty("hibernate.connection.username", "root");
            properties.setProperty("hibernate.connection.password", "dingo1975");
            properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/proselyte_module_2_4_db");
            properties.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//            properties.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/proselyte_developers_hibernate_db");
//            properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
//            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.setProperty("hibernate.show_sql", "true");
//            properties.setProperty("hibernate.hbm2ddl.auto", "create");
//            properties.setProperty("hibernate.hbm2ddl.auto", "none");

            sessionFactory = new Configuration()
                    .addPackage("com.DmitryElkin_Servlets_REST_API.model")//package where entity is placed
                    .addProperties(properties)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(File.class)
                    .addAnnotatedClass(Event.class)
//                    .addAnnotatedClass(Developer.class)
                    .buildSessionFactory();
//            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static Session getSession()
            throws HibernateException {
        return sessionFactory.openSession();
    }
}