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
 * Tự động tăng số lượt xem (views) khi người dùng truy cập.
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
        String videoId = req.getParameter("id");
        if (videoId == null || videoId.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        videoId = videoId.trim();

        // 1. Tự động tăng lượt xem (views + 1)
        videoService.increaseViews(videoId);

        // 2. Lấy thông tin video sau khi cập nhật
        Video video = videoService.findById(videoId);
        if (video == null) {
            req.setAttribute("errorMessage", "Video không tồn tại hoặc đã bị xóa!");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        req.setAttribute("video", video);
        req.getRequestDispatcher("/WEB-INF/views/video-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
