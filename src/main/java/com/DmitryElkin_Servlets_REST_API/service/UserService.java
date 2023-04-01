package com.DmitryElkin_Servlets_REST_API.service;

import com.DmitryElkin_Servlets_REST_API.model.User;
import com.DmitryElkin_Servlets_REST_API.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {

    private final UserRepository userRepository = new UserRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object obj = null;
        String match = request.getHttpServletMapping().getMatchValue();
        if (Objects.equals(match, "")) {
            obj = userRepository.getAll();

        } else {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(match);
            int start;
            int end;
            int id;
            if (matcher.find()) {
                start = matcher.start();
                end = matcher.end();
                id = Integer.parseInt(match.substring(start, end));
                if (id !=0 ) {
                    obj = userRepository.getById(id);
                }
            }
        }

        if (obj != null) {
            final String jsonItem = objectMapper.writeValueAsString(obj);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(jsonItem);
        }
    }

    public void doPost(HttpServletRequest request) {
        JsonObject jsonObject = getJSON(request);

        String userName = jsonObject.get("userName").toString();
        int userId = Integer.parseInt(jsonObject.get("userId").toString());
        User user;
        if (userId == 0) {
            user = new User(userName);
            userRepository.insert(user);
        }
    }

    public void doPut(HttpServletRequest request) {
        JsonObject jsonObject = getJSON(request);
        String userName = jsonObject.get("userName").toString();
        int userId = Integer.parseInt(jsonObject.get("userId").toString());
        User user;
        if (userId != 0) {
            user = new User(userName);
            userRepository.update(user);
        }
    }

//    public void doDelete(HttpServletRequest request) {
//
//        StringBuilder jb = new StringBuilder();
//        String line;
//        try (BufferedReader reader = request.getReader()) {
//            while ((line = reader.readLine()) != null)
//                jb.append(line);
//        } catch (Exception e) { /*report an error*/ }
//
//        JsonObject jsonObject = JsonParser.parseString(jb.toString()).getAsJsonObject();
//
//        int userId = Integer.parseInt(jsonObject.get("userId").toString());
//        if (userId != 0) {
//            userRepository.delete(userId);
//        }
//
//    }

    private JsonObject getJSON(HttpServletRequest request){
        StringBuilder jb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        return JsonParser.parseString(jb.toString()).getAsJsonObject();
    }

}
