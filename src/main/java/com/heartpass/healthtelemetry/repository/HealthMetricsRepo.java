package com.heartpass.healthtelemetry.repository;

import com.clickhouse.client.api.Client;
import com.clickhouse.client.api.data_formats.ClickHouseBinaryFormatReader;
import com.clickhouse.client.api.insert.InsertResponse;
import com.clickhouse.client.api.insert.InsertSettings;
import com.clickhouse.client.api.metrics.ClientMetrics;
import com.clickhouse.client.api.query.QueryResponse;
import com.heartpass.healthtelemetry.domain.HealthEvent;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Repo classes are using the Clickhouse V2 client and not JDBC for performance.
 */
@Slf4j
@Service
public class HealthMetricsRepo {

    @Autowired
    Client chDirectClient;

    @Value("${clickhouse.database}")
    private String databaseSchema;

    static final String TABLE_NAME = "health_events";

    @PostConstruct
    public void init() {
        // Register the class with the Clickhouse Client
        chDirectClient.register(HealthEvent.class, chDirectClient.getTableSchema(TABLE_NAME));
    }

    public ArrayList<HealthEvent> findByUserIdAndSessionId(String userId, String sessionId) {
        ArrayList<HealthEvent> events = new ArrayList<>();
        log.info("Reading data from table: {}", TABLE_NAME);

        final String sql = "select * from " + TABLE_NAME + " where user_id = '" + userId + "' and " +
                "session_id = '" + sessionId + "'";
        log.info("Executing query: {}", sql);
         // Default format is RowBinaryWithNamesAndTypesFormatReader
        QueryResponse response = null;
        try  {
            response = chDirectClient.query(sql).get(3, TimeUnit.SECONDS);

            ClickHouseBinaryFormatReader reader = chDirectClient.newBinaryFormatReader(response);

            while (reader.hasNext()) {
                reader.next(); // Read the next record from stream and parse it
                // get values
                HealthEvent healthEvent = HealthEvent.builder()
                        .userId(reader.getString("user_id"))
                        .sessionId(reader.getString("session_id"))
                        .heartRateBpm(reader.getInteger("heart_rate_bpm"))
                        .eventDateTime(reader.getLocalDateTime("event_datetime"))
                        .build();
                events.add(healthEvent);
            }
            log.info("Data read successfully: {} ms for {} rows. ",
                    response.getMetrics().getMetric(ClientMetrics.OP_DURATION).getLong(), response.getReadRows());
        } catch (Exception e) {
            log.error("Failed to read data", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return events;
    }

    public int save(ArrayList<HealthEvent> events) {
        InsertSettings insertSettings = new InsertSettings();
        try (InsertResponse response = chDirectClient.insert(TABLE_NAME, events, insertSettings).get()) {
            // handle response, then it will be closed and connection that served request will be released.
            log.info("Data saved successfully: {} ms for {} rows.",
                    response.getMetrics().getMetric(ClientMetrics.OP_DURATION).getLong(), response.getWrittenRows());
            response.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return events.size();
    }
}
