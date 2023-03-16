package com.DmitryElkin_Servlets_REST_API.controller;

import com.DmitryElkin_Servlets_REST_API.model.Event;
import com.DmitryElkin_Servlets_REST_API.model.TypeOfEvent;
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

import java.io.*;
import java.util.List;

@WebServlet(name = "FileServlet", value = "/FileServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileServlet extends HttpServlet {
    //    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository = new UserRepository();
    private final FileRepository fileRepository = new FileRepository();
    private final EventRepository eventRepository = new EventRepository();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
//        RequestDispatcher view = request.getRequestDispatcher("/templates/start.jsp");
//        view.forward(request, response);
        String uploadPath = "D:" + File.separator + "fileStorage" + File.separator;

        String fileName = request.getParameter("fileName");
        if (fileName == null || fileName.equals("")) {
            throw new ServletException("File Name can't be null or empty");
        }

//        File file = new File(request.getServletContext().getAttribute("FILES_DIR")+File.separator+fileName);
        File file = new File(uploadPath + File.separator + fileName);

        if (!file.exists()) {
            throw new ServletException("File doesn't exists on server.");
        }
        System.out.println("File location on server::" + file.getAbsolutePath());
        ServletContext ctx = getServletContext();

        String mimeType = ctx.getMimeType(file.getAbsolutePath());
        response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (InputStream fis = new FileInputStream(file);
             ServletOutputStream os = response.getOutputStream()) {
            byte[] bufferData = new byte[1024];
            int read;
            while ((read = fis.read(bufferData)) != -1) {
                os.write(bufferData, 0, read);
            }
            os.flush();
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        System.out.println("File downloaded at client successfully");

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

            if (user == null) {
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
            List<User> userList = userRepository.getByName(userName);
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
            String uploadPath = "D:" + File.separator + "fileStorage" + File.separator;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            for (Part part : request.getParts()) {
                String fileName = part.getSubmittedFileName();
                if (fileName == null) {
                    break;
                }
                part.write(uploadPath + File.separator + fileName);

                com.DmitryElkin_Servlets_REST_API.model.File file =
                        new com.DmitryElkin_Servlets_REST_API.model.File(fileName, uploadPath);

                Event event = new Event(user, file, TypeOfEvent.WRITE);
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
            answer = "some error was happened: " + errorDescription;
        }
        String jsonString = (new Gson()).toJson(answer);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();

    }
}
