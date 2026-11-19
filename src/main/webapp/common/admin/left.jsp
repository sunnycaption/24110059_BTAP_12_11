<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!-- Admin Sidebar / Left Menu -->
<div class="card shadow-sm border-0 mb-4">
    <div class="card-header bg-dark text-white fw-bold py-3">
        <i class="bi bi-grid-fill me-2"></i> Menu Quản Trị
    </div>
    <div class="list-group list-group-flush">
        <a href="${pageContext.request.contextPath}/admin" class="list-group-item list-group-item-action d-flex align-items-center py-3">
            <i class="bi bi-speedometer2 text-primary me-2"></i> Tổng Quan (Dashboard)
        </a>
        <a href="${pageContext.request.contextPath}/admin/users" class="list-group-item list-group-item-action d-flex align-items-center py-3">
            <i class="bi bi-people-fill text-success me-2"></i> Quản Lý Người Dùng
        </a>
        <a href="${pageContext.request.contextPath}/video" class="list-group-item list-group-item-action d-flex align-items-center py-3">
            <i class="bi bi-film text-warning me-2"></i> Quản Lý Videos
        </a>
        <a href="${pageContext.request.contextPath}/video/add" class="list-group-item list-group-item-action d-flex align-items-center py-3">
            <i class="bi bi-cloud-arrow-up-fill text-info me-2"></i> Đăng Video Mới
        </a>
        <a href="${pageContext.request.contextPath}/profile" class="list-group-item list-group-item-action d-flex align-items-center py-3">
            <i class="bi bi-person-gear text-secondary me-2"></i> Cấu Hình Tài Khoản
        </a>
        <a href="${pageContext.request.contextPath}/home" class="list-group-item list-group-item-action d-flex align-items-center py-3">
            <i class="bi bi-arrow-left-circle text-danger me-2"></i> Quay Lại Trang Chủ
        </a>
    </div>
</div>
