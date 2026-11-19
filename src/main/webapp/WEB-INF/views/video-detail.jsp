<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>${video.title} - Xem Video</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-lg-10">
            <%-- Video Player --%>
            <div class="card shadow-sm border-0 mb-3">
                <div class="ratio ratio-16x9 bg-dark rounded-top">
                    <c:choose>
                        <c:when test="${not empty video.videoFile && (video.videoFile.startsWith('http://') || video.videoFile.startsWith('https://'))}">
                            <video controls autoplay class="w-100">
                                <source src="${video.videoFile}" type="video/mp4">
                                Trình duyệt không hỗ trợ phát video.
                            </video>
                        </c:when>
                        <c:when test="${not empty video.videoFile}">
                            <video controls autoplay class="w-100">
                                <source src="${pageContext.request.contextPath}/${video.videoFile.startsWith('uploads/') ? video.videoFile : 'uploads/'.concat(video.videoFile)}" type="video/mp4">
                                Trình duyệt không hỗ trợ phát video.
                            </video>
                        </c:when>
                        <c:otherwise>
                            <div class="d-flex align-items-center justify-content-center text-white h-100">
                                <div class="text-center">
                                    <i class="bi bi-camera-video-off" style="font-size: 48px;"></i>
                                    <p class="mt-2">Không có file video để phát.</p>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="card-body">
                    <h4 class="card-title mb-2">${video.title}</h4>
                    <div class="d-flex justify-content-between align-items-center text-muted mb-3" style="font-size: 14px;">
                        <span><i class="bi bi-person"></i> ${video.user.fullname}</span>
                        <span><i class="bi bi-eye"></i> ${video.views} lượt xem</span>
                        <span><i class="bi bi-calendar"></i> ${video.createdDate}</span>
                    </div>
                    <c:if test="${not empty video.description}">
                        <hr>
                        <p class="text-muted">${video.description}</p>
                    </c:if>
                </div>
            </div>

            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-primary btn-sm">
                    <i class="bi bi-house"></i> Trang Chủ
                </a>
                <a href="${pageContext.request.contextPath}/video" class="btn btn-outline-secondary btn-sm">
                    <i class="bi bi-arrow-left"></i> Videos Của Tôi
                </a>
            </div>
        </div>
    </div>
</body>
</html>
