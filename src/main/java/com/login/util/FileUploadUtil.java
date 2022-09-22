package com.login.util;

import com.login.config.CommonException;
import com.login.util.code.ApiExceptionCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FileUploadUtil {


    /**
     * 파일 저장
     *
     * @param file     업로드 파일
     * @param location 파일 저장 위치
     */
    public static void save(MultipartFile file, Path location) {
        try {
            Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("[FileService.save] File save error, ", e);
            throw new CommonException(ApiExceptionCode.INTERNAL_SERVER_EXCEPTION);
        }
    }
}
