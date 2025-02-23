package com.example.market.kafka;


import com.example.market.dto.ErrorResponseDto;
import com.example.market.dto.MetaDataDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumer {

    @Value("${kafka.host}")
    private String bootstrapServers;

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return props;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MetaDataDto> kafkaMetaDataListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MetaDataDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        ConsumerFactory<String, MetaDataDto> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(MetaDataDto.class));
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ErrorResponseDto> kafkaErrorResponseListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ErrorResponseDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        ConsumerFactory<String, ErrorResponseDto> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(ErrorResponseDto.class));
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}



