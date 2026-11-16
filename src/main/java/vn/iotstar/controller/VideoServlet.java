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
 * Cấu hình chuẩn Jakarta Servlet Multipart theo đề bài:
 * - fileSizeThreshold = 2MB
 * - maxFileSize = 50MB
 * - maxRequestSize = 100MB
 */
@WebServlet(urlPatterns = {"/video", "/video/add", "/video/edit", "/video/delete"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 50,      // 50MB cho file video & ảnh
        maxRequestSize = 1024 * 1024 * 100   // 100MB tổng request
)
public class VideoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6; // Phân trang cố định chuẩn 6 sản phẩm/trang theo yêu cầu
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
     * Hiển thị danh sách video của user với phân trang chuẩn 6 sp/trang tại URL /video.
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

        // Lấy danh sách video của user đang đăng nhập có phân trang (6 sp/trang)
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
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        try {
            int videoId = Integer.parseInt(idParam.trim());
            Video video = videoService.findById(videoId);
            if (video == null || video.getUser().getId() != currentUser.getId()) {
                req.getSession().setAttribute("message", "Video không tồn tại hoặc bạn không có quyền sửa!");
                resp.sendRedirect(req.getContextPath() + "/video");
                return;
            }

            req.setAttribute("video", video);
            req.setAttribute("mode", "edit");
            req.getRequestDispatcher("/WEB-INF/views/video-form.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/video");
        }
    }

    /**
     * Xử lý thêm mới video với Upload Multipart (posterFile & videoFile).
     */
    private void handleAddVideo(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws ServletException, IOException {
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

        // 1. Upload file Poster bằng Multipart
        Part posterPart = req.getPart("posterFile");
        String posterPath = saveUploadedFile(posterPart, req, "poster");
        if (posterPath == null) {
            posterPath = req.getParameter("posterUrl"); // Fallback URL ngoài nếu có
        }

        // 2. Upload file Video bằng Multipart
        Part videoPart = req.getPart("videoFile");
        String videoFilePath = saveUploadedFile(videoPart, req, "video");
        if (videoFilePath == null) {
            videoFilePath = req.getParameter("videoUrl"); // Fallback URL ngoài nếu có
        }

        Video newVideo = new Video(title.trim(), posterPath, videoFilePath, description, active, currentUser);
        videoService.insert(newVideo);

        req.getSession().setAttribute("message", "Thêm video mới thành công!");
        resp.sendRedirect(req.getContextPath() + "/video");
    }

    /**
     * Xử lý cập nhật video: nếu người dùng không chọn file mới thì giữ nguyên poster và videoFile cũ.
     */
    private void handleEditVideo(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws ServletException, IOException {
        String idParam = req.getParameter("videoId");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        try {
            int videoId = Integer.parseInt(idParam.trim());
            Video existing = videoService.findById(videoId);
            if (existing == null || existing.getUser().getId() != currentUser.getId()) {
                req.getSession().setAttribute("message", "Bạn không có quyền chỉnh sửa video này!");
                resp.sendRedirect(req.getContextPath() + "/video");
                return;
            }

            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String activeStr = req.getParameter("active");
            boolean active = "on".equals(activeStr) || "true".equalsIgnoreCase(activeStr);

            existing.setTitle(title.trim());
            existing.setDescription(description);
            existing.setActive(active);

            // Xử lý upload poster: nếu có file mới thì lưu file mới, nếu không thì giữ nguyên poster cũ
            Part posterPart = req.getPart("posterFile");
            String newPosterPath = saveUploadedFile(posterPart, req, "poster");
            if (newPosterPath != null) {
                existing.setPoster(newPosterPath);
            } else {
                String posterUrl = req.getParameter("posterUrl");
                if (posterUrl != null && !posterUrl.trim().isEmpty()) {
                    existing.setPoster(posterUrl.trim());
                }
                // Nếu cả 2 đều không nhập, giữ nguyên existing.getPoster()
            }

            // Xử lý upload video: nếu có file mới thì lưu file mới, nếu không thì giữ nguyên videoFile cũ
            Part videoPart = req.getPart("videoFile");
            String newVideoPath = saveUploadedFile(videoPart, req, "video");
            if (newVideoPath != null) {
                existing.setVideoFile(newVideoPath);
            } else {
                String videoUrl = req.getParameter("videoUrl");
                if (videoUrl != null && !videoUrl.trim().isEmpty()) {
                    existing.setVideoFile(videoUrl.trim());
                }
                // Nếu cả 2 đều không nhập, giữ nguyên existing.getVideoFile()
            }

            videoService.update(existing);
            req.getSession().setAttribute("message", "Cập nhật video thành công!");
            resp.sendRedirect(req.getContextPath() + "/video");
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/video");
        }
    }

    /**
     * Xử lý xóa video.
     */
    private void handleDeleteVideo(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int videoId = Integer.parseInt(idParam.trim());
                Video v = videoService.findById(videoId);
                if (v != null && v.getUser().getId() == currentUser.getId()) {
                    videoService.delete(videoId);
                    req.getSession().setAttribute("message", "Đã xóa video thành công!");
                } else {
                    req.getSession().setAttribute("message", "Không tìm thấy video hoặc bạn không có quyền xóa!");
                }
            } catch (NumberFormatException e) {
                req.getSession().setAttribute("message", "Mã video không hợp lệ!");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/video");
    }

    /**
     * Lưu trữ file vật lý vào thư mục uploads/ trong webapp và trả về đường dẫn tương đối uploads/<tên_file>.
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

        // Lưu vật lý vào thư mục uploads trong ứng dụng webapp
        String uploadDir = req.getServletContext().getRealPath("/uploads");
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        part.write(uploadDir + File.separator + uniqueFileName);

        // Trả về đường dẫn tương đối lưu vào DB: uploads/<tên_file>
        return "uploads/" + uniqueFileName;
    }
}
