This was my first java project. There are many errors in this project, USE AT YOUR OWN RISK.

It is a simple application for managing grades, mess bills etc.

To run this you must have a mysql server running on your system.

  - Login as root user in mysql
  - Run erp.sql from mysql as <nowiki>source erp.sql</nowiki>
  - <nowiki>Create a user javafp with password javafp</nowiki>
  - Run the following commands from mysql to grant user javafp with required permission
    - <nowiki>grant usage on *.* to javafp@localhost identified by 'javafp';</nowiki>
    - <nowiki>grant all privileges on erp.* to javafp@localhost;</nowiki>

After mysql has been set and running:

  - Run ant
  - A jar file is gnenerated. To use it type <nowiki>java -jar <path to DataManager>/dist/lib/*.jar</nowiki>
  - Default username "5050" password "user1"
