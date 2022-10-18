package JOINs.INNER;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8429210#questions

// https://dev.mysql.com/doc/refman/8.0/en/join.html



/*                                             INNER JOINs




    ○ extract only records in which the values in the related columns match.

                 * The SAME values appearing in the in two or more tables are

                                - the matching values also called

                                - matching records.

                  * Null values,

                  * or values appearing in just one of the two tables and not appearing in the other,

                  * are NOT displayed

          Only non-null matching values are in play.


                  * And what if such matching values did not exist?

                  * Simply, the result set will be empty.

                  * There will be no link between the two tables.


    ○ Result set

                  * the area that belongs to both circles, which is filled with red,

                  * represents all records belonging to both the

                                - “Department Manager Duplicate”

                                - and the “Departments Duplicate” tables

                 * inner join can help us to extract result set


                                            SQL


    ○ SELECT t1.column_name, t1.column_name, …, t2.column_name, …

       FROM table_1 t1

       JOIN table_2 t2 ON t1.column_name = t2.column_name;


    ○ EXAMPLE   -- INNER JOIN can be shortcut to JOIN( SQL will understand INNER JOIN)

                  * SELECT     m.dept_no, m.emp_no, d.dept_name

                     FROM    dept_manager_dup m

                    INNER JOIN  departments_dup d  ON  m.dept_no = d.dept_no

                    ORDER BY m.dept_no;


                                              Venn diagram


    ○ a mathematical tool representing all possible logical relations

                * between a finite collection of sets

 */

public class INNER_ {
}
