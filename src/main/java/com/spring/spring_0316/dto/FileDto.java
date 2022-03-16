package com.spring.spring_0316.dto;

import com.spring.spring_0316.domain.entity.FileEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {
    private Long id;
    private String originalFilename;
    private String filename;
    private String filePath;
    private String fileType;
    private String fileExtension;

    public FileEntity toEntity() {
        FileEntity build = FileEntity.builder()
                .id(id)
                .originalFilename(originalFilename)
                .filename(filename)
                .filePath(filePath)
                .fileType(fileType)
                .fileExtension(fileExtension)
                .build();
        return build;
    }

    @Builder
    public FileDto(Long id, String originalFilename, String filename, String filePath, String fileType, String fileExtension) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.filename = filename;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileExtension = fileExtension;
    }
}
