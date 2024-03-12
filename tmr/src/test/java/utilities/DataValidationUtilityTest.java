package utilities;

import exceptions.InvalidRequestAttributeValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataValidationUtilityTest {

    /**
     * tests validation logic to ensure that a null task title is not allowed
     * and correctly throws an InvalidRequestAttributeValueException
     */
    @Test
    void validateNullTitleNotAllowed() {
        assertThrows(InvalidRequestAttributeValueException.class, () ->
                DataValidationUtility.validateTaskTitle(null, false));
    }

    /**
     * tests validation logic to ensure that an empty task title is not allowed
     * and correctly throws an InvalidRequestAttributeValueException
     */
    @Test
    void validateEmptyTitleNotAllowed() {
        assertThrows(InvalidRequestAttributeValueException.class, () ->
                DataValidationUtility.validateTaskTitle("", false));
    }

    /**
     * validates exception is thrown when task title is blank (whitespace) and not allowed
     */
    @Test
    void validateBlankTitleNotAllowed() {
        assertThrows(InvalidRequestAttributeValueException.class, () ->
                DataValidationUtility.validateTaskTitle(" ", false));
    }

    /**
     * validates that a null task title is permitted and returns null
     */
    @Test
    void validateNullTitleAllowed() {
        assertNull(DataValidationUtility.validateTaskTitle(null, true));
    }

    /**
     * validates that an empty string as a task title is permitted and returns null
     */
    @Test
    void validateEmptyTitleAllowed() {
        assertNull(DataValidationUtility.validateTaskTitle("", true));
    }

    /**
     * validates that a task title containing only whitespace is permitted and returns null
     */
    @Test
    void validateBlankTitleAllowed() {
        assertNull(DataValidationUtility.validateTaskTitle(" ", true));
    }

    /**
     * confirms that whitespace around a valid task title is trimmed
     */
    @Test
    void validateTrimTitle() {
        String titleWithSpaces = "  Valid Title  ";
        String expectedTitle = "Valid Title";
        String result = DataValidationUtility.validateTaskTitle(titleWithSpaces, false);
        assertEquals(expectedTitle, result, "Should trim the title and return the valid part only.");
    }

    /**
     * validates a valid task title is accepted without throwing an exception.
     */
    @Test
    void validateValidTitle() {
        String validTitle = "Valid Title";
        String result = assertDoesNotThrow(() ->
                DataValidationUtility.validateTaskTitle(validTitle, false));
        assertEquals(validTitle, result);
    }

    /**
     * validates null task description is allowed and returns null.
     */
    @Test
    void validateNullDescriptionAllowed() {
        assertNull(DataValidationUtility.validateTaskDescription(null, true));
    }

    /**
     * validates empty task description is allowed and returns null.
     */
    @Test
    void validateEmptyDescriptionAllowed() {
        assertNull(DataValidationUtility.validateTaskDescription("", true));
    }

    /**
     * validates blank task description is allowed and returns null
     */
    @Test
    void validateBlankDescriptionAllowed() {
        assertNull(DataValidationUtility.validateTaskDescription(" ", true));
    }

    /**
     * validates a valid task description is accepted and returned trimmed
     */
    @Test
    void validateValidDescription() {
        String validDescription = "Valid Description";
        String result = assertDoesNotThrow(() ->
                DataValidationUtility.validateTaskDescription(validDescription, true));
        assertEquals(validDescription, result.trim());
    }

    /**
     * validates exception is thrown when task description is null and not allowed
     */
    @Test
    void validateNullDescriptionNotAllowed() {
        assertThrows(InvalidRequestAttributeValueException.class, () ->
                DataValidationUtility.validateTaskDescription(null, false));
    }

    /**
     * tests validation of a valid task ID against the expected format (UUID)
     */
    @Test
    void isValidTaskIdValid() {
        // example of a valid UUID
        String validUUID = "123e4567-e89b-12d3-a456-426614174000";
        assertTrue(DataValidationUtility.isValidTaskId(validUUID), "Should return true for a valid UUID");
    }

    /**
     * tests validation of an invalid task ID against the expected format (UUID)
     */
    @Test
    void isValidTaskIdInvalid() {
        String invalidUUID = "invalid-uuid";
        assertFalse(DataValidationUtility.isValidTaskId(invalidUUID), "Should return false for an invalid UUID");
    }
}