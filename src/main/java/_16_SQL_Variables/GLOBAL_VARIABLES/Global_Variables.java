package _16_SQL_Variables.GLOBAL_VARIABLES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8745702#questions


/*                                                     Global Variables


    ○ global variables apply to

                  * ALL connections related to a specific server


    ○ You CANNOT set just any variable as global

                  * a specific group of pre-defined variables in MySQL

                  * is suitable for this job.

          They are called System Variables


    ○ EXAMPLE OF System Variable

                 * .max_connections()

                               - indicates the maximum number of connections to a server

                               - that can be established at a certain point in time.


                 * .max_join_size()

                               - sets the maximum memory space allocated for the joins

                               - created by a certain connection


                SET GLOBAL max_connections = 1000;


                                                      SQL


    1. SET GLOBAL var_name = value;


    2. SET @@global.var_name = value;





 */


public class Global_Variables {
}
