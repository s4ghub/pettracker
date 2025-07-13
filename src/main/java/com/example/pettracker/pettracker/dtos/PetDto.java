package com.example.pettracker.pettracker.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetDto {

    @Nullable //Null during insertion
    private Long petId;

    @NotNull(message = "ownerId may not be NULL")
    private Integer ownerId;

    @NotNull(message = "Pettype may not be NULL")
    private String petType;

    @NotNull(message = "Trackertype may not be NULL")
    private String trackerType;

    @NotNull(message = "inZone is either true or false")
    private Boolean inZone;

    @Nullable //Null for the dogs
    private Boolean lostTracker;
}
