package com.example.image.kafka;

import com.example.image.dto.ErrorResponseDto;
import com.example.image.dto.MetaDataDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducer {

    @Value("${kafka.host}")
    private String bootstrapServers;

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, MetaDataDto> kafkaMetaDataTemplate() {
        ProducerFactory<String, MetaDataDto> producerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, ErrorResponseDto> kafkaErrorResponseTemplate() {
        ProducerFactory<String, ErrorResponseDto> producerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
        return new KafkaTemplate<>(producerFactory);
    }
}

