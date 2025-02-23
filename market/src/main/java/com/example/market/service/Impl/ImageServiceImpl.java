package com.example.market.service.Impl;

import com.example.market.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
@Setter
public class ImageServiceImpl implements ImageService {

    private final RestClient restClient;

    @Value("${service.upload.url}")
    private String requestUrl;

    public void sendfile(MultipartFile multipartFile, String fileName) {
        byte[] fileContent;
        try {
            fileContent = multipartFile.getBytes();
        } catch (IOException e) {
            log.error("Error reading file content", e);
            return;
        }
        ByteArrayResource fileResource = new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        body.add("name", fileName);
        restClient.post()
                .uri(requestUrl)
                .body(body)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .retrieve()
                .toBodilessEntity();
    }
}
