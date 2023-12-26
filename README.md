
# Tasks manager

Simple task-management system that provide functionality to create, update and etc. user's tasks.


## API Reference

#### Create a user

```http
  POST /users/register
```
Using User object as request body
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `String` | **Required** - Email of a new user |
| `password` | `String` | **Required** - Password of a new user (At least 6 chars) |

#### Get all users

```http
  GET /users/get-all
```

#### Create a task

```http
  POST /tasks/create
```
Using Task object as request body

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `title` | `String` | **Required**  |
| `description` | `String` | **Required**  |
| `taskStatus` | `Object` | **Optional** - by default - PENDING but user can specify status such as IS_COMPLETED or PERFORMING |
| `taskPriority` | `Object` | **Optional** - by default - DEFAULT_PRIORITY but user can specify priority such as HIGH_PRIORITY or LOW_PRIORITY |
| `comment` | `String` | **Optional**  |
| `performerEmail` | `String` | **Required** - Email of attached user (existing users only) |

#### Get all tasks

```http
  GET /tasks/get-all
```

#### Get all assigned to specific user tasks

```http
  GET /tasks/get-assigned/{performerEmail}
```

#### Get all created user tasks

```http
  GET /tasks/get-assigned/{authorEmail}
```

#### Delete a specific task (by Id) of logged in user

```http
  DELETE /tasks/{email}/delete/{taskId}
```

#### Edit a specific task (by Id) of logged in user

```http
  PUT /tasks/{email}/delete/{taskId}
```







## Documentation

[Documentation](http://localhost:8080/swagger-ui/index.html)


## Run Locally
[Install Docker Compose](https://docs.docker.com/compose/install/)

Run Docker-daemon on your machine

Clone project
```bash
  git clone https://github.com/Lyfyfision/task-management-system.git
```

Go to the project directory

```bash
  cd taskManagement
```
Start Postgres through docker compose

```bash
  docker-compose up
```
Build project

```bash
  mvn package
```

Start the server

```bash
  java -cp target/taskManagement-0.0.1-SNAPSHOT.jar

```


## Built with
- Java 17
- Spring Boot 3.2.0
- PostgreSQL 16
- Hibernate 6.0
- Docker-Compose
- SpringDOC 2.2.0