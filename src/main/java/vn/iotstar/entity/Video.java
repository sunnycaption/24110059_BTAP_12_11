package vn.iotstar.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entity Video ánh xạ tới bảng Videos.
 * Có quan hệ N-1 với User (userId) và N-1 với Category (categoryId).
 */
@Entity
@Table(name = "Videos")
@NamedQueries({
    @NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v"),
    @NamedQuery(name = "Video.findTop10Latest", query = "SELECT v FROM Video v WHERE v.active = true ORDER BY v.videoId DESC"),
    @NamedQuery(name = "Video.findByUserId", query = "SELECT v FROM Video v WHERE v.user.id = :userId ORDER BY v.videoId DESC"),
    @NamedQuery(name = "Video.countByUserId", query = "SELECT COUNT(v) FROM Video v WHERE v.user.id = :userId")
})
public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "videoId", length = 50)
    private String videoId;

    @Column(name = "title", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String title;

    @Column(name = "poster", columnDefinition = "NVARCHAR(255)")
    private String poster;

    @Column(name = "videoPath", columnDefinition = "NVARCHAR(500)")
    private String videoPath;

    @Column(name = "views")
    private int views;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "active")
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = true)
    private Category categories;

    // Default constructor
    public Video() {
    }

    // Parameterized constructor
    public Video(String videoId, String title, String poster, String videoPath, int views, String description, boolean active, User user) {
        this.videoId = videoId;
        this.title = title;
        this.poster = poster;
        this.videoPath = videoPath;
        this.views = views;
        this.description = description;
        this.active = active;
        this.user = user;
    }

    // Full constructor
    public Video(String videoId, String title, String poster, String videoPath, int views, String description, boolean active, User user, Category categories) {
        this.videoId = videoId;
        this.title = title;
        this.poster = poster;
        this.videoPath = videoPath;
        this.views = views;
        this.description = description;
        this.active = active;
        this.user = user;
        this.categories = categories;
    }

    // Getters and Setters
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
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
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", videoPath='" + videoPath + '\'' +
                ", views=" + views +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", userId=" + (user != null ? user.getId() : "null") +
                '}';
    }
}
