package com.spring.spring_0316.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
@Table(name = "file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private String fileExtension;

    @ManyToOne
    @JoinColumn(name="board_id")
    private BoardEntity board;

    @Builder
    public FileEntity(Long id, String originalFilename, String filename, String filePath, String fileType, String fileExtension) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.filename = filename;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileExtension = fileExtension;
    }

    // 게시글의 정보 저장
    public void setBoard(BoardEntity board) {
        this.board = board;

        if(!board.getFiles().contains(this))
        {
            board.getFiles().add(this);
        }
    }
}
