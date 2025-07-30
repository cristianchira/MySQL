package _11_STORED_ROUTINES.STORED_PROCEDURES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692208#questions

// https://dev.mysql.com/doc/refman/8.0/en/create-procedure.html


/*                                           The MySQL Syntax for Stored Procedures


    ○ semicolons ;

                  * they function as a statement terminator

                                - technically, they can also be called delimiters

    ○ DELIMITER $$    OR  DELIMITER //

                  * by typing DELIMITER $$,

                                - you’ll be able to use the dollar symbols as your delimiter


                                        CREATE PROCEDURES SYNTAX


    1. DROP PROCEDURE IF EXISTS procedure_name;
      DELIMITER $$
      CREATE PROCEDURE procedure_name(param_1, param_2)
       BEGIN
          SELECT * FROM employees
          LIMIT 1000;
      END$$

      DELIMITER;  -- reset delimiter to;

                  *  Parameters represent certain values that the procedure

                                 - will use to complete the calculation it is supposed to execute


    2. DROP PROCEDURE IF EXISTS procedure_name;
        DELIMITER $$
      CREATE PROCEDURE procedure_name()
      BEGIN
          SELECT * FROM employees
          LIMIT 1000;
      END$$

      DELIMITER;   -- reset delimiter to;


                   * A procedure can be created without parameters too!

                   * Nevertheless, the parentheses must always be attached to its name


                                          CALL PROCEDURE


    1. CALL stored_procedure();

                 * if database is selected

                 *  work without parenthesis

    2. CALL database_name.stored_procedure();

                 * work without parenthesis

    3. from WorkBench

                * https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692216#questions

 */

public class STORED_PROCEDURES_ {
}
