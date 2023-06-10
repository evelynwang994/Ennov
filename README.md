# Product Aggregator
Users can use this application to upload a product file, use filters to query and download an Excel file to show differences between Product Line and Product Generic.
## Tech Stack
Java, Spring Boot, Maven, HTML, CSS, jQuery and Bootstrap.
## How to Run the Application
1. Download the Dockerfile.
2. At the folder where the Dockerfile is located, run "docker build -t myapp:1.1 ."
3. Run "docker run -itd -p 8081:8080 --name="my_application_container" myapp:1.1"
4. Run http://localhost:8081/index to enter the application.