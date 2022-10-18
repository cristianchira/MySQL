package SQL_STATEMENTS.DML.INSERT.FROM_ANOTHER_TABLE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8346176#questions


/*                                                            FROM ANOTHER TABLE


    ○



                                                                 SQL

    ○ INSERT INTO SELECT

      INSERT INTO table_2 (column_1, column_2, …, column_n)

       SELECT column_1, column_2, …, column_n

       FROM table_1

       WHERE condition;


    ○ Example

      INSERT INTO department_dup (dept_no, dept_name )

      SELECT * FROM departments;


 */

public class FROM_ANOTHER_TABLE_ {
}
