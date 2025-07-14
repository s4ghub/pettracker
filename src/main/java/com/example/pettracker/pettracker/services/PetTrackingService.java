package com.example.pettracker.pettracker.services;

import com.example.pettracker.pettracker.dtos.IOutOfZoneCount;
import com.example.pettracker.pettracker.dtos.PetDto;
import com.example.pettracker.pettracker.dtos.UpdateDto;

import java.util.List;

//Pagination not used for simplicity
public interface PetTrackingService {

    PetDto createPetInfo(PetDto dto);

    PetDto fetchPetInfo(long id);

    void modifyPetInfo(UpdateDto dto);

    List<PetDto> getAllPets();

    List<IOutOfZoneCount> petsOutOfPowerSavingZone();

    List<PetDto> getAllLostPets();

    List<PetDto> getAllByOwner(int ownerId);
}
