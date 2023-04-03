package com.DmitryElkin_Servlets_REST_API.controller;

import com.DmitryElkin_Servlets_REST_API.service.FileService;
import com.DmitryElkin_Servlets_REST_API.repository.utils.PrepareDB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;

@WebServlet(name = "FileRestControllerV1", value = "/api/v1/files/*")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileRestControllerV1 extends HttpServlet {
    FileService fileService = new FileService();

    @Override
    public void init() {
        PrepareDB.doPrepare();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        fileService.downloadFile(ctx, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        fileService.uploadFile(request, response);
    }

}
