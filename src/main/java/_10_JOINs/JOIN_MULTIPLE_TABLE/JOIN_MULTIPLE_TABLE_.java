package _10_JOINs.JOIN_MULTIPLE_TABLE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8499204#questions



/*                                                       JOIN MULTIPLE TABLE


    ○ When creating a query that joins multiple tables,

                   * you must back it with strong intuition

                   * and a crystal-clear idea of how you would like the tables to be connected


    ○ EXAMPLE  -- join 4 table

SELECT
    e.emp_no,
    e.first_name,
    e.last_name,
    e.hire_date,
    dm.from_date,
    d.dept_name,
    AVG(s.salary) AS avg_salary,
    t.title
FROM
    employees e
        JOIN
    titles t ON e.emp_no = t.emp_no
        JOIN
    salaries s ON e.emp_no = s.emp_no
        JOIN
    dept_manager dm ON e.emp_no = dm.emp_no
        JOIN
    departments d ON d.dept_no = dm.dept_no
GROUP BY e.emp_no
HAVING avg_salary > 50000
ORDER BY d.dept_name , dm.from_date;



 */


public class JOIN_MULTIPLE_TABLE_ {
}
