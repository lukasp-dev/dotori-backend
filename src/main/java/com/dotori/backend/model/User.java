package com.dotori.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users")  
public class User {

    @Id
    @Column(name = "\"id\"")
    private String id;

    @Column(name = "\"firstname\"", nullable = false)
    private String firstname;

    @Column(name = "\"lastname\"", nullable = false)
    private String lastname;

    @Column(name = "\"email\"", nullable = false, unique = true)
    private String email;

    @Column(name = "\"password\"")
    private String password;

    @Column(name = "\"role\"")
    private String role;

    @Column(name = "\"image\"")
    private String image;

    @Column(name = "\"name\"")
    private String name;

    @Column(name = "\"emailVerified\"")
    private Timestamp emailVerified;

    @Column(name = "\"createdAt\"", nullable = false)
    private Timestamp createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        Timestamp now = Timestamp.from(java.time.Instant.now());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Timestamp.from(java.time.Instant.now());
    }
}
