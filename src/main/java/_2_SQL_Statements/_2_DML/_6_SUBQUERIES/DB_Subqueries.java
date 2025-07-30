package _2_SQL_Statements._2_DML._6_SUBQUERIES;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8510168#questions/3352670

//Subqueries
//https://dev.mysql.com/doc/refman/8.0/en/subqueries.html


/*                                   Subqueries with IN Nested Inside WHERE


    ▬▬▐ Subqueries = Inner Queries = Nested Queries = Inner Select
                * are queries embedded in a query.
                * subqueries are part of another query
                            - called Outer Query (Outer Select)
                * A subquery is a SELECT statement within another statement.
                * All subquery forms and operations that the SQL standard requires are supported,
                      as well as a few features that are MySQL-specific.

    ○ Here is an example of a subquery:
                 * SELECT * FROM t1 WHERE column1 = (SELECT column1 FROM t2);
                             - This query will return all rows from t1 where
                                         • the value of column1 matches the value of column1 in t2.


                                          A subquery may return


    1.Single value (a scalar)

                * SELECT
                      emp_no, salary
                   FROM
                       salaries
                  WHERE salary = (SELECT MIN(salary) FROM salaries);


    2. Single row,

    3. Single column,

    4. Entire table


                                          A subquery can be executed like


    1. Standalone query

    2. You can have a lot more than one subquery in your outer query

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



                                      FROM PROCESSING POINT OF VIEW


    1) the SQL engine starts by

                   * running the INNER QUERY(SUBQUERY OR NESTED QUERY)

     2) then it uses its returned output,

                   * which is intermediate,

                   * to execute the OUTER QUERY




 */


public class DB_Subqueries {
}
