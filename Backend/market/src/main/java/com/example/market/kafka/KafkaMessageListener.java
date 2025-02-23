package com.example.market.kafka;

import com.example.market.dto.ErrorResponseDto;
import com.example.market.dto.MetaDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaMessageListener {

    @KafkaListener(topics = "image.saved", groupId = "consuming", containerFactory = "kafkaMetaDataListenerFactory")
    public void listenerOnSuccess(MetaDataDto metaData) {
        log.info("Successfully received metadata from kafka: {}", metaData);
    }

    @KafkaListener(topics = "image.saved.failure", groupId = "consuming", containerFactory = "kafkaErrorResponseListenerFactory")
    public void listenerOnFailure(ErrorResponseDto errorResponse) {
        log.error("Error received from kafka: {}", errorResponse);
    }
}
