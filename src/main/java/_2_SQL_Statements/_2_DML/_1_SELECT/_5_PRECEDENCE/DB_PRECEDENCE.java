package _2_SQL_Statements._2_DML._1_SELECT._5_PRECEDENCE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345096#overview


/*                                      Operator Precedence


    ○ So far:
                  * AND
                                - conditions set on different columns
                  * OR
                                - OR conditions set on the same column

    ○ The logical order with which you must comply
                  * when you use both operators
                  * in the same WHERE block

    ▬▬▐ An SQL rule stating that in the execution of the query,
                  * the operator AND
                                - is applied FIRST,
                                - while the operator OR is applied SECOND
                                - AND > OR
         Regardless of the order in which you use these operators,
                 * SQL will always start by reading the conditions
                                - around the AND operator


                                        ▬▬ Query order ▬▬


    ○ SELECT *
      FROM employees
      WHERE last_name ='Dennis' AND gender= 'M' OR gender = 'F';
                  * Condition1 = last_name= 'Dennis' AND gender ='M'
                  * Condition2 =  gender= 'F'

    ○ SELECT *
       FROM employees
       WHERE last_name ='Denis' AND (gender= 'M' OR gender = 'F');
           Parentheses can help us restructure the logic of the operation
                  * to retrieve a list of people with the last name Dennis
                  * independent of their gender.
           You must place parentheses around the conditions about gender.
                  * Condition1 = last_name= 'Dennis'
                                       AND
                  * Condition2 = (gender ='M'  gender= 'F')

    ○ the logical operator PRECEDENCE
                  *  is an important phenomenon when
                                 - enlisting conditions in the WHERE clause.
 */

public class DB_PRECEDENCE {
}
