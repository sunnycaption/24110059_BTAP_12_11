<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>${mode == 'edit' ? 'Chỉnh sửa Video' : 'Đăng Video Mới'} - Java Web MVC JPA</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-dark text-white">
                    <h5 class="mb-0">
                        <c:choose>
                            <c:when test="${mode == 'edit'}"><i class="bi bi-pencil-square"></i> Chỉnh Sửa Video</c:when>
                            <c:otherwise><i class="bi bi-plus-circle"></i> Đăng Video Mới</c:otherwise>
                        </c:choose>
                    </h5>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle"></i> ${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/video/${mode == 'edit' ? 'edit' : 'add'}"
                          method="post" enctype="multipart/form-data" class="needs-validation" novalidate>

                        <c:if test="${mode == 'edit'}">
                            <input type="hidden" name="videoId" value="${video.videoId}">
                        </c:if>

                        <div class="mb-3">
                            <label for="title" class="form-label">Tiêu đề Video <span class="text-danger">*</span></label>
                            <input type="text" id="title" name="title" class="form-control"
                                   value="${mode == 'edit' ? video.title : (title != null ? title : '')}"
                                   placeholder="Nhập tiêu đề video" required>
                            <div class="invalid-feedback">Tiêu đề video không được để trống.</div>
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label">Mô tả</label>
                            <textarea id="description" name="description" class="form-control" rows="3"
                                      placeholder="Mô tả nội dung video">${mode == 'edit' ? video.description : (description != null ? description : '')}</textarea>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="posterFile" class="form-label">Ảnh Poster (image/*)</label>
                                <input type="file" id="posterFile" name="posterFile" class="form-control" accept="image/*">
                                <small class="text-muted">Chấp nhận: JPG, PNG, GIF (tối đa 50MB)</small>
                                <c:if test="${mode == 'edit' && not empty video.poster}">
                                    <div class="mt-2">
                                        <small class="text-info"><i class="bi bi-image"></i> Hiện tại: ${video.poster}</small>
                                    </div>
                                </c:if>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="videoFile" class="form-label">File Video (video/*)</label>
                                <input type="file" id="videoFile" name="videoFile" class="form-control" accept="video/*">
                                <small class="text-muted">Chấp nhận: MP4, WebM (tối đa 50MB)</small>
                                <c:if test="${mode == 'edit' && not empty video.videoFile}">
                                    <div class="mt-2">
                                        <small class="text-info"><i class="bi bi-camera-video"></i> Hiện tại: ${video.videoFile}</small>
                                    </div>
                                </c:if>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="posterUrl" class="form-label">Hoặc URL Poster</label>
                                <input type="url" id="posterUrl" name="posterUrl" class="form-control" placeholder="https://...">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="videoUrl" class="form-label">Hoặc URL Video</label>
                                <input type="url" id="videoUrl" name="videoUrl" class="form-control" placeholder="https://...">
                            </div>
                        </div>

                        <div class="mb-3 form-check">
                            <input type="checkbox" id="active" name="active" class="form-check-input" value="true"
                                   ${mode == 'edit' ? (video.active ? 'checked' : '') : 'checked'}>
                            <label for="active" class="form-check-label">Hiển thị video (Active)</label>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-save"></i> ${mode == 'edit' ? 'Cập Nhật' : 'Đăng Video'}
                            </button>
                            <a href="${pageContext.request.contextPath}/video" class="btn btn-secondary">
                                <i class="bi bi-arrow-left"></i> Quay lại
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
