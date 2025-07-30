package _2_SQL_Statements._2_DML._1_SELECT._10_IS_NOT_NULL_IS_NULL;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345126#overview


/*                                          IS NOT NULL


    ○ It is used to extract values that are NOT NULL


                                       IS_NOT_NULL Query


    ○ SELECT column_1, column_2,… column_n
      FROM table_name
      WHERE column_name IS NOT NULL;


                                           IS NULL


    ○ used to extract values that are null

    ○ SELECT column_1, column_2,… column_n
      FROM table_name
      WHERE column_name IS NULL;


    ○ Example
                * SELECT first_name, last_name, email
                  FROM employees
                  WHERE email IS NULL;



 */


public class DB_IS_NOT_NULL_IS_NULL {
}
