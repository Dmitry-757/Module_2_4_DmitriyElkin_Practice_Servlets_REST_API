package com.DmitryElkin_Servlets_REST_API.service;


import com.DmitryElkin_Servlets_REST_API.model.User;
import com.DmitryElkin_Servlets_REST_API.repository.EventRepository;
import com.DmitryElkin_Servlets_REST_API.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventService {
    private final EventRepository eventRepository = new EventRepository();
    private final UserRepository userRepository = new UserRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object obj = null;
        String match = request.getHttpServletMapping().getMatchValue();
        if (Objects.equals(match, "")) {
            obj = eventRepository.getAll();

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
                    User user = userRepository.getById(id);
                    obj = eventRepository.getByUser(user);
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


}
