package com.canyoncompanion.canyon_api.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    String saveImage(MultipartFile file, StorageType storageType);

    void deleteFile(String fileUrl, StorageType storageType);

    void deleteFiles(List<String> urls);

    String saveGpxFile(MultipartFile file);;
}