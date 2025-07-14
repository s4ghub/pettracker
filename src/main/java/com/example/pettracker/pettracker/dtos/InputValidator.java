package com.example.pettracker.pettracker.dtos;

import com.example.pettracker.pettracker.domainmodels.Pet;
import com.example.pettracker.pettracker.exceptionhandling.BaduserInputException;
import org.springframework.stereotype.Component;

@Component
public class InputValidator {

    public void validate(PetDto dto) {
        Pet.PetType petType = null;
        //Is the petType among {CAT, DOG}?
        try {
            petType = Enum.valueOf(Pet.PetType.class, dto.getPetType().toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BaduserInputException("Pet type should be either CAT or DOG");
        }

        Pet.TrackerType trackerType = null;
        //Is the trackerType among {SMALL, MEDIUM, BIG}?
        try {
            trackerType = Enum.valueOf(Pet.TrackerType.class, dto.getTrackerType().toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BaduserInputException("Tracker type should be SMALL, MEDIUM or BIG");
        }

        //Cat may not have a medium tracker
        if(petType == Pet.PetType.CAT && trackerType == Pet.TrackerType.MEDIUM) {
            throw new BaduserInputException("Cat may not have a medium tracker");
        }

        //For Cat the lostTracker should be non-null
        if(dto.getLostTracker() == null && petType == Pet.PetType.CAT) {
            throw new BaduserInputException("For Cat, lostTracker should be true or false");
        }

        //For Dog the lostTracker should be null
        if(dto.getLostTracker() != null && petType == Pet.PetType.DOG) {
            throw new BaduserInputException("For Dog, lostTracker should be null");
        }
    }
}
