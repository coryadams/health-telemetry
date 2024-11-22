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
            XMLStreamReader streamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(resource.getFile()));
            HealthEvent healthEvent = null;
            while (streamReader.hasNext()) {
                //Move to next event
                streamReader.next();

                //Check if its 'START_ELEMENT'
                if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
                    //employee tag - opened
                    if (streamReader.getLocalName().equalsIgnoreCase("Trackpoint")) {

                        healthEvent = new HealthEvent();

                        //Read name data
                        if (streamReader.getLocalName().equalsIgnoreCase("Time")) {
                            healthEvent.setEventDateTime(Instant.parse(streamReader.getElementText()));
                        }

                        //Read title data
                        if (streamReader.getLocalName().equalsIgnoreCase("Value")) {
                            healthEvent.setHeartRateBpm(Integer.parseInt(streamReader.getElementText()));
                        }
                    }

                    //If employee tag is closed then add the employee object to list
                    if (streamReader.getEventType() == XMLStreamReader.END_ELEMENT) {
                        if (streamReader.getLocalName().equalsIgnoreCase("employee")) {
                            healthEventList.add(healthEvent);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return healthEventList;
    }

}
