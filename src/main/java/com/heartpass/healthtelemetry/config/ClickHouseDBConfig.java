package com.heartpass.healthtelemetry.config;

import com.clickhouse.client.api.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClickHouseDBConfig {

    @Value("${clickhouse.username}")
    private String username;

    @Value("${clickhouse.password}")
    private String password;

    @Value("${clickhouse.url}")
    private String clickhouseUrl;

    @Value("${clickhouse.database}")
    private String database;

    @Value("${clickhouse.max.connections}")
    private int maxConnections;

    @Bean
    public Client chDirectClient() {
        return new Client.Builder()
                .addEndpoint(clickhouseUrl)
                .setDefaultDatabase(database)
                .setUsername(username)
                .setPassword(password)
                .useNewImplementation(true) // using new transport layer implementation

                // sets the maximum number of connections to the server at a time
                // this is important for services handling many concurrent requests to ClickHouse
                .setMaxConnections(100)
                .setLZ4UncompressedBufferSize(1058576)
                .setSocketRcvbuf(500_000)
                .setSocketTcpNodelay(true)
                .setSocketSndbuf(500_000)
                .setClientNetworkBufferSize(500_000)
                .allowBinaryReaderToReuseBuffers(true) // using buffer pool for binary reader
                .build();
    }
}


