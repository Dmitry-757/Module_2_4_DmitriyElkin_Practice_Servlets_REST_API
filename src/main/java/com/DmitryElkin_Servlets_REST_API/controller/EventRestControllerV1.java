package com.DmitryElkin_Servlets_REST_API.controller;

import com.DmitryElkin_Servlets_REST_API.repository.utils.PrepareDB;
import com.DmitryElkin_Servlets_REST_API.service.EventService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "EventRestControllerV1", value = "/api/v1/events/*")
public class EventRestControllerV1 extends HttpServlet {
    private EventService service = new EventService();
    

    @Override
    public void init() {
        PrepareDB.doPrepare();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.doGet(request, response);
    }

}
