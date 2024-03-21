package utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import io.github.cdimascio.dotenv.Dotenv;

public class CommonUtility {
    // private attributes
    private static boolean isDotEnvLoaded = false;
    private static Dotenv dotenv;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss");

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

    /**
     * method to fetch value for an environment variable either from system
     * environment variables, or from system properties, or from loaded dotenv file
     * 
     * @param key - key to find the value
     * @return value specified by the key, else null if no value found with the
     *         given key
     */
    public static String getEnvironmentVariableValue(String key) {
        // check if the dotenv file has not been loaded
        if (!CommonUtility.isDotEnvLoaded) {
            // load dotenv file
            CommonUtility.dotenv = Dotenv.configure().filename("tmr.env").ignoreIfMalformed().ignoreIfMissing().load();

            // update flag value
            CommonUtility.isDotEnvLoaded = true;
        }

        // create variable to store the environment variable value from system
        // environment variables
        String value = System.getenv(key);

        // check if the value does not exists in the system envirnment
        if (value == null) {
            // fetch value from system properties
            value = System.getProperty(key);
        }

        // check if the value does not exists in the system properties
        if (value == null) {
            // fetch value from dotenv file
            value = CommonUtility.dotenv.get(key);
        }

        // return environment variable value
        return value == null ? null : value.trim();
    }
}
