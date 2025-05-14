package com.sunyesle.board_project.file;

import com.sunyesle.board_project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "FILE")
public class FileEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private Long id;

    private String fileName;

    private String fileUrl;

    private Long fileSize;

    private String contentType;

    protected FileEntity() {
    }

    public FileEntity(String fileName, String fileUrl, Long fileSize, String contentType) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.contentType = contentType;
    }
}
