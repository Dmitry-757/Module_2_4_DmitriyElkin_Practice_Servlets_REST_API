package com.DmitryElkin_Servlets_REST_API.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "FileServlet", value = "/FileServlet")
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());

        try {
            List<FileItem> multiFiles = fileUpload.parseRequest((RequestContext) request);
            for (FileItem fileItem : multiFiles){
                try {
                    fileItem.write(new File("D:\\upload\\" + fileItem.getName()));
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        } catch (FileUploadException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
    }
}
