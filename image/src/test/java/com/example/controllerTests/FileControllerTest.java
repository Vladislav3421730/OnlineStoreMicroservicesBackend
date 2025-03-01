package com.example.controllerTests;


import com.example.factory.FileFactory;
import com.example.factory.MetaDataFactory;
import com.example.image.ImageApplication;
import com.example.image.dto.ImageDownloadDto;
import com.example.image.dto.ImageViewDto;
import com.example.image.dto.MetaDataDto;
import com.example.image.exception.ImageNotFoundException;
import com.example.image.service.FileService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = ImageApplication.class)
@AutoConfigureMockMvc
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileService fileService;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> "mongodb://" + mongoDBContainer.getHost()
                + ":" + mongoDBContainer.getMappedPort(27017) + "/mongodb");
    }

    private static MetaDataDto savingMetaDataDto;
    private static MetaDataDto metaDataDto;
    private static MockMultipartFile multipartFileJpeg;
    private static ImageDownloadDto imageDownloadDto;
    private static ImageViewDto imageViewDto;

    @BeforeAll
    static void setup() throws IOException {
        imageViewDto = MetaDataFactory.getImageViewDto();
        metaDataDto = MetaDataFactory.getMetaDataDto();
        savingMetaDataDto = MetaDataFactory.getSavingMetaDataDto();
        multipartFileJpeg = FileFactory.getFileWithJpegFormat();
        imageDownloadDto = MetaDataFactory.getImageDownloadDto();
    }

    @Test
    @Order(1)
    @DisplayName("Test save file")
    public void testSaveFile() throws Exception {
        when(fileService.saveFile(multipartFileJpeg)).thenReturn(savingMetaDataDto);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(multipartFileJpeg)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(savingMetaDataDto.getId()),
                        jsonPath("$.filename").value(savingMetaDataDto.getFilename()),
                        jsonPath("$.type").value(savingMetaDataDto.getType())
                );
        verify(fileService, times(1)).saveFile(multipartFileJpeg);
    }

    @Test
    @Order(2)
    @DisplayName("Test find file meta data by id")
    public void testFindMetaDataById() throws Exception {

        String id = "file_id";
        when(fileService.findMetaDataById(id)).thenReturn(metaDataDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/upload/meta/{id}", id))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.filename").value(metaDataDto.getFilename()),
                        jsonPath("$.type").value(metaDataDto.getType())
                );
        verify(fileService, times(1)).findMetaDataById(id);
    }

    @Test
    @Order(3)
    @DisplayName("Test find file meta data by wrong id")
    public void testFindMetaDataByWrongId() throws Exception {

        String id = "file_id";
        when(fileService.findMetaDataById(id)).thenThrow(new ImageNotFoundException("image not found"));
        mockMvc.perform(MockMvcRequestBuilders.get("/upload/meta/{id}", id))
                .andExpect(status().isNotFound())
                .andExpectAll(
                        jsonPath("$.message").isNotEmpty(),
                        jsonPath("$.code").value(404)
                );
        verify(fileService, times(1)).findMetaDataById(id);
    }

    @Test
    @Order(4)
    @DisplayName("Test find all files meta")
    public void testFindAllFilesMetaData() throws Exception {

        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("uploadDate"));
        when(fileService.findAll(pageRequest))
                .thenReturn(new PageImpl<>(List.of(savingMetaDataDto, metaDataDto), pageRequest, 2));
        mockMvc.perform(MockMvcRequestBuilders.get("/upload/all"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.totalElements").value(2),
                        jsonPath("$.totalPages").value(1),
                        jsonPath("$.first").value(true),
                        jsonPath("$.empty").value(false),
                        jsonPath("$.content.length()").value(2)
                );
        verify(fileService, times(1)).findAll(pageRequest);
    }

    @Test
    @Order(5)
    @DisplayName("Test find file by filename")
    public void testFindFileByFilename() throws Exception {

        String filename = "filename";
        when(fileService.findFileByName(filename)).thenReturn(imageViewDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/upload/{filename}", filename))
                .andExpect(status().isOk());
        verify(fileService, times(1)).findFileByName(filename);
    }

    @Test
    @Order(6)
    @DisplayName("Test find file by wrong filename")
    public void testFindFileByWrongFilename() throws Exception {

        String filename = "wrong_filename";
        when(fileService.findFileByName(filename)).thenThrow(new ImageNotFoundException("image not found"));
        mockMvc.perform(MockMvcRequestBuilders.get("/upload/{filename}", filename))
                .andExpect(status().isNotFound())
                .andExpectAll(
                        jsonPath("$.message").isNotEmpty(),
                        jsonPath("$.code").value(404)
                );
        verify(fileService, times(1)).findFileByName(filename);
    }

    @Test
    @Order(7)
    @DisplayName("Test delete file by id")
    public void testDeleteFileById() throws Exception {
        String id = "file_id";
        doNothing().when(fileService).delete(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/upload/{filename}", id))
                .andExpect(status().isNoContent());
        verify(fileService, times(1)).delete(id);
    }

    @Test
    @Order(8)
    @DisplayName("Test delete file by wrong id")
    public void testDeleteFileByWrongId() throws Exception {
        String id = "wrong_id";
        doThrow(new ImageNotFoundException("File with id " + id + " not found")).when(fileService).delete(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/upload/{filename}", id))
                .andExpect(status().isNotFound())
                .andExpectAll(
                        jsonPath("$.message").isNotEmpty(),
                        jsonPath("$.code").value(404)
                );
        verify(fileService, times(1)).delete(id);
    }

    @Test
    @Order(9)
    @DisplayName("Test download file by  id")
    public void testDownloadFileById() throws Exception {
        String id = "file_id";
        when(fileService.findFileById(id)).thenReturn(imageDownloadDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/upload/download/{id}", id))
                .andExpect(status().isOk());
        verify(fileService, times(1)).findFileById(id);
    }

    @Test
    @Order(10)
    @DisplayName("Test download file by wrong id")
    public void testDownloadFileByWrongId() throws Exception {
        String id = "wrong_id";
        doThrow(new ImageNotFoundException("File with id " + id + " not found")).when(fileService).findFileById(id);
        mockMvc.perform(MockMvcRequestBuilders.get("/upload/download/{id}", id))
                .andExpect(status().isNotFound())
                .andExpectAll(
                        jsonPath("$.message").isNotEmpty(),
                        jsonPath("$.code").value(404)
                );
        verify(fileService, times(1)).findFileById(id);
    }
}
