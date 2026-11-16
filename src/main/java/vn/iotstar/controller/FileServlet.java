package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Servlet phụ trách stream và phục vụ các file media (ảnh poster, video mp4) từ thư mục uploads.
 */
@WebServlet(urlPatterns = {"/uploads/*"})
public class FileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String fileName = pathInfo.substring(1);
        String uploadDir = req.getServletContext().getRealPath("/uploads");
        File file = new File(uploadDir, fileName);

        if (!file.exists() || !file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Tự động nhận diện MIME type
        String mimeType = req.getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            String lower = file.getName().toLowerCase();
            if (lower.endsWith(".mp4")) {
                mimeType = "video/mp4";
            } else if (lower.endsWith(".webm")) {
                mimeType = "video/webm";
            } else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
                mimeType = "image/jpeg";
            } else if (lower.endsWith(".png")) {
                mimeType = "image/png";
            } else if (lower.endsWith(".gif")) {
                mimeType = "image/gif";
            } else {
                mimeType = "application/octet-stream";
            }
        }

        resp.setContentType(mimeType);
        resp.setContentLengthLong(file.length());
        resp.setHeader("Accept-Ranges", "bytes");

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
