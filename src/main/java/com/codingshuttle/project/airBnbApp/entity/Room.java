package com.codingshuttle.project.airBnbApp.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="hotel_id", nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false,precision=10,scale=2)
    private BigDecimal basePrice;

    @ElementCollection
    @CollectionTable(
            name = "room_photos",
            joinColumns = @JoinColumn(name = "room_id")
    )
    @Column(name = "photo_url")
    private List<String> photos;

    @ElementCollection
    @CollectionTable(
            name = "room_amenities",
            joinColumns = @JoinColumn(name = "room_id")
    )
    @Column(name = "amenity")
    private List<String> amenities;

   @Column(nullable= false)
    private Integer totalcount;


    @Column(nullable= false)
    private Integer capacity;


    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(updatable = false)
    private Date updatedAt;

}
