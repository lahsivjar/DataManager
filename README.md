This was my first java project. It is a simple interface for managing grades, mess bills etc.

To run this you must have a mysql server running on your system.

  - Login as root user in mysql
  - Run erp.sql from mysql as "source erp.sql"
  - Create a user javafp with password javafp
  - Run the following commands from mysql to grant user javafp with required permission
        - "grant usage on *.* to javafp@localhost identified by 'javafp';"
        - "grant all privileges on erp.* to javafp@localhost;"
