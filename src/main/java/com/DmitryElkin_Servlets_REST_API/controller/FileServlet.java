package com.DmitryElkin_Servlets_REST_API.controller;

import com.DmitryElkin_Servlets_REST_API.model.Event;
import com.DmitryElkin_Servlets_REST_API.model.User;
import com.DmitryElkin_Servlets_REST_API.repository.EventRepository;
import com.DmitryElkin_Servlets_REST_API.repository.FileRepository;
import com.DmitryElkin_Servlets_REST_API.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.IOException;

@WebServlet(name = "FileServlet", value = "/FileServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository = new UserRepository();
    private final FileRepository fileRepository = new FileRepository();
    private final EventRepository eventRepository = new EventRepository();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher view = request.getRequestDispatcher("/templates/start.jsp");
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject jsonObject = JsonParser
                .parseString(request.getParameter("userInfoJSON"))
                .getAsJsonObject();

        boolean hasNoError = false;
        String userName;
        int userId;
        User user = null;
        if (jsonObject.has("userId")) {
            userId = Integer.parseInt(jsonObject.get("userId").toString());
            user = userRepository.getById(userId);

            if (jsonObject.has("userName")) {
                userName = jsonObject.get("userName").toString();
                if (userName.equals(user.getName())){
                    hasNoError = true;
                }
            }
        }


        if ((hasNoError) &&
                (request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data"))
        ) {
//        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
            String uploadPath = "D:" + File.separator + "upload" + File.separator;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            for (Part part : request.getParts()) {
                String fileName = part.getSubmittedFileName();
                part.write(uploadPath + File.separator + fileName);

                com.DmitryElkin_Servlets_REST_API.model.File file =
                        new com.DmitryElkin_Servlets_REST_API.model.File(fileName, uploadPath + File.separator);

                Event event = new Event(user, file);
                user.addEvent(event);
                try{
                    fileRepository.insert(file);
                    eventRepository.insert(event);
                    userRepository.update(user);
                }catch (Exception e){
                    e.printStackTrace();
                    hasNoError = false;
                }finally {
                    if (!hasNoError){
                        fileRepository.delete(file.getId());
                        eventRepository.delete(event.getId());
                        user.removeEvent(event);
                        userRepository.update(user);
                    }
                }


            }
        }





    }
}
