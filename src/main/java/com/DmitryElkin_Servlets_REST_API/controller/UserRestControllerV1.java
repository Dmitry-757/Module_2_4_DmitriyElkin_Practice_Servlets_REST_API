package com.DmitryElkin_Servlets_REST_API.controller;

import com.DmitryElkin_Servlets_REST_API.service.UserService;
import com.DmitryElkin_Servlets_REST_API.repository.utils.PrepareDB;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UserRestControllerV1", value = "/api/v1/users/*")
//@MultipartConfig
public class UserRestControllerV1 extends HttpServlet {
    UserService userService = new UserService();

    @Override
    public void init() {
        PrepareDB.doPrepare();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        userService.doGet(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        userService.doPost(request);

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

        userService.doPut(request);

    }

//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
//
//        UserService.doDelete(request);
//
//    }

}
