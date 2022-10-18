package SUBQUERIES.COMPLEX;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8565128#questions/3352670


/*                                                              COMPLEX


    ○ TASK

      Fill emp_manager with data about employees, the number of the department they are working in, and their managers.

        Your query skeleton must be:

          Insert INTO emp_manager SELECT

       U.*

      FROM

                 (A)

       UNION (B) UNION (C) UNION (D) AS U;

      A and B should be the same subsets used in the last lecture (SQL Subqueries Nested in SELECT and FROM).
      In other words, assign employee number 110022 as a manager to all employees from 10001 to 10020 (this must be subset A),
      and employee number 110039 as a manager to all employees from 10021 to 10040 (this must be subset B).

      Use the structure of subset A to create subset C, where you must assign employee number 110039 as a manager to
      employee 110022.

      Following the same logic, create subset D. Here you must do the opposite - assign employee 110022 as a manager
      to employee 110039.

      Your output must contain 42 rows.


    ○ ANSWER

DROP TABLE IF EXISTS emp_manager;

CREATE TABLE emp_manager (

   emp_no INT(11) NOT NULL,

   dept_no CHAR(4) NULL,

   manager_no INT(11) NOT NULL

);

    INSERT INTO emp_manager
SELECT
    u.*
FROM
    (SELECT
        a.*
    FROM
        (SELECT
        e.emp_no AS employee_ID,
            MIN(de.dept_no) AS department_code,
            (SELECT
                    emp_no
                FROM
                    dept_manager
                WHERE
                    emp_no = 110022) AS manager_ID
    FROM
        employees e
    JOIN dept_emp de ON e.emp_no = de.emp_no
    WHERE
        e.emp_no <= 10020
    GROUP BY e.emp_no
    ORDER BY e.emp_no) AS a UNION SELECT
        b.*
    FROM
        (SELECT
        e.emp_no AS employee_ID,
            MIN(de.dept_no) AS department_code,
            (SELECT
                    emp_no
                FROM
                    dept_manager
                WHERE
                    emp_no = 110039) AS manager_ID
    FROM
        employees e
    JOIN dept_emp de ON e.emp_no = de.emp_no
    WHERE
        e.emp_no > 10020
    GROUP BY e.emp_no
    ORDER BY e.emp_no
    LIMIT 20) AS b UNION SELECT
        c.*
    FROM
        (SELECT
        e.emp_no AS employee_ID,
            MIN(de.dept_no) AS department_code,
            (SELECT
                    emp_no
                FROM
                    dept_manager
                WHERE
                    emp_no = 110039) AS manager_ID
    FROM
        employees e
    JOIN dept_emp de ON e.emp_no = de.emp_no
    WHERE
        e.emp_no = 110022
    GROUP BY e.emp_no) AS c UNION SELECT
        d.*
    FROM
        (SELECT
        e.emp_no AS employee_ID,
            MIN(de.dept_no) AS department_code,
            (SELECT
                    emp_no
                FROM
                    dept_manager
                WHERE
                    emp_no = 110022) AS manager_ID
    FROM
        employees e
    JOIN dept_emp de ON e.emp_no = de.emp_no
    WHERE
        e.emp_no = 110039
    GROUP BY e.emp_no) AS d) as u;


 */



public class COMPLEX_ {


}
