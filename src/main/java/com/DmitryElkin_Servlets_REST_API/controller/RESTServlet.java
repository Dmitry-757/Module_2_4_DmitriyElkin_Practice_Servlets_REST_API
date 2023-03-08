package com.DmitryElkin_Servlets_REST_API.controller;

import com.DmitryElkin_Servlets_REST_API.model.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RESTServlet", value = "/RESTServlet")
public class RESTServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File file = new File("fileName","path2File");
        final String jsonItem = objectMapper.writeValueAsString(file);
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(jsonItem);

//        objectMapper.readValue(jsonItem, File.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        try {
            File file = objectMapper.readValue(jb.toString(), File.class);
            System.out.println(file.toString());
//            JSONObject jsonObject =  HTTP.toJSONObject(jb.toString());
//        } catch (JSONException e) {
            // crash and burn
        } catch (Exception e) {
            throw new IOException("Error parsing JSON request string");
        }

        File obj = new Gson().fromJson(request.getReader(), File.class);
        System.out.println(obj.toString());
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
//        JsonObject o = parser.parse(request.getParameter("jsondata"));
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        JsonObject jsonObject = JsonParser.parseString(jb.toString()).getAsJsonObject();

    }

}
