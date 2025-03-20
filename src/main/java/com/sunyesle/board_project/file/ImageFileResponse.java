package com.sunyesle.board_project.file;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ImageFileResponse {
    private final Long id;
    private final String fileName;
    private final String fileUrl;
    private final Long fileSize;
    private final String contentType;
    private final LocalDateTime createdAt;

    public ImageFileResponse(Long id, String fileName, String fileUrl, Long fileSize, String contentType, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.createdAt = createdAt;
    }
}
