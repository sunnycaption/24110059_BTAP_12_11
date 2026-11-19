package vn.iotstar.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * Tiện ích tích hợp Cloudinary API để upload và quản lý ảnh đại diện (Avatar).
 * Tự động đọc cấu hình từ file cloudinary.properties.
 * Hỗ trợ fallback lưu local vào thư mục uploads/ nếu chưa cấu hình API key thực tế.
 */
public class CloudinaryUtil {

    private static Cloudinary cloudinary;
    private static String cloudName;
    private static String apiKey;
    private static String apiSecret;
    private static boolean isConfigured = false;

    static {
        Properties props = new Properties();
        try (InputStream is = CloudinaryUtil.class.getClassLoader().getResourceAsStream("cloudinary.properties")) {
            if (is != null) {
                props.load(is);
                cloudName = props.getProperty("cloudinary.cloud_name", "").trim();
                apiKey = props.getProperty("cloudinary.api_key", "").trim();
                apiSecret = props.getProperty("cloudinary.api_secret", "").trim();

                if (!cloudName.isEmpty() && !apiKey.isEmpty() && !apiSecret.isEmpty()
                        && !cloudName.contains("your_cloud_name")
                        && !apiKey.contains("your_api_key")) {
                    cloudinary = new Cloudinary(ObjectUtils.asMap(
                            "cloud_name", cloudName,
                            "api_key", apiKey,
                            "api_secret", apiSecret,
                            "secure", true
                    ));
                    isConfigured = true;
                    System.out.println("✅ [CloudinaryUtil] Đã kết nối Cloudinary thành công với Cloud: " + cloudName);
                } else {
                    System.out.println("ℹ️ [CloudinaryUtil] cloudinary.properties đang chứa giá trị mẫu (placeholder). Sử dụng chế độ fallback lưu local.");
                }
            } else {
                System.err.println("⚠️ [CloudinaryUtil] Không tìm thấy file cloudinary.properties trên classpath!");
            }
        } catch (Exception e) {
            System.err.println("❌ [CloudinaryUtil] Lỗi khi nạp cấu hình Cloudinary: " + e.getMessage());
        }
    }

    /**
     * Upload file Part ảnh từ Servlet Multipart lên Cloudinary.
     * @param part Part ảnh nhận được từ HttpServletRequest.getPart("imageFile")
     * @param req HttpServletRequest để hỗ trợ fallback lưu local nếu chưa cấu hình Cloudinary
     * @return secure_url từ Cloudinary (hoặc đường dẫn uploads/... nếu fallback)
     */
    public static String uploadPart(Part part, HttpServletRequest req) throws IOException {
        if (part == null || part.getSize() <= 0) {
            return null;
        }

        String submittedName = part.getSubmittedFileName();
        if (submittedName == null || submittedName.isBlank()) {
            return null;
        }

        byte[] bytes;
        try (InputStream in = part.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            bytes = baos.toByteArray();
        }

        // 1. Nếu đã cấu hình Cloudinary thực tế -> upload trực tiếp lên Cloudinary
        if (isConfigured && cloudinary != null) {
            try {
                @SuppressWarnings("rawtypes")
                Map uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.asMap(
                        "folder", "avatars",
                        "resource_type", "image"
                ));
                String secureUrl = (String) uploadResult.get("secure_url");
                if (secureUrl != null && !secureUrl.isBlank()) {
                    System.out.println("☁️ [CloudinaryUtil] Upload thành công lên Cloudinary: " + secureUrl);
                    return secureUrl;
                }
            } catch (Exception e) {
                System.err.println("⚠️ [CloudinaryUtil] Lỗi khi upload lên Cloudinary: " + e.getMessage() + ". Chuyển sang fallback lưu local.");
            }
        }

        // 2. Chế độ Fallback: Lưu vào thư mục uploads/ trên server local để luôn hiển thị được ảnh
        String originalName = Paths.get(submittedName).getFileName().toString();
        String ext = "";
        int dot = originalName.lastIndexOf(".");
        if (dot >= 0) {
            ext = originalName.substring(dot);
        }
        String fileName = "avatar_" + System.currentTimeMillis() + ext;

        String uploadDir = req.getServletContext().getRealPath("/uploads");
        if (uploadDir == null) {
            uploadDir = System.getProperty("catalina.base", ".") + File.separator + "webapps"
                    + File.separator + req.getContextPath().replace("/", "") + File.separator + "uploads";
        }
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File target = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(target)) {
            fos.write(bytes);
        }

        return "uploads/" + fileName;
    }

    public static boolean isConfigured() {
        return isConfigured;
    }
}
