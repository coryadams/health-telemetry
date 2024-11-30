package com.heartpass.healthtelemetry.biz;

import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.domain.HealthEventContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;

@Slf4j
@Service
public class TcxFileProcessor implements FileProcessor {

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public HealthEventContainer processFile(String fileName, Integer userProfileId, Integer activityId) throws IOException {
        HealthEventContainer healthEventContainer = new HealthEventContainer();
        ArrayList<HealthEvent> healthEvents = new ArrayList();
        try {
            File file = new File(fileName);
            InputStream inputStream = new FileInputStream(file);
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(inputStream);
            HealthEvent healthEvent = null;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {

                    StartElement element = event.asStartElement();

                    switch (element.getName().getLocalPart()) {
                        case "Lap":
                            if (element.getAttributeByName(new QName("StartTime")) != null) {
                                // Parse the ISO 8601 string into an OffsetDateTime
                                OffsetDateTime offsetDateTime = OffsetDateTime.parse(element.getAttributeByName(new QName("StartTime")).getValue());
                                // Convert the OffsetDateTime to a LocalDateTime
                                healthEventContainer.setEventStartDateTime(offsetDateTime.toLocalDateTime());
                            }
                            break;
                        case "TotalTimeSeconds":
                            event = reader.nextEvent();
                            if(event.asCharacters().getData() != null) {
                                healthEventContainer.setTotalTimeSeconds(Math.round(Float.parseFloat(event.asCharacters().getData())));
                            }
                            break;
                        case "DistanceMeters":
                            event = reader.nextEvent();
                            healthEventContainer.setDistanceMeters(Math.round(Float.parseFloat(event.asCharacters().getData())));
                            break;
                        case "MaximumSpeed":
                            event = reader.nextEvent();
                            if(event.asCharacters().getData() != null) {
                                healthEventContainer.setMaxSpeed(Math.round(Float.parseFloat(event.asCharacters().getData())));
                            }
                            break;
                        case "Calories":
                            event = reader.nextEvent();
                            if(event.asCharacters().getData() != null) {
                                healthEventContainer.setCalories(Math.round(Float.parseFloat(event.asCharacters().getData())));
                            }
                            break;
                        case "AverageHeartRateBpm":
                            event = reader.nextEvent();
                            event = reader.nextEvent();
                            event = reader.nextEvent();
                            if(event.asCharacters().getData() != null) {
                                healthEventContainer.setAverageHeartRateBpm(Integer.parseInt(event.asCharacters().getData()));
                            }
                            break;
                        case "MaximumHeartRateBpm":
                            event = reader.nextEvent();
                            event = reader.nextEvent();
                            event = reader.nextEvent();
                            if(event.asCharacters().getData() != null) {
                                healthEventContainer.setMaxHeartRateBpm(Integer.parseInt(event.asCharacters().getData()));
                            }
                            break;
                        case "Trackpoint":
                            healthEvent = new HealthEvent();
                            healthEvent.setActivityId(activityId);
                            healthEvent.setUserProfileId(userProfileId);
                            break;
                        case "Time":
                            event = reader.nextEvent();
                            if (event.isCharacters()) {
                                // Parse the ISO 8601 string into an OffsetDateTime
                                OffsetDateTime offsetDateTime = OffsetDateTime.parse(event.asCharacters().getData());
                                // Convert the OffsetDateTime to a LocalDateTime
                                healthEvent.setEventDateTime(offsetDateTime.toLocalDateTime());
                            }
                            break;
                        case "HeartRateBpm":
                            event = reader.nextEvent();
                            event = reader.nextEvent();
                            event = reader.nextEvent();
                            healthEvent.setHeartRateBpm(Integer.parseInt(event.asCharacters().getData()));
                            break;
                    }
                }
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals("Trackpoint")) {
                        healthEvents.add(healthEvent);
                    }
                }
            }
            healthEventContainer.setHealthEvents(healthEvents);
            inputStream.close();
            reader.close();
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("TCX file name {} processed with {} events.", fileName, healthEvents.size());
        return healthEventContainer;
    }
}
