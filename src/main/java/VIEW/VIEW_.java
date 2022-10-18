package VIEW;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8565322#questions/3445222


/*                                          SQL VIEW


    ○ A virtual table whose contents

                  * are obtained from an existing

                                - table or

                                - tables,

                        called base tables


    ○ Think of a view object as

                 * a view into the base table

                 * the view itself

                                - DOES NOT CONTAIN ANY REAL DATA;

                 * the data is physically stored in the base table

                 * the view simply shows the data contained in the base table


    ○ A view acts as a shortcut for writing

                 * the same SELECT statement every time a new request has been made


    ○ Acts as a dynamic table because it

                 * instantly reflects

                               - data and

                               - structural changes in the base table

    ○ Don’t forget they are not real, physical data sets,

                 * we cannot perform CRUD operations on view

                               - reflected in base table

                       the information that has already been extracted.

                 * they should be seen as temporary virtual data tables

                 * retrieving information from base tables


                                                            SQL


    ○ CREATE [OR REPLACE] VIEW v_name AS

       SELECT
           column_1, column_2,… column_n
        FROM
          table_name;


                                                        EXAMPLE


    ○  CREATE OR REPLACE VIEW v_dept_emp_latest_date AS
        SELECT
            emp_no,
            MAX(from_date) AS max_date,
            MIN(from_date) AS min_date
        FROM
            dept_emp
        GROUP BY emp_no
        LIMIT 10;


                                                   Execute VIEW


    ○  select * employees.from v_dept_emp_latest_date;



                                                       Using SQL Views


    ○ Saves a lot of coding time

    ○ Occupies no extra memory






 */

public class VIEW_ {
}
