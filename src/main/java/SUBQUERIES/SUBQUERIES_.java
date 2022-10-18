package SUBQUERIES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8510168#questions/3352670


/*                                   Subqueries with IN Nested Inside WHERE


    ○ Subqueries = INNER query = NESTED query = INNER select     -- means same thing

                  * queries embedded in a query

                  * they are part of another query, called an

                               - OUTER query = OUTER select;

    ○ A subquery may return

                 * a single value (a scalar),

                 * a single row,

                 * a single column,

                 * or an entire table

    ○ A subquery can be executed like

                 * standalone query

    ○ You can have a lot more than one subquery in your outer query

                   * it is possible to nest inner queries

                                 - within other inner queries

            in that case, the SQL engine would execute

                  * the innermost query first,

                  * and then each subsequent query,

                  * until it runs the outermost query last


 ○ Subqueries:


                  * allow for better structuring of the OUTER QUERY

                  * thus, each inner query can be thought of in isolation

                  * hence the name of SQL

                                – Structured Query Language!

                  * in some situations, the use of subqueries

                                - is much more intuitive

                                - compared to the use of complex joins and unions

                  * many users prefer subqueries simply because

                                - they offer enhanced code readability


                                              EXAMPLE   SQL


    ○ #select first and last name from EMPLOYEES table for
      # the same employee number that can be found in DEPT_MANAGER table  in order to retrieve all Managers


      SELECT
          e.emp_no, e.first_name, e.last_name
      FROM
          employees e

      WHERE
          e.emp_no IN (SELECT  dm.emp_no  FROM dept_manager dm)


      ORDER BY e.emp_no;

    ○ SUBQUERY OR INNER QUERY

                  * (SELECT  dm.emp_no  FROM dept_manager dm)

    ○ OUTER QUERY

                  * code until INNER QUERY


                                      FROM PROCESSING POINT OF VIEW


    1) the SQL engine starts by

                   * running the INNER QUERY(SUBQUERY OR NESTED QUERY)

     2) then it uses its returned output,

                   * which is intermediate,

                   * to execute the OUTER QUERY




 */


public class SUBQUERIES_ {
}
