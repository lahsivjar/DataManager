This was my first java project. There are many errors in this project, USE AT YOUR OWN RISK.

It is a simple application for managing grades, mess bills etc.

To run this you must have a mysql server running on your system.

  - Login as root user in mysql
  - Run erp.sql from mysql as <code>source erp.sql</code>
  - <code>Create a user javafp with password javafp</code>
  - Run the following commands from mysql to grant user javafp with required permission
    - <code>grant usage on *.* to javafp@localhost identified by 'javafp';</code>
    - <code>grant all privileges on erp.* to javafp@localhost;</code>

After mysql has been set and running:

  - Run ant
  - A jar file is gnenerated. To use it type <code>java -jar <path to DataManager>/dist/lib/*.jar</code>
  - Default username "5050" password "user1"
