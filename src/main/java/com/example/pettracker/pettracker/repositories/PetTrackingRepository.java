package com.example.pettracker.pettracker.repositories;

import com.example.pettracker.pettracker.domainmodels.Pet;
import com.example.pettracker.pettracker.dtos.IOutOfZoneCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PetTrackingRepository extends JpaRepository<Pet, Long> {

    /*@Query(value = "SELECT p.petType AS petType, p.trackerType AS trackerType, COUNT(p.*) AS totalOutside "
  + "FROM Pet AS p GROUP BY p.petType, p.trackerType WHERE p.lostTracker <> 1 AND p.inZone = 0 ORDER BY p.petType", nativeQuery = true)*/
    @Query("SELECT p.petType AS petType, p.trackerType AS trackerType, COUNT(p.id) AS totalOutside "+"FROM Pet AS p WHERE ((p.lostTracker = false OR p.lostTracker IS NULL) AND p.inZone = false) GROUP BY p.petType, p.trackerType ORDER BY p.petType")
    List<IOutOfZoneCount> countTotalOutOfPowerSavingZone();
}
