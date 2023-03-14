package com.DmitryElkin_Servlets_REST_API.repository;

import com.DmitryElkin_Servlets_REST_API.model.User;
import com.DmitryElkin_Servlets_REST_API.repository.utils.HibernateUtil;
import org.hibernate.Session;


import java.util.List;

public class UserRepository extends HibernateRepository<User>{
    public UserRepository() {
        super(User.class);
    }

    public List<User> findByName(String userName) {
        List<User> itemList;
        try (Session session = HibernateUtil.getSession()) {
//            itemList = session.createQuery("SELECT r FROM User r", typeParameterClass).getResultList();

            String hql = "select i from User i left join fetch i.events where i.name = :param";
            itemList = session.createQuery(hql, User.class)
                    .setParameter("param", userName)
                    .getResultList();

        }
        return itemList;
    }
}
