package com.example.pettracker.pettracker.domainmodels;

import jakarta.persistence.*;
import lombok.Data;

//TODO: Need to add equals and hashCode

/**
 * Single table and no join with other tables needed
 */
@Data
@Entity
@Table(name = "pet")
public class Pet {
    public enum PetType {CAT, DOG};
    public enum TrackerType {SMALL, MEDIUM, BIG};

    //id of the tracker in the cat or the dog
    //TODO: Exposed id in the db. Should be modified later
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ownerId")
    private Integer ownerId;

    @Column(name = "petType")
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Column(name = "trackerType")
    @Enumerated(EnumType.STRING)
    private TrackerType trackerType;

    @Column(name = "inZone")
    private Boolean inZone;

    //For simplicity, we didn't create Entity inheritance
    //null means it's a dog
    @Column(name = "lostTracker")
    private Boolean lostTracker;
}
