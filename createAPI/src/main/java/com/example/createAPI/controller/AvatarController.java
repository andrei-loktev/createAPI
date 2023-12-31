package com.example.createAPI.controller;

import com.example.createAPI.model.Avatar;
import com.example.createAPI.service.AvatarService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> save(@RequestParam Long studentId, MultipartFile multipartFile) {
        try {
            Long avatarId = avatarService.save(studentId, multipartFile);
            return ResponseEntity.ok(avatarId);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/from-disk/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void getFromDisk(@PathVariable("id") Long id, HttpServletResponse response) {
        Avatar avatar = avatarService.getById(id);
        response.setContentType(avatar.getMediaType());  //присваиваем тип
        response.setContentLength((int) avatar.getFileSize());   //присваиваем длину
        try {
            FileInputStream fis = new FileInputStream(avatar.getFilePath());
            fis.transferTo(response.getOutputStream());
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/from-DB/{id}")
    public ResponseEntity<byte[]> getFromDb(@PathVariable("id") Long id) {
        Avatar avatar = avatarService.getById(id);  //достаём аватурку из БД
        byte[] data = avatar.getData();  //достали данные из БД
        HttpHeaders headers = new HttpHeaders();  //создаём headers
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getFileSize());
        return ResponseEntity.status(200).headers(headers).body(data);
    }

}
