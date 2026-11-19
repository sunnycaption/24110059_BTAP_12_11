<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Videos Của Tôi - Java Web MVC JPA</title>
</head>
<body>
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="bi bi-collection-play"></i> Videos Của Tôi</h4>
        <a href="${pageContext.request.contextPath}/video/add" class="btn btn-primary btn-sm">
            <i class="bi bi-plus-circle"></i> Đăng Video Mới
        </a>
    </div>

    <%-- Flash message --%>
    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle"></i> ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty videos}">
            <div class="text-center py-5 bg-light rounded-3">
                <i class="bi bi-camera-video" style="font-size: 48px; color: #adb5bd;"></i>
                <p class="text-muted mt-2">Bạn chưa đăng tải video nào.</p>
                <a href="${pageContext.request.contextPath}/video/add" class="btn btn-primary">Tải lên video đầu tiên</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table table-hover table-bordered align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th style="width: 80px;">Poster</th>
                            <th>Tiêu đề</th>
                            <th style="width: 100px;">Lượt xem</th>
                            <th style="width: 100px;">Trạng thái</th>
                            <th style="width: 160px;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="v" items="${videos}" varStatus="loop">
                            <tr>
                                <td class="text-center">${(currentPage - 1) * 6 + loop.index + 1}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty v.poster && (v.poster.startsWith('http://') || v.poster.startsWith('https://'))}">
                                            <img src="${v.poster}" alt="Poster" style="width: 60px; height: 40px; object-fit: cover; border-radius: 4px;" onerror="this.src='https://placehold.co/60x40/667eea/fff?text=V';">
                                        </c:when>
                                        <c:when test="${not empty v.poster}">
                                            <img src="${pageContext.request.contextPath}/${v.poster.startsWith('uploads/') ? v.poster : 'uploads/'.concat(v.poster)}" alt="Poster" style="width: 60px; height: 40px; object-fit: cover; border-radius: 4px;" onerror="this.src='https://placehold.co/60x40/667eea/fff?text=V';">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="https://placehold.co/60x40/667eea/fff?text=V" alt="No Poster" style="width: 60px; height: 40px; border-radius: 4px;">
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="text-decoration-none fw-semibold">${v.title}</a>
                                    <c:if test="${not empty v.description}">
                                        <br><small class="text-muted" style="display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden;">${v.description}</small>
                                    </c:if>
                                </td>
                                <td class="text-center"><span class="badge bg-info">${v.views}</span></td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${v.active}"><span class="badge bg-success">Hiển thị</span></c:when>
                                        <c:otherwise><span class="badge bg-secondary">Ẩn</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/video/edit?id=${v.videoId}" class="btn btn-warning btn-sm" title="Sửa"><i class="bi bi-pencil"></i></a>
                                    <a href="${pageContext.request.contextPath}/video/delete?id=${v.videoId}" class="btn btn-danger btn-sm" title="Xóa"
                                       onclick="return confirm('Bạn có chắc chắn muốn xóa video này?');"><i class="bi bi-trash"></i></a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <%-- Phân trang --%>
            <div class="d-flex justify-content-between align-items-center mt-3">
                <small class="text-muted">Tổng: ${totalVideos} video | Trang ${currentPage} / ${totalPages}</small>
                <nav>
                    <ul class="pagination pagination-sm mb-0">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/video?page=${currentPage - 1}">&laquo; Trước</a>
                        </li>
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/video?page=${i}">${i}</a>
                            </li>
                        </c:forEach>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/video?page=${currentPage + 1}">Sau &raquo;</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>
