package com.protobuf.backend_league.controller;

import com.protobuf.backend_league.service.MatrixFunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("{action}")
    public ResponseEntity<String> handleCsv(@PathVariable String action, @RequestParam MultipartFile file) {
        String result = matrixFunctionService.handleCsv(action, file);
        return ResponseEntity.ok(result);
    }
}
