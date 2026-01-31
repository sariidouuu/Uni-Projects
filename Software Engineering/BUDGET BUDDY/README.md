# Budget Buddy
Budget Buddy is a Software Engineering project developed for the "Software Engineering" course. The application is designed to help users manage their finances and team expenses efficiently. It is provided as a compressed .jar file, allowing it to run without the need for an IDE.

## 🚀 Getting Started
Follow these instructions to get the project up and running on your local machine.

**Prerequisites**
Before running the application, ensure you have the following installed:

* Java Runtime Environment (JRE)
* XAMPP (or any local server environment with Apache and MySQL)

## 🛠 Setup and Installation
### 1. How to verify Java Runtime Environment (JRE) installation:
1. Open the Command Prompt (cmd).
2. Type the following command and press Enter: java -version
3. If Java is installed, you will see version information. If you receive an error message or 'command not found', you need to download and install Java (JRE) from the official [Oracle](https://www.oracle.com/java/technologies/downloads/) website or [Adoptium](https://adoptium.net/en-GB)."
4. This application was compiled using Java 24 with preview features enabled.
*To run the project, you must have JDK 24 installed.*

### 2. Database Configuration (XAMPP)

1. The application requires a MySQL database to function. Follow these steps:
2. Open the XAMPP Control Panel and start the Apache and MySQL modules.
3. Open your browser and navigate to: http://localhost/phpmyadmin/.
4. Click on "New" in the left sidebar to create a new database.
5. In the Database name field, enter budgetbuddy and click Create.
6. Select the newly created budgetbuddy database and click on the "Import" tab at the top.
7. Click "Choose File" and locate the budgetbuddy.sql file (found inside the project folder).
8. Scroll to the bottom of the page and click "Import" to finalize the database setup.

### 3. Running the Application
Once the database is ready, you can launch the app:

1. Download the "BUDGET BUDDY.zip" file from the repository.
2. Unzip the folder to your preferred location.
3. Open the folder and locate the run.bat file (Windows Batch File).
4. Double-click run.bat to start the application.

### ℹ️ Technical Note
The application is distributed as a **.jar** file, ensuring it can run immediately without requiring a development environment (IDE). 

What? Looking for the source code? 
