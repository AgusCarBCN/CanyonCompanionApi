package com.canyoncompanion.canyon_api.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    private static final String UPLOAD_DIR =
            "/data/descents/images/";

    @Override
    public String saveDescentImage(MultipartFile file) {

        try {

            String filename = UUID.randomUUID()
                    + "-"
                    + file.getOriginalFilename();

            Path path = Paths.get(UPLOAD_DIR + filename);

            Files.copy(file.getInputStream(), path);

            return "/descents/images/" + filename;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
