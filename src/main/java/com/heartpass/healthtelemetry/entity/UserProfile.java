package com.heartpass.healthtelemetry.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "user_profile")
@Scope("session")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userId;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "id")
    private List<Activity> activities;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
