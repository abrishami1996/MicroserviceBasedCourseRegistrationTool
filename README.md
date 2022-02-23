## Description
This project is a basic implementation of a course registration software used in an education portal of a university. This project is based on micro-services and leverages Docker as the containerization tool and Kubernetes(minikube) for container orchestration.

Micro-services architecture is a new model for developing and deploying software that can result in an increase in scalability and modularity of the deployed product. In micro-services architecture the system is broken into smaller and loosely coupled modules called services. Each service is responsible for a specific task independent of other services. If a service is dependent on another service for performing a task, it needs to communicate with the other service using its exposed APIs. Most common form of having communication between services is using RESTful APIs which is what we have used in this project. Furthermore, software created based on the micro-services architecture are often deployed in cloud environments so that in case of a sudden increase in the load on a particular service, the service under load can be scaled up accordingly.

 This project is composed of 4 services each performing one specific task:

* Authentication : registering and authenticating users of the system
* Course Management : managing the courses defined in the system
* Course Selection : keeping track of courses taken by different students
* User Panel : The GUI for interacting with the system

Each of these services exposes different APIs as explained later in the document. Since each service works on a different task and should not require direct access to the data of other services, the database of the system is also broken in to 3 part where each service uses its own database.

Each database is also considered a service by itself. In this project we also leverage docker containers for our databases and are deployed on minikube. To prevent data corruption and data inconstancy, in this project we do not allow database pods to be replicated.

For implementing ours services we have used different programming languages and frameworks. For Authentication, Course Management and Course Selection we use the the Spring Boot framework. The user panel however, is developed in python and Django framework.

## APIs exposed by each module

### Auth Module APIs
| URL | Method | Authorized to| Description|
|-------|--|----|---------|
/login|GET||This API is used for authenticating users. The authentication is based on the JWT server-side authentication methods. A user is authenticated based on the username and password and then, if the authentication is succesful, a JWT token containing the role of user in the payload section is returned.
/StudentSignUp|POST|ADMIN|The HTTPRequest body is transformed to a StudentRequest doamin object as the input using @RequestBody annotation. If the student number does not exist before, the sign up process (including assigning the student role to this user) is proceeded and finishes succesfully as the new student object is saved in the database.
/ProfessorSignUp|POST|ADMIN|HTTPRequest body is transformed to a ProfessorRequest doamin object as the input using @RequestBody annotation. If the professor number does not exist before, the sign up process (including assigning the professor role to this user) is proceeded and finishes succesfully as the new professor object is saved in the database.
/EmployeeSignUp|POST|ADMIN|HTTPRequest body is transformed to a EmployeeRequest doamin object as the input using @RequestBody annotation. If the employee number does not exist before, the sign up process (including assigning the employee role to this user) is proceeded and finishes succesfully as the new employee object is saved in the database.
/DeleteUser|POST|ADMIN|The user's id in the table corresponding to his\her role is extracted using the foreign key stored in the user table. Then, the user is deleted from the table of his\her role.
/addRole|GET|ADMIN|If the user does not have this role before, it is assigned as his\her new role.
/getRole|GET||The user's role is returned based on the role embedded in the payload section of JWT token.
/getUserDetails|GET||The user is found in the User table and foreign key which corresponds to the id of user in his\her role table is extracted. Based on this id, the object including user information is returned. 
/getProfessors|GET|ADMIN|Returns all members of the Professor table. 
/getEmployees|GET|ADMIN|Returns all members of the Employee table. 
/getStudents|GET|ADMIN|Returns all members of the Student table. 
/getUserId|GET||The user's username is extracted and used to retrieve user's id in the User table. 
/getUserFK|GET||The user's username is extracted and the foreign key which corresponds to the user's id in his\her role table is returned from the User table. 
/getStudentNumber|GET||The student's number is returned from the Student table through the foreign key stored in the User tabel (the corresponding student is found based on this key in the Student table).



### CourseManager APIs
| URL | Method | Authorized to| Description|
|-------|--|----|---------|
/add|POST|ADMIN,EMPLOYEE|The role of user and existence of the course is controlled. In order to get the role, the JWT token is sent to the authentication service and the role is received. If the role is "ADMIN" or "EMPLOYEE" and the course does not exist, it is added.
/edit|POST|ADMIN,EMPLOYEE|The role of user and existence of the course is controlled. In order to get the role, the JWT token is sent to the authentication service and the role is received. If the role is "ADMIN" or "EMPLOYEE" and the course does exist, the new course information is set.
/remove|GET|ADMIN,EMPLOYEE|The role of user and existence of the course is controlled. In order to get the role, the JWT token is sent to the authentication service and the role is received. If the role is "ADMIN" or "EMPLOYEE" and the course does exist (the existence of course is controlled by looking for the passed course code), it is deleted. 
/exists|GET||Existence of a course is controlled by looking for the passed course code.
/getListOfCourses|GET||The list of all existing courses is returned. There is no limitation for the role of user invoking this API. 

