package com.DmitryElkin_Servlets_REST_API.controller.service;

import com.DmitryElkin_Servlets_REST_API.model.Event;
import com.DmitryElkin_Servlets_REST_API.model.TypeOfEvent;
import com.DmitryElkin_Servlets_REST_API.model.User;
import com.DmitryElkin_Servlets_REST_API.repository.EventRepository;
import com.DmitryElkin_Servlets_REST_API.repository.FileRepository;
import com.DmitryElkin_Servlets_REST_API.repository.UserRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.util.Enumeration;
import java.util.List;

//*** !!! обязательно спросить про static и многопоточность !!!
//*** Что будет при использовании статических переменных в многопоточке?
public class FileServletService {
    private static final UserRepository userRepository = new UserRepository();
    private static final FileRepository fileRepository = new FileRepository();
    private static final EventRepository eventRepository = new EventRepository();
    private static final String FILE_STORAGE_PATH = "D:" + File.separator + "fileStorage" + File.separator;

    public static void downloadFile(ServletContext ctx, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String errorDescription;
        String uploadPath = FILE_STORAGE_PATH;

        String fileName = request.getParameter("fileName");
        if (fileName == null || fileName.equals("")) {
            errorDescription = "File Name can't be null or empty...";
            showResult(response, true, errorDescription);
            return;
        }

        File file = new File(uploadPath + File.separator + fileName);

        if (!file.exists()) {
            errorDescription = "File doesn't exists on server...";
            showResult(response, true, errorDescription);
            return;
        }

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
        showResult(response, false, "");
    }

    private static void showResult(HttpServletResponse response, boolean hasError, String errorDescription) throws IOException {

        PrintWriter out = response.getWriter();

        String answer = "All right! ))";
        if (hasError) {
            response.setStatus(500);
            answer = "some error was happened: " + errorDescription;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(answer);
        out.flush();
    }

    public static void uploadFile(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        boolean hasError = false;
        String errorDescription;

        Enumeration paramaterNames = request.getParameterNames();
        while (paramaterNames.hasMoreElements()) {
            System.out.println(paramaterNames.nextElement());
        }

        JsonObject jsonObject;
        String param = request.getParameter("userInfoJSON");
        if (param == null) {
            errorDescription = "userInfo is uncorrected or absent or http-method is incorrect...";
            System.out.println(errorDescription);
            showResult(response, true, errorDescription);
            return;
        } else {
            jsonObject = JsonParser
                    .parseString(param)
                    .getAsJsonObject();
        }


        String userName;
        int userId;
        User user;
        if (jsonObject.has("userId")) {
            userId = Integer.parseInt(jsonObject.get("userId").toString());
        } else {
            errorDescription = "There is no userId!";
            System.out.println(errorDescription);
            showResult(response, true, errorDescription);
            return;
        }

        if (jsonObject.has("userName")) {
            userName = jsonObject.get("userName").toString();
        } else {
            errorDescription = "There is no userName!";
            System.out.println(errorDescription);
            showResult(response, true, errorDescription);
            return;
        }

        if (userId != 0) {
            user = userRepository.getById(userId);

            if (user == null) {
                errorDescription = "The user with passed id was not found in BD!";
                System.out.println(errorDescription);
                showResult(response, true, errorDescription);
                return;
            }
            if (!userName.equals(user.getName())) {
                errorDescription = "The name of user, passed through json, is not equal to name of user found in BD by id!";
                System.out.println(errorDescription);
                showResult(response, true, errorDescription);
                return;
            }
        } else {
            List<User> userList = userRepository.getByName(userName);
            if (userList.size() == 0) {
                user = new User(userName);
                userRepository.insert(user);
            } else {
                user = userList.get(0);
            }
        }


        if (request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data")) {
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
                    showResult(response, false, "");

                } catch (Exception e) {
                    e.printStackTrace();
                    hasError = true;
                    errorDescription = e.getMessage();
                    showResult(response, true, errorDescription);
                    return;

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

    }
}
