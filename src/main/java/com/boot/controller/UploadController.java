package com.boot.controller;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.boot.util.ImageHashUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UploadController {

    private final String uploadRoot = "C:/dev/upload";

    /** 파일 업로드 */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {

        try {
            // 1) 바이트 읽기
            byte[] bytes = file.getBytes();

            // 2) 해시 생성
            String hash = ImageHashUtil.getReadableHash(bytes);

            // 3) 업로드 파일명 최종 결정
            String uploadName = hash + "_" + file.getOriginalFilename();

            // 4) 폴더 생성 (연/월/일)
            String folder = getFolder();
            File uploadPath = new File(uploadRoot, folder);
            if (!uploadPath.exists()) uploadPath.mkdirs();

            // 5) 실제 파일 저장
            File saveFile = new File(uploadPath, uploadName);
            file.transferTo(saveFile);

            return ResponseEntity.ok(folder + "/" + uploadName);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("fail");
        }
    }

    /** display - 이미지 출력 */
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

    /** 파일 다운로드 */
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("path") String path) {

        Resource resource = new FileSystemResource(uploadRoot + "/" + path);

        if (!resource.exists()) return ResponseEntity.notFound().build();

        String filename = resource.getFilename();
        String originalName = filename.substring(filename.indexOf("_") + 1);

        HttpHeaders header = new HttpHeaders();
        try {
            header.add("Content-Disposition",
                "attachment; filename=" +
                new String(originalName.getBytes("UTF-8"), "ISO-8859-1"));
        } catch (Exception e) { }

        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }

    /** yyyy/MM/dd 폴더 생성 */
    private String getFolder() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }
}
