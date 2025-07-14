package com.example.pettracker.pettracker;


import com.example.pettracker.pettracker.dtos.InputValidator;
import com.example.pettracker.pettracker.dtos.PetDto;
import com.example.pettracker.pettracker.exceptionhandling.BaduserInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PetTrackerInputValidationUnitTests {

    private InputValidator validator = new InputValidator();

    @Test
    void testWrongPetType() {
        PetDto dto = TestHelper.getInsertableDto(123, "Elephant", "BIG", false, false);
        Exception thrown = Assertions.assertThrows(BaduserInputException.class, () -> {validator.validate(dto);});
        Assertions.assertEquals("Pet type should be either CAT or DOG", thrown.getMessage());
    }

    @Test
    void testWrongTrackerType() {
        PetDto dto = TestHelper.getInsertableDto(123, "CAT", "VERYBIG", false, false);
        Exception thrown = Assertions.assertThrows(BaduserInputException.class, () -> {validator.validate(dto);});
        Assertions.assertEquals("Tracker type should be SMALL, MEDIUM or BIG", thrown.getMessage());
    }

    @Test
    void testWrongTrackerTypeForCat() {
        PetDto dto = TestHelper.getInsertableDto(123, "CAT", "medium", false, false);
        Exception thrown = Assertions.assertThrows(BaduserInputException.class, () -> {validator.validate(dto);});
        Assertions.assertEquals("Cat may not have a medium tracker", thrown.getMessage());
    }

    @Test
    void testWronglostTrackerValueForCat() {
        PetDto dto = TestHelper.getInsertableDto(123, "CAT", "big", false, null);
        Exception thrown = Assertions.assertThrows(BaduserInputException.class, () -> {validator.validate(dto);});
        Assertions.assertEquals("For Cat, lostTracker should be true or false", thrown.getMessage());
    }

    @Test
    void testWronglostTrackerValueTrueForDog() {
        PetDto dto = TestHelper.getInsertableDto(123, "dog", "big", false, true);
        Exception thrown = Assertions.assertThrows(BaduserInputException.class, () -> {validator.validate(dto);});
        Assertions.assertEquals("For Dog, lostTracker should be null", thrown.getMessage());
    }

    @Test
    void testWronglostTrackerValueFalseForDog() {
        PetDto dto = TestHelper.getInsertableDto(123, "dog", "big", false, false);
        Exception thrown = Assertions.assertThrows(BaduserInputException.class, () -> {validator.validate(dto);});
        Assertions.assertEquals("For Dog, lostTracker should be null", thrown.getMessage());
    }
}
