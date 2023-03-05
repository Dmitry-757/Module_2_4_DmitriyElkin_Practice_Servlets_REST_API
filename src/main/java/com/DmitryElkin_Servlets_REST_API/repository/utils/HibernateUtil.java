package com.Dmitry_Elkin.PracticeTaskCRUD.utils;



import com.Dmitry_Elkin.PracticeTaskCRUD.model.Developer;
import com.Dmitry_Elkin.PracticeTaskCRUD.model.Skill;
import com.Dmitry_Elkin.PracticeTaskCRUD.model.Specialty;
import com.Dmitry_Elkin.PracticeTaskCRUD.model.Status;
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
//            properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/proselyte_developers_hibernate_db");
//            properties.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
//            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            properties.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/proselyte_developers_hibernate_db");
            properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.setProperty("hibernate.show_sql", "true");
//            properties.setProperty("hibernate.hbm2ddl.auto", "create");
//            properties.setProperty("hibernate.hbm2ddl.auto", "none");

            sessionFactory = new Configuration()
                    .addPackage("com.Dmitry_Elkin.PracticeTaskCRUD.Model")//package where entity is placed
                    .addProperties(properties)
                    .addAnnotatedClass(Status.class)
                    .addAnnotatedClass(Specialty.class)
                    .addAnnotatedClass(Skill.class)
                    .addAnnotatedClass(Developer.class)
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