package utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CommonUtility {
    /**
     * private constructor to forbid instantiation
     */
    private CommonUtility() {
    }

    /**
     * method to get current date and time as String
     * 
     * @return current date and time as String
     */
    public static String getCurrentDateAndTimeStampString() {
        // create formatter with required pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy hh:mm:ss");

        // return formatted current date and time as String
        return LocalDateTime.now().format(formatter);
    }

    /**
     * method to generate a random UUID v4
     * 
     * @return randomly generated UUID v4
     */
    public static String generateUUIDv4() {
        // return a randomly generated a random uuid v4
        return UUID.randomUUID().toString();
    }
}
