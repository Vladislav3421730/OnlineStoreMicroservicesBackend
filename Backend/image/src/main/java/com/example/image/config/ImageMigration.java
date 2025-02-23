package com.example.image.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageMigration {

    private final MongoTemplate mongoTemplate;
    private final GridFsTemplate gridFsTemplate;

    @PostConstruct
    public void copyImagesIfFirstRun() {
        boolean imagesAlreadyExist = mongoTemplate.collectionExists("fs.files");

        if (!imagesAlreadyExist) {
            try {
                PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource[] resources = resolver.getResources("classpath:/images/*");

                log.info("Found {} files to upload", resources.length);
                System.out.println("Found " + resources.length + " files to upload");

                for (Resource resource : resources) {
                    try (InputStream inputStream = resource.getInputStream()) {
                        String filename = resource.getFilename();
                        ObjectId fileId = gridFsTemplate.store(inputStream, filename, "image/jpeg");
                        log.info("Uploaded file: {} with ID: {}", filename, fileId);
                        System.out.println("Uploaded file: " + filename + " with ID: " + fileId);
                    }
                }
                log.info("All images uploaded successfully!");
                System.out.println("All images uploaded successfully!");
            } catch (IOException e) {
                log.error("Error reading files", e);
            }
        }
    }
}
