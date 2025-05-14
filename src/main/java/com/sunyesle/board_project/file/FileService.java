package com.sunyesle.board_project.file;

import com.sunyesle.board_project.common.exception.ErrorCodeException;
import com.sunyesle.board_project.common.exception.FileErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class FileService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp");

    private final Tika tika = new Tika();

    @Value("${file.base-dir}")
    private String baseDir;

    private final FileRepository fileRepository;

    @Transactional
    public ImageFileResponse storeImageFile(MultipartFile file) {
        validateImageFile(file);

        // 이미지 파일 디렉터리 경로
        String imageDir = baseDir + "images/";

        // 저장 폴더 생성
        createDir(imageDir);

        // 파일 저장
        String fileName = saveFile(file, imageDir);

        // 파일 정보 DB 저장
        FileEntity fileEntity = saveFileMetadata(file, fileName);

        return new ImageFileResponse(fileEntity.getId(),
                fileEntity.getFileName(),
                fileEntity.getFileUrl(),
                fileEntity.getFileSize(),
                fileEntity.getContentType(),
                fileEntity.getCreatedAt()
        );
    }

    /**
     * 이미지 파일 형식을 검증한다.
     */
    private void validateImageFile(MultipartFile file) {
        // 확장자 검증
        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ErrorCodeException(FileErrorCode.INVALID_IMAGE_FORMAT);
        }

        // MIME 타입 검증
        String mimeType;
        try {
            mimeType = tika.detect(file.getInputStream());
        } catch (IOException e) {
            throw new ErrorCodeException(FileErrorCode.INVALID_IMAGE_FORMAT);
        }

        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new ErrorCodeException(FileErrorCode.INVALID_IMAGE_FORMAT);
        }
    }

    /**
     * 파일 이름에서 파일 확장자를 추출한다.
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 지정된 경로에 디렉터리가 없으면 새로 생성한다.
     */
    private void createDir(String uploadDir) {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 파일을 저장하고 파일 이름을 반환한다.
     */
    private String saveFile(MultipartFile file, String uploadDir) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new ErrorCodeException(FileErrorCode.INVALID_FILE_NAME);
        }

        String newFileName = generateFileName(StringUtils.cleanPath(originalFilename));
        File destinationFile = new File(uploadDir + newFileName);

        try {
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new ErrorCodeException(FileErrorCode.FILE_STORE_FAILED);
        }

        return newFileName;
    }

    /**
     * 고유한 파일 이름을 생성한다.
     */
    private String generateFileName(String originalFilename) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "_" + UUID.randomUUID() + "." + getFileExtension(originalFilename);
    }

    /**
     * 파일 정보를 DB에 저장한다.
     */
    private FileEntity saveFileMetadata(MultipartFile file, String fileName) {
        FileEntity fileEntity = new FileEntity(
                fileName,
                "/files/images/" + fileName,
                file.getSize(),
                file.getContentType()
        );
        fileRepository.save(fileEntity);
        return fileEntity;
    }
}
