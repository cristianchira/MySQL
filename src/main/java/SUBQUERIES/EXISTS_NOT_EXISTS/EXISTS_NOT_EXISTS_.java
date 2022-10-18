package SUBQUERIES.EXISTS_NOT_EXISTS;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8565124#questions/3352670


/*                            SQL Subqueries with EXISTS-NOT EXISTS Nested Inside WHERE


    ○ Checks whether certain row values are found within a subquery

                   * this check is conducted row by row

                   * it returns a Boolean value


   1. If a row value of a subquery   EXISTS            ->  TRUE    ->     the corresponding record of
                                                                          the outer query is extracted

   2. If a row value of a subquery DOESN'T EXISTS     ->  FALSE   ->     no row value from the outer
                                                                         query is extracted

   ○ ORDER BY (nested queries)

                 * it is more professional to apply ORDER BY

                               - in the OUTER QUERY

                 * it is more acceptable logically

                                - to sort the final version of your dataset


    ○ Some, though not all,

                * NESTED QUERIES can be rewritten

                                - using JOINs, which are more efficient in general

                * this is true particularly for INNER queries using

                                - the WHERE clause


                                          EXAMPLE


    ○ SELECT
           e.first_name, e.last_name
       FROM
          employees e
      WHERE
          EXISTS( SELECT
                *
            FROM
                dept_manager dm
            WHERE
                dm.emp_no = e.emp_no);


                    1.  EXISTS                                    2.     IN


               tests row values for existence                   searches among values

                quicker in retrieving                          faster with smaller datasets
                large amounts of data



    1. SELECT
          e.first_name, e.last_name
      FROM
         employees e
      WHERE

          EXISTS( SELECT
                *
            FROM
                dept_manager dm
            WHERE
                dm.emp_no = e.em);


    2. SELECT
          e.first_name, e.last_name
      FROM
          employees e
      WHERE
          emp_no IN (SELECT
                dm.emp_no
            FROM
                dept_manager dm);





 */

public class EXISTS_NOT_EXISTS_ {
}
