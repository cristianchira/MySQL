package _2_SQL_Statements._2_DML._1_SELECT._15_WHERE_OR_HAVING;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345188#overview


/*                                                   WHERE_HAVING


    ○ WHERE

                   * allows us to set conditions

                   * that refer to subsets of individual rows

                   * applied before re-organizing the output into groups


    ○ HAVING

                  * cannot have both

                                - an aggregated

                                - and a non aggregated condition

                        in the having clause.


    ○ Aggregate function

                 * use Group By and Having

    ○ General condition

                 * WHERE


    ○ EXAMPLE

                 * SELECT  first_name, COUNT(first_name)

                   FROM  employees

                   WHERE   hire_date > '1998-01-01'

                   GROUP BY first_name

                   HAVING COUNT(first_name) < 100

                   ORDER BY first_name DESC;


 */


public class WHERE_OR_HAVING {
}
