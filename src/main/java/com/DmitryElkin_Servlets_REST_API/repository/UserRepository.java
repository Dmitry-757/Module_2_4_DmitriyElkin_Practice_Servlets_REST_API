package com.DmitryElkin_Servlets_REST_API.repository;

import com.DmitryElkin_Servlets_REST_API.model.User;

public class UserRepository extends HibernateRepository<User>{
    protected UserRepository(Class<User> typeParameterClass) {
        super(typeParameterClass);
    }
}
