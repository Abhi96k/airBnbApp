package com.codingshuttle.project.airBnbApp.entity;

import com.codingshuttle.project.airBnbApp.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter @Setter
@Table(name="app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;  // In a real application, ensure passwords are hashed and secured.


    @Column(nullable = false)
    private String name;


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


    @OneToMany
    @JoinColumn(name="user_id")
    private Set<Guest> guests;

    @OneToMany
    @JoinColumn(name="user_id")
    private Set<Booking> bookings;



}
