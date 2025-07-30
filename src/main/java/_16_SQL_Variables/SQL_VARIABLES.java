package _16_SQL_Variables;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692226#questions


/*                                             VARIABLES


    ○ Types of variables by scope:

                  * Local variables

                                - a variable that is visible only in the BEGIN – END block

                                - in which it was created.

                                - DECLARE is a keyword that can be used when creating local variables only

                                 - v_avg_salary is visible only in the BEGIN - END block

                  * Session variables

                  * Global variable


                                            Create a variable


    ○ set @p_emp_no = 0;                    -- convention is to use 0;


                                            Get variable


    ○ select @p_emp_no;

    ○ When you are defining a program,

                   * such as a stored procedure for instance,

                   * you can say you are using ‘parameters’ Variables

    ○ DELIMITER $$
      CREATE PROCEDURE procedure_name (in parameter , out parameter )

    ○ ‘parameters’ are a more abstract term


    ○ Once the structure has been solidified,

                  * Then it will be applied to the database.

    ○ The input value you insert

                  * is typically referred to as the ‘argument’,

                  * while the obtained output value is stored in a ‘variable’


                                       Implement a parameterized procedure


    ○ set @p_emp_no = 0;                    -- convention is to use 0;

      call employees.emp_info('Georgi', 'Facello', @p_emp_no);

      select @p_emp_no;



 */

public class SQL_VARIABLES {
}
