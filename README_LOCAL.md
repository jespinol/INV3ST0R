# INV3$T0R: Local installation
### Pre-requisites
#### Java 17.0+
#### Gradle 8.7+
#### Database:
INV3$T0R requires mySQL 8.0.27+. Additional information to install and run mySQL server can be found [here](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/) and [here](https://www.mysql.com/products/workbench/)

Once mySQL server is installed and running, you must create the required database.
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

### Download and launch the application
Clone the repository
```shell
git clone https://github.com/jespinol/inv3st0r.git && cd inv3st0r && git checkout dev
```
From the repository root, run
```shell
mvn spring-boot:run 
```
This will start the application on port 8080.