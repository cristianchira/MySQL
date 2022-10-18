package SQL_VARIABLES.USER_DEFINED_VS_SYSTEM_VARIABLES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8745706#questions


/*                                                   User-Defined vs System Variables


    ○ Variables in MySQL can be characterized

                   * according to the way they have been created

    ○ Both user-defined and system variables

                    * can be set as SESSION variables

                    * ONLY a group onSystem variables can be set as session variables

    ○ SET SESSION max_connections

                    * not works because max_connections variable can be only global

    ○ SET SESSION sql_mode= 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION'

                    * works



                                             1. User-Defined


    ○ Variables that can be set by the user manually.


                                              2 System


    ○ Variables that are pre-defined on our system

                 * the MySQL server

    ○ Only System variables can be

                 * SET as global



 */

public class USER_DEFINED_VS_SYSTEM_VARIABLES_ {
}
