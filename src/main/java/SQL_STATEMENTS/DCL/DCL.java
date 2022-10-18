package SQL_STATEMENTS.DCL;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338038#overview

// https://dev.mysql.com/doc/refman/8.0/en/sql-server-administration-statements.html

//  https://dev.mysql.com/doc/refman/8.0/en/account-management-statements.html


/*                                           Data Control Language (DCL)


    ○ DCL is a syntax containing only 2 statements:

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
