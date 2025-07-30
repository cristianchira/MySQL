package _2_SQL_Statements._2_DML._3_DROP_TRUNCATE_DELETE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8376492#questions


/*                                                              DROP_TRUNCATE_DELETE


    ○ DROP

                  * you won’t be able to roll back

                                - to its initial state,

                                - or to the last COMMIT statement

                   * use DROP TABLE only when you are sure you

                                 - aren’t going to use the table in question anymore


    ○ TRUNCATE

                 * ~ DELETE without WHERE

                 * remove all records from a table

                                 - but it's structure REMAINS INTACT.

                 * AUTOINCREMENT value

                                 - WILL BE RESET and start from position 1


    ○ Delete

               * Remove RECORDS row by row

               * if where condition is omitted

                                 - will have same effect like TRUNCATE


                                          TRUNCATE vs DELETE without WHERE


    ○ The SQL optimizer will implement

              * different programmatic approaches

                            - when we are using TRUNCATE or DELETE

             * TRUNCATE delivers the output

                             - MUCH QUICKER than DELETE row by row

             * Auto-increment values are not reset with DELETE

 */


public class DROP_TRUNCATE_DELETE_ {
}
