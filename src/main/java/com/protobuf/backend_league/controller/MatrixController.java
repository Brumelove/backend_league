package com.protobuf.backend_league.controller;

import com.protobuf.backend_league.service.MatrixFunctionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Brume on 13/06/2025
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MatrixController {
    private final MatrixFunctionService matrixFunctionService;

    @PostMapping(value = "{action}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleMatrixAction(@PathVariable String action,
                                                       @Parameter(description = "CSV file", content =
                                                       @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
                                                       @RequestPart MultipartFile file) {
        return ResponseEntity.ok(matrixFunctionService.handleMatriAction(action, file));
    }
}
