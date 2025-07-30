package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.USER_DEFINED_FUNCTION;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692228#questions


/*                                    User-Defined Functions in MySQL


    ○  DELIMITER $$

            CREATE FUNCTION function_name(parameter data_type) RETURNS data_type
            DECLARE variable_name data_type
            BEGIN
            SELECT …
            RETURN variable_name
            END$$
            DELIMITER ;

    ○ PARAMETER

                  * NO OUT parameters to define

                                - between the parentheses after the object’s name

                  * all parameters are IN,

                                - and since this is well known,

                                - you need not explicitly indicate it with the word, ‘IN’


    ○ RETURN

                * although there are no OUT parameters,

                * there is a ‘return value’

                * it is obtained after running

                * the query contained in the body of the function

                * it can be of ANY data type


                                               We cannot call a function!


    ○  we can SELECT it, indicating an input value within parentheses

    ○ SELECT function_name(input_value);


                                                  AVOID ERROR code:1418


    ○ https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/16168789#questions

    ○ SET @@global.log_bin_trust_function_creators := 1;



                                                  EXAMPLE


     ○ DROP FUNCTION IF EXISTS f_emp_avg_salary;
      DELIMITER //
      CREATE FUNCTION f_emp_avg_salary(p_emp_no integer ) RETURNS decimal(10,2)

      BEGIN
          DECLARE v_avg_salary decimal(10,2);

     SELECT
        AVG(s.salary)
     INTO v_avg_salary FROM

    salaries s
        JOIN
    employees e ON e.emp_no = s.emp_no

    WHERE
    e.emp_no = p_emp_no;

   RETURN v_avg_salary;

   END //

  DELIMITER ;

  SELECT f_emp_avg_salary(10001);


                                                   EXERCISE  -- not works


    ○ Task:

                  * Create a function called ‘emp_info’ that takes for parameters

                               - the first and last name of an employee,

                               - and returns the salary from the newest contract of that employee.

                   Hint: In the BEGIN-END block of this program, you need to declare and use two variables

                               – v_max_from_date that will be of the DATE type,

                               - and v_salary, that will be of the DECIMAL (10,2) type.

                  Finally, select this function.


    ○ Solution:

                * CREATE FUNCTION emp_info(p_first_name varchar(255), p_last_name varchar(255)) RETURNS decimal(10,2)

                 DETERMINISTIC NO SQL READS SQL DATA

               BEGIN
                   DECLARE v_max_from_date date;
                   DECLARE v_salary decimal(10,2);

                   SELECT

                       MAX(from_date)

                  INTO v_max_from_date FROM

                   employees e

                         JOIN  salaries s ON e.emp_no = s.emp_no

                 WHERE
                     e.first_name = p_first_name
                    AND e.last_name = p_last_name;



SELECT

    s.salary

INTO v_salary FROM

    employees e

        JOIN

    salaries s ON e.emp_no = s.emp_no

WHERE

    e.first_name = p_first_name

        AND e.last_name = p_last_name

        AND s.from_date = v_max_from_date;



                RETURN v_salary;



END$$



DELIMITER ;



SELECT EMP_INFO('Aruna', 'Journel');



*/

public class USER_DEFINED_FUNCTION_ {

}
