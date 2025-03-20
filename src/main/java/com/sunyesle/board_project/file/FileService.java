package com.sunyesle.board_project.file;

import com.sunyesle.board_project.common.exception.ErrorCodeException;
import com.sunyesle.board_project.common.exception.FileErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class FileService {

    private static final String UPLOAD_DIR = "D:/dev/images/";

    private final FileRepository fileRepository;

    @Transactional
    public ImageFileResponse storeImageFile(MultipartFile file) {
        // 저장 폴더 생성
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 파일명 변환
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new ErrorCodeException(FileErrorCode.INVALID_FILE_NAME);
        }
        String newFileName = generateFileName(StringUtils.cleanPath(originalFilename));
        File destinationFile = new File(UPLOAD_DIR + newFileName);

        // 파일 저장
        try {
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new ErrorCodeException(FileErrorCode.FILE_STORE_FAILED);
        }

        // 파일 정보 DB 저장
        FileEntity fileEntity = new FileEntity(
                newFileName,
                "/files/images/" + newFileName,
                file.getSize(),
                file.getContentType()
        );
        fileRepository.save(fileEntity);

        return new ImageFileResponse(fileEntity.getId(),
                fileEntity.getFileName(),
                fileEntity.getFileUrl(),
                fileEntity.getFileSize(),
                fileEntity.getContentType(),
                fileEntity.getCreatedAt()
        );
    }

    private String generateFileName(String originalFilename) {
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }

        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "_" + UUID.randomUUID() + extension;
    }
}
