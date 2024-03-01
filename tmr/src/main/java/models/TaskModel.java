package models;

/**
 * model class for a task
 */
public class TaskModel {
    // attributes of a task
    private String taskId;
    private String taskTitle;
    private String taskDescription;
    private String taskCreatedOn;
    private String taskLastUpdatedOn;
    private boolean taskCompleted;
    private boolean taskDeleted;

    /**
     * constructor to initialize a task after creating
     * 
     * @param taskId            - id of the task
     * @param taskTitle         - title of the task
     * @param taskDescription   - description of the task
     * @param taskCreatedOn     - date on which the task was created
     * @param taskLastUpdatedOn - date on which the task was last modified
     * @param taskCompleted     - boolean flag indicating if the task has been
     *                          completed or not
     * @param taskDeleted       - boolean flag indicating if the task has been
     *                          deleted or not
     */
    public TaskModel(String taskId, String taskTitle, String taskDescription, String taskCreatedOn,
            String taskLastUpdatedOn,
            boolean taskCompleted, boolean taskDeleted) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskCreatedOn = taskCreatedOn;
        this.taskLastUpdatedOn = taskLastUpdatedOn;
        this.taskCompleted = taskCompleted;
        this.taskDeleted = taskDeleted;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskCreatedOn() {
        return taskCreatedOn;
    }

    public void setTaskCreatedOn(String taskCreatedOn) {
        this.taskCreatedOn = taskCreatedOn;
    }

    public String getTaskLastUpdatedOn() {
        return taskLastUpdatedOn;
    }

    public void setTaskLastUpdatedOn(String taskLastUpdatedOn) {
        this.taskLastUpdatedOn = taskLastUpdatedOn;
    }

    public boolean isTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(boolean taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    public boolean isTaskDeleted() {
        return taskDeleted;
    }

    public void setTaskDeleted(boolean taskDeleted) {
        this.taskDeleted = taskDeleted;
    }
}
