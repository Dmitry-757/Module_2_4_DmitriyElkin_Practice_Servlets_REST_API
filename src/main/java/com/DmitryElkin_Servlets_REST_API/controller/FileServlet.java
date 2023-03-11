package com.DmitryElkin_Servlets_REST_API.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

@WebServlet(name = "FileServlet", value = "/FileServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher view = request.getRequestDispatcher("/templates/start.jsp");
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data")) {
//        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
            String uploadPath = "D:" + File.separator + "upload" + File.separator;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            for (Part part : request.getParts()) {
                String fileName = part.getSubmittedFileName();
                part.write(uploadPath + File.separator + fileName);
            }
        }


        JsonObject jsonObject = JsonParser
                .parseString(request.getParameter("userAndFileInfoJSON"))
                .getAsJsonObject();

        String userName = "";
        String fileName = "";
        String fileName2 = "";
        if (jsonObject.has("userName")) {
            userName = jsonObject.get("userName").toString();
        }
        if (jsonObject.has("fileName")) {
            fileName = jsonObject.get("fileName").toString();
        }
        if (jsonObject.has("fileName2")) {
            fileName2 = jsonObject.get("fileName2").toString();
        }

        System.out.println(userName + "  " + fileName + " " + fileName2);


    }
}
