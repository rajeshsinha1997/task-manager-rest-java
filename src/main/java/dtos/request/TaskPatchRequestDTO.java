package dtos.request;

import com.google.gson.annotations.SerializedName;

public class TaskPatchRequestDTO {
    // attributes of the dto object
    @SerializedName("task-title")
    private String taskTitle;

    @SerializedName("task-description")
    private String taskDescription;

    @SerializedName("task-completed")
    private Boolean isTaskCompleted;

    /**
     * constructor
     * 
     * @param taskTitle       - title of the task
     * @param taskDescription - description of the task
     * @param isTaskCompleted - boolean flag indicating if a task has been completed
     *                        or not
     */
    public TaskPatchRequestDTO(String taskTitle, String taskDescription, Boolean isTaskCompleted) {
        // initialize the attributes of the dto object
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.isTaskCompleted = isTaskCompleted;
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

    public Boolean getIsTaskCompleted() {
        return isTaskCompleted;
    }

    public void setIsTaskCompleted(Boolean isTaskCompleted) {
        this.isTaskCompleted = isTaskCompleted;
    }

}
