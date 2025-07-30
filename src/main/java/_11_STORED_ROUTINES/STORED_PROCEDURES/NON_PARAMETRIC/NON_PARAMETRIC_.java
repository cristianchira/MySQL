package _11_STORED_ROUTINES.STORED_PROCEDURES.NON_PARAMETRIC;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692214#questions


/*                                                       CREATE PROCEDURES SYNTAX

    ○ DROP PROCEDURE IF EXISTS procedure_name;
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



 */

public class NON_PARAMETRIC_ {
}
