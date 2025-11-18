package com.boot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.boot.service.UploadService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;
    private final String uploadRoot = "C:/dev/upload";

    /** 단일 업로드 (이미지/첨부파일 공용) */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "etc") String folder
    ) {
        try {
            // 유저/펫 외에는 일반 저장 방식 사용
            String saved = uploadService.saveImage(file, folder);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            log.error("업로드 실패", e);
            return ResponseEntity.status(500).body("fail");
        }
    }

    /** 이미지 출력 */
    @GetMapping("/display")
    public ResponseEntity<byte[]> display(@RequestParam("path") String path) {
        File file = new File(uploadRoot + "/" + path);

        try {
            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            return new ResponseEntity<>(Files.readAllBytes(file.toPath()), header, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** 다운로드 */
    @GetMapping("/download")
    public void download(@RequestParam("path") String filename, HttpServletResponse response) throws IOException {

        // ★ 파일 저장 경로 (네가 알려준 실제 경로)
        String savePath = "C:/dev/upload/notices/";

        File file = new File(savePath + filename);

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // MIME 타입
        response.setContentType("application/octet-stream");

        // 다운로드 시 파일명 처리
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\"");

        // 파일 스트림 처리
        FileInputStream fis = new FileInputStream(file);
        ServletOutputStream os = response.getOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        fis.close();
        os.close();
    }


}
