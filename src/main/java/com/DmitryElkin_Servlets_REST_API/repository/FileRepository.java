package com.DmitryElkin_Servlets_REST_API.repository;

import com.DmitryElkin_Servlets_REST_API.model.File;

public class FileRepository extends HibernateRepository<File> {
    public FileRepository() {
        super(File.class);
    }
}
