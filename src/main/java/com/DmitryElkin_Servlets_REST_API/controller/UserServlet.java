package com.DmitryElkin_Servlets_REST_API.controller;

import com.DmitryElkin_Servlets_REST_API.controller.service.UserService;
import com.DmitryElkin_Servlets_REST_API.repository.utils.PrepareDB;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UserServlet", value = "/api/v1/users/*")
//@MultipartConfig
public class UserServlet extends HttpServlet {

    @Override
    public void init() {
        PrepareDB.doPrepare();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        UserService.doGet(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        UserService.doPost(request);

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

        UserService.doPut(request);

    }

//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
//
//        UserService.doDelete(request);
//
//    }

}
