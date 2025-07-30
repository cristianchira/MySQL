package _11_STORED_ROUTINES.STORED_PROCEDURES.OUTPUT_PARAMETER;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692220#questions


/*                                       Stored Procedures with an Output Parameter


    ○ every time you create a procedure containing both an IN and an OUT parameter,

                  * remember that you must use the SELECT-INTO structure

                  * in the query of this object’s body!

    ○ out parameter

                 * it will represent the variable containing

                 * the output value of the operation

                               - executed by the query of the stored procedure


                                                               Sql


    ○ DELIMITER $$
      CREATE PROCEDURE procedure_name(in parameter, out parameter)
      BEGIN
      SELECT * FROM employees
      LIMIT 1000;
      END$$
      DELIMITER ;


                                                        EXERCISE


    ○ Create a procedure called ‘emp_info’ that uses as parameters

                  * the first and the last name of an individual,

                  * and returns their employee number.

    ○ DELIMITER $$

    CREATE PROCEDURE emp_info(in p_first_name varchar(255), in p_last_name varchar(255), out p_emp_no integer)

BEGIN

                SELECT

                                e.emp_no

                INTO p_emp_no FROM

                                employees e

                WHERE

                                e.first_name = p_first_name

                                                AND e.last_name = p_last_name;

END$$

DELIMITER ;



 */

public class OUTPUT_PARAMETER_ {
}
