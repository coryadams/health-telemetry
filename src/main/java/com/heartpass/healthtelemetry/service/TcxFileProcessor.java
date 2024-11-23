package com.heartpass.healthtelemetry.service;

import com.heartpass.healthtelemetry.domain.HealthEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.*;
import javax.xml.stream.XMLInputFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Slf4j
@Service
public class TcxFileProcessor {

    @Autowired
    private ResourceLoader resourceLoader;

    public ArrayList<HealthEvent> processFile(String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:activity_11_21_2024_gym.tcx");
        ArrayList<HealthEvent> healthEventList = new ArrayList();
        try (InputStream inputStream = resource.getInputStream()) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(resource.getFile()));
            HealthEvent healthEvent = null;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {

                    StartElement element = event.asStartElement();
                    String foo = element.getName().getLocalPart();
                    switch (element.getName().getLocalPart()) {
                        // if <staff>
                        case "Trackpoint":
                            healthEvent = new HealthEvent();
                            healthEvent.setUserId("coryadams@yahoo.com");
                            break;
                        case "Time":
                            event = reader.nextEvent();
                            if (event.isCharacters()) {
                                healthEvent.setEventDateTime(ZonedDateTime.parse(event.asCharacters().getData()));
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
                        healthEventList.add(healthEvent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return healthEventList;
    }


}
