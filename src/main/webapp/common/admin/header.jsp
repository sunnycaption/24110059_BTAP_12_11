<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!-- Admin Header -->
<nav class="navbar navbar-expand-lg navbar-dark bg-secondary fixed-top shadow-sm">
    <div class="container-fluid px-4">
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/admin">
            <i class="bi bi-shield-lock-fill text-warning me-2"></i>
            <span class="fw-bold">Admin Dashboard</span>
            <span class="badge bg-dark ms-2" style="font-size: 11px;">MSSV: 24110059</span>
        </a>
        <div class="d-flex align-items-center">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-light btn-sm me-3">
                <i class="bi bi-globe me-1"></i> Xem Trang Web
            </a>
            <c:set var="authUser" value="${not empty sessionScope.currentUser ? sessionScope.currentUser : sessionScope.user}" />
            <span class="text-white me-3 d-none d-md-inline">Xin chào, <strong>${not empty authUser ? authUser.fullname : 'Admin'}</strong></span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">
                <i class="bi bi-box-arrow-right me-1"></i> Đăng Xuất
            </a>
        </div>
    </div>
</nav>
