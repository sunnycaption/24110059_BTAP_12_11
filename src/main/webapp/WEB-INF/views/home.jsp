<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Trang Chủ - Hệ Thống Video & Xác Thực OTP (24110059)</title>
</head>
<body>
    <!-- Banner Chào mừng -->
    <div class="card mb-4 border-0 bg-gradient" style="background: linear-gradient(135deg, #667eea, #764ba2);">
        <div class="card-body text-white p-4">
            <div class="d-flex justify-content-between align-items-center flex-wrap gap-3">
                <div>
                    <h4 class="mb-1">Xin chào, ${sessionScope.currentUser.fullname}! 👋</h4>
                    <p class="mb-0 opacity-75" style="font-size: 14px;">
                        Tài khoản: <strong>${sessionScope.currentUser.username}</strong> |
                        Email: <strong>${sessionScope.currentUser.email}</strong> |
                        Trạng thái: <span class="badge bg-success">Đã kích hoạt ✅</span>
                    </p>
                </div>
                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/video" class="btn btn-light btn-sm">
                        <i class="bi bi-collection-play"></i> Quản Lý Video
                    </a>
                    <a href="${pageContext.request.contextPath}/video/add" class="btn btn-warning btn-sm">
                        <i class="bi bi-plus-circle"></i> Upload Video
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Section: Top 10 Video Mới Nhất -->
    <div class="d-flex justify-content-between align-items-center mb-3 pb-2 border-bottom">
        <h5 class="mb-0"><i class="bi bi-fire text-danger"></i> 10 Video Mới Nhất</h5>
        <small class="text-muted">Sắp xếp theo ngày tạo mới nhất (Top 10)</small>
    </div>

    <c:choose>
        <c:when test="${empty top10Videos}">
            <div class="text-center py-5 bg-light rounded-3">
                <i class="bi bi-camera-video" style="font-size: 48px; color: #adb5bd;"></i>
                <p class="text-muted mt-2">Hiện chưa có video nào được đăng tải trên hệ thống.</p>
                <a href="${pageContext.request.contextPath}/video/add" class="btn btn-primary btn-sm">
                    Hãy là người đầu tiên tải lên video!
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 g-3">
                <c:forEach var="v" items="${top10Videos}">
                    <div class="col">
                        <div class="card h-100 shadow-sm border-0">
                            <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="text-decoration-none">
                                <c:choose>
                                    <c:when test="${not empty v.poster && (v.poster.startsWith('http://') || v.poster.startsWith('https://'))}">
                                        <img src="${v.poster}" class="card-img-top" alt="${v.title}" style="height: 160px; object-fit: cover;" onerror="this.src='https://placehold.co/400x200/667eea/fff?text=Video';">
                                    </c:when>
                                    <c:when test="${not empty v.poster}">
                                        <img src="${pageContext.request.contextPath}/${v.poster.startsWith('uploads/') ? v.poster : 'uploads/'.concat(v.poster)}" class="card-img-top" alt="${v.title}" style="height: 160px; object-fit: cover;" onerror="this.src='https://placehold.co/400x200/667eea/fff?text=Video';">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://placehold.co/400x200/667eea/fff?text=No+Poster" class="card-img-top" alt="${v.title}" style="height: 160px; object-fit: cover;">
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <div class="card-body p-2">
                                <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="text-decoration-none text-dark">
                                    <h6 class="card-title mb-1" style="font-size: 14px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">${v.title}</h6>
                                </a>
                                <div class="d-flex justify-content-between" style="font-size: 12px; color: #718096;">
                                    <span><i class="bi bi-person"></i> ${v.user.fullname}</span>
                                    <span><i class="bi bi-eye"></i> ${v.views}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>
