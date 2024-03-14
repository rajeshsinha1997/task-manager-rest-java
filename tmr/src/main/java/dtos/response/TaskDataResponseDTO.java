package dtos.response;

import com.google.gson.annotations.SerializedName;

public class TaskDataResponseDTO {
    // attributes of the dto object
    @SerializedName("task-id")
    private String taskId;

    @SerializedName("task-title")
    private String taskTitle;

    @SerializedName("task-description")
    private String taskDescription;

    @SerializedName("task-completed")
    private boolean isTaskCompleted;

    @SerializedName("task-created-on")
    private String taskCreatedOn;

    /**
     * constructor
     * 
     * @param taskId          - id of the task object as String
     * @param taskTitle       - title of the task object as String
     * @param taskDescription - description of the task object as String
     * @param isTaskCompleted - boolean flag indicating if the task has completed or
     *                        not
     * @param taskCreatedOn   - date and timestamp as String on which the task was
     *                        created
     */
    public TaskDataResponseDTO(String taskId, String taskTitle, String taskDescription, boolean isTaskCompleted,
            String taskCreatedOn) {
        // initialize the attributes of the dto object
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.isTaskCompleted = isTaskCompleted;
        this.taskCreatedOn = taskCreatedOn;
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

    public boolean isTaskCompleted() {
        return isTaskCompleted;
    }

    public void setTaskCompleted(boolean isTaskCompleted) {
        this.isTaskCompleted = isTaskCompleted;
    }

    public String getTaskCreatedOn() {
        return taskCreatedOn;
    }

    public void setTaskCreatedOn(String taskCreatedOn) {
        this.taskCreatedOn = taskCreatedOn;
    }

}
