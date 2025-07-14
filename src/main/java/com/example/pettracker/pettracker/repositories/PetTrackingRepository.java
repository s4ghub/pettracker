package com.example.pettracker.pettracker.repositories;

import com.example.pettracker.pettracker.domainmodels.Pet;
import com.example.pettracker.pettracker.dtos.IOutOfZoneCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PetTrackingRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p.petType AS petType, p.trackerType AS trackerType, COUNT(p.id) AS totalOutside "+"FROM Pet AS p WHERE ((p.lostTracker = false OR p.lostTracker IS NULL) AND p.inZone = false) GROUP BY p.petType, p.trackerType ORDER BY p.petType")
    List<IOutOfZoneCount> countTotalOutOfPowerSavingZone();

    @Query("select p from Pet p where (p.lostTracker = true)")
    List<Pet> getLostOnes();

    List<Pet> findByOwnerId(int id);
}
