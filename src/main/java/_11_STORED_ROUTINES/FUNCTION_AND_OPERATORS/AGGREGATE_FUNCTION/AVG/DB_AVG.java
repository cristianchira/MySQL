package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION.AVG;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8376504#questions

// https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_avg


/*                                                     AVG(expr)


    ○ AVG([DISTINCT] expr) [over_clause]
                  * Returns the average value of all non-null value of expr.
                  * The DISTINCT option can be used to return the average of the distinct values of expr.

          If there are no matching rows,

                  * AVG() returns NULL.

          The function also returns NULL if expr is NULL.

This function executes as a window function if over_clause is present. over_clause is as described in Section 12.21.2, “Window Function Concepts and Syntax”; it cannot be used with DISTINCT.


 */

public class DB_AVG {
}
