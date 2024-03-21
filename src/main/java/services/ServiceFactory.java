package services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * factory class to provide instance of service class at runtime on-demand
 */
public class ServiceFactory {
    // create logger instance
    private static final Logger logger = LogManager.getLogger(ServiceFactory.class);

    /**
     * private constructor to forbid instantiation
     */
    private ServiceFactory() {
    }

    // static instance of service class visible to only this class
    private static TaskService taskService;

    /**
     * method to update the static instance of the service class with an instance
     * value provided
     * 
     * @param taskService - provided instance value of the service class
     */
    public static void setTaskServiceInstance(TaskService taskService) {
        // update the static instance of the service class with the provided instance
        // value
        ServiceFactory.taskService = taskService;
    }

    /**
     * method to get the static instance of the service class
     * 
     * @return static instance of the service class
     */
    public static TaskService getTaskServiceInstance() {
        // check if the static instance of the service class has not been instantiated
        if (ServiceFactory.taskService == null) {
            ServiceFactory.logger.debug("no service instance available, creating a new service instance");

            // instantiate the static instance of the service class
            ServiceFactory.taskService = new TaskService();
            ServiceFactory.logger.info("created service instance");
        }

        // return the static instance of the service class
        return ServiceFactory.taskService;
    }

}
