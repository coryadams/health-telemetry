package com.heartpass.healthtelemetry.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private String fileName;

    int totalTimeSeconds;

    int distanceMeters;

    int maxSpeed;

    int calories;

    int avgHeartRateBpm;

    int maxHeartRateBpm;

    private LocalDateTime activityDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_profile_id", nullable=false)
    private UserProfile userProfile;

    private LocalDateTime createdAt;
}
