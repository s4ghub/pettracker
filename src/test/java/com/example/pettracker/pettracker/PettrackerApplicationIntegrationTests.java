package com.example.pettracker.pettracker;

import com.example.pettracker.pettracker.dtos.IOutOfZoneCount;
import com.example.pettracker.pettracker.dtos.PetDto;
import com.example.pettracker.pettracker.repositories.PetTrackingRepository;
import com.example.pettracker.pettracker.services.PetTrackingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class PettrackerApplicationIntegrationTests {

    @Autowired
    private PetTrackingService trackingService;
    @Autowired
    private PetTrackingRepository repository;

    @BeforeEach
    void beforeTest() {
        repository.deleteAll(); //Clean DB
        List<PetDto> dtos = TestHelper.getInitialDataToInsert();
		//Insert data into the DB
        for (PetDto dto : dtos) {
            trackingService.createPetInfo(dto);
        }
    }

    @AfterEach
    void afterTest() {
        repository.deleteAll(); //Clean DB
    }

    @Test //All 5 are outside
    void shouldReturnAllPetOutsideAnd_3_ByOwner456() {
        List<PetDto> dtos = trackingService.getAllPets();
        Assertions.assertEquals(5, dtos.size());
        List<IOutOfZoneCount> outOfZones = trackingService.petsOutOfPowerSavingZone();
        Assertions.assertEquals(5, outOfZones.size());
        for (IOutOfZoneCount count : outOfZones) {
            Assertions.assertEquals(1, count.getTotalOutside());
        }
        dtos = trackingService.getAllByOwner(456);
        Assertions.assertEquals(3, dtos.size());
    }

	@Test //All 6 are outside. Each group, total is 1, except DOG-BIG total is 2.
	void shouldReturnTwoAfterOne_DOG_BIG_added() {
		List<PetDto> dtos = trackingService.getAllPets();
		Assertions.assertEquals(5, dtos.size());
		List<IOutOfZoneCount> outOfZones = trackingService.petsOutOfPowerSavingZone();
		Assertions.assertEquals(5, outOfZones.size());
		for (IOutOfZoneCount count : outOfZones) {
			Assertions.assertEquals(1, count.getTotalOutside());
		}
		PetDto dto = TestHelper.getInsertableDto(789, "DOG", "BIG", false, null);
		trackingService.createPetInfo(dto); //Insert a new info record
		dtos = trackingService.getAllPets();
		Assertions.assertEquals(6, dtos.size());
		outOfZones = trackingService.petsOutOfPowerSavingZone();
		for (IOutOfZoneCount count : outOfZones) {
			if (count.getPetType().equals("DOG") && count.getTrackerType().equals("BIG")) {
				Assertions.assertEquals(2, count.getTotalOutside()); //2 DOG-BIG outside
				continue;
			}
			Assertions.assertEquals(1, count.getTotalOutside());
		}
	}

	@Test //Only 5 are outside. Each group, total is 1.
	void shouldReturnOneAfterOne_DOG_BIG_ReturnsPowerSavingZone() {
		//Insert a new info record with out of zone
		PetDto dto = trackingService.createPetInfo(TestHelper.getInsertableDto(789, "DOG", "BIG", false, null));
		//The dog comes inside
		trackingService.modifyPetInfo(TestHelper.getUpdateDto(dto.getPetId(), true, null));
		List<PetDto> dtos = trackingService.getAllPets();
		Assertions.assertEquals(6, dtos.size());
		List<IOutOfZoneCount> outOfZones = trackingService.petsOutOfPowerSavingZone();
		for (IOutOfZoneCount count : outOfZones) {
			Assertions.assertEquals(1, count.getTotalOutside());
		}
	}

    @Test //CAT-BIG comes back in power saving zone. 4 others are outside.
    void shouldReturnNoCatBigOutside() {
        List<PetDto> dtos = trackingService.getAllPets();
        Assertions.assertEquals(5, dtos.size());
        for (PetDto dto : dtos) {
            if (dto.getPetType().equals("CAT") && dto.getTrackerType().equals("BIG")) {
                trackingService.modifyPetInfo(TestHelper.getUpdateDto(dto.getPetId(), true, false));
            }
        }
        List<IOutOfZoneCount> outOfZones = trackingService.petsOutOfPowerSavingZone();
        Assertions.assertEquals(4, outOfZones.size());
        for (IOutOfZoneCount count : outOfZones) {
            Assertions.assertNotEquals("CAT-BIG", count.getPetType() + "-" + count.getTrackerType()); //0
            Assertions.assertEquals(1, count.getTotalOutside());
        }
    }

    @Test //CAT-BIG tracker lost (No data received since long).Total 4 are outside. From each group 1 is outside except CAT-BIG (Lost).
    void shouldReturnCatBigLost() {
        List<PetDto> dtos = trackingService.getAllPets();
        Assertions.assertEquals(5, dtos.size());
        long idOfLost = 0;
        for (PetDto dto : dtos) {
            if (dto.getPetType().equals("CAT") && dto.getTrackerType().equals("BIG")) {
                idOfLost = dto.getPetId();
                trackingService.modifyPetInfo(TestHelper.getUpdateDto(idOfLost, false, true)); //This is considered lost
            }
        }
		PetDto lost = trackingService.fetchPetInfo(idOfLost);
		Assertions.assertEquals(true, lost.getLostTracker()); //Verify

        List<IOutOfZoneCount> outOfZones = trackingService.petsOutOfPowerSavingZone();
        Assertions.assertEquals(4, outOfZones.size());
        for (IOutOfZoneCount count : outOfZones) {
            Assertions.assertNotEquals("CAT-BIG", count.getPetType() + "-" + count.getTrackerType()); //Lost is not considered in the calculation
            Assertions.assertEquals(1, count.getTotalOutside());
        }
        dtos = trackingService.getAllLostPets();
        Assertions.assertEquals(1, dtos.size());
    }

}
