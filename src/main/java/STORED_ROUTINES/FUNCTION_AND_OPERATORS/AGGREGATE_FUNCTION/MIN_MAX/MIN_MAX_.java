package STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION.MIN_MAX;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8376500#questions

//


/*                                                MON MAX   -- aggregate function


    ○ MAX([DISTINCT] expr) [over_clause]

                 * https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_max

                  * Returns the maximum value of expr.

                  * MAX() may take a string argument;

                  * in such cases, it returns the maximum string value.

                                - See Section 8.3.1, “How MySQL Uses Indexes”.

                                - The DISTINCT keyword can be used to find the maximum of the distinct values of expr,

                                 - however, this produces the same result as omitting DISTINCT.

          If there are no matching rows, or if expr is NULL,

                 * MAX() returns NULL.

    ○ MIN([DISTINCT] expr) [over_clause]

                  * https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_min

                  * Returns the minimum value of expr.

                  * MIN() may take a string argument;

                  * in such cases, it returns the minimum string value. See Section 8.3.1, “How MySQL Uses Indexes”.

                  * The DISTINCT keyword can be used to find the minimum of the distinct values of expr,

                  * however, this produces the same result as omitting DISTINCT.

           If there are no matching rows, or if expr is NULL, MIN() returns NULL.


    ○ Example

           SELECT  emp_no, MIN(salary) AS min_salary, MAX(salary) AS max_salary

           FROM  salaries

           WHERE  rom_date > '1997-01-01'

           GROUP BY emp_no

           ORDER BY max_salary DESC

           LIMIT 6;



 */

public class MIN_MAX_ {
}
