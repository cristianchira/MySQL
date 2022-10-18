package JOINs.CROSS_JOIN;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/10653042#questions


/*                                                     CROSS JOIN


    ○ a cross join will take the values from a certain table

                  * and connect them with all the values from the tables we want to join it with


    ○ CROSS JOIN

                  * connects all the values, not just those that match

                  * the Cartesian product of the values of two or more sets

                  * particularly useful when the tables in a database

                                - are not well-connected

    ○ JOIN

                 * typically connects only the matching values


   ○ EXAMPLE CROSS JOIN 2 table

                *  SELECT dm.*, d.*
                   FROM dept_manager dm

                       CROSS JOIN departments d

                  ORDER BY dm.emp_no, d.dept_no;


 ○ EXAMPLE CROSS JOIN 3 table


               * SELECT e.*, d.*
                 FROM departments d

                 CROSS JOIN dept_manager dm
                 JOIN employees e on dm.emp_no = e.emp_no

	             WHERE d.dept_no <> dm.dept_no
                 ORDER BY dm.emp_no, d.dept_no;


    ○  EXAMPLE

               * Return a list with the first 10 employees with all the departments they can be assigned to.

               SELECT  e.*, d.*
               FROM employees e

                   CROSS JOIN  departments d

               WHERE  e.emp_no < 10011
               ORDER BY e.emp_no, d.dept_name;


 */


public class CROSS_JOIN_ {
}
