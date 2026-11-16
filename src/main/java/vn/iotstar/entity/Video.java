package vn.iotstar.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity Video ánh xạ tới bảng Videos.
 * Có quan hệ N-1 với User (userId).
 */
@Entity
@Table(name = "Videos")
@NamedQueries({
    @NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v ORDER BY v.createdDate DESC"),
    @NamedQuery(name = "Video.findTop10Latest", query = "SELECT v FROM Video v WHERE v.active = true ORDER BY v.createdDate DESC, v.videoId DESC"),
    @NamedQuery(name = "Video.findByUserId", query = "SELECT v FROM Video v WHERE v.user.id = :userId ORDER BY v.createdDate DESC, v.videoId DESC"),
    @NamedQuery(name = "Video.countByUserId", query = "SELECT COUNT(v) FROM Video v WHERE v.user.id = :userId")
})
public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "videoId")
    private int videoId;

    @Column(name = "title", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String title;

    @Column(name = "poster", columnDefinition = "NVARCHAR(255)")
    private String poster; // Lưu đường dẫn tương đối (ví dụ: uploads/poster_xxx.jpg)

    @Column(name = "videoFile", columnDefinition = "NVARCHAR(500)")
    private String videoFile; // Lưu đường dẫn file video (ví dụ: uploads/video_xxx.mp4) hoặc URL

    @Column(name = "views")
    private int views = 0;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "active")
    private boolean active = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDate")
    private Date createdDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = true)
    private Category categories;

    // Default constructor
    public Video() {
        this.createdDate = new Date();
        this.views = 0;
        this.active = true;
    }

    // Constructor phục vụ thêm mới video
    public Video(String title, String poster, String videoFile, String description, boolean active, User user) {
        this.title = title;
        this.poster = poster;
        this.videoFile = videoFile;
        this.description = description;
        this.active = active;
        this.user = user;
        this.views = 0;
        this.createdDate = new Date();
    }

    // Full constructor
    public Video(int videoId, String title, String poster, String videoFile, int views, String description, boolean active, Date createdDate, User user) {
        this.videoId = videoId;
        this.title = title;
        this.poster = poster;
        this.videoFile = videoFile;
        this.views = views;
        this.description = description;
        this.active = active;
        this.createdDate = (createdDate != null) ? createdDate : new Date();
        this.user = user;
    }

    // Getters and Setters
    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public void setVideoId(String videoId) {
        if (videoId != null && !videoId.trim().isEmpty()) {
            try {
                this.videoId = Integer.parseInt(videoId.trim());
            } catch (NumberFormatException e) {
                this.videoId = 0;
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    // Alias methods hỗ trợ videoUrl / videoPath
    public String getVideoUrl() {
        return videoFile;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoFile = videoUrl;
    }

    public String getVideoPath() {
        return videoFile;
    }

    public void setVideoPath(String videoPath) {
        this.videoFile = videoPath;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategories() {
        return categories;
    }

    public void setCategories(Category categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoId=" + videoId +
                ", title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", videoFile='" + videoFile + '\'' +
                ", views=" + views +
                ", active=" + active +
                ", createdDate=" + createdDate +
                ", userId=" + (user != null ? user.getId() : "null") +
                '}';
    }
}
