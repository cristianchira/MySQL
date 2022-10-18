package STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345176#overview

// https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html


/*                                             Aggregate_Function


    ○ they are applied on multiple rows of a single column of a table

                  * and return an output of a single value

                  * they ignore NULL values unless told not to


                                          Aggregate Function  query


    ○ SELECT COUNT(column_name)
       FROM table_name;


    1. COUNT()

                  * counts the number of non-null records in a field

                  * the parentheses after COUNT()

                                - must start right after the keyword,

                                - not after a whitespace in order to work

    1.1 SELECT COUNT(DISTINCT column_name)
          FROM table_name;

    2. SUM()

                  * sums all the non-null values in a column

    3. MIN()

                 * returns the minimum value from the entire list

   4. MAX()

                 * returns the maximum value from the entire list


   5. AVG()

                * Return the average value of the argument

 */

public class AGGREGATE_FUNCTION_ {
}
