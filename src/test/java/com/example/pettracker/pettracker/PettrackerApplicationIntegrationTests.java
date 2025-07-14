package com.example.pettracker.pettracker;

import com.example.pettracker.pettracker.dtos.IOutOfZoneCount;
import com.example.pettracker.pettracker.dtos.PetDto;
import com.example.pettracker.pettracker.exceptionhandling.BaduserInputException;
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

    @Test
        //All 5 are outside
    void shouldReturnAllPetOutside() {
        List<PetDto> dtos = trackingService.getAllPets();
        Assertions.assertEquals(5, dtos.size());
        List<IOutOfZoneCount> outOfZones = trackingService.petsOutOfPowerSavingZone();
        Assertions.assertEquals(5, outOfZones.size());
        String allOutSideAsString = TestHelper.iOutOfZoneCountListToString(outOfZones);
        Assertions.assertTrue(allOutSideAsString.contains("DOG-BIG-1"));
        Assertions.assertTrue(allOutSideAsString.contains("CAT-BIG-1"));
        Assertions.assertTrue(allOutSideAsString.contains("CAT-SMALL-1"));
        Assertions.assertTrue(allOutSideAsString.contains("DOG-MEDIUM-1"));
        Assertions.assertTrue(allOutSideAsString.contains("DOG-SMALL-1"));
    }

    @Test
    void shouldReturnPetsOfOwner123() {
        List<PetDto> dtos = trackingService.getAllByOwner(123);
        Assertions.assertEquals(2, dtos.size());
        StringBuilder dtoStrBuilder = new StringBuilder();
        for (PetDto dto : dtos) {
            dtoStrBuilder.append(TestHelper.petDtoToString(dto)).append("-");
        }
        String dtosListToString = dtoStrBuilder.toString();
        Assertions.assertTrue(dtosListToString.contains("123-CAT-BIG-false-false"));
        Assertions.assertTrue(dtosListToString.contains("123-CAT-SMALL-false-false"));
    }

    @Test
        //All 6 are outside zone. Each group, total is 1, except DOG-BIG total is 2.
    void shouldReturnTwoAfterOne_DOG_BIG_added() {
        PetDto dto = TestHelper.getInsertableDto(789, "DOG", "BIG", false, null);
        trackingService.createPetInfo(dto); //Insert a new info record
        List<PetDto> dtos = trackingService.getAllPets();
        Assertions.assertEquals(6, dtos.size());
        List<IOutOfZoneCount> outOfZones = trackingService.petsOutOfPowerSavingZone();
        String allOutSideAsString = TestHelper.iOutOfZoneCountListToString(outOfZones);
        Assertions.assertTrue(allOutSideAsString.contains("DOG-BIG-2"));
        Assertions.assertTrue(allOutSideAsString.contains("CAT-BIG-1"));
        Assertions.assertTrue(allOutSideAsString.contains("CAT-SMALL-1"));
        Assertions.assertTrue(allOutSideAsString.contains("DOG-MEDIUM-1"));
        Assertions.assertTrue(allOutSideAsString.contains("DOG-SMALL-1"));
    }

    @Test
        //CAT-BIG comes back in power saving zone. 4 others are outside.
    void shouldReturnNoCatBigOutside() {
        List<PetDto> dtos = trackingService.getAllPets();
        Assertions.assertEquals(5, dtos.size());
        for (PetDto dto : dtos) {
            //Returns inZone
            if (dto.getPetType().equals("CAT") && dto.getTrackerType().equals("BIG")) {
                trackingService.modifyPetInfo(TestHelper.getUpdateDto(dto.getPetId(), true, false));
            }
        }
        List<IOutOfZoneCount> outOfZones = trackingService.petsOutOfPowerSavingZone();
        Assertions.assertEquals(4, outOfZones.size());
        String allOutSideAsString = TestHelper.iOutOfZoneCountListToString(outOfZones);
        //All true
        Assertions.assertTrue(allOutSideAsString.contains("DOG-BIG-1"));
        Assertions.assertTrue(allOutSideAsString.contains("CAT-SMALL-1"));
        Assertions.assertTrue(allOutSideAsString.contains("DOG-MEDIUM-1"));
        Assertions.assertTrue(allOutSideAsString.contains("DOG-SMALL-1"));
        //False
        Assertions.assertFalse(allOutSideAsString.contains("CAT-BIG-1"));
    }

    @Test
        //CAT-BIG tracker lost (No data received since long).Total 4 are outside. From each group 1 is outside except CAT-BIG (Lost).
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
        Assertions.assertTrue("CAT".equals(lost.getPetType()));
        Assertions.assertTrue("BIG".equals(lost.getTrackerType()));

        List<IOutOfZoneCount> outOfZones = trackingService.petsOutOfPowerSavingZone();
        Assertions.assertEquals(4, outOfZones.size());
        String allOutSideAsString = TestHelper.iOutOfZoneCountListToString(outOfZones);
        //All true
        Assertions.assertTrue(allOutSideAsString.contains("DOG-BIG-1"));
        Assertions.assertTrue(allOutSideAsString.contains("CAT-SMALL-1"));
        Assertions.assertTrue(allOutSideAsString.contains("DOG-MEDIUM-1"));
        Assertions.assertTrue(allOutSideAsString.contains("DOG-SMALL-1"));
        //False
        Assertions.assertFalse(allOutSideAsString.contains("CAT-BIG-1"));
    }

    @Test
    void shouldThrowExceptionCatLostTrackerValueNull() {
        List<PetDto> dtos = trackingService.getAllPets();
        Assertions.assertEquals(5, dtos.size());
        for (PetDto dto : dtos) {
            if (dto.getPetType().equals("CAT") && dto.getTrackerType().equals("BIG")) {
                Exception thrown = Assertions.assertThrows(BaduserInputException.class, () -> {
                    trackingService.modifyPetInfo(TestHelper.getUpdateDto(dto.getPetId(), true, null));
                });
                Assertions.assertEquals("value of lostTracker may not be null for Cat", thrown.getMessage());
            }
        }
    }

    @Test
    void shouldThrowExceptionDogLostTrackerValueTrue() {
        List<PetDto> dtos = trackingService.getAllPets();
        Assertions.assertEquals(5, dtos.size());
        for (PetDto dto : dtos) {
            if (dto.getPetType().equals("DOG") && dto.getTrackerType().equals("BIG")) {
                Exception thrown = Assertions.assertThrows(BaduserInputException.class, () -> {
                    trackingService.modifyPetInfo(TestHelper.getUpdateDto(dto.getPetId(), true, true));
                });
                Assertions.assertEquals("value of lostTracker must be null for dog", thrown.getMessage());
            }
        }
    }

    @Test
    void shouldThrowExceptionWrongPetId() {
        Exception thrown = Assertions.assertThrows(BaduserInputException.class, () -> {
            trackingService.fetchPetInfo(100);
        });
        Assertions.assertEquals("Pet id is not valid", thrown.getMessage());
    }

}
