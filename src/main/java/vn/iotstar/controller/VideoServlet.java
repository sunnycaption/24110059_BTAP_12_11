package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Servlet Controller quản lý Video của người dùng (CRUD, Upload Multipart & Phân trang 6 sp/trang).
 */
@WebServlet(urlPatterns = {"/video", "/video/add", "/video/edit", "/video/delete"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 100,      // 100MB cho file video & ảnh
        maxRequestSize = 1024 * 1024 * 120    // 120MB tổng request
)
public class VideoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6; // Phân trang 6 sản phẩm/trang theo yêu cầu đề bài
    private VideoService videoService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.videoService = new VideoServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Kiểm tra đăng nhập
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getServletPath();

        switch (path) {
            case "/video/add":
                req.setAttribute("mode", "add");
                req.getRequestDispatcher("/WEB-INF/views/video-form.jsp").forward(req, resp);
                break;

            case "/video/edit":
                showEditForm(req, resp, currentUser);
                break;

            case "/video/delete":
                handleDeleteVideo(req, resp, currentUser);
                break;

            case "/video":
            default:
                showVideoList(req, resp, currentUser);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getServletPath();
        if ("/video/add".equals(path)) {
            handleAddVideo(req, resp, currentUser);
        } else if ("/video/edit".equals(path)) {
            handleEditVideo(req, resp, currentUser);
        } else {
            resp.sendRedirect(req.getContextPath() + "/video");
        }
    }

    /**
     * Hiển thị danh sách video của user với phân trang 6 sp/trang.
     */
    private void showVideoList(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws ServletException, IOException {
        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam.trim());
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Lấy danh sách video của user đang đăng nhập có phân trang
        List<Video> videos = videoService.findByUserIdWithPaging(currentUser.getId(), page, PAGE_SIZE);
        int totalVideos = videoService.countByUserId(currentUser.getId());
        int totalPages = (int) Math.ceil((double) totalVideos / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;

        req.setAttribute("videos", videos);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalVideos", totalVideos);

        // Flash message
        HttpSession session = req.getSession();
        String message = (String) session.getAttribute("message");
        if (message != null) {
            req.setAttribute("message", message);
            session.removeAttribute("message");
        }

        req.getRequestDispatcher("/WEB-INF/views/video-list.jsp").forward(req, resp);
    }

    /**
     * Hiển thị form chỉnh sửa video.
     */
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws ServletException, IOException {
        String videoId = req.getParameter("id");
        if (videoId == null || videoId.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        Video video = videoService.findById(videoId.trim());
        if (video == null || video.getUser().getId() != currentUser.getId()) {
            req.getSession().setAttribute("message", "Video không tồn tại hoặc bạn không có quyền sửa!");
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        req.setAttribute("video", video);
        req.setAttribute("mode", "edit");
        req.getRequestDispatcher("/WEB-INF/views/video-form.jsp").forward(req, resp);
    }

    /**
     * Xử lý thêm mới video cùng Multipart Upload (Poster & Video file).
     */
    private void handleAddVideo(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws ServletException, IOException {
        String videoId = req.getParameter("videoId");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String activeStr = req.getParameter("active");
        boolean active = "on".equals(activeStr) || "true".equalsIgnoreCase(activeStr);

        if (title == null || title.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Tiêu đề video không được để trống!");
            req.setAttribute("mode", "add");
            req.getRequestDispatcher("/WEB-INF/views/video-form.jsp").forward(req, resp);
            return;
        }

        if (videoId == null || videoId.trim().isEmpty()) {
            videoId = "VID" + System.currentTimeMillis();
        } else {
            videoId = videoId.trim();
        }

        // Kiểm tra trùng videoId
        if (videoService.findById(videoId) != null) {
            req.setAttribute("errorMessage", "Mã Video '" + videoId + "' đã tồn tại! Vui lòng chọn mã khác.");
            req.setAttribute("mode", "add");
            req.getRequestDispatcher("/WEB-INF/views/video-form.jsp").forward(req, resp);
            return;
        }

        // 1. Upload ảnh Poster
        Part posterPart = req.getPart("posterFile");
        String posterName = saveUploadedFile(posterPart, req, "poster");
        if (posterName == null) {
            posterName = req.getParameter("posterUrl"); // Link ngoài nếu có
        }

        // 2. Upload file Video
        Part videoPart = req.getPart("videoFile");
        String videoPath = saveUploadedFile(videoPart, req, "video");
        if (videoPath == null) {
            videoPath = req.getParameter("videoUrl"); // Link ngoài hoặc YouTube nếu có
        }

        Video newVideo = new Video(videoId, title.trim(), posterName, videoPath, 0, description, active, currentUser);
        videoService.insert(newVideo);

        req.getSession().setAttribute("message", "Thêm video mới thành công!");
        resp.sendRedirect(req.getContextPath() + "/video");
    }

    /**
     * Xử lý chỉnh sửa video cùng Multipart Upload.
     */
    private void handleEditVideo(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws ServletException, IOException {
        String videoId = req.getParameter("videoId");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String activeStr = req.getParameter("active");
        boolean active = "on".equals(activeStr) || "true".equalsIgnoreCase(activeStr);

        if (videoId == null || videoId.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        Video existing = videoService.findById(videoId.trim());
        if (existing == null || existing.getUser().getId() != currentUser.getId()) {
            req.getSession().setAttribute("message", "Bạn không có quyền chỉnh sửa video này!");
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        existing.setTitle(title.trim());
        existing.setDescription(description);
        existing.setActive(active);

        // Upload ảnh mới nếu có
        Part posterPart = req.getPart("posterFile");
        String posterName = saveUploadedFile(posterPart, req, "poster");
        if (posterName != null) {
            existing.setPoster(posterName);
        } else {
            String posterUrl = req.getParameter("posterUrl");
            if (posterUrl != null && !posterUrl.trim().isEmpty()) {
                existing.setPoster(posterUrl.trim());
            }
        }

        // Upload video mới nếu có
        Part videoPart = req.getPart("videoFile");
        String videoPath = saveUploadedFile(videoPart, req, "video");
        if (videoPath != null) {
            existing.setVideoPath(videoPath);
        } else {
            String videoUrl = req.getParameter("videoUrl");
            if (videoUrl != null && !videoUrl.trim().isEmpty()) {
                existing.setVideoPath(videoUrl.trim());
            }
        }

        videoService.update(existing);
        req.getSession().setAttribute("message", "Cập nhật video thành công!");
        resp.sendRedirect(req.getContextPath() + "/video");
    }

    /**
     * Xử lý xóa video.
     */
    private void handleDeleteVideo(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws IOException {
        String videoId = req.getParameter("id");
        if (videoId != null && !videoId.trim().isEmpty()) {
            Video v = videoService.findById(videoId.trim());
            if (v != null && v.getUser().getId() == currentUser.getId()) {
                videoService.delete(videoId.trim());
                req.getSession().setAttribute("message", "Đã xóa video thành công!");
            } else {
                req.getSession().setAttribute("message", "Không tìm thấy video hoặc bạn không có quyền xóa!");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/video");
    }

    /**
     * Tiện ích lưu trữ file Multipart an toàn vào thư mục uploads của webapp.
     */
    private String saveUploadedFile(Part part, HttpServletRequest req, String prefix) throws IOException {
        if (part == null || part.getSize() == 0) {
            return null;
        }

        String submittedFileName = part.getSubmittedFileName();
        if (submittedFileName == null || submittedFileName.trim().isEmpty()) {
            return null;
        }

        String originalName = Paths.get(submittedFileName).getFileName().toString();
        String ext = "";
        int dotIndex = originalName.lastIndexOf(".");
        if (dotIndex >= 0) {
            ext = originalName.substring(dotIndex);
        }

        String uniqueFileName = prefix + "_" + System.currentTimeMillis() + ext;

        // Lưu vào thư mục uploads trong ứng dụng
        String uploadDir = req.getServletContext().getRealPath("/uploads");
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        part.write(uploadDir + File.separator + uniqueFileName);
        return uniqueFileName;
    }
}
