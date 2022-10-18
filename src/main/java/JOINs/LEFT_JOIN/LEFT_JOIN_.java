package JOINs.LEFT_JOIN;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8441432#questions


/*                                              LEFT JOIN

   ○ When working with left joins,

                 * the order in which you join tables matters

                 * All matching values of the two tables +

                 * All values from the left table

                               * that match no values from the right table


                                                SQL


    ○ SELECT  t1.column_name, t1.column_name, …, t2.column_name, …

       FROM  table_1 t1

       LEFT JOIN  table_2 t2 ON t1.column_name = t2.column_name

       WHERE  column_name … IS NULL;     --   use this statement to obtain only rows from left column which sre not in
                                              right table



    ○ SELECT  m.emp_no, m.dept_no, d.dept_name

       FROM  dept_manager_dup m

       LEFT JOIN departments_dup d on m.dept_no = d.dept_no

       ORDER BY m.emp_no;




 */


public class LEFT_JOIN_ {
}
