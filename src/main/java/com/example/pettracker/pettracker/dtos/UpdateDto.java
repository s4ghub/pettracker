package com.example.pettracker.pettracker.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDto {
    @NotNull
    private Long petId;

    @NotNull(message = "Either true or false")
    private Boolean inZone;

    @Nullable //Null for the dogs
    private Boolean lostTracker;
}
