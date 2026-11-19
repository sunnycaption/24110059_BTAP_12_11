<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title">Java Web MVC JPA</sitemesh:write></title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { padding-top: 56px; background-color: #f8f9fa; }
        .navbar-brand i { color: #667eea; }
        .footer { background: #343a40; color: #adb5bd; padding: 20px 0; margin-top: 40px; font-size: 14px; }
        .content-wrapper { min-height: calc(100vh - 180px); padding: 20px 0; }
        .avatar-img { width: 32px; height: 32px; border-radius: 50%; object-fit: cover; }
    </style>
    <sitemesh:write property="head"/>
</head>
<body>
    <!-- Navbar Bootstrap 5 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top shadow-sm">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
                <i class="bi bi-camera-video-fill"></i> Video Portal
                <small class="text-muted" style="font-size: 11px;">(24110059)</small>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarMain">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/home">
                            <i class="bi bi-house-door"></i> Trang Chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/video">
                            <i class="bi bi-collection-play"></i> Videos Của Tôi
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/video/add">
                            <i class="bi bi-plus-circle"></i> Đăng Video
                        </a>
                    </li>
                </ul>
                <c:if test="${not empty sessionScope.currentUser}">
                    <ul class="navbar-nav">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="userDropdown"
                               role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.currentUser.images && (sessionScope.currentUser.images.startsWith('http://') || sessionScope.currentUser.images.startsWith('https://'))}">
                                        <img src="${sessionScope.currentUser.images}" alt="Avatar" class="avatar-img me-1" onerror="this.src='https://placehold.co/32x32/667eea/fff?text=U';">
                                    </c:when>
                                    <c:when test="${not empty sessionScope.currentUser.images}">
                                        <img src="${pageContext.request.contextPath}/${sessionScope.currentUser.images.startsWith('uploads/') ? sessionScope.currentUser.images : 'uploads/'.concat(sessionScope.currentUser.images)}" alt="Avatar" class="avatar-img me-1" onerror="this.src='https://placehold.co/32x32/667eea/fff?text=U';">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="bi bi-person-circle me-1"></i>
                                    </c:otherwise>
                                </c:choose>
                                ${sessionScope.currentUser.fullname}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="bi bi-person-badge"></i> Hồ Sơ Cá Nhân
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right"></i> Đăng Xuất
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </c:if>
            </div>
        </div>
    </nav>

    <!-- Main Content Area -->
    <div class="content-wrapper">
        <div class="container">
            <sitemesh:write property="body"/>
        </div>
    </div>

    <!-- Footer -->
    <footer class="footer text-center">
        <div class="container">
            <p class="mb-1">&copy; 2026 - Java Web MVC JPA | MSSV: 24110059 - Lê Thị Thanh Thúy</p>
            <p class="mb-0"><small>Jakarta EE 10 &middot; Servlet/JSP &middot; JPA &middot; SiteMesh 3 &middot; Bootstrap 5</small></p>
        </div>
    </footer>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Bootstrap Form Validation Script -->
    <script>
    (function () {
        'use strict';
        var forms = document.querySelectorAll('.needs-validation');
        Array.prototype.slice.call(forms).forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    })();
    </script>
</body>
</html>
