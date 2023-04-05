package com.DmitryElkin_Servlets_REST_API.repository;


import com.DmitryElkin_Servlets_REST_API.repository.utils.HibernateUtil;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

public abstract class HibernateRepository<T> {
    final Class<T> typeParameterClass;

    protected HibernateRepository(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public T insert(T item) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.persist(item);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        }
        return item;
    }

    public T update(T item) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.merge(item);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        }
        return item;
    }

    public void delete(long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            T item = getById(id);
            tx = session.beginTransaction();
            session.remove(item);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public T getById(long id) {
        T item;
        try (Session session = HibernateUtil.getSession()) {
            item = session.find(typeParameterClass, id);
        }
        return item;
    }

    public List<T> getAll() {
        List<T> itemList;
        try (Session session = HibernateUtil.getSession()) {
            String className = typeParameterClass.getName();
            itemList = session.createQuery("SELECT r FROM "+className+" r", typeParameterClass).getResultList();
        }
        return itemList;
    }

    public List<T> getAllUsingCriteria() {
        List<T> itemList;
        try (Session session = HibernateUtil.getSession()) {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = builder.createQuery(typeParameterClass);
            Query<T> query = session.createQuery(criteriaQuery);
            itemList = query.getResultList();
        }

        return itemList;
    }


}
