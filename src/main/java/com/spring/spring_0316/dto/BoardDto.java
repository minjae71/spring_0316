package com.spring.spring_0316.dto;

import com.spring.spring_0316.domain.entity.BoardEntity;
import com.spring.spring_0316.domain.entity.FileEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;

    @NotBlank(message = "게시글 유형을 선택해 주세요.")
    private int type;

    @NotBlank(message = "게시글 제목은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "작성자는 필수 입력 값입니다.")
    private String writer;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String content;

    private List<FileEntity> files;

    private int count;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public BoardEntity toEntity() {
        BoardEntity boardEntity = BoardEntity.builder()
                .id(id)
                .type(type)
                .title(title)
                .writer(writer)
                .content(content)
                .count(count)
                .build();
        return boardEntity;
    }

    @Builder
    public BoardDto(Long id, int type, String title, String writer, String content, List<FileEntity> files, int count, LocalDateTime createdDate, LocalDateTime modifiedDate)
    {
        this.id = id;
        this.type = type;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.files = files;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.count = count;
    }
}
