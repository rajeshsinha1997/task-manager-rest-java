package repositories;

/**
 * factory class to provide repository instance at runtime on-demand
 */
public class RepositoryFactory {

    // static instance of repository visible to this class only
    private static ITaskRepository taskRepository;

    /**
     * method to get the static instance of the repository
     * 
     * @return static instance of the repository
     */
    public static ITaskRepository getTaskRepositoryInstance() {
        // check if the static instance of the repository has not been instantiated
        if (RepositoryFactory.taskRepository == null) {
            // instantiate the static instance of the repository
            RepositoryFactory.taskRepository = new TaskRepositoryLocalMemoryImpl();
        }

        // return the static instance of the repository
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
