package utilities;

import exceptions.InvalidRequestAttributeValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataValidationUtilityTest {

    /**
     * tests validation logic to ensure that a null task title is not allowed
     * and correctly throws an InvalidRequestAttributeValueException.
     */
    @Test
    void validateNullTitleNotAllowed() {
        assertThrows(InvalidRequestAttributeValueException.class, () ->
                DataValidationUtility.validateTaskTitle(null, false));
    }

    /**
     * tests validation logic to ensure that an empty task title is not allowed
     * and correctly throws an InvalidRequestAttributeValueException.
     */
    @Test
    void validateEmptyTitleNotAllowed() {
        assertThrows(InvalidRequestAttributeValueException.class, () ->
                DataValidationUtility.validateTaskTitle("", false));
    }

    /**
     * validates exception is thrown when task title is blank (whitespace) and not allowed.
     */
    @Test
    void validateBlankTitleNotAllowed() {
        assertThrows(InvalidRequestAttributeValueException.class, () ->
                DataValidationUtility.validateTaskTitle(" ", false));
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
     * validates blank task description is allowed and returns null.
     */
    @Test
    void validateBlankDescriptionAllowed() {
        assertNull(DataValidationUtility.validateTaskDescription(" ", true));
    }

    /**
     * validates a valid task description is accepted and returned trimmed.
     */
    @Test
    void validateValidDescription() {
        String validDescription = "Valid Description";
        String result = assertDoesNotThrow(() ->
                DataValidationUtility.validateTaskDescription(validDescription, true));
        assertEquals(validDescription, result.trim());
    }

    /**
     * validates exception is thrown when task description is null and not allowed.
     */
    @Test
    void validateNullDescriptionNotAllowed() {
        assertThrows(InvalidRequestAttributeValueException.class, () ->
                DataValidationUtility.validateTaskDescription(null, false));
    }
}
