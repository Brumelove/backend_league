package com.protobuf.backend_league.helper;

import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

/**
 * @author Brume on 13/06/2025
 */
public class TestHelper {

    public static MockMultipartFile getMockMultipartFile(String csv) {
        return new MockMultipartFile("file", "matrix.csv", "text/csv",
                csv.getBytes(StandardCharsets.UTF_8));
    }
}
