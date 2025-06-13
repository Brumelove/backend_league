package com.protobuf.backend_league.controller;

import com.protobuf.backend_league.service.MatrixFunctionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.protobuf.backend_league.helper.TestHelper.getMockMultipartFile;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatrixController.class)
class MatrixControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatrixFunctionService matrixFunctionService;

    @Test
    void testHandleCsv_EchoAction() throws Exception {
        MockMultipartFile file = getMockMultipartFile("1,2\n3,4");
        when(matrixFunctionService.handleMatriAction(eq("echo"), any())).thenReturn("1,2\n3,4");

        mockMvc.perform(multipart("/echo")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("1,2\n3,4"));
    }

    @Test
    void testHandleCsv_InvertAction() throws Exception {
        MockMultipartFile file = getMockMultipartFile("1,2\n3,4");
       when(matrixFunctionService.handleMatriAction(eq("invert"), any())).thenReturn("1,3\n2,4");

        mockMvc.perform(multipart("/invert")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("1,3\n2,4"));
    }

    @Test
    void testHandleCsv_MissingFile() throws Exception {
        mockMvc.perform(multipart("/echo")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResponse().getContentAsString().contains("Missing required file part:")
                ));
    }
}