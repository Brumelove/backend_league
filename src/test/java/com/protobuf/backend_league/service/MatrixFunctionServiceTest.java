package com.protobuf.backend_league.service;

import com.protobuf.backend_league.exceptions.InvalidMatrixException;
import com.protobuf.backend_league.helper.AbstractJunitMockitoRunner;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import static com.protobuf.backend_league.helper.TestHelper.getMockMultipartFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Brume on 13/06/2025
 */
class MatrixFunctionServiceTest extends AbstractJunitMockitoRunner {
    private final int[][] sample = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };
    @InjectMocks
    private MatrixFunctionService matrixFunctionService;
    @Mock
    private ParseCsvService parseCsvService;

    @Test
    void testEcho_Return_Success() {
        when(parseCsvService.convertArrayToString(sample)).thenCallRealMethod();
        assertEquals("1,2,3\n4,5,6\n7,8,9", matrixFunctionService.echo(sample));
    }

    @Test
    void testEcho_EmptyMatrix_ThrowsException() {
        int[][] empty = new int[0][0];
        when(parseCsvService.convertArrayToString(empty)).thenCallRealMethod();
        assertThrows(StringIndexOutOfBoundsException.class, () -> matrixFunctionService.echo(empty));
    }

    @Test
    void testEcho_SingleElement() {
        int[][] single = {{7}};
        when(parseCsvService.convertArrayToString(single)).thenCallRealMethod();
        assertEquals("7", matrixFunctionService.echo(single));
    }

    @Test
    void testInvert_Normal() {
        when(parseCsvService.convertArrayToString(any(int[][].class))).thenReturn("1,4,7\n2,5,8\n3,6,9");
        assertEquals("1,4,7\n2,5,8\n3,6,9", matrixFunctionService.invert(sample));
    }

    @Test
    void testInvert_SingleElement() {
        int[][] single = {{5}};
        when(parseCsvService.convertArrayToString(any(int[][].class))).thenReturn("5");
        assertEquals("5", matrixFunctionService.invert(single));
    }

    @Test
    void testFlatten_Normal() {
        assertEquals("1,2,3,4,5,6,7,8,9", matrixFunctionService.flatten(sample));
    }

    @Test
    void testFlatten_EmptyMatrix() {
        int[][] empty = new int[0][0];
        assertThrows(InvalidMatrixException.class, () -> matrixFunctionService.flatten(empty));
    }

    @Test
    void testFlatten_SingleElement() {
        int[][] single = {{42}};
        assertEquals("42", matrixFunctionService.flatten(single));
    }

    @Test
    void testSum_Normal() {
        assertEquals(45, matrixFunctionService.sum(sample));
    }

    @Test
    void testSum_EmptyMatrix() {
        int[][] empty = new int[0][0];
        assertEquals(0, matrixFunctionService.sum(empty));
    }

    @Test
    void testSum_SingleElement() {
        int[][] single = {{-3}};
        assertEquals(-3, matrixFunctionService.sum(single));
    }

    @Test
    void testMultiply_Normal() {
        assertEquals(362880, matrixFunctionService.multiply(sample));
    }

    @Test
    void testMultiply_EmptyMatrix() {
        int[][] empty = new int[0][0];
        assertEquals(1, matrixFunctionService.multiply(empty));
    }

    @Test
    void testMultiply_SingleElement() {
        int[][] single = {{7}};
        assertEquals(7, matrixFunctionService.multiply(single));
    }

    @Test
    void testHandleCsv_ValidOperation() throws Exception {
        String csv = "1,2\n3,4";
        MockMultipartFile file = getMockMultipartFile(csv);

        int[][] matrix = {{1, 2}, {3, 4}};
        when(parseCsvService.readAndValidateMatrix(file)).thenReturn(matrix);
        when(parseCsvService.convertArrayToString(matrix)).thenReturn("1,2\n3,4");

        assertEquals("1,2\n3,4", matrixFunctionService.handleCsv("echo", file));
        assertEquals("1,2,3,4", matrixFunctionService.handleCsv("flatten", file));
        assertEquals("10", matrixFunctionService.handleCsv("sum", file));
        assertEquals("24", matrixFunctionService.handleCsv("multiply", file));

        when(parseCsvService.convertArrayToString(any(int[][].class))).thenReturn("1,3\n2,4");
        assertEquals("1,3\n2,4", matrixFunctionService.handleCsv("invert", file));

    }

    @Test
    void testHandleCsv_InvalidOperation() throws Exception {
        MockMultipartFile file = getMockMultipartFile("1,2\n3,4");
        int[][] matrix = {{1, 2}, {3, 4}};
        when(parseCsvService.readAndValidateMatrix(file)).thenReturn(matrix);

        Exception ex = assertThrows(RuntimeException.class, () -> matrixFunctionService
                .handleCsv("unknown", file));
        assertTrue(ex.getMessage().contains("Unexpected value"));
    }

    @Test
    void testHandleCsv_ParseCsvThrows() throws Exception {
        MockMultipartFile file = getMockMultipartFile("parse");
        when(parseCsvService.readAndValidateMatrix(file)).thenThrow(new RuntimeException("parse error"));

        Exception ex = assertThrows(RuntimeException.class, () -> matrixFunctionService
                .handleCsv("echo", file));
        assertTrue(ex.getMessage().contains("Error processing CSV file"));
    }
}