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
        // 生成唯一文件名（避免覆盖）
        // 保存文件到本地
        String newFileName = UUID.randomUUID().toString();
        Path path = Paths.get(FILE_DIR, newFileName);
        file.transferTo(path);  // 核心方法：将上传文件写入目标路径

        return newFileName;
    }

    public Resource loadFile(String fileName) throws MalformedURLException {

        // 创建文件资源
        Path filePath = Paths.get(FILE_DIR, fileName);
        return new UrlResource(filePath.toUri());
    }

    public Path getFilePath(String fileName) {
        return Paths.get(FILE_DIR, fileName);
    }
}
