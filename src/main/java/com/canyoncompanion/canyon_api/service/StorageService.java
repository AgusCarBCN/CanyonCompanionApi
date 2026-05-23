package com.canyoncompanion.canyon_api.service;


import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String saveDescentImage(MultipartFile file);

}