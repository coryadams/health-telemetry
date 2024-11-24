package com.heartpass.healthtelemetry.service;

import com.heartpass.healthtelemetry.domain.HealthEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@Slf4j
@Service
public class TcxFileProcessor {

    @Autowired
    private ResourceLoader resourceLoader;

    public ArrayList<HealthEvent> processFile(String fileName, String userName) throws IOException {
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
                    String foo = element.getName().getLocalPart();
                    switch (element.getName().getLocalPart()) {
                        // if <staff>
                        case "Trackpoint":
                            healthEvent = new HealthEvent();
                            healthEvent.setUserId(userName);
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
                        healthEvents.add(healthEvent);
                    }
                }
            }
            inputStream.close();
            reader.close();
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("TCX file processed with {} events.", healthEvents.size());
        return healthEvents;
    }
}
