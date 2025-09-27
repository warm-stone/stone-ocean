package com.example.stoneocean.controller;

import com.example.stoneocean.entity.ApiResponse;
import com.example.stoneocean.service.IFileManagerService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@RestController
@RequestMapping("/file")
public class FileManagerController {

    private final IFileManagerService service;
    public FileManagerController(IFileManagerService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ApiResponse<String> uploadFile(
            @RequestParam("file") MultipartFile file,  // 接收单个文件
            @RequestParam(value = "description", required = false) String description) throws IOException {

        // 检查文件是否为空
        if (file.isEmpty()) {
            return ApiResponse.failed("文件为空");
        }
        String fileName = service.uploadFile(file);
        return ApiResponse.success(fileName);
    }

    // 多文件上传（参数用数组接收）
    @PostMapping("/upload/multi")
    public ApiResponse<String> uploadMultiFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            return ApiResponse.failed("文件为空");
        }
        for (MultipartFile file : files) {
            this.uploadFile(file, null);
        }
        return ApiResponse.success("全部成功");
    }

    @GetMapping("/load/{fileName}")
    public ResponseEntity<Resource> loadFile(@PathVariable String fileName) {
        try {
            // 创建文件资源
            Resource resource = service.loadFile(fileName);

            // （如果无法识别类型，浏览器可能仍会下载，需确保文件是浏览器支持的格式）
            String contentType = Files.probeContentType(service.getFilePath(fileName));
            if (contentType == null) {
                // 无法识别类型时，默认使用二进制流（可能导致下载，建议针对具体类型优化）
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            String contentDisposition = "inline; filename*=UTF-8''" + encodedFileName;
            // 检查资源是否存在
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}