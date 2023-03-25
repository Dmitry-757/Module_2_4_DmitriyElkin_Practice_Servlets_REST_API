package com.DmitryElkin_Servlets_REST_API.controller;

import com.DmitryElkin_Servlets_REST_API.model.User;
import com.DmitryElkin_Servlets_REST_API.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "UserServlet", value = "/api/v1/users/*")
//@MultipartConfig
public class UserServlet extends HttpServlet {
    private final UserRepository userRepository = new UserRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String match = request.getHttpServletMapping().getMatchValue();
        if (Objects.equals(match, "")) {
            List<User> usersList = userRepository.getAll();

            final String jsonItem = objectMapper.writeValueAsString(usersList);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(jsonItem);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        JsonObject jsonObject;
//        String param = request.getParameter("userInfoJSON");
//        jsonObject = JsonParser
//                    .parseString(param)
//                    .getAsJsonObject();
//        int userId = Integer.parseInt(jsonObject.get("userId").toString());

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        JsonObject jsonObject = JsonParser.parseString(jb.toString()).getAsJsonObject();


        String userName = jsonObject.get("userName").toString();
        int userId = Integer.parseInt(jsonObject.get("userId").toString());
        User user;
        if (userId == 0) {
            user = new User(userName);
            userRepository.insert(user);
        }


    }
}
