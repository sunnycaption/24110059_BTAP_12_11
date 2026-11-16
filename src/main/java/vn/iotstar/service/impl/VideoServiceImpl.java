package vn.iotstar.service.impl;

import vn.iotstar.dao.VideoDAO;
import vn.iotstar.dao.impl.VideoDAOImpl;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;

import java.util.List;

/**
 * Triển khai nghiệp vụ quản lý Video.
 */
public class VideoServiceImpl implements VideoService {

    private final VideoDAO videoDAO;

    public VideoServiceImpl() {
        this.videoDAO = new VideoDAOImpl();
    }

    public VideoServiceImpl(VideoDAO videoDAO) {
        this.videoDAO = videoDAO;
    }

    @Override
    public void insert(Video video) {
        if (video != null) {
            videoDAO.insert(video);
        }
    }

    @Override
    public void update(Video video) {
        if (video != null) {
            videoDAO.update(video);
        }
    }

    @Override
    public void delete(String videoId) {
        if (videoId != null && !videoId.trim().isEmpty()) {
            videoDAO.delete(videoId.trim());
        }
    }

    @Override
    public Video findById(String videoId) {
        if (videoId == null || videoId.trim().isEmpty()) {
            return null;
        }
        return videoDAO.findById(videoId.trim());
    }

    @Override
    public List<Video> findAll() {
        return videoDAO.findAll();
    }

    @Override
    public List<Video> findTop10Latest() {
        return videoDAO.findTop10Latest();
    }

    @Override
    public List<Video> findByUserId(int userId) {
        return videoDAO.findByUserId(userId);
    }

    @Override
    public List<Video> findByUserIdWithPaging(int userId, int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 6;
        return videoDAO.findByUserIdWithPaging(userId, page, pageSize);
    }

    @Override
    public int countByUserId(int userId) {
        return videoDAO.countByUserId(userId);
    }

    @Override
    public void increaseViews(String videoId) {
        if (videoId != null && !videoId.trim().isEmpty()) {
            videoDAO.increaseViews(videoId.trim());
        }
    }
}
