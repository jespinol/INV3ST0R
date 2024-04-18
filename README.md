# Welcome to My Spring Portfolio
***

## Task
TODO - What is the problem? And where is the challenge?

## Description
TODO - How have you solved the problem?

## Installation
### Pre-requisites
#### Database
INV3ST0R requires mySQL 8.0.27+. Additional information to install and run mySQL server can be found [here](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/) and [here](https://www.mysql.com/products/workbench/)

Once mySQL is installed and running, you must create the required database. 
First, log into mySQL:
```shell
sudo /path/to/mysql --password
```
You may need to enter your OS admin password, followed by the mySQL root password. 

Then, to create the database run the following commands:
```sql
CREATE DATABASE IF NOT EXISTS inv3st0r;
CREATE USER IF NOT EXISTS 'inv3st0r'@'%' IDENTIFIED BY 'password';
GRANT ALL ON inv3st0r.* TO 'inv3st0r'@'%';
```
#### Other Dependencies
- Java 21.0.2+
- 
- Gradle 8.7+

#### Download and launch the application
Clone the repository
`git clone https://github.com/jespinol/inv3st0r.git && cd inv3st0r`
From the repository root, run
`gradle bootRun`
This will start the application on port 8080.

## Usage
Navigate to `http://localhost:8080` in your browser to access the application.

From the login page, create a new account or log in with your credentials, and start using the application to simulate stock trading.


<br></br>
***

#### Created by [Jose M. Espinola Lopez](https://github.com/jespinol)
