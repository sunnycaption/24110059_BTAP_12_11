<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ - Hệ Thống Video & Xác Thực OTP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body style="display: block; padding: 25px 15px;">
    <div class="container-wide">
        <!-- Navigation Bar -->
        <div class="navbar-custom">
            <div style="font-size: 20px; font-weight: bold; color: #667eea;">
                🎬 Video Portal <span style="font-size: 13px; color: #718096; font-weight: normal;">(24110059)</span>
            </div>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/home" class="active">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/video">Videos Của Tôi</a>
                <a href="${pageContext.request.contextPath}/video/add">+ Đăng Video Mới</a>
                <span style="color: #cbd5e0; margin: 0 10px;">|</span>
                <span style="font-weight: 600; color: #2d3748;">👤 ${sessionScope.currentUser.fullname}</span>
                <a href="${pageContext.request.contextPath}/logout" style="color: #e53e3e;">Đăng Xuất</a>
            </div>
        </div>

        <!-- Banner Chào mừng & Thông tin User -->
        <div class="user-card" style="margin-bottom: 30px;">
            <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px;">
                <div>
                    <h2 style="font-size: 22px; color: #2d3748; margin-bottom: 5px;">
                        Xin chào, ${sessionScope.currentUser.fullname}! 👋
                    </h2>
                    <p style="color: #718096; font-size: 14px;">
                        Tài khoản: <strong>${sessionScope.currentUser.username}</strong> | 
                        Email: <strong>${sessionScope.currentUser.email}</strong> | 
                        Trạng thái: <span style="color: #38a169; font-weight: bold;">Đã kích hoạt OTP ✅</span>
                    </p>
                </div>
                <div style="display: flex; gap: 10px;">
                    <a href="${pageContext.request.contextPath}/video" class="btn btn-primary btn-sm">Quản Lý Video Của Tôi</a>
                    <a href="${pageContext.request.contextPath}/video/add" class="btn btn-success btn-sm">+ Upload Video</a>
                </div>
            </div>
        </div>

        <!-- Section: Top 10 Video Mới Nhất -->
        <div style="margin-bottom: 20px; border-bottom: 2px solid #edf2f7; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
            <h3 style="color: #2d3748; font-size: 20px;">
                🔥 10 Video Mới Nhất Trên Hệ Thống
            </h3>
            <span style="font-size: 13px; color: #718096;">Tự động cập nhật theo thời gian đăng</span>
        </div>

        <c:choose>
            <c:when test="${empty top10Videos}">
                <div style="text-align: center; padding: 40px; color: #718096; background: #f7fafc; border-radius: 8px;">
                    <p>Hiện chưa có video nào được đăng tải trên hệ thống.</p>
                    <a href="${pageContext.request.contextPath}/video/add" class="btn btn-primary btn-sm" style="display: inline-block; margin-top: 10px;">
                        Hãy là người đầu tiên tải lên video!
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="video-grid">
                    <c:forEach var="v" items="${top10Videos}">
                        <div class="video-card">
                            <!-- Poster / Thumbnail -->
                            <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" style="text-decoration: none;">
                                <c:choose>
                                    <c:when test="${not empty v.poster && (v.poster.startsWith('http://') || v.poster.startsWith('https://'))}">
                                        <img src="${v.poster}" alt="${v.title}" class="video-thumbnail" onerror="this.src='https://placehold.co/600x400/667eea/ffffff?text=Video';">
                                    </c:when>
                                    <c:when test="${not empty v.poster}">
                                        <img src="${pageContext.request.contextPath}/uploads/${v.poster}" alt="${v.title}" class="video-thumbnail" onerror="this.src='https://placehold.co/600x400/667eea/ffffff?text=Video';">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://placehold.co/600x400/667eea/ffffff?text=No+Poster" alt="${v.title}" class="video-thumbnail">
                                    </c:otherwise>
                                </c:choose>
                            </a>

                            <!-- Card Body -->
                            <div class="video-card-body">
                                <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="video-card-title">
                                    ${v.title}
                                </a>

                                <p style="font-size: 13px; color: #718096; margin-bottom: 12px; line-height: 1.4; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                                    ${not empty v.description ? v.description : 'Không có mô tả chi tiết cho video này.'}
                                </p>

                                <div class="video-card-meta">
                                    <span>👤 ${v.user.fullname}</span>
                                    <span>👁️ ${v.views} lượt xem</span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
