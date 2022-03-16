package com.spring.spring_0316.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
@Table(name = "board")
public class BoardEntity extends TimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(columnDefinition = "integer default 1")
    private int type;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 10, nullable = false)
    private String writer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(
            mappedBy = "board",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<FileEntity> files = new ArrayList<>();

    @Column(columnDefinition = "integer default 0")
    private int count;

    @Builder
    public BoardEntity(Long id, int type, String title, String writer, String content, int count) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.count = count;
    }

    // board에서 파일 처리 위함
    public void addFile(FileEntity file) {
        this.files.add(file);

        if(file.getBoard() != this) {
            file.setBoard(this);
        }
    }
}
