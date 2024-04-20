# Welcome to INV3$T0R
***

## Task
[Spring Boot](https://spring.io/projects/spring-boot) simplifies the development of back-end applications with Java, offering a wide range of pre-configured components that reduce development time, increase productivity, and allow developers to focus more on business logic rather than boilerplate code.

Learning Spring Boot can be challenging for beginners. It involves understanding concepts like dependency injection, RESTful services, and database integration. However, with a solid grasp of Java fundamentals and by following tutorials and practical projects, beginners can become proficient in Spring Boot.

The goal of this project is to develop web application to help keep track of a users’ investments by offering a platform to simulate stock trading.

INV3$T0R is a Spring Boot web application that allows users to simulate stock trading. Users can create an account, log in, and start trading stocks. The application provides a platform to simulate buying and selling stocks, view the user’s portfolio, and track the user’s investment history.

## Description
### Features
- Ability to create, edit, and delete user accounts
- Basic user authentication provided by Spring Security
- Persistent user data storage using a MySQL database
- A home view with summary of all investment accounts as well as current state and news of US markets (taken from [The Polygon.io Stocks API](https://polygon.io/docs/stocks/getting-started))
- A detailed view of each investment account
- Ability to create, edit, and delete investment account
- Ability to "add funds" to an investment account
- Sortable (by dollar amount, stock ticker, date, etc.) and searchable transaction history for each account
- Ability to "buy and sell" stocks using recent prices (taken from the previous close of a specified stock ticker [The Polygon.io Stocks API](https://polygon.io/docs/stocks/getting-started))
- A simple notification system to alert users of new transactions and account updates
- A responsive user interface (created by [Start Bootstrap](https://startbootstrap.com/theme/sb-admin-2/))

### Tech Stack
- Maven
- Java
- Spring Boot
- JavaScript
- HTML
- CSS
- Thymeleaf
- Bootstrap 4
- MySQL
- GCP

## Installation
INV3$T0R has been deployed on Google Cloud Platform and can be [accessed here](http://inv3st0r.ue.r.appspot.com). Alternatively, a local instance can be run locally as described [here](README_LOCAL.md).


## Usage
In your browser, Navigate to the [home page](http://inv3st0r.ue.r.appspot.com) (or http://localhost:8080 if running a local instance) to access the application.

From the login page, create a new account or log in with your credentials, create or navigate to your investment account, and start using the application to simulate stock trading.

<br></br>
***

#### Created by [Jose M. Espinola Lopez](https://github.com/jespinol)
