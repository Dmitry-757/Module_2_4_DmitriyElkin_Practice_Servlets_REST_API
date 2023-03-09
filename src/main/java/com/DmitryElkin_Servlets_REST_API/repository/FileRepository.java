package com.DmitryElkin_Servlets_REST_API.repository;

import com.DmitryElkin_Servlets_REST_API.model.File;

public class FileRepository extends HibernateRepository<File> {
    protected FileRepository(Class<File> typeParameterClass) {
        super(typeParameterClass);
    }
}
