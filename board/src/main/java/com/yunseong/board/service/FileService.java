package com.yunseong.board.service;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileService {

    private final Resource resource = new DefaultResourceLoader().getResource("file:D:/Springboot/[20201007]Third_Project/board/src/main/resources/images/");

    public void save(long id, MultipartFile file) {
        try {
            Path path = this.resource.getFile().toPath();
            Files.createDirectory(path.resolve(String.valueOf(id)));
            Files.copy(file.getInputStream(), path.resolve(id + "/" + file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
