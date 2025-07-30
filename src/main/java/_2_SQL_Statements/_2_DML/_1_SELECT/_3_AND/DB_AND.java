package _2_SQL_Statements._2_DML._1_SELECT._3_AND;

//Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8344960#overview


/*                                           AND


    ○ SELECT column_1, column_2,… column_n
       FROM table_name
       WHERE condition_1 AND condition_2;

    ○ Conditions set on DIFFERENT columns

    ○ = equal operator

    ○ Example
               * SELECT emp_no, salary, dept_name
                 FROM employees
                 WHERE salary > 50000 AND dept_name = 'Engineering';



    ○ in SQL, there are
                   * many other linking keywords
                   * and symbols,
           called operators,
                   * that you can use with the WHERE clause
                                 - AND
                                  - OR
                                  - IN   - NOT IN
                                  - LIKE  - NOT LIKE
                                  - BETWEEN… AND…
                                  - EXISTS
                                  - IS NULL  - IS NOT NULL
                                  - NOT EXISTS
                                  - comparison operators


                                               AND query


    1. SELECT column_1, column_2,… column_n

      FROM table_name

      WHERE condition_1 AND condition_2;

 */

public class DB_AND {
}
