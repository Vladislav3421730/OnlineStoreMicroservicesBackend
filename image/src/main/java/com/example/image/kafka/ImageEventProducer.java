package com.example.image.kafka;

import com.example.image.dto.ErrorResponseDto;
import com.example.image.dto.MetaDataDto;
import com.example.image.mapper.GridFSFileMapper;
import com.example.image.util.Topics;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageEventProducer {

    private final KafkaTemplate<String, MetaDataDto> kafkaMetaDataTemplate;
    private final KafkaTemplate<String, ErrorResponseDto> kafkaErrorResponseTemplate;

    public void sendSuccessMessage(GridFSFile file) {
        MetaDataDto metaDataDto = GridFSFileMapper.map(file);
        kafkaMetaDataTemplate.send(Topics.IMAGE_SAVED, metaDataDto);
        log.info("Sent success message to Kafka: {}", metaDataDto);
    }

    public void sendFailureMessage(String message) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(message);
        kafkaErrorResponseTemplate.send(Topics.IMAGE_SAVED_FAILURE, errorResponseDto);
        log.error("Sent failure message to Kafka: {}", errorResponseDto);
    }
}

