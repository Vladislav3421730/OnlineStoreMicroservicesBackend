package com.example.serviceTests;

import com.example.factory.FileFactory;
import com.example.image.ImageApplication;
import com.example.image.dto.ImageDownloadDto;
import com.example.image.dto.ImageViewDto;
import com.example.image.dto.MetaDataDto;
import com.example.image.exception.ImageNotFoundException;
import com.example.image.exception.ImageUploadingException;
import com.example.image.service.FileService;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest(classes = ImageApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withExposedPorts(27017);

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose-test-kafka.yaml"))
                    .withExposedService("kafka", 29092,
                            Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));


    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> "mongodb://" + mongoDBContainer.getHost()
                + ":" + mongoDBContainer.getMappedPort(27017) + "/mongodb");
    }

    private static MultipartFile multipartFileJpeg;
    private static MultipartFile multipartFilePng;

    @BeforeAll
    static void setup() throws IOException {
        environment.start();
        multipartFileJpeg = FileFactory.getFileWithJpegFormat();
        multipartFilePng = FileFactory.getFileWithPngFormat();
    }

    @AfterAll
    static void teardown() {
        environment.stop();
    }

    @Test
    @Order(1)
    @DisplayName("Test save image with .jpeg format")
    public void testSaveImageJpeg() {
        MetaDataDto metaDataDto = fileService.saveFile(multipartFileJpeg);
        assertEquals(metaDataDto.getType(), multipartFileJpeg.getContentType());
        assertTrue(metaDataDto.getFilename().matches(".*test\\.jpg$"));
    }

    @Test
    @Order(2)
    @DisplayName("Test save image with .png format")
    public void testSaveImagePng() {
        MetaDataDto metaDataDto = fileService.saveFile(multipartFilePng);
        assertEquals(metaDataDto.getType(), multipartFilePng.getContentType());
        assertTrue(metaDataDto.getFilename().matches(".*test\\.png$"));
    }

    @Test
    @Order(3)
    @DisplayName("Test save image with .jpeg format - error case")
    public void testSaveImageJpegError() throws Exception {

        MultipartFile faultyMultipartFile = mock(MultipartFile.class);
        when(faultyMultipartFile.getInputStream()).thenThrow(new IOException("Input stream error"));
        when(faultyMultipartFile.getOriginalFilename()).thenReturn("test.jpg");
        assertThrows(ImageUploadingException.class, () -> fileService.saveFile(faultyMultipartFile));
    }

    @Test
    @Order(4)
    @DisplayName("Test save image with .jpeg format, which filename is in table image in postgres db")
    public void testSaveImageWhichFileNameInDb() {
        String nameInPostgresDb = "image_postgres_name.jpg";
        MetaDataDto metaDataDto = fileService.saveFile(multipartFileJpeg, nameInPostgresDb);
        assertEquals(metaDataDto.getType(), multipartFileJpeg.getContentType());
        assertEquals(metaDataDto.getFilename(), nameInPostgresDb);
    }

    @Test
    @Order(5)
    @DisplayName("Test find image by id")
    public void testFindImageById() {
        MetaDataDto metaDataDto = fileService.saveFile(multipartFileJpeg);
        ImageDownloadDto imageDownloadDto = fileService.findFileById(metaDataDto.getId());
        assertEquals(imageDownloadDto.getType(), metaDataDto.getType());
        assertEquals(imageDownloadDto.getFilename(), metaDataDto.getFilename());
        assertNotNull(imageDownloadDto.getResource());
    }

    @Test
    @Order(6)
    @DisplayName("Test find image by filename")
    public void testFindImageByFileName() {
        MetaDataDto metaDataDto = fileService.saveFile(multipartFileJpeg);
        ImageViewDto imageViewDto = fileService.findFileByName(metaDataDto.getFilename());
        assertEquals(imageViewDto.getType(), metaDataDto.getType());
        assertNotNull(imageViewDto.getResource());
    }

    @Test
    @Order(7)
    @DisplayName("Test find all images")
    public void testFindAllImages() {
        fileService.saveFile(multipartFileJpeg);
        fileService.saveFile(multipartFileJpeg);
        Page<MetaDataDto> dtoPage = fileService.findAll(PageRequest.of(1, 10));
        assertNotNull(dtoPage);
        assertTrue(dtoPage.getTotalElements() >= 2);
    }

    @Test
    @Order(8)
    @DisplayName("Test delete image")
    public void testDeleteImage() {
        MetaDataDto metaDataDto = fileService.saveFile(multipartFileJpeg);
        fileService.delete(metaDataDto.getId());
        assertThrows(ImageNotFoundException.class, () -> fileService.findFileById(metaDataDto.getId()));
    }

    @Test
    @Order(9)
    @DisplayName("Test delete image by wrong id")
    public void testDeleteImageWithWrongId() {
        assertThrows(ImageNotFoundException.class, () -> fileService.delete("wrong_id"));
    }

    @Test
    @Order(10)
    @DisplayName("Test find image by wrong id")
    public void testFindImageWrongById() {
        assertThrows(ImageNotFoundException.class, () -> fileService.findFileById("wrong_id"));
    }

    @Test
    @Order(11)
    @DisplayName("Test find image by wrong filename")
    public void testFindImageWrongByFileName() {
        assertThrows(ImageNotFoundException.class, () -> fileService.findFileByName("wrong_filename"));
    }

}
