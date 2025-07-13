package com.example.pettracker.pettracker.dtos;

import com.example.pettracker.pettracker.domainmodels.Pet;

public class DtoCustomValidator {

    public void validate(PetDto dto) {
        Pet.PetType petType = null;
        //Is the petType among {CAT, DOG}?
        try {
            petType = Enum.valueOf(Pet.PetType.class, dto.getPetType().toUpperCase());
        } catch(IllegalArgumentException e) {
            //TODO: throw bad request
        }

        Pet.TrackerType trackerType = null;
        //Is the trackerType among {SMALL, MEDIUM, BIG}?
        try {
            trackerType = Enum.valueOf(Pet.TrackerType.class, dto.getTrackerType().toUpperCase());
        } catch(IllegalArgumentException e) {
            //TODO: throw bad request
        }

        //Cat may not have a medium tracker
        if(petType == Pet.PetType.CAT && trackerType == Pet.TrackerType.MEDIUM) {
            //TODO: Throw bad request
        }

        //For Cat the lostTracker should be non-null
        if(dto.getLostTracker() == null && petType == Pet.PetType.CAT) {
            //TODO: throw bad request
        }

        //For Dog the lostTracker should be null
        if(dto.getLostTracker() != null && petType == Pet.PetType.DOG) {
            //TODO: throw bad request
        }
    }
}
