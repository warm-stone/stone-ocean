package com.example.stoneocean.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface IFileManagerService {

    String uploadFile(MultipartFile file) throws IOException;

    Resource loadFile(String fileName) throws MalformedURLException;

    Path getFilePath(String fileName);

}
