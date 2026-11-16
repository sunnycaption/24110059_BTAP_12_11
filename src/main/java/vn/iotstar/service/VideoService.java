package vn.iotstar.service;

import vn.iotstar.entity.Video;
import java.util.List;

/**
 * Interface Service xử lý nghiệp vụ quản lý Video.
 */
public interface VideoService {
    void insert(Video video);

    void update(Video video);

    void delete(int videoId);

    Video findById(int videoId);

    List<Video> findAll();

    // Lấy 10 video mới nhất hiển thị trang chủ
    List<Video> findTop10Latest();

    // Lấy video của user đang đăng nhập
    List<Video> findByUserId(int userId);

    // Lấy video của user có phân trang (mặc định 6 sp/trang)
    List<Video> findByUserIdWithPaging(int userId, int page, int pageSize);

    // Đếm tổng số video của user để tính tổng số trang
    int countByUserId(int userId);

    // Tăng lượt xem cho video
    void increaseViews(int videoId);
}
