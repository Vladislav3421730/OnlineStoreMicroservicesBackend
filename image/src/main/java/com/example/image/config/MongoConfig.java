package com.example.image.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String url;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(url);
    }

    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient) {
        return GridFSBuckets.create(mongoClient.getDatabase(database));
    }
}
