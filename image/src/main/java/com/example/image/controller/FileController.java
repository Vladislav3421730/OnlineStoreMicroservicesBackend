package com.example.image.controller;

import com.example.image.dto.ImageDownloadDto;
import com.example.image.dto.ImageViewDto;
import com.example.image.dto.MetaDataDto;
import com.example.image.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", exposedHeaders = "*")
@Tag(name = "File",description = "Using this controller you can download, view, delete pictures")
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MetaDataDto> saveFile(
            @RequestParam("file") MultipartFile file) {
        MetaDataDto metaDataDto = fileService.saveFile(file);
        return ResponseEntity.ok(metaDataDto);
    }

    @PostMapping(value = "/db", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MetaDataDto> saveFileWithFilenameInDatabase(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String fileName) {
        MetaDataDto metaDataDto = fileService.saveFile(file, fileName);
        return ResponseEntity.ok(metaDataDto);
    }

    @GetMapping("/meta/{id}")
    public ResponseEntity<MetaDataDto> getMetaData(@PathVariable String id) {
        MetaDataDto metaDataDto = fileService.findMetaDataById(id);
        return ResponseEntity.ok(metaDataDto);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Page<MetaDataDto>> findAll(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy) {
        if (page == null) page = 0;
        if (pageSize == null) pageSize = 20;
        if (sortBy == null || sortBy.isEmpty()) sortBy = "uploadDate";
        Page<MetaDataDto> files = fileService.findAll(PageRequest.of(page, pageSize, Sort.by(sortBy)));
        return ResponseEntity.ok(files);
    }

    @GetMapping(value = "/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        ImageViewDto imageDto = fileService.findFileByName(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imageDto.getType()))
                .body(imageDto.getResource());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {
        fileService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) {
        ImageDownloadDto imageDownloadDto = fileService.findFileById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imageDownloadDto.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageDownloadDto.getFilename() + "\"")
                .body(imageDownloadDto.getResource());
    }
}
