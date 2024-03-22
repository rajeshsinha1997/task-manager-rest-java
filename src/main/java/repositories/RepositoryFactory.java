package repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * factory class to provide repository instance at runtime on-demand
 */
public class RepositoryFactory {
    // create logger instance
    private static final Logger logger = LogManager.getLogger(RepositoryFactory.class);

    // static instance of repository visible to this class only
    private static ITaskRepository taskRepository;

    /**
     * method to get the static instance of the repository
     * 
     * @return static instance of the repository
     */
    public static ITaskRepository getTaskRepositoryInstance() {
        RepositoryFactory.logger.info("checking if any repository instance has been created");

        // check if the static instance of the repository has not been instantiated
        if (RepositoryFactory.taskRepository == null) {
            RepositoryFactory.logger.debug("no repository instance available, creating a new repository instance");

            // instantiate the static instance of the repository
            RepositoryFactory.taskRepository = new TaskRepositoryLocalMemoryImpl();
            RepositoryFactory.logger.info("created repository instance");
        }

        // return the static instance of the repository
        RepositoryFactory.logger.info("sending back repository instance");
        return taskRepository;
    }

    /**
     * method to update the static instance of the repository with an instance value
     * provided
     * 
     * @param taskRepository - provided instance value of repository
     */
    public static void setTaskRepositoryInstance(ITaskRepository taskRepository) {
        // update the static instance of the repository with the provided instance value
        RepositoryFactory.taskRepository = taskRepository;
    }
}
