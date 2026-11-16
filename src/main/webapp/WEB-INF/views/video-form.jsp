<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${mode == 'edit' ? 'Chỉnh Sửa Video' : 'Thêm Video Mới'} - Upload Multipart (24110059)</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body style="display: block; padding: 25px 15px;">
    <div class="container-wide" style="max-width: 800px;">
        <!-- Navigation Bar -->
        <div class="navbar-custom">
            <div style="font-size: 20px; font-weight: bold; color: #667eea;">
                🎬 Video Portal <span style="font-size: 13px; color: #718096; font-weight: normal;">(24110059)</span>
            </div>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/video">Videos Của Tôi</a>
                <a href="${pageContext.request.contextPath}/video/add" class="${mode == 'add' ? 'active' : ''}">+ Đăng Video</a>
                <span style="color: #cbd5e0; margin: 0 10px;">|</span>
                <span style="font-weight: 600; color: #2d3748;">👤 ${sessionScope.currentUser.fullname}</span>
                <a href="${pageContext.request.contextPath}/logout" style="color: #e53e3e;">Đăng Xuất</a>
            </div>
        </div>

        <div class="header-title" style="text-align: left; border-bottom: 2px solid #edf2f7; padding-bottom: 12px; margin-bottom: 25px;">
            <h2 style="font-size: 22px; color: #2d3748;">
                ${mode == 'edit' ? 'Chỉnh Sửa Thông Tin Video' : 'Tải Lên Video Mới (Upload Multipart)'}
            </h2>
            <p style="color: #718096; font-size: 14px;">
                Hỗ trợ tải lên file video MP4/WebM và file ảnh đại diện Poster trực tiếp lên máy chủ
            </p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/video/${mode}" method="post" enctype="multipart/form-data">
            <!-- Mã Video -->
            <div class="form-group">
                <label for="videoId">Mã Video (Video ID):</label>
                <input type="text" id="videoId" name="videoId" class="form-control" 
                       value="${video != null ? video.videoId : ''}" 
                       placeholder="Ví dụ: VID001 (để trống hệ thống tự sinh mã)" 
                       ${mode == 'edit' ? 'readonly style="background-color: #edf2f7;"' : ''}>
            </div>

            <!-- Tiêu đề Video -->
            <div class="form-group">
                <label for="title">Tiêu Đề Video (*):</label>
                <input type="text" id="title" name="title" class="form-control" 
                       value="${video != null ? video.title : ''}" 
                       placeholder="Nhập tiêu đề video" required>
            </div>

            <!-- Upload Ảnh Poster -->
            <div class="form-group" style="background: #f7fafc; padding: 15px; border-radius: 8px; border: 1px dashed #cbd5e0;">
                <label for="posterFile" style="font-weight: 600; color: #2d3748;">
                    🖼️ Ảnh đại diện Poster (Tải lên file ảnh):
                </label>
                <input type="file" id="posterFile" name="posterFile" class="form-control" accept="image/*" style="background: #ffffff;">
                
                <div style="margin-top: 10px;">
                    <label for="posterUrl" style="font-size: 13px; color: #718096; font-weight: normal;">
                        Hoặc nhập đường dẫn ảnh online (URL):
                    </label>
                    <input type="text" id="posterUrl" name="posterUrl" class="form-control" 
                           value="${video != null ? video.poster : ''}" 
                           placeholder="https://example.com/poster.jpg">
                </div>

                <c:if test="${mode == 'edit' && not empty video.poster}">
                    <div style="margin-top: 10px; font-size: 13px; color: #4a5568;">
                        Ảnh hiện tại: <code>${video.poster}</code>
                    </div>
                </c:if>
            </div>

            <!-- Upload File Video -->
            <div class="form-group" style="background: #f7fafc; padding: 15px; border-radius: 8px; border: 1px dashed #cbd5e0;">
                <label for="videoFile" style="font-weight: 600; color: #2d3748;">
                    🎥 File Video (Tải lên file .mp4, .webm):
                </label>
                <input type="file" id="videoFile" name="videoFile" class="form-control" accept="video/mp4,video/webm,video/*" style="background: #ffffff;">

                <div style="margin-top: 10px;">
                    <label for="videoUrl" style="font-size: 13px; color: #718096; font-weight: normal;">
                        Hoặc nhập đường dẫn link video:
                    </label>
                    <input type="text" id="videoUrl" name="videoUrl" class="form-control" 
                           value="${video != null ? video.videoPath : ''}" 
                           placeholder="https://example.com/sample_video.mp4">
                </div>

                <c:if test="${mode == 'edit' && not empty video.videoPath}">
                    <div style="margin-top: 10px; font-size: 13px; color: #4a5568;">
                        File/Link video hiện tại: <code>${video.videoPath}</code>
                    </div>
                </c:if>
            </div>

            <!-- Mô tả chi tiết -->
            <div class="form-group">
                <label for="description">Mô Tả Video:</label>
                <textarea id="description" name="description" class="form-control" rows="4" 
                          placeholder="Nhập tóm tắt nội dung video">${video != null ? video.description : ''}</textarea>
            </div>

            <!-- Kích hoạt -->
            <div class="form-group" style="display: flex; align-items: center; gap: 8px;">
                <input type="checkbox" id="active" name="active" ${video == null || video.active ? 'checked' : ''} style="width: 18px; height: 18px;">
                <label for="active" style="margin-bottom: 0; cursor: pointer;">Công khai video (Cho phép mọi người xem)</label>
            </div>

            <div style="display: flex; gap: 15px; margin-top: 25px;">
                <button type="submit" class="btn btn-primary" style="flex: 2;">
                    ${mode == 'edit' ? 'Lưu Cập Nhật' : 'Đăng Video'}
                </button>
                <a href="${pageContext.request.contextPath}/video" class="btn btn-secondary" style="flex: 1;">
                    Hủy Bỏ
                </a>
            </div>
        </form>
    </div>
</body>
</html>
