
/**                                MySQL

Martin Gancev -Why MySQL
    https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8337988#questions


Code Decode -RD or NoSQL
    https://www.udemy.com/course/master-spring-boot-microservice-angular-with-k8s-cicd-aws/learn/lecture/39271116#questions


MySQL 8.0.32  Source Code Documentation
    https://dev.mysql.com/doc/dev/mysql-server/latest/


MySQL 8.0 Reference Manual
    https://dev.mysql.com/doc/refman/8.0/en/


Overview of the MySQL Database Management System
    https://dev.mysql.com/doc/refman/8.0/en/what-is.html


What is MySQL?
    https://dev.mysql.com/doc/refman/8.0/en/what-is-mysql.html


                               
    ○ MySQL includes two components:
                1. MySQL Database Server
                            - is the main engine of the database.
                            - This is where we store the data for the actual database
                            - and it also has support for CRUD features on the data.
                2. MySQL Workbench
                            - is a client GUI for interacting with the database.

    ○ MySQL is
                * a reliable
                * mature
                * and open source.

    ○ To implement SQL commands,
                * you will need a database management system, often called a
                            - database or a platform.

    ○ There are a few popular databases which are all based on SQL.
                * They support various forms of the original language
                * and there are only slight differences between different versions.


                                        ▬▬ DB platforms ▬▬


    ○ Oracle
                * A powerful and feature-rich relational database management system (RDBMS)
                      developed by Oracle Corporation.
                * It is widely used in large-scale applications and supports advanced features such as
                             - partitioning,
                             - materialized views,
                             - and replication.

    ○ MySQL
               * An open-source RDBMS developed and supported by Oracle Corporation.
               * It is widely used for web applications and is one of the most popular databases in the world.
                           - MySQL supports
                                       • ACID transactions,
                                       • stored procedures,
                                       • and triggers.

    ○ SQLite
                * A serverless, self-contained, and lightweight database engine
                            - used mainly in mobile and embedded applications.
                * SQLite is an open-source project and stores data in a single file,
                             - which simplifies database management.

    ○ Microsoft SQL Server,
                * A powerful, enterprise-class RDBMS developed by Microsoft.
                * It supports advanced features such as
                            - data warehousing,
                            - business intelligence,
                            - and analytics.
                     SQL Server offers high performance, scalability, and security.

    ○ PostgreSQL
                * An open-source object-relational database system
                            - that offers many advanced features such as
                                        • full-text search,
                                        • GIS support,
                                        • and support for custom data types.
                 * PostgreSQL is known for its reliability, data integrity, and extensibility.

    ○ MariaDB
                  * A fork of MySQL created by the original developers of MySQL
                              - after Oracle acquired MySQL.
                   * MariaDB is designed as a drop-in replacement for MySQL,
                   * providing similar features and performance but with an emphasis on being
                               - more open and community-driven.


                     ▬▬ How to choose between a Relational Database and a NoSQL ▬▬


    1. READ Vs WRITE Intensive Application
                * When your application is READ INTENSIVE
                            - then use NoSQL DB.
                * If the application is WRITE INTENSIVE,
                    then go for Relational DB.
        Why?
                * If we have to read a lot and update less,
                * then having multiple relational DBs
                * and having join queries to read a lot of data base
                            - will decrease the performance.
         So better go with Non-relational DBs.
                * Now everything will be in one document and
                            - no joins,
                            - views complex queries wool be required
                            - which decreases the
                                        • readability,
                                        • performance ,
                                        • speed and other non functional parameters of application

    2. Unstructured VS Structured Data
        When your application is going to persist the data which is NOT STRUCTURED,
                * there is no guarantee on how many fields we will get
                * and we need to store then its unstructured data.
                            - then go for No sql DB choice
                                        • as there is no restriction on columns or fields
                                        • in which data has to be inserted.
        But If we are very sure the data structure
                * wont change ever in whole life cycle of application
                            - then even Relational DB is also a good choice

    3. Concurrency and atomicity is top priority (RD)
                * In applications like hotel / movie ticket booking
                            - Several users simultaneously read and write the data.
                * To prevent data inconsistency,
                            - such as the double booking of hotel rooms,
                            - we must coordinate such transactions.
                * Transactional access to the data in a relational database manages concurrency.
                * A transaction is regarded as an atomic operation,
                            - making it possible to roll back or commit a transaction
                            - after a successful execution in error handling.
                * To facilitate this feature we need Relational Dbs .

    4. Cost associated (NoSQL)
                * If we are allotted a very less budget for the project,
                            - go for open source NoSQL DB.
                                        •as many NoSQL databases are open source and freely available.
                * While with Relational DBs,
                             - these are not open source usually we need to purchase the License for it.

    5. Redundancy tolerance ()
                * Eliminating data redundancy
                            - is one of the relational database's main benefits.
                * One table contains information about a particular entity,
                * whereas other tables connected by foreign keys
                            - include information pertinent  to that entity.

 */


public class MySQL {
}
