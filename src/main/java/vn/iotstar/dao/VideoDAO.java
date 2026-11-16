package vn.iotstar.dao;

import vn.iotstar.entity.Video;
import java.util.List;

/**
 * Interface DAO cho đối tượng Video sử dụng JPA.
 */
public interface VideoDAO {
    void insert(Video video);

    void update(Video video);

    void delete(int videoId);

    Video findById(int videoId);

    List<Video> findAll();

    // Lấy 10 video mới nhất đang kích hoạt (active = true) sắp xếp giảm dần theo ngày tạo
    List<Video> findTop10Latest();

    // Lấy danh sách video của user đang đăng nhập
    List<Video> findByUserId(int userId);

    // Lấy danh sách video của user có phân trang (6 sp/trang)
    List<Video> findByUserIdWithPaging(int userId, int page, int pageSize);

    // Đếm tổng số video của user
    int countByUserId(int userId);

    // Tăng lượt xem (views) của video lên 1
    void increaseViews(int videoId);
}
