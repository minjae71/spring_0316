package com.spring.spring_0316.service;

import com.spring.spring_0316.domain.entity.FileEntity;
import com.spring.spring_0316.domain.repository.FileRepository;
import com.spring.spring_0316.dto.FileDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Long saveFile(FileDto fileDto) {
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FileEntity saveFiles(FileDto fileDto) {
        return fileRepository.save(fileDto.toEntity());
    }

    @Transactional
    public FileDto getFile(Long id) {
        FileEntity fileEntity = fileRepository.findById(id).get();

        FileDto fileDto = FileDto.builder()
                .id(id)
                .originalFilename(fileEntity.getOriginalFilename())
                .filename(fileEntity.getFilename())
                .filePath(fileEntity.getFilePath())
                .fileType(fileEntity.getFileType())
                .fileExtension(fileEntity.getFileExtension())
                .build();
        return fileDto;
    }

    @Transactional
    public List<FileEntity> getBoardFile(Long boardId) {
        List<FileEntity> fileEntity = fileRepository.findByBoardId(boardId);

        return fileEntity;
    }

    @Transactional
    public String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    @Transactional
    public String setFileType(String fileExtension) {
        if(fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png") || fileExtension.equals("gif")) {
            return "image";
        } else {
            return "general";
        }
    }
}
