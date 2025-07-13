package com.example.pettracker.pettracker.dtos;

import com.example.pettracker.pettracker.domainmodels.Pet;
import lombok.Data;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
//@Data
public class DtoEntityMapper {
    public Pet dtoToEntity(@NonNull PetDto dto, Pet entity) {
        if(entity == null) {
            entity = new Pet();
        }
        entity.setOwnerId(dto.getOwnerId());
        entity.setPetType(Enum.valueOf(Pet.PetType.class, dto.getPetType().toUpperCase()));
        entity.setTrackerType(Enum.valueOf(Pet.TrackerType.class, dto.getTrackerType().toUpperCase()));
        entity.setInZone(dto.getInZone());
        entity.setLostTracker(dto.getLostTracker());

        return entity;
    }

    public PetDto entityToDto(@NonNull Pet entity) {
        PetDto dto = new PetDto();
        dto.setPetId(entity.getId());
        dto.setOwnerId(entity.getOwnerId());
        dto.setPetType(entity.getPetType().toString());
        dto.setTrackerType(entity.getTrackerType().toString());
        dto.setInZone(entity.getInZone());
        dto.setLostTracker(entity.getLostTracker());

        return dto;
    }
}
