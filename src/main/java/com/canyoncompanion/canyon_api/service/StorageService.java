package com.canyoncompanion.canyon_api.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    String saveDescentImage(MultipartFile file);

    void deleteDescentImage(String imageUrl);

    void deleteFiles(List<String> urls);

}