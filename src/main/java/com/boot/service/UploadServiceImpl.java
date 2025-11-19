package com.boot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.boot.util.ImageHashUtil;

import java.io.File;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    private final String ROOT = "C:/dev/upload/";

    // 일반 파일 저장
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
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    // 해시 기반 프로필 이미지 저장
    @Override
    public String saveImageWithHash(MultipartFile file, String folder) {
        try {
            byte[] bytes = file.getBytes();

            String hash = ImageHashUtil.getReadableHash(bytes);
            String uuid = UUID.randomUUID().toString();
            String original = file.getOriginalFilename();

            String fileName = uuid + "_" + hash + "_" + original;

            File dir = new File(ROOT + folder);
            if (!dir.exists()) dir.mkdirs();

            File target = new File(dir, fileName);
            file.transferTo(target);

            return folder + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("해시 파일 저장 실패", e);
        }
    }
    
    @Override
    public String saveRawFile(MultipartFile file, String folder) {
        try {
            String original = file.getOriginalFilename();  // 확장자 포함

            // 저장 폴더 생성
            File dir = new File(ROOT + folder);
            if (!dir.exists()) dir.mkdirs();

            File target = new File(dir, original);

            // 동일 이름 중복 → 번호 붙이기
            if (target.exists()) {
                int index = 1;
                String newName;

                int dotIdx = original.lastIndexOf(".");
                String base = (dotIdx != -1) ? original.substring(0, dotIdx) : original;
                String ext = (dotIdx != -1) ? original.substring(dotIdx) : "";

                do {
                    newName = "(" + index + ")_" + base + ext;
                    target = new File(dir, newName);
                    index++;
                } while (target.exists());
            }

            file.transferTo(target);

            return target.getName();  // ★ DB에는 파일명만 저장

        } catch (Exception e) {
            throw new RuntimeException("파일 저장 실패: " + file.getOriginalFilename(), e);
        }
    }


    // 파일 삭제
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
    
    @Override
    public void deleteAttachment(String folder, String filename) {
        if (filename == null || filename.isEmpty()) return;
        deleteFile(folder + "/" + filename);
    }

    @Override
    public void deleteSummernoteFiles(String folder, String html) {
        if (html == null || html.trim().isEmpty()) return;

        // 이미지 삭제
        Matcher img = Pattern.compile(folder + "_img/([^\"']+)").matcher(html);
        while (img.find()) {
            deleteFile(folder + "_img/" + img.group(1));
        }

        // 영상 삭제
        Matcher video = Pattern.compile(folder + "_video/([^\"']+)").matcher(html);
        while (video.find()) {
            deleteFile(folder + "_video/" + video.group(1));
        }
    }

}
