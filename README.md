# Task Manager REST API
This is a very basic REST API developed using _Java_ to provide an API-based **Task-Management-System**.

## Development Information
- Developer:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;[RAJESH SINHA](https://github.com/rajeshsinha1997)
- Development Language:&emsp;Java
- Web-Framework Used:&emsp;&nbsp;&nbsp;Servlet
- Database Used:&emsp;&emsp;&emsp;&emsp;&emsp;Local In-Memory
- Published:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;As Docker Image
- Contributors:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;[ALEXEY SHARY](https://github.com/AlexeyShary)

## Steps to Setup Project In Local System Without Docker
1. Clone or Download the project from this [GitHub Repository](https://github.com/rajeshsinha1997/task-manager-rest-java/tree/main)
2. Open the cloned or downloaded project directory in any Java IDE (Visual Studio Code, JetBrains IntelliJ Idea, Apache NetBeans, Eclipse IDE) as per your choice.
3. Download all project dependencies using **Maven**. To download the project dependencies, run the below command inside the project sub-directory where the file **pom.xml** is present.
```
mvn clean install
```
4. Once all dependencies have been downloaded and the project build is successfully, a new 'target' folder will be created, which will contain the deployable ___war___ file (**tmr.war**).
5. You can deploy the generated war file on your choice of **web-server** (Take a note of the **PORT** value on which the server is listening to the requests for the deployed ___war___ file).
6. Now you can send the HTTP requests which are already added in the postman collection present in the repository. Make sure to update the PORT value in the postman request URLs (___if you're using any PORT value other than 8000___) before sending any request.

## Steps to Setup Project In Local System With Docker