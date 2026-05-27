package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    private static final String DESCENT_DIR =
            "/data/descents/images/";

    private static final String WAYPOINT_DIR =
            "/data/routes/waypoint-images/";

    private static final String GPX_DIR = "/data/routes/gpx/";


    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2MB
    private static final long MAX_GPX_SIZE = 10 * 1024 * 1024;  // 10MB

    @Override
    public String saveImage(MultipartFile file, StorageType type) {

        validateImage(file);

        try {
            String dir = resolveDir(type);

            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

            Path uploadPath = Paths.get(dir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            Path targetPath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), targetPath);

            return switch (type) {
                case DESCENT_IMAGE -> "/descents/images/" + filename;
                case WAYPOINT_IMAGE -> "/routes/waypoint-images/" + filename;
                default -> throw new BusinessException("Invalid type",
                        ErrorCode.INVALID_IMAGE_TYPE.getDefaultMessage(),
                        HttpStatus.BAD_REQUEST);
            };

        } catch (Exception e) {
            throw new BusinessException(
                    "Failed to upload image",
                    ErrorCode.IMAGE_NOT_UPLOADED.getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /*@Override
    public String saveDescentImage(MultipartFile file) {

        try {

            if (file == null || file.isEmpty()) {
                throw new BusinessException(
                        "File is empty",
                        ErrorCode.IMAGE_NOT_UPLOADED.getDefaultMessage(),
                        HttpStatus.BAD_REQUEST
                );
            }

            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

            Path uploadPath = Paths.get(DESCENT_DIR).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            Path targetPath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), targetPath);

            return "/descents/images/" + filename;

        } catch (Exception e) {

            throw new BusinessException(
                    "Failed to upload image",
                    ErrorCode.IMAGE_NOT_UPLOADED.getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }*/
    @Override
    public void deleteFile(String imageUrl, StorageType type) {

        try {

            String dir = resolveDir(type);

            String filename = Paths.get(imageUrl)
                    .getFileName()
                    .toString();

            Path path = Paths.get(dir).resolve(filename);

            Files.deleteIfExists(path);

        } catch (Exception e) {

            throw new BusinessException(
                    "Error deleting image file",
                    ErrorCode.IMAGE_NOT_DELETED.getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /*@Override
    public void deleteDescentImage(String imageUrl) {

        try {

            // si imageUrl es "/descents/images/abc.jpg"
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            Path path = Paths.get(DESCENT_DIR).resolve(filename);

            Files.deleteIfExists(path);

        } catch (IOException e) {

            throw new BusinessException(
                    "Error deleting image file",
                    ErrorCode.IMAGE_NOT_DELETED.getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }*/

    @Override
    public String saveGpxFile(MultipartFile file) {

        validateGpx(file);

        try {

            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

            Path uploadPath = Paths.get(GPX_DIR).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            Path target = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), target);

            return "/routes/gpx/" + filename;

        } catch (Exception e) {
            throw new BusinessException("Failed to upload GPX",
                    ErrorCode.FAILED_GPX_UPLOAD .getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @Override
    @Async
    public void deleteFiles(List<String> urls) {

        for (String url : urls) {
            try {
                Path path = Paths.get(extractFilename(url));
                Files.deleteIfExists(path);
            } catch (Exception e) {
                throw new  BusinessException(
                        "You are not authorized to delete this descent",
                        ErrorCode.IMAGE_NOT_DELETED.getDefaultMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        }
    }

    private String extractFilename(String url) {
        return Paths.get(url).getFileName().toString();
    }

    private String resolveDir(StorageType type) {
        return switch (type) {
            case DESCENT_IMAGE -> DESCENT_DIR;
            case WAYPOINT_IMAGE -> WAYPOINT_DIR;
            case GPX_FILE -> GPX_DIR;
        };
    }
    private void validateImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BusinessException("File is empty",
                    ErrorCode.FILE_IS_EMPTY.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            throw new BusinessException("Invalid image type",
                    ErrorCode.INVALID_IMAGE_TYPE.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        String name = file.getOriginalFilename();
        if (name == null || !name.matches(".*\\.(jpg|jpeg|png)$")) {
            throw new BusinessException("Invalid extension",
                    ErrorCode.INVALID_EXTENSION.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        if(file.getSize() > MAX_IMAGE_SIZE) {
            throw new BusinessException(
                    "Image too large",
                    ErrorCode.IMAGE_TOO_BIG.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    private void validateGpx(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BusinessException("GPX file is empty",
                    ErrorCode.FILE_IS_EMPTY.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        String name = file.getOriginalFilename();

        if (name == null || !name.toLowerCase().endsWith(".gpx")) {
            throw new BusinessException(
                    "Only GPX files allowed",
                    ErrorCode.INVALID_FILE_TYPE.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
        if(file.getSize() > MAX_GPX_SIZE) {
            throw new BusinessException(
                    "Gpx file too large",
                    ErrorCode.GPX_FILE_TOO_BIG.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    private void validateImagev2(MultipartFile file) {

        validateFileNotEmpty(file, "Image");
        validateFileSize(file, MAX_IMAGE_SIZE, ErrorCode.IMAGE_TOO_BIG, "Image");

        String contentType = file.getContentType();

        if (contentType == null ||
                !List.of("image/jpeg", "image/png", "image/webp").contains(contentType)) {

            throw new BusinessException(
                    "Invalid image type",
                    ErrorCode.INVALID_IMAGE_TYPE.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    private void validateGpxv2(MultipartFile file) {

        validateFileNotEmpty(file, "GPX");
        validateFileSize(file, MAX_GPX_SIZE, ErrorCode.GPX_FILE_TOO_BIG, "GPX");

        String name = file.getOriginalFilename();

        if (name == null || !name.toLowerCase().endsWith(".gpx")) {
            throw new BusinessException(
                    "Only GPX files allowed",
                    ErrorCode.INVALID_FILE_TYPE.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateFileNotEmpty(MultipartFile file, String type) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(
                    " file is empty",
                    ErrorCode.FILE_IS_EMPTY.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    private void validateFileSize(MultipartFile file,
                                  long maxSize,
                                  ErrorCode errorCode,
                                  String type) {
        if (file.getSize() > maxSize) {
            throw new BusinessException(
                    " file too large",
                    errorCode.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}

