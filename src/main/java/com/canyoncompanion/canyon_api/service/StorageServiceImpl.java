package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    private static final String UPLOAD_DIR =
            "/data/descents/images/";

   /* @Override
    public String saveDescentImage(MultipartFile file) {

        try {

            String filename = UUID.randomUUID()
                    + "-"
                    + file.getOriginalFilename();

            Path path = Paths.get(UPLOAD_DIR + filename);

            Files.copy(file.getInputStream(), path);

            return "/descents/images/" + filename;

        } catch (Exception e) {
            throw new BusinessException(
                    "You are not authorized to delete this descent",
                    ErrorCode.IMAGE_NOT_UPLOADED.getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }*/

    @Override
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

            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
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
    }

    @Override
    public void deleteDescentImage(String imageUrl) {

        try {

            Path path = Paths.get(imageUrl);

            Files.deleteIfExists(path);

        } catch (IOException e) {

            throw new  BusinessException(
                    "You are not authorized to delete this descent",
                    ErrorCode.IMAGE_NOT_DELETED.getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );

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
}
