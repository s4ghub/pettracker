package com.example.pettracker.pettracker.controllers;

import com.example.pettracker.pettracker.dtos.IOutOfZoneCount;
import com.example.pettracker.pettracker.dtos.InputValidator;
import com.example.pettracker.pettracker.dtos.PetDto;
import com.example.pettracker.pettracker.dtos.UpdateDto;
import com.example.pettracker.pettracker.services.PetTrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * As per requirement the application should receive data from these trackers.
 * So Assumptions
 * Point 1: there is no question of batch updates
 * Point 2: No data race possible as each tracker will insert and update its own record periodically.
 *          And 2 requests from the same tracker should have a time gap so that they don't race. So
 *          no Jpa @Version column is needed.
 * Point 3: For each tracker each update request should reach this application in the order of time.
 *          By saying this we mean that the request sent from tracker x at 8:00 am should not
 *          reach after the request sent this tracker at 8:05 am. Else stale data replaces
 *          more fresh data. The tracker should make this sure.
 * Point 4: From the requirement it's assumed that no delete api should be there.
 */
@RestController
@RequestMapping("/api/petinfos")
public class PetTrackingController {
    private final PetTrackingService trackingService;
    private final InputValidator inputValidator;

    @Autowired
    public PetTrackingController(PetTrackingService trackingService, InputValidator inputValidator) {
        this.trackingService = trackingService;
        this.inputValidator = inputValidator;
    }

    /**
     * This endpoint inserts the record for a pet for the first time
     */
    @PostMapping
    public ResponseEntity<PetDto> insertPetInfo(@Valid @RequestBody PetDto dto) {
        inputValidator.validate(dto);
        return new ResponseEntity<>(trackingService.createPetInfo(dto), HttpStatus.CREATED);
    }

    /**
     * This endpoint gets a single record
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPetInfo(@PathVariable("id") Integer petId) {
        return new ResponseEntity<>(trackingService.fetchPetInfo(petId), HttpStatus.OK);
    }

    /**
     * Updates a single record
     * The tracker for cat also identifies whether it's lost (maybe due to suspicious movement)
     */
    @PutMapping
    public ResponseEntity<?> updatePetInfo(@Valid @RequestBody UpdateDto dto) {
        //inputValidator.validate(dto);
        trackingService.modifyPetInfo(dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * get all info: Did not use pagination for simplicity
     */
    @GetMapping
    public ResponseEntity<List<PetDto>> allComponents() {
        return new ResponseEntity<>(trackingService.getAllPets(), HttpStatus.OK);
    }

    /**
     * Returns how many are out of power saving zone
     */
    @GetMapping("/outofpowersavingzone")
    public ResponseEntity<List<IOutOfZoneCount>> outOfZone() {
        return new ResponseEntity<>(trackingService.petsOutOfPowerSavingZone(), HttpStatus.OK);
    }
}
