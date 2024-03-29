# TODOs
- document the one-time database setup: setup instructions should be able to be run by anyone or myself
- All commands should be able to be run in the mysql shell (shouldn't require mysql workbench to do it)
    - e.g. create database (with statement) `CREATE DATABASE `inv3st0r` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;`
    - e.g. create a new application user and development user with passwords (generate statements)
    - assign roles (generate statements)
    - statements should be idempotent (doesn't cause error if you run twice)
    - test by deleting all of the above and running again
    - lastly put it all into a single script rather than entering commands one-by-one
