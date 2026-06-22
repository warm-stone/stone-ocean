package com.example.stoneocean.service.impl;

import com.example.stoneocean.service.IFileManagerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class FileManagerService implements IFileManagerService {
    private static String FILE_DIR;

    public FileManagerService(@Value("${file.upload.url}") String FILE_DIR) {
        FileManagerService.FILE_DIR = FILE_DIR;
        // 创建存储目录（若不存在）
        Path path = Paths.get(FILE_DIR);
        if (!Files.exists(path)) {
            path.toFile().mkdirs();
        }
    }
    public String uploadFile(MultipartFile file) throws IOException {

        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }
        // 生成唯一文件名（避免覆盖）；保留原始扩展名以改善下载体验与类型识别
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null) {
            int lastDot = originalFilename.lastIndexOf('.');
            if (lastDot >= 0 && lastDot < originalFilename.length() - 1) {
                String ext = originalFilename.substring(lastDot + 1);
                // 仅允许字母数字扩展名，防止通过扩展名注入路径分隔符造成路径穿越
                if (ext.matches("[a-zA-Z0-9]+")) {
                    extension = "." + ext;
                }
            }
        }
        String newFileName = UUID.randomUUID().toString() + extension;
        Path path = Paths.get(FILE_DIR, newFileName);
        file.transferTo(path);  // 核心方法：将上传文件写入目标路径

        return newFileName;
    }

    public Resource loadFile(String fileName) throws MalformedURLException {

        // 创建文件资源
        Path filePath = Paths.get(FILE_DIR, fileName).normalize();
        if (!filePath.startsWith(FILE_DIR)) {
            throw new IllegalArgumentException("非法文件路径");
        }
        return new UrlResource(filePath.toUri());
    }

    public Path getFilePath(String fileName) {
        Path filePath = Paths.get(FILE_DIR, fileName).normalize();
        if (!filePath.startsWith(FILE_DIR)) {
            throw new IllegalArgumentException("非法文件路径");
        }
        return filePath;
    }
}
