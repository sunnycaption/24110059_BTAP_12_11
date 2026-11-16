package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;

import java.io.IOException;

/**
 * Servlet Controller hiển thị chi tiết và phát Video (HTML5 Player).
 * Tự động tăng số lượt xem (views + 1) khi người dùng truy cập.
 */
@WebServlet(urlPatterns = {"/video/detail", "/watch"})
public class VideoDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VideoService videoService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.videoService = new VideoServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            int videoId = Integer.parseInt(idParam.trim());

            // 1. Tự động tăng lượt xem (views + 1) và cập nhật xuống DB
            videoService.increaseViews(videoId);

            // 2. Lấy thông tin video sau khi tăng views
            Video video = videoService.findById(videoId);
            if (video == null) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            req.setAttribute("video", video);
            req.getRequestDispatcher("/WEB-INF/views/video-detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
