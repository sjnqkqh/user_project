package com.challenge.ably.config;

import com.challenge.ably.dto.ApiExceptionResp;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiExceptionResp> exceptionHandler(final MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();

        List<String> errorFieldList = result.getFieldErrors().stream().map(FieldError::getField).collect(Collectors.toList());
        log.error("[ApiExceptionAdvice.exceptionHandler] Method argument validation fail" + errorFieldList);

        return ResponseEntity.status(400)
            .body(ApiExceptionResp.builder().errCd("E0005").errMsg("Method argument validation fail" + errorFieldList).build());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiExceptionResp> exceptionHandler(final HttpMessageNotReadableException e) {
        log.error("[ApiExceptionAdvice.exceptionHandler] Bad Request Exception");

        return ResponseEntity.status(400)
            .body(ApiExceptionResp.builder().errCd("E0001").errMsg("Bad Request Exception").build());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiExceptionResp> exceptionHandler(final Exception e) {
        log.error("[ApiExceptionAdvice.exceptionHandler] Internal Exception. errMsg=", e);
        return ResponseEntity.status(400)
            .body(ApiExceptionResp.builder()
                .errCd("E9999")
                .errMsg("Unexpected Exception occur")
                .build());
    }
}