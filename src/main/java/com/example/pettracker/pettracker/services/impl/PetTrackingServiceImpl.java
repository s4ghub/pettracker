package com.example.pettracker.pettracker.services.impl;

import com.example.pettracker.pettracker.domainmodels.Pet;
import com.example.pettracker.pettracker.dtos.DtoEntityMapper;
import com.example.pettracker.pettracker.dtos.IOutOfZoneCount;
import com.example.pettracker.pettracker.dtos.PetDto;
import com.example.pettracker.pettracker.exceptionhandling.BaduserInputException;
import com.example.pettracker.pettracker.repositories.PetTrackingRepository;
import com.example.pettracker.pettracker.services.PetTrackingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//TODO: implement methods
@Service
public class PetTrackingServiceImpl implements PetTrackingService {

    private final PetTrackingRepository repository;
    private final DtoEntityMapper mapper;

    @Autowired
    public PetTrackingServiceImpl(PetTrackingRepository repository, DtoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    //TODO: Transactional may not be strictly necessary here
    @Transactional(value = "petTrackingSqlTransactionManager")
    @Override
    public PetDto createPetInfo(PetDto dto) {
        Pet created = repository.save(mapper.dtoToEntity(dto, null));
        return mapper.entityToDto(created);
    }

    //TODO: Transactional may not be strictly necessary here
    @Transactional(value = "petTrackingSqlTransactionManager", readOnly = true)
    @Override
    public PetDto fetchPetInfo(long id) {
        Optional<Pet> found = repository.findById(id);
        if(!found.isPresent()) {
            throw new BaduserInputException("Pet id is not valid");
        }
        return mapper.entityToDto(found.get());
    }

    //TODO: Transactional may not be strictly necessary here
    @Transactional(value = "petTrackingSqlTransactionManager")
    @Override
    public void modifyPetInfo(PetDto dto) {
        Optional<Pet> found = repository.findById(dto.getPetId());
        if(!found.isPresent()) {
            throw new BaduserInputException("Pet id is not valid");
        }
        mapper.dtoToEntity(dto, found.get());
    }

    //TODO: Transactional may not be strictly necessary here
    @Transactional(value = "petTrackingSqlTransactionManager", readOnly = true)
    @Override
    public List<IOutOfZoneCount> petsOutOfPowerSavingZone() {
        return repository.countTotalOutOfPowerSavingZone();
    }
}
