# Event Feedback Analyzer

Event Feedback Analyzer is a full-stack application for creating events and analyzing feedback sentiment. It features a Spring Boot backend and a React frontend to provide a seamless user experience.

## Clone and setup

```bash
git clone https://github.com/NickSinazhenski/ibm_task.git
cd ibm_task
mvn install
npm install
```

## Run the application

1. Start the backend server:
```bash
mvn spring-boot:run
```
2. Start the frontend development server:
```bash
cd frontend && npm run dev
```

## How to test

- Access the frontend at [http://localhost:5173](http://localhost:5173) to interact with the app.
- Use the API endpoint [http://localhost:8080/events](http://localhost:8080/events) to test backend event services.

## How to use the web app

1. Open the website or the local frontend at [http://localhost:5173](http://localhost:5173).
2. Create a new event by providing a title and description (example: Title: "IBM Hackathon", Description: "Innovation Challenge").
3. Add feedback to the event (example: "I loved this event, the energy was great!").

