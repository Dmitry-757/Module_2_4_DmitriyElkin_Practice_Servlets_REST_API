package com.DmitryElkin_Servlets_REST_API.repository;

import com.DmitryElkin_Servlets_REST_API.model.User;
import com.DmitryElkin_Servlets_REST_API.repository.utils.HibernateUtil;
import org.hibernate.Session;


import java.util.List;

public class UserRepository extends HibernateRepository<User>{
    public UserRepository() {
        super(User.class);
    }

    @Override
    public User getById(long id) {
        try (Session session = HibernateUtil.getSession()) {

            String hql = "select i from User i left join fetch i.events where i.id = :id";
            return session.createQuery(hql, User.class)
                    .setParameter("id", id)
                    .getSingleResult();

        }
    }

    public List<User> getByName(String userName) {
        List<User> itemList;
        try (Session session = HibernateUtil.getSession()) {

            String hql = "select i from User i left join fetch i.events where i.name = :param";
            itemList = session.createQuery(hql, User.class)
                    .setParameter("param", userName)
                    .getResultList();

        }
        return itemList;
    }
}
