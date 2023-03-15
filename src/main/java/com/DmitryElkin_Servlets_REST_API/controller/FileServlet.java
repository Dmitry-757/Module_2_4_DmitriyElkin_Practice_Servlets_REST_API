package com.DmitryElkin_Servlets_REST_API.controller;

import com.DmitryElkin_Servlets_REST_API.model.Event;
import com.DmitryElkin_Servlets_REST_API.model.User;
import com.DmitryElkin_Servlets_REST_API.repository.EventRepository;
import com.DmitryElkin_Servlets_REST_API.repository.FileRepository;
import com.DmitryElkin_Servlets_REST_API.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
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

        boolean hasError = false;
        String errorDescription = "";

        JsonObject jsonObject = JsonParser
                .parseString(request.getParameter("userInfoJSON"))
                .getAsJsonObject();

        if (jsonObject.isEmpty()) {
            hasError = true;
            errorDescription = "userInfo is uncorrected or absent";
            System.out.println(errorDescription);
        }

        String userName = "";
        int userId = 0;
        User user = null;
        if ((!hasError) && (jsonObject.has("userId"))) {
            userId = Integer.parseInt(jsonObject.get("userId").toString());
        } else {
            hasError = true;
            errorDescription = "There is no userId!";
            System.out.println(errorDescription);
        }

        if ((!hasError) && (jsonObject.has("userName"))) {
            userName = jsonObject.get("userName").toString();
        } else {
            hasError = true;
            errorDescription = "There is no userName!";
            System.out.println(errorDescription);
        }

        if ((!hasError) && (userId != 0)) {
            user = userRepository.getById(userId);

            if (user == null){
                errorDescription = "The user with passed id was not found in BD!";
                System.out.println(errorDescription);
                hasError = true;
            }
            if ((user != null) && (!userName.equals(user.getName()))) {
                hasError = true;
                errorDescription = "The name of user, passed through json, is not equal to name of user found in BD by id!";
                System.out.println(errorDescription);
            }
        } else if (!hasError) {
            List<User> userList = userRepository.findByName(userName);
            if (userList.size() == 0) {
                user = new User(userName);
                userRepository.insert(user);
            } else {
                user = userList.get(0);
            }
        }


        if ((!hasError) &&
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
                if (fileName == null){
                    break;
                }
                part.write(uploadPath + File.separator + fileName);

                com.DmitryElkin_Servlets_REST_API.model.File file =
                        new com.DmitryElkin_Servlets_REST_API.model.File(fileName, uploadPath);

                Event event = new Event(user, file);
                user.addEvent(event);
                try {
                    fileRepository.insert(file);
                    eventRepository.insert(event);
                    userRepository.update(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    hasError = true;
                    errorDescription = e.getMessage();
                } finally {
                    if (hasError) {
                        fileRepository.delete(file.getId());
                        eventRepository.delete(event.getId());
                        user.removeEvent(event);
                        userRepository.update(user);
                    }
                }
            }
        }

        String answer = "All right! ))";
        if (hasError) {
            answer = "some error was happened: "+ errorDescription;
        }
        String jsonString = (new Gson()).toJson(answer);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();

    }
}
