package com.example.image.service.Impl;

import com.example.image.dto.ImageDownloadDto;
import com.example.image.dto.ImageViewDto;
import com.example.image.dto.MetaDataDto;
import com.example.image.exception.ImageNotFoundException;
import com.example.image.exception.ImageUploadingException;
import com.example.image.kafka.ImageEventProducer;
import com.example.image.mapper.GridFSFileMapper;
import com.example.image.service.FileService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ImageEventProducer imageEventProducer;
    private final GridFSBucket gridFSBucket;
    private final GridFsTemplate template;
    private final MongoTemplate mongoTemplate;

    @Override
    public MetaDataDto saveFile(MultipartFile multipartFile) {
        try {
            ObjectId fileId = template.store(multipartFile.getInputStream(),
                    UUID.randomUUID() + "_" + multipartFile.getOriginalFilename(),
                    multipartFile.getContentType());
            GridFSFile file = template.findOne(new Query(Criteria.where("_id").is(fileId)));

            imageEventProducer.sendSuccessMessage(file);
            log.info("File stored successfully with id: {}", fileId);

            return GridFSFileMapper.map(file);
        } catch (IOException e) {
            imageEventProducer.sendFailureMessage(e.getMessage());
            log.error("Error saving file: {}", e.getMessage());
            throw new ImageUploadingException("Error saving file");
        }
    }

    @Override
    public MetaDataDto saveFile(MultipartFile multipartFile, String name) {
        try {
            ObjectId fileId = template.store(multipartFile.getInputStream(), name, multipartFile.getContentType());
            GridFSFile file = template.findOne(new Query(Criteria.where("_id").is(fileId)));

            imageEventProducer.sendSuccessMessage(file);
            log.info("File stored successfully with id: {}", fileId);

            return GridFSFileMapper.map(file);
        } catch (IOException e) {
            imageEventProducer.sendFailureMessage(e.getMessage());
            log.error("Error saving file: {}", e.getMessage());
            throw new ImageUploadingException("Error saving file");
        }
    }

    @Override
    public MetaDataDto findMetaDataById(String id) {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));
        if (gridFSFile == null) {
            log.error("image {} not found", id);
            throw new ImageNotFoundException(String.format("image with id %s not found", id));
        }
        return GridFSFileMapper.map(gridFSFile);
    }

    @Override
    public ImageDownloadDto findFileById(String id) {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));
        if (gridFSFile == null) {
            log.error("image {} not found", id);
            throw new ImageNotFoundException(String.format("image with id %s not found", id));
        }
        InputStream inputStream = gridFSBucket.openDownloadStream(gridFSFile.getFilename());
        InputStreamResource resource = new InputStreamResource(inputStream);

        String encodedFilename = URLEncoder.encode(gridFSFile.getFilename(), StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ImageDownloadDto.builder()
                .type(gridFSFile.getMetadata().getString("_contentType"))
                .resource(resource)
                .filename(encodedFilename)
                .build();
    }

    @Override
    public ImageViewDto findFileByName(String filename) {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("filename").is(filename)));
        if (gridFSFile == null) {
            log.error("image {} not found", filename);
            throw new ImageNotFoundException(String.format("image with filename %s not found", filename));
        }
        InputStream inputStream = gridFSBucket.openDownloadStream(gridFSFile.getFilename());
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ImageViewDto.builder()
                .type(gridFSFile.getMetadata().getString("_contentType"))
                .resource(resource)
                .build();
    }

    @Override
    public Page<MetaDataDto> findAll(PageRequest pageRequest) {
        long totalElements = mongoTemplate.getCollection("fs.files").countDocuments();
        List<MetaDataDto> files = template.find(new Query().with(pageRequest))
                .into(new ArrayList<>()).stream()
                .map(GridFSFileMapper::map)
                .toList();
        return new PageImpl<>(files, pageRequest, totalElements);
    }

    @Override
    public void delete(String id) {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));
        if (gridFSFile == null) {
            log.error("image {} not found,deleting is impossible", id);
            throw new ImageNotFoundException(String.format("image with id %s not found", id));
        }
        template.delete(new Query(Criteria.where("_id").is(id)));
        log.info("Image with id {} was deleted successfully", id);
    }
}
