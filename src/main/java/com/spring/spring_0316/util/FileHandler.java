package com.spring.spring_0316.util;

import com.spring.spring_0316.domain.entity.FileEntity;
import com.spring.spring_0316.dto.FileDto;
import com.spring.spring_0316.service.FileService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class FileHandler {
    private final FileService fileService;

    public FileHandler(FileService fileService) {
        this.fileService = fileService;
    }

    public List<FileEntity> parseFileInfo(List<MultipartFile> multipartFiles) throws Exception {
        // 반환할 파일 List
        List<FileEntity> fileList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty())
            {
                String originalFilename = multipartFile.getOriginalFilename();
                String filename = new MD5Generator(originalFilename).toString();
                String fileExtension = fileService.extractExt(originalFilename);
                String fileType = fileService.setFileType(fileExtension);

                // 실행되는 위치의 'files' 폴더에 파일이 저장된다.
                String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\" + fileType;
                // 파일이 저장되는 폴더가 없으면 폴더 생성
                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch(Exception e) {
                        e.getStackTrace();
                    }
                }
                String filePath = savePath + "\\" + filename;
                multipartFile.transferTo(new File(filePath));

                FileDto fileDto = new FileDto();
                fileDto.setOriginalFilename(originalFilename);
                fileDto.setFilename(filename);
                fileDto.setFilePath(filePath);
                fileDto.setFileExtension(fileExtension);
                fileDto.setFileType(fileType);

                FileEntity file = fileDto.toEntity();

                fileList.add(file);
            }

        }
        return fileList;
    }
}
