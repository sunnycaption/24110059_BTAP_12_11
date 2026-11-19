<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!-- Web Header / Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top shadow-sm">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/home">
            <i class="bi bi-camera-video-fill text-primary me-2"></i>
            <span class="fw-bold">Video Portal</span>
            <span class="badge bg-secondary ms-2" style="font-size: 11px;">24110059</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain" aria-controls="navbarMain" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarMain">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">
                        <i class="bi bi-house-door me-1"></i> Trang Chủ
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/video">
                        <i class="bi bi-collection-play me-1"></i> Videos Của Tôi
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/video/add">
                        <i class="bi bi-plus-circle me-1"></i> Đăng Video
                    </a>
                </li>
            </ul>

            <c:set var="authUser" value="${not empty sessionScope.currentUser ? sessionScope.currentUser : sessionScope.user}" />
            <c:choose>
                <c:when test="${not empty authUser}">
                    <ul class="navbar-nav align-items-center">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="userDropdown"
                               role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <c:choose>
                                    <c:when test="${not empty authUser.images && (authUser.images.startsWith('http://') || authUser.images.startsWith('https://'))}">
                                        <img src="${authUser.images}" alt="Avatar" class="avatar-img me-2" onerror="this.src='https://placehold.co/32x32/667eea/fff?text=U';">
                                    </c:when>
                                    <c:when test="${not empty authUser.images}">
                                        <img src="${pageContext.request.contextPath}/${authUser.images.startsWith('uploads/') ? authUser.images : 'uploads/'.concat(authUser.images)}" alt="Avatar" class="avatar-img me-2" onerror="this.src='https://placehold.co/32x32/667eea/fff?text=U';">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="bi bi-person-circle fs-5 me-2"></i>
                                    </c:otherwise>
                                </c:choose>
                                <span class="fw-medium">${authUser.fullname}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow-sm" aria-labelledby="userDropdown">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="bi bi-person-badge me-2 text-primary"></i> Hồ Sơ Cá Nhân
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i> Đăng Xuất
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </c:when>
                <c:otherwise>
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                <i class="bi bi-box-arrow-in-right me-1"></i> Đăng Nhập
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-outline-light btn-sm ms-2" href="${pageContext.request.contextPath}/register">
                                <i class="bi bi-person-plus me-1"></i> Đăng Ký
                            </a>
                        </li>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>
