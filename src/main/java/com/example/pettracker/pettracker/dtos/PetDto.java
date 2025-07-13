package com.example.pettracker.pettracker.dtos;

import com.example.pettracker.pettracker.domainmodels.Pet;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetDto {

    @Nullable //Null during insertion
    private Long petId;

    @NotNull(message = "ownerId may not be NULL")
    private Integer ownerId;

    //@NotBlank(message = "Pettype may be CAT or DOG")
    @NotNull(message = "Pettype may not be NULL")
    private String petType;

    //@NotBlank(message = "Trackertype may be SMALL, MEDIUM or BIG")
    @NotNull(message = "Trackertype may not be NULL")
    private String trackerType;

    @NotNull(message = "Either true or false")
    private Boolean inZone;

    @Nullable //Null for the dogs
    private Boolean lostTracker;
}
