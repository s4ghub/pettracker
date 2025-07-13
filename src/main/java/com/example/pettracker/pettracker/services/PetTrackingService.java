package com.example.pettracker.pettracker.services;

import com.example.pettracker.pettracker.dtos.IOutOfZoneCount;
import com.example.pettracker.pettracker.dtos.PetDto;

import java.util.List;

//TODO: define methods
public interface PetTrackingService {

    PetDto createPetInfo(PetDto dto);

    PetDto fetchPetInfo(long id);

    void modifyPetInfo(PetDto dto);

    List<PetDto> getAllPets();

    List<IOutOfZoneCount> petsOutOfPowerSavingZone();
}
