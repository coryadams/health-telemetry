package com.heartpass.healthtelemetry.biz;

import com.garmin.fit.RecordMesg;
import com.garmin.fit.RecordMesgListener;
import com.garmin.fit.util.DateTimeConverter;
import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.domain.HealthEventContainer;

import java.time.OffsetDateTime;
import java.util.ArrayList;

/**
 * Use when decoding FIT files.
 * <p>
 * Implement any particular Garmin SDK Listeners that are appropriate.
 */
public class GarminMessageListener implements RecordMesgListener {

    HealthEventContainer healthEventContainer;
    ArrayList<HealthEvent> healthEvents;
    Integer userProfileId;
    Integer activityId;

    public GarminMessageListener(HealthEventContainer healthEventContainer, Integer userProfileId, Integer activityId) {
        this.healthEventContainer = healthEventContainer;
        this.healthEvents = new ArrayList<>();
        healthEventContainer.setHealthEvents(healthEvents);
        this.userProfileId = userProfileId;
        this.activityId = activityId;
    }

    /**
     * The RecordMesg provides timestamp and heart rate information.
     *
     * @param mesg
     */
    @Override
    public void onMesg(RecordMesg mesg) {
        HealthEvent healthEvent = new HealthEvent();
        healthEvent.setUserProfileId(userProfileId);
        if (mesg.getTimestamp() != null && mesg.getTimestamp() != null) {
            String isoDateTime = DateTimeConverter.fitTimestampToISO8601(mesg.getTimestamp().getTimestamp());
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoDateTime);
            healthEvent.setEventDateTime(offsetDateTime.toLocalDateTime());
            healthEvent.setActivityId(activityId);
        }

        healthEvent.setHeartRateBpm((mesg.getHeartRate() != null) ? Integer.valueOf(mesg.getHeartRate().intValue()) : null);
        this.healthEvents.add(healthEvent);
    }
}
