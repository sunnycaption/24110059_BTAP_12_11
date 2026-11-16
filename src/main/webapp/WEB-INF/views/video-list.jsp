<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Videos - Phân Trang 6 sp/trang (24110059)</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        .table-custom {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        .table-custom th, .table-custom td {
            padding: 12px 14px;
            border-bottom: 1px solid #edf2f7;
            text-align: left;
            vertical-align: middle;
            font-size: 14px;
        }
        .table-custom th {
            background-color: #f7fafc;
            color: #4a5568;
            font-weight: 600;
        }
        .table-custom tr:hover {
            background-color: #f8fafc;
        }
        .badge {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }
        .badge-success { background-color: #c6f6d5; color: #22543d; }
        .badge-secondary { background-color: #edf2f7; color: #718096; }
    </style>
</head>
<body style="display: block; padding: 25px 15px;">
    <div class="container-wide">
        <!-- Navigation Bar -->
        <div class="navbar-custom">
            <div style="font-size: 20px; font-weight: bold; color: #667eea;">
                🎬 Video Portal <span style="font-size: 13px; color: #718096; font-weight: normal;">(24110059)</span>
            </div>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/video" class="active">Videos Của Tôi</a>
                <a href="${pageContext.request.contextPath}/video/add">+ Đăng Video Mới</a>
                <span style="color: #cbd5e0; margin: 0 10px;">|</span>
                <span style="font-weight: 600; color: #2d3748;">👤 ${sessionScope.currentUser.fullname}</span>
                <a href="${pageContext.request.contextPath}/logout" style="color: #e53e3e;">Đăng Xuất</a>
            </div>
        </div>

        <!-- Tiêu đề & Nút Thêm mới -->
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 10px;">
            <div>
                <h2 style="font-size: 22px; color: #2d3748; margin-bottom: 4px;">Danh Sách Video Của Tôi</h2>
                <p style="color: #718096; font-size: 14px;">
                    Tổng số: <strong>${totalVideos}</strong> videos | Phân trang chuẩn <strong>6 sản phẩm/trang</strong>
                </p>
            </div>
            <a href="${pageContext.request.contextPath}/video/add" class="btn btn-primary btn-sm" style="width: auto; padding: 10px 18px;">
                + Thêm Video Mới
            </a>
        </div>

        <!-- Thông báo flash nếu có -->
        <c:if test="${not empty message}">
            <div class="alert alert-success">
                ${message}
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty videos}">
                <div style="text-align: center; padding: 50px 20px; background: #f7fafc; border-radius: 8px; color: #718096;">
                    <p style="font-size: 16px; margin-bottom: 12px;">Bạn chưa tải lên video nào!</p>
                    <a href="${pageContext.request.contextPath}/video/add" class="btn btn-primary btn-sm" style="display: inline-block;">
                        Tải lên video đầu tiên của bạn
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Bảng hiển thị danh sách Video -->
                <div style="overflow-x: auto;">
                    <table class="table-custom">
                        <thead>
                            <tr>
                                <th style="width: 100px;">Ảnh Poster</th>
                                <th style="width: 110px;">Mã Video</th>
                                <th>Tiêu Đề (Bấm để xem/phát)</th>
                                <th style="width: 100px;">Lượt Xem</th>
                                <th style="width: 110px;">Trạng Thái</th>
                                <th style="width: 160px; text-align: center;">Hành Động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="v" items="${videos}">
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty v.poster && (v.poster.startsWith('http://') || v.poster.startsWith('https://'))}">
                                                <img src="${v.poster}" alt="${v.title}" style="width: 80px; height: 50px; object-fit: cover; border-radius: 4px;" onerror="this.src='https://placehold.co/160x100/667eea/ffffff?text=Video';">
                                            </c:when>
                                            <c:when test="${not empty v.poster}">
                                                <img src="${pageContext.request.contextPath}/uploads/${v.poster}" alt="${v.title}" style="width: 80px; height: 50px; object-fit: cover; border-radius: 4px;" onerror="this.src='https://placehold.co/160x100/667eea/ffffff?text=Video';">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="https://placehold.co/160x100/667eea/ffffff?text=No+Poster" alt="${v.title}" style="width: 80px; height: 50px; object-fit: cover; border-radius: 4px;">
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><code style="font-weight: bold; color: #4a5568;">${v.videoId}</code></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" style="color: #2b6cb0; font-weight: 600; text-decoration: none;">
                                            ${v.title}
                                        </a>
                                        <div style="font-size: 12px; color: #a0aec0; margin-top: 4px; max-width: 400px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                            ${not empty v.description ? v.description : 'Không có mô tả'}
                                        </div>
                                    </td>
                                    <td><span style="font-weight: 600; color: #4a5568;">👁️ ${v.views}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${v.active}">
                                                <span class="badge badge-success">Công khai</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-secondary">Tạm ẩn</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="text-align: center;">
                                        <a href="${pageContext.request.contextPath}/video/edit?id=${v.videoId}" class="btn btn-secondary btn-sm" style="margin-right: 5px;">Sửa</a>
                                        <a href="${pageContext.request.contextPath}/video/delete?id=${v.videoId}" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc chắn muốn xóa video này không?');">Xóa</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Phân Trang (Pagination) 6 sp/trang -->
                <c:if test="${totalPages > 1}">
                    <div style="text-align: center; margin-top: 25px;">
                        <ul class="pagination">
                            <!-- Nút Trước (Prev) -->
                            <c:choose>
                                <c:when test="${currentPage > 1}">
                                    <li class="page-item">
                                        <a href="${pageContext.request.contextPath}/video?page=${currentPage - 1}">&laquo; Trước</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item disabled">
                                        <span>&laquo; Trước</span>
                                    </li>
                                </c:otherwise>
                            </c:choose>

                            <!-- Danh sách các số trang -->
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <c:choose>
                                    <c:when test="${i == currentPage}">
                                        <li class="page-item active">
                                            <span>${i}</span>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="page-item">
                                            <a href="${pageContext.request.contextPath}/video?page=${i}">${i}</a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>

                            <!-- Nút Sau (Next) -->
                            <c:choose>
                                <c:when test="${currentPage < totalPages}">
                                    <li class="page-item">
                                        <a href="${pageContext.request.contextPath}/video?page=${currentPage + 1}">Sau &raquo;</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item disabled">
                                        <span>Sau &raquo;</span>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                        <div style="font-size: 13px; color: #718096; margin-top: 8px;">
                            Trang <strong>${currentPage}</strong> / <strong>${totalPages}</strong> (Hiển thị tối đa 6 video mỗi trang)
                        </div>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
