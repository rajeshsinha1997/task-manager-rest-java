package exceptions;

/**
 * exception to be thrown if an invalid value is given via request
 */
public class InvalidRequestAttributeValueException extends RuntimeException {

    /**
     * constructor
     * 
     * @param errorMessage - error message to be displayed
     */
    public InvalidRequestAttributeValueException(String errorMessage) {
        // call super by passing the error message
        super(errorMessage);
    }
}
