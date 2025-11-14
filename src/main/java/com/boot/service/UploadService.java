package com.boot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.boot.dto.GroomingResDTO;

public interface UploadService {
    String saveImage(MultipartFile file, String folder);           // 일반 파일 저장
    String saveImageWithHash(MultipartFile file, String folder);   // 해시 포함 저장
    boolean deleteFile(String fullPath);                           // 삭제
}
