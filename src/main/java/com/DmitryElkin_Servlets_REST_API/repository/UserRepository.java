package com.DmitryElkin_Servlets_REST_API.repository;

import com.DmitryElkin_Servlets_REST_API.model.User;

public class UserRepository extends HibernateRepository<User>{
    public UserRepository() {
        super(User.class);
    }
}
