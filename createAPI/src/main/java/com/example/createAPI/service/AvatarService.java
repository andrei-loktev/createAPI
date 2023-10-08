package com.example.createAPI.service;

import com.example.createAPI.model.Avatar;
import com.example.createAPI.model.Student;
import com.example.createAPI.repository.AvatarRepository;
import com.example.createAPI.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private AvatarRepository avatarRepository;
    private StudentRepository studentRepository;

    @Value("${path.to.avatars.folder}")
    private Path pathToAvatars;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public Avatar getById(Long id){
        logger.info("running metod getById");
        return avatarRepository.findById(id).orElseThrow();
    }

    public Long save(Long studentId, MultipartFile multipartFile) throws IOException {
        logger.info("running metod save");
        String fullPath = saveToDisk(studentId, multipartFile);
        Avatar avatar = saveToDB(studentId, multipartFile, fullPath);
        
        return avatar.getId();
    }

    private Avatar saveToDB(Long studentId, MultipartFile multipartFile, String fullPath) throws IOException {
        Student studentReference = studentRepository.getReferenceById(studentId);
        Avatar avatar = avatarRepository.findByStudent(studentReference).orElse(new Avatar());

        avatar.setStudent(studentRepository.getReferenceById(studentId));
        avatar.setFilePath(fullPath);
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setFileSize(multipartFile.getSize());
        avatar.setData(multipartFile.getBytes());
        avatarRepository.save(avatar);
        return avatar;
    }

    private String saveToDisk(Long studentId, MultipartFile multipartFile) throws IOException {
        Files.createDirectories(pathToAvatars);   //создание папки для аватарки
        String originalFilename = multipartFile.getOriginalFilename();   //получаем название файла из запроса
        int index = originalFilename.lastIndexOf(".");   //получение индекса точки с конца
        String extension = originalFilename.substring(index);   //берём подстроку начиная с точки
        String fileName = studentId + extension;
        FileOutputStream stream = new FileOutputStream(pathToAvatars.toAbsolutePath() + "/" + fileName);//полный путь к папке с файлом
        String fullPath = pathToAvatars.toAbsolutePath() + "/" + fileName;
        multipartFile.getInputStream().transferTo(stream);  //переносим байты в папку
        stream.close();
        return fullPath;
    }
}
