package _2_SQL_Statements._3_DCL;

// Martin G. DCL
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338038#overview

//MySQL 8.0.32
//Source Code Documentation
//  https://dev.mysql.com/doc/refman/8.0/en/account-management-statements.html


/*                                           Data Control Language (DCL)


    ○ GRANT și REVOKE nu sunt singurele propoziții de administrare de cont. Manualul 9.7, 15.7.1, mai numește ALTER USER, CREATE USER, DROP USER, RENAME USER, CREATE ROLE, DROP ROLE, SET PASSWORD, SET DEFAULT ROLE și SET ROLE. 15.3.3 spune că GRANT, REVOKE, ALTER USER, CREATE USER, DROP USER, RENAME USER și SET PASSWORD fac commit implicit, pentru că modifică tabele din schema mysql.
    HISTORICAL: DCL is a syntax containing only 2 statements:
    Regula citită în tur: MySQL 9.7 Reference Manual, 15.7.1 Account Management Statements și 15.3.3 Statements That Cause an Implicit Commit.

                   * GRANT

                                 - gives(or grants) certain permission to users

                                 - GRANT type_of_permission
                                   ON database_name.table_name
                                   TO 'username'@'localhost'

                   * REVOKE

           These statements allow us

                   * to manage the rights users have in the database

    ○ A database is used of very large numbers of users(people)

                   * Typically, users don't have

                   * same rights of access to the information.


                                            Database administrators


    ○ people who have complete rights to a database

                  * they can grant access to users

                  * and can revoke it


 */

public class DCL {
}
