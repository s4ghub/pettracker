package com.example.pettracker.pettracker;

import com.example.pettracker.pettracker.dtos.PetDto;
import com.example.pettracker.pettracker.dtos.UpdateDto;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    /**
     * Initial info inserted everytime before each test
     */
    public static List<PetDto> getInitialDataToInsert() {
        List<PetDto> dtos = new ArrayList<>();
        dtos.add(getInsertableDto(123, "CAT", "BIG", false, false));
        dtos.add(getInsertableDto(123, "CAT", "SMALL", false, false));
        dtos.add(getInsertableDto(456, "DOG", "BIG", false, null));
        dtos.add(getInsertableDto(456, "DOG", "MEDIUM", false, null));
        dtos.add(getInsertableDto(456, "DOG", "SMALL", false, null));
        return dtos;
    }

    public static PetDto getInsertableDto(int ownerId, String petType, String trackerType, Boolean inZone, Boolean lostTracker) {
        PetDto dto = new PetDto();
        dto.setOwnerId(ownerId);
        dto.setPetType(petType);
        dto.setTrackerType(trackerType);
        dto.setInZone(inZone);
        dto.setLostTracker(lostTracker);
        return dto;
    }

    public static UpdateDto getUpdateDto(long petId, Boolean inZone, Boolean lostTracker) {
        UpdateDto dto = new UpdateDto();
        dto.setPetId(petId);
        dto.setInZone(inZone);
        dto.setLostTracker(lostTracker);
        return dto;
    }
}
