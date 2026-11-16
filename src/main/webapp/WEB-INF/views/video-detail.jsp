<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${video.title} - Phát Video HTML5 (24110059)</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        .player-wrapper {
            position: relative;
            background: #000000;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            margin-bottom: 25px;
        }
        .meta-box {
            background: #f7fafc;
            border: 1px solid #e2e8f0;
            border-radius: 10px;
            padding: 24px;
        }
    </style>
</head>
<body style="display: block; padding: 25px 15px;">
    <div class="container-wide" style="max-width: 950px;">
        <!-- Navigation Bar -->
        <div class="navbar-custom">
            <div style="font-size: 20px; font-weight: bold; color: #667eea;">
                🎬 Video Portal <span style="font-size: 13px; color: #718096; font-weight: normal;">(24110059)</span>
            </div>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/video">Videos Của Tôi</a>
                <a href="${pageContext.request.contextPath}/video/add">+ Đăng Video</a>
                <span style="color: #cbd5e0; margin: 0 10px;">|</span>
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <span style="font-weight: 600; color: #2d3748;">👤 ${sessionScope.currentUser.fullname}</span>
                        <a href="${pageContext.request.contextPath}/logout" style="color: #e53e3e;">Đăng Xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login">Đăng Nhập</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Video Player (HTML5 <video controls> / Iframe) -->
        <div class="player-wrapper">
            <c:choose>
                <%-- Trường hợp nhúng link YouTube --%>
                <c:when test="${not empty video.videoPath && (video.videoPath.contains('youtube.com') || video.videoPath.contains('youtu.be'))}">
                    <iframe width="100%" height="500" 
                            src="${video.videoPath.replace('watch?v=', 'embed/').replace('youtu.be/', 'youtube.com/embed/')}" 
                            title="${video.title}" frameborder="0" 
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                            allowfullscreen style="display: block;"></iframe>
                </c:when>

                <%-- Trường hợp đường dẫn video online trực tiếp --%>
                <c:when test="${not empty video.videoPath && (video.videoPath.startsWith('http://') || video.videoPath.startsWith('https://'))}">
                    <video controls autoplay style="width: 100%; max-height: 520px; display: block;" 
                           poster="${video.poster != null && (video.poster.startsWith('http') ? video.poster : pageContext.request.contextPath += '/uploads/' += video.poster)}">
                        <source src="${video.videoPath}" type="video/mp4">
                        Trình duyệt của bạn không hỗ trợ thẻ phát video HTML5.
                    </video>
                </c:when>

                <%-- Trường hợp file video upload lưu trữ trên server /uploads/ --%>
                <c:when test="${not empty video.videoPath}">
                    <video controls autoplay style="width: 100%; max-height: 520px; display: block;" 
                           poster="${video.poster != null ? (pageContext.request.contextPath += '/uploads/' += video.poster) : ''}">
                        <source src="${pageContext.request.contextPath}/uploads/${video.videoPath}" type="video/mp4">
                        Trình duyệt của bạn không hỗ trợ thẻ phát video HTML5.
                    </video>
                </c:when>

                <%-- Trường hợp chưa có file video --%>
                <c:otherwise>
                    <div style="height: 350px; display: flex; justify-content: center; align-items: center; color: #a0aec0; flex-direction: column;">
                        <span style="font-size: 48px; margin-bottom: 12px;">🎥</span>
                        <p>Video này chưa được tải lên file phát hoặc đường dẫn nguồn!</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Video Metadata -->
        <div class="meta-box">
            <h1 style="font-size: 24px; color: #2d3748; margin-bottom: 12px; line-height: 1.3;">
                ${video.title}
            </h1>

            <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #e2e8f0; padding-bottom: 15px; margin-bottom: 18px; flex-wrap: wrap; gap: 10px;">
                <div style="display: flex; gap: 20px; font-size: 14px; color: #4a5568;">
                    <span>👤 Người đăng: <strong>${video.user.fullname}</strong></span>
                    <span>👁️ Lượt xem: <strong style="color: #667eea;">${video.views}</strong></span>
                    <span>🔖 Mã video: <code>${video.videoId}</code></span>
                </div>
                <div style="display: flex; gap: 10px;">
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary btn-sm">&larr; Về Trang Chủ</a>
                    <a href="${pageContext.request.contextPath}/video" class="btn btn-primary btn-sm">Quản Lý Videos</a>
                </div>
            </div>

            <!-- Mô tả chi tiết -->
            <div>
                <h3 style="font-size: 16px; color: #2d3748; margin-bottom: 8px;">Mô Tả Video:</h3>
                <div style="font-size: 14px; color: #4a5568; line-height: 1.6; white-space: pre-wrap; background: #ffffff; padding: 16px; border-radius: 8px; border: 1px solid #e2e8f0;">${not empty video.description ? video.description : 'Không có mô tả nào cho video này.'}</div>
            </div>
        </div>
    </div>
</body>
</html>
