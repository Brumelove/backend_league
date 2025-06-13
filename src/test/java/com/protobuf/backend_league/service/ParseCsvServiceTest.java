// File: src/test/java/com/protobuf/backend_league/service/ParseCsvServiceTest.java
package com.protobuf.backend_league.service;

import com.protobuf.backend_league.exceptions.InvalidMatrixException;
import com.protobuf.backend_league.helper.AbstractJunitMockitoRunner;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static com.protobuf.backend_league.helper.TestHelper.getMockMultipartFile;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * @author Brume on 13/06/2025
 */
class ParseCsvServiceTest extends AbstractJunitMockitoRunner {

    @InjectMocks
    private ParseCsvService parseCsvService;

    @Test
    void testConvertArrayToString_NormalMatrix() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        String expected = "1,2,3\n4,5,6\n7,8,9";
        assertEquals(expected, parseCsvService.convertArrayToString(matrix));
    }

    @Test
    void testConvertArrayToString_SingleElement() {
        int[][] matrix = {{42}};
        assertEquals("42", parseCsvService.convertArrayToString(matrix));
    }

    @Test
    void testConvertArrayToString_EmptyMatrix() {
        int[][] matrix = new int[0][0];
        assertThrows(StringIndexOutOfBoundsException.class, () -> parseCsvService.convertArrayToString(matrix));
    }

    @Test
    void testReadAndValidateMatrix_ValidSquareMatrix() throws Exception {
        String csv = "1,2,3\n4,5,6\n7,8,9";
        MockMultipartFile file = getMockMultipartFile(csv);

        int[][] matrix = parseCsvService.readAndValidateMatrix(file);
        assertArrayEquals(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, matrix);
    }

    @Test
    void testReadAndValidateMatrix_SingleRow() throws Exception {
        String csv = "5";
        MockMultipartFile file = getMockMultipartFile(csv);

        int[][] matrix = parseCsvService.readAndValidateMatrix(file);
        assertArrayEquals(new int[][]{{5}}, matrix);
    }

    @Test
    void testReadAndValidateMatrix_NonSquareMatrix() {
        String csv = "1,2,3\n4,5,6";
        MockMultipartFile file = getMockMultipartFile(csv);

        Exception ex = assertThrows(InvalidMatrixException.class, () -> parseCsvService.readAndValidateMatrix(file));
        assertTrue(ex.getMessage().contains("Matrix must be square"));
    }

    @Test
    void testReadAndValidateMatrix_InconsistentRowLengths() {
        String csv = "1,2,3\n4,5";
        MockMultipartFile file = getMockMultipartFile(csv);

        Exception ex = assertThrows(InvalidMatrixException.class, () -> parseCsvService.readAndValidateMatrix(file));
        assertTrue(ex.getMessage().contains("All rows must have the same number of columns"));
    }


    @Test
    void testReadAndValidateMatrix_InvalidNumber() {
        String csv = "1,2,foo\n4,5,6\n7,8,9";
        MockMultipartFile file = getMockMultipartFile(csv);
        assertThrows(NumberFormatException.class, () -> parseCsvService.readAndValidateMatrix(file));
    }

    @Test
    void testReadAndValidateMatrix_EmptyFile() {
        String csv = "";
        MockMultipartFile file = getMockMultipartFile(csv);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> parseCsvService.readAndValidateMatrix(file));
    }
}