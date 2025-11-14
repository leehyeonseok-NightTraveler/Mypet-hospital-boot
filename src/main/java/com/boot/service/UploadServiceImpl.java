package com.boot.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.boot.dto.GroomingResDTO;
import com.boot.util.ImageHashUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    private final String ROOT = "C:/dev/upload/";

    @Override
    public String saveImage(MultipartFile file, String folder) {
        try {
            String uuid = UUID.randomUUID().toString();
            String original = file.getOriginalFilename();

            String fileName = uuid + "_" + original;

            File dir = new File(ROOT + folder);
            if (!dir.exists()) dir.mkdirs();

            File target = new File(dir, fileName);
            file.transferTo(target);

            return folder + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }


    @Override
    public String saveImageWithHash(MultipartFile file, String folder) {
        try {
            byte[] bytes = file.getBytes();
            String hash = ImageHashUtil.getReadableHash(bytes); // 네가 만든 해시 유틸

            String original = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();

            // 파일명 = UUID_HASH_originalName
            String fileName = uuid + "_" + hash + "_" + original;

            File dir = new File(ROOT + folder);
            if (!dir.exists()) dir.mkdirs();

            File target = new File(dir, fileName);
            file.transferTo(target);

            return folder + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("해시 이미지 저장 실패", e);
        }
    }


    @Override
    public boolean deleteFile(String fullPath) {
        try {
            File file = new File(ROOT + fullPath);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            log.error("파일 삭제 실패", e);
        }
        return false;
    }
}
