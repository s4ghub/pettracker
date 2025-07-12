package com.example.pettracker.pettracker.controllers;

import com.example.pettracker.pettracker.dtos.PetDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * This endpoint inserts the record for a pet for the first time
     * @param dto as info from tracker
     * @return the dto
     */
    @PostMapping
    public ResponseEntity<PetDto> insertPetInfo(@Valid @RequestBody PetDto dto) {
        //TODO: implement the method
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    /**
     * This endpoint gets a single record
     * @param petId as id
     * @return dto
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPetInfo(@PathVariable("id") Integer petId) {
        //TODO: implement the method
        return new ResponseEntity<>(new PetDto(), HttpStatus.OK);
    }

    /**
     * The tracker for cat also identifies that it's lost (maybe due to suspicious movement)
     * @param dto as info from tracker
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity<?> updatePetInfo(@Valid @RequestBody PetDto dto) {
        //TODO: implement the method
        return ResponseEntity.noContent().build();
    }
}
