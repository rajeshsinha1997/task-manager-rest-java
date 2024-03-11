# Task Manager REST API
This is a very basic REST API developed using _Java_ to provide an API-based **Task-Management-System**.

## Development Information
- Developer:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;[RAJESH SINHA](https://github.com/rajeshsinha1997)
- Development Language:&emsp;Java
- Web-Framework Used:&emsp;&nbsp;&nbsp;Servlet
- Database Used:&emsp;&emsp;&emsp;&emsp;&emsp;Local In-Memory
- Published:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;[Docker Image](https://hub.docker.com/r/rajeshsinha1997/task-manager-rest-api)
- Contributors:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;[ALEXEY SHARY](https://github.com/AlexeyShary)
- Postman Collection:&emsp;&emsp;&emsp;[Click Here](https://github.com/rajeshsinha1997/task-manager-rest-java/blob/main/tmr/postman_collection/task-manager-rest-api.postman_collection.json)

## Steps to Setup Project In Local System Without Docker
1. Clone or Download the project from this [GitHub Repository](https://github.com/rajeshsinha1997/task-manager-rest-java/tree/main)
2. Open the cloned or downloaded project directory in any Java IDE (Visual Studio Code, JetBrains IntelliJ Idea, Apache NetBeans, Eclipse IDE) as per your choice.
3. Download all project dependencies using **Maven**. To download the project dependencies, run the below command inside the project sub-directory where the file **pom.xml** is present.
```
mvn clean install
```
4. Once all dependencies have been downloaded and the project build is successfully, a new 'target' folder will be created, which will contain the deployable ___war___ file (**tmr.war**).
5. You can deploy the generated war file on your choice of **web-server** (Take a note of the **PORT** value on which the server is listening to the requests for the deployed ___war___ file).
6. Now you can send the HTTP requests which are already added in the postman collection present in the repository. Make sure to update the PORT value in the postman request URLs (___if you're using any PORT value other than 8080___) before sending any request.

## Steps to Setup Project In Local System With Docker
1. Pull the docker image for the current project from Docker Hub using the below command (make sure to replace the '**<version_number>**' with the corresponding available version of your choice),
```
docker pull rajeshsinha1997/task-manager-rest-api:<version_number>
```

2. Run the docker image using the below command (make sure to replace the '**<PORT_NUMBER>**' with the port value you want to use to receive the requests for this application, and replace the '**<version_number>**' with the corresponding available version of your choice),
```
docker run -it --rm -d -p <PORT_NUMBER>:8080 rajeshsinha1997/task-manager-rest-api:<version_number>
```

3. Now you can send the HTTP requests which are already added in the postman collection present in the repository. Make sure to update the PORT value in the postman request URLs (___if you're using any PORT value other than 8080___) before sending any request.

## Endpoints
Below are the endpoints which are available to use,
1. POST /tasks&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;- create a new task
2. GET /tasks&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;- get a list of all active tasks
3. GET /tasks/{task_id}&emsp;&emsp;&nbsp;&nbsp;- find a particular task by it's corresponding id
4. DELETE /tasks/{task_id}&nbsp;&nbsp;&nbsp;- delete a particular task by it's corresponding id

The [Postman Collection](https://github.com/rajeshsinha1997/task-manager-rest-java/blob/main/tmr/postman_collection/task-manager-rest-api.postman_collection.json) present in the repository contains more detailed information about these endpoints.
