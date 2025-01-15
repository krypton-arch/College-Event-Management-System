# College Event Management System 🎓

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17-red.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Servlet](https://img.shields.io/badge/Servlet-Jakarta-green.svg)](https://jakarta.ee/)

A web-based event management system designed for colleges, enabling students to discover and book events while providing administrators with comprehensive event management capabilities.

## 📚 Table of Contents
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Database Setup](#-database-setup)
- [Configuration](#-configuration)
- [API Documentation](#-api-documentation)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### Student Portal
- 📅 Browse upcoming college events
- 🎟️ Book event tickets
- 👀 View personal RSVPs
- ❌ Cancel bookings

### Admin Dashboard
- ➕ Create new events
- 📊 Monitor event listings
- 🔄 Update event details
- 🎮 Access admin controls

## 🛠️ Tech Stack

- **Backend:** Java (Jakarta Servlets)
- **Frontend:** HTML5, CSS3, JavaScript
- **Database:** MySQL
- **Libraries:** 
  - Google Gson
  - MySQL JDBC Driver
  - Jakarta Servlet API

## 📁 Project Structure

```plaintext
src/
├── java/
│   └── college/
│       ├── AddEvent.java
│       ├── BookingServlet.java
│       ├── EventFetchServlet.java
│       ├── LoginServlet.java
│       ├── MyRSVPsServlet.java
│       └── RegisterServlet.java
└── webapp/
    ├── adminhome.html
    ├── adminnew.html
    ├── home.html
    ├── login.html
    ├── my_rsvps.html
    ├── register.html
    └── css/
        ├── adminhome_css.css
        ├── css_loginregister.css
        ├── register_css.css
        ├── styles2.css
        └── styles3.css
```

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/college-event-management.git
   cd college-event-management
   ```

2. **Set up the development environment**
   - Install JDK 17 or higher
   - Install Apache Tomcat
   - Install MySQL 8.0

3. **Deploy the application**
   ```bash
   # Build the project
   mvn clean install

   # Deploy to Tomcat
   cp target/college-event-management.war /path/to/tomcat/webapps/
   ```

## 💾 Database Setup

1. **Create the database**
   ```sql
   CREATE DATABASE user_management;
   USE user_management;
   ```

2. **Create required tables**
   ```sql
   CREATE TABLE users (
       id INT PRIMARY KEY AUTO_INCREMENT,
       name VARCHAR(100) NOT NULL,
       email VARCHAR(100) UNIQUE NOT NULL,
       password VARCHAR(100) NOT NULL,
       user_type VARCHAR(20) NOT NULL
   );

   CREATE TABLE events (
       name VARCHAR(100) PRIMARY KEY,
       description TEXT NOT NULL,
       date DATE NOT NULL,
       location VARCHAR(100) NOT NULL
   );

   CREATE TABLE bookings (
       id INT PRIMARY KEY AUTO_INCREMENT,
       event_name VARCHAR(100) NOT NULL,
       user_email VARCHAR(100) NOT NULL,
       booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (event_name) REFERENCES events(name),
       FOREIGN KEY (user_email) REFERENCES users(email)
   );
   ```

## ⚙️ Configuration

Update `JDBC_URL`, `JDBC_USER`, and `JDBC_PASS` in the servlet files if needed:

```java
private static final String JDBC_URL = "jdbc:mysql://localhost:3306/user_management";
private static final String JDBC_USER = "root";
private static final String JDBC_PASS = "root";
```

## 📝 API Documentation

### Authentication Endpoints
- `POST /login` - User authentication
- `POST /register` - New user registration

### Event Endpoints
- `GET /fetchEvents` - Retrieve all events
- `POST /addEvent` - Create new event
- `POST /bookEvent` - Book an event

### User Endpoints
- `GET /myRSVPs` - Get user's bookings
- `POST /myRSVPs` - Cancel booking

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. Commit your changes
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
4. Push to the branch
   ```bash
   git push origin feature/AmazingFeature
   ```
5. Open a Pull Request

## 🔒 Security Considerations

> ⚠️ **Note:** This is a development version. For production:
> - Implement password hashing
> - Add CSRF protection
> - Enable HTTPS
> - Implement rate limiting
> - Add input sanitization

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">
Made with ❤️ for college event management
</div>
