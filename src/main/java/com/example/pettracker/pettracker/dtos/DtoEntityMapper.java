package com.example.pettracker.pettracker.dtos;

import com.example.pettracker.pettracker.domainmodels.Pet;
import com.example.pettracker.pettracker.exceptionhandling.BaduserInputException;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public void updateEntity(UpdateDto dto, Pet entity) {
        entity.setInZone(dto.getInZone());
        if(entity.getPetType() == Pet.PetType.CAT) {
            if(dto.getLostTracker() == null) {
                throw new BaduserInputException("value of lostTracker may not be null for Cat");
            }
        } else {
            if(dto.getLostTracker() != null) {
                throw new BaduserInputException("value of lostTracker must be null for dog");
            }
        }
        entity.setLostTracker(dto.getLostTracker());
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

    public List<PetDto> entitiesToDtos(@NonNull List<Pet> entities) {
        List<PetDto> dtos = new ArrayList<>();
        for(Pet entity:entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }
}
