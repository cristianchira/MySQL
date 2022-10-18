package UNION_UNION_ALL;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8510164#questions


/*                                                       UNION  --  UNION ALL


    ○ UNION ALL

                  * used to combine a few SELECT statements in a single output

                  * you can think of it as a tool that allows you to unify tables


    ○ When uniting two identically organized tables

                 * UNION

                               - displays only distinct values in the output

                 * UNION

                               - uses more MySQL resources(computational power and storage space)

                 * UNION ALL

                                - retrieves the duplicates as well


    ○ Looking for better results?

               * use UNION

    ○ Seeking to optimize performance?

               * opt for UNION ALL


                                                         SQL


    ○ SELECT  N columns

      FROM table_1

          UNION [ALL]

      SELECT

            N columns

     FROM table_2;

    ○ We have to select the  ------------------------------------------------------------------------------------------

                   * same number of columns from each table

    ○ These column should have ---------------------------------------------------------------------------------------

                  * same name

                  * same order

                  * contain related data type

                  * use NULL AS

                                - to achieve these goals


                                                        EXAMPLE


    ○  SELECT
           e.emp_no,
           e.first_name,
           e.last_name,
           NULL AS dept_no,
           NULL AS from_date

       FROM

          employees_dup e

      WHERE

          e.emp_no = '10001'

     UNION [ALL] SELECT

         NULL AS emp_no,
         NULL AS first_name,
         NULL AS last_name,
         m.dept_no,
         m.from_date

     FROM

        dept_manager m;


 */

public class UNION_UNION_ALL_ {
}