### CourseSelection APIs
| URL | Method | Authorized to| Description|
|----|--|--|----------------------|
/takecourse|GET|STUDENT|The role of user and exitence of the requested course are controlled. In order to get the role, the JWT token is sent to the authentication service and the role is received. In order to check the existence of course, the Course Manager service is queried. If the role is "STUDENT" and the course doest exist, it is taken. The student number is also acquired through querying the Authentication service. 
/takecourseforstudent|GET|ADMIN,EMPLOYEE|The role of user and exitence of the requested course are controlled. In order to get the role, the JWT token is sent to the authentication service and the role is received. In order to check the existence of course, the Course Manager service is queried. If the role is "ADMIN" or "EMPLOYEE" and the course doest exist, it is taken for the student whose student number is passed to this API. 
/removetakencourse|GET|STUDENT|The role of user is controlled. In order to get the role, the JWT token is sent to the authentication service and the role is received. If the role is "STUDENT", the course is deleted for the student. The student number is aqcuired through querying the Authentication service.
/removetakencourseforstudent|GET|ADMIN,EMPLOYEE|The role of user and exitence of the requested course are controlled. In order to get the role, the JWT token is sent to the authentication service and the role is received. In order to check the existence of course, the Course Manager service is queried. If the role is "ADMIN" or "EMPLOYEE" and the course doest exist, the course is removed for the student whose student number is passed to the API. 
/getstudentcourseslist|GET|ADMIN,EMPLOYEE,STUDENT ?? |The role of user is controlled. In order to get the role, the JWT token is sent to the authentication service and the role is received. If the role is "ADMIN", "EMPLOYEE", or "STUDENT", the list of courses for the student whose student number is passed to the API is returned.
/getcoursememberlist|GET|ADMIN,EMPLOYEE,PROFESSOR|The role of user is controlled. In order to get the role, the JWT token is sent to the authentication service and the role is received. If the role is "ADMIN", "EMPLOYEE", or "Professor", the list of students who have taken this course (the one whose code is passed to this API) is returned.

### UserPanel
| URL | Authorized to| Description|
|-------|----|---------|
/|ALL logged in users|The home page of each user|
/login|ALL|User login page, redirects to home page|
/logout|ALL|User logout page, redirects to login page|
/register/student|ADMIN|define a student in the system|
/register/professor|ADMIN|define a professor in the system|
/register/employee|ADMIN|define an employee in the system|
/roles/add|ADMIN|add role to a user|
/student/all|ADMIN, EMPLOYEE|get all students|
/professor/all|ADMIN, EMPLOYEE|get all professors|
/employee/all|ADMIN|get all employees|
/deleteUser|ADMIN|delete a user|
/courses/available/all|ALL|get all available courses|
/courses/available/add|ADMIN,EMPLOYEE|add a new course to the system|
/courses/available/<int: courseId>/edit|ADMIN,EMPLOYEE|get all courses defined in system|
/courses/available/remove|ADMIN,EMPLOYEE|delete a course from system|
/courses/taking/\<int:studentId>/all|ADMIN, EMPLOYEE, STUDENT|??|
/courses/taking/\<int:courseId>/add|STUDENT|take a course as a student|
/courses/taking/\<int:courseId>/remove|STUDENT|drop a course as a student|
/courses/taking/\<int:courseId>/classList|ADMIN,EMPLOYEE,PROFESSOR|get list of students registered for a course|
/courses/forStudent/<int:student_id>/addCourse/<int:course_id>|ADMIN,EMPLOYEE| take a course for a student
/courses/forStudent/<int:student_id>/dropCourse/<int:course_id>|ADMIN,EMPLOYEE| drop a course for a student
/courses/taught/|PROFESSOR| get the list of courses taught by the professor logged in
/courses/taughtBy/<int: professor_id>|ADMIN, EMPLOYEE|get the list of courses taught by the professor specified in the URL
/courses/takeCourseForStudentForm|ADMIN, EMPLOYEE| shows a page for taking a course for a students, redirects to /courses/forStudent/<int:student_id>/addCourse/<int:course_id>
/courses/dropCourseForStudentForm|ADMIN, EMPLOYEE| shows a page for dropping a course for a students, redirects to /courses/forStudent/<int:student_id>/dropCourse/<int:course_id>
