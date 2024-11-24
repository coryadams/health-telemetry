package com.heartpass.healthtelemetry.config;

import com.clickhouse.jdbc.ClickHouseDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class ClickHouseDBConfig {

    @Value("${clickhouse.username}")
    private String username;

    @Value("${clickhouse.password}")
    private String password;

    @Value("${clickhouse.jdbc.url}")
    private String clickhouseUrl;

    @Bean
    public ClickHouseDataSource getClickHouseDataSource() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        return new ClickHouseDataSource(clickhouseUrl, properties);
    }
}


