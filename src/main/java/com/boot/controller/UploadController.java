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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "etc") String folder
    ) {
        try {
            String savedFileName;

            // 1) 폴더에 따라 업로드 방식 자동 분기
            if (folder.equals("user") || folder.equals("pet")) {
                // 유저/펫 이미지 → 해시 기반
                savedFileName = uploadService.saveImageWithHash(file, folder);

            } else {
                // 게시판 / 공지 / QNA / 기타 → 원본명 유지 방식
                savedFileName = uploadService.saveRawFile(file, folder);
            }

            // 2) 확장자 체크 (미리보기 / 본문삽입에 필요)
            String lower = savedFileName.toLowerCase();
            boolean isImage = lower.matches(".*\\.(jpg|jpeg|png|gif|webp)$");
            boolean isVideo = lower.matches(".*\\.(mp4|webm|ogg)$");

            // 3) 프론트에서 바로 쓰게 JSON 형태로 리턴
            HashMap<String, Object> result = new HashMap<>();
            result.put("path", folder + "/" + savedFileName);  // /display?path= 에 쓰임
            result.put("fileName", savedFileName);             // 필요 시 출력용
            result.put("image", isImage);
            result.put("video", isVideo);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("업로드 실패", e);
            HashMap<String, Object> err = new HashMap<>();
            err.put("result", "fail");
            return ResponseEntity.status(500).body(err);
        }
    }
    
    @PostMapping("/upload/summernote")
    @ResponseBody
    public Map<String, Object> uploadSummernote(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "community") String type
    ) {

        Map<String, Object> result = new HashMap<>();

        try {
            String folder = (type.equals("qna")) ? "qna_img" : "community_img";

            String saved = uploadService.saveRawFile(file, folder);

            result.put("url", "/display?path=" + folder + "/" + saved);
            result.put("responseCode", "success");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("responseCode", "error");
        }

        return result;
    }


    /** --------------------------
     *  Summernote 영상 업로드(mp4/webm)
     *  -------------------------- */
    @PostMapping("/upload/summernote/video")
    @ResponseBody
    public Map<String, Object> uploadSummernoteVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "community") String type
    ) {

        Map<String, Object> result = new HashMap<>();

        try {
            String original = file.getOriginalFilename().toLowerCase();
            String ext = original.substring(original.lastIndexOf("."));

            if (!ext.equals(".mp4") && !ext.equals(".webm")) {
                result.put("responseCode", "error");
                result.put("message", "mp4 또는 webm 형식만 지원합니다.");
                return result;
            }

            String folder = (type.equals("qna")) ? "qna_video" : "community_video";

            String saved = uploadService.saveRawFile(file, folder);

            result.put("url", "/display?path=" + folder + "/" + saved);
            result.put("responseCode", "success");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("responseCode", "error");
        }

        return result;
    }

    
    @PostMapping("/upload/cleanup-temp")
    @ResponseBody
    public void cleanupTempFiles(@RequestParam("files") List<String> files) {

        for (String url : files) {
            String path = url.replace("/display?path=", "");
            uploadService.deleteFile(path);
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
    public void download(
            @RequestParam("folder") String folder,
            @RequestParam("file") String filename,
            HttpServletResponse response
    ) throws IOException {

        String basePath = "C:/dev/upload/";
        File file = new File(basePath + folder + "/" + filename);

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/octet-stream");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\""
        );

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
