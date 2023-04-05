package com.DmitryElkin_Servlets_REST_API.repository;

import com.DmitryElkin_Servlets_REST_API.model.Event;
import com.DmitryElkin_Servlets_REST_API.model.User;
import com.DmitryElkin_Servlets_REST_API.repository.utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class EventRepository extends HibernateRepository<Event> {
    public EventRepository() {
        super(Event.class);
    }

    public List<Event> getByUser(User user) {
        List<Event> itemList;
        try (Session session = HibernateUtil.getSession()) {

//            String hql = "select i from Event i left join fetch i.file where i.user = :param";
            String hql = "select i from Event i  where i.user = :param";
            itemList = session.createQuery(hql, Event.class)
                    .setParameter("param", user)
                    .getResultList();

        }
        return itemList;
    }

}
