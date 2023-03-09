package com.DmitryElkin_Servlets_REST_API.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Enumeration;

@WebServlet(
        name = "StartServlet",
        value = "/StartServlet",
        initParams = {
                @WebInitParam(name = "version", value = "1.001"),
                @WebInitParam(name = "some_param", value = "p_001")
        }
)
public class StartServlet extends HttpServlet {
    @Override
    public void init() {
//        HibernateUtil.getSession();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletConfig config = this.getServletConfig();
        Enumeration<String> e = config.getInitParameterNames();
        while (e.hasMoreElements()){
            String name = (String) e.nextElement();
            String value = (String) config.getInitParameter(name);
        }

        RequestDispatcher view = request.getRequestDispatcher("/templates/start.jsp");
        // don't add your web-app name to the path
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
