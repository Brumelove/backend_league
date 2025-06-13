package com.protobuf.backend_league.service;

import com.protobuf.backend_league.exceptions.InvalidMatrixException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brume on 13/06/2025
 */
@Service
@RequiredArgsConstructor
public class ParseCsvService {

    public String convertArrayToString(int[][] matrix) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : matrix) {
            for (int val : row) {
                stringBuilder.append(val).append(",");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            stringBuilder.append("\n");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public int[][] readAndValidateMatrix(MultipartFile file) throws Exception {
        List<int[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int rowLength = -1;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
               
                if (rowLength == -1) {
                    rowLength = values.length;
                }
                else if (values.length != rowLength) {
                    throw new InvalidMatrixException("All rows must have the same number of columns.");
                }
                
                int[] row = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    row[i] = Integer.parseInt(values[i].trim());
                }
                rows.add(row);
            }
        }

        int[][] matrix = new int[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            matrix[i] = rows.get(i);
        }
        if (matrix.length != matrix[0].length)
            throw new InvalidMatrixException("Matrix must be square: same number of rows and columns.");

        return matrix;
    }
}
