package com.boot.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String saveImage(MultipartFile file, String folder);
    String saveImageWithHash(MultipartFile file, String folder);
    boolean deleteFile(String fullPath);
    // 새로 추가한 공지사항/파일용 업로드(원본 유지 + 중복시 번호 붙이기)
    String saveRawFile(MultipartFile file, String folder);
    void deleteAttachment(String folder, String filename);
    void deleteSummernoteFiles(String folder, String html);
}
	