package com.protobuf.backend_league.service;

import com.protobuf.backend_league.exceptions.CsvProcessingException;
import com.protobuf.backend_league.exceptions.InvalidMatrixException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Brume on 13/06/2025
 */
@Service
@RequiredArgsConstructor
public class MatrixFunctionService {
    private final ParseCsvService parseCsvService;

    public String echo(int[][] matrix) {
        return parseCsvService.convertArrayToString(matrix);
    }

    public String invert(int[][] matrix) {
        int n = matrix.length;
        int[][] transposed = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                transposed[j][i] = matrix[i][j];
        return parseCsvService.convertArrayToString(transposed);
    }

    public String flatten(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new InvalidMatrixException("Matrix is empty or malformed.");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : matrix) {
            for (int val : row) {
                stringBuilder.append(val).append(",");
            }
        }

        // Remove the final trailing comma
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public int sum(int[][] matrix) {
        int total = 0;
        for (int[] row : matrix) {
            for (int val : row) {
                total += val;
            }
        }
        return total;
    }

    public int multiply(int[][] matrix) {
        int result = 1;
        for (int[] row : matrix) {
            for (int val : row) {
                result *= val;
                }
        }
        return result;
    }

    public String handleMatriAction(String operation, MultipartFile file) {

        try {
            int[][] matrix = parseCsvService.readAndValidateMatrix(file);
            return switch (operation) {
                case "echo" -> echo(matrix);
                case "invert" -> invert(matrix);
                case "flatten" -> flatten(matrix);
                case "sum" -> String.valueOf(sum(matrix));
                case "multiply" -> String.valueOf(multiply(matrix));
                default -> throw new IllegalStateException("Unexpected value: " + operation);
            };
        } catch (Exception e) {
            throw new CsvProcessingException("Error processing CSV file: " + e.getMessage(), e);
        }

    }

}
