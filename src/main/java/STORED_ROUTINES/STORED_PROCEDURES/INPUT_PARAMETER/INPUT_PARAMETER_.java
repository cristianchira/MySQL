package STORED_ROUTINES.STORED_PROCEDURES.INPUT_PARAMETER;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692218#questions


/*                                   Stored Procedures with an Input Parameter


    ○ A stored routine can perform a calculation that transforms

                  * an INPUT value

                  * in an OUTPUT value

    ○ Stored Procedures

                 * can take an input value

                 * and then use it in the query, or queries, written in the body of the procedure

    ○ This value is represented by

                 * the IN parameter

    ○ After that calculation is ready,

                * a RESULT will be returned




                                              SQL


    ○ DELIMITER $$
      CREATE PROCEDURE procedure_name(in parameter)

          BEGIN
              SELECT * FROM employees
              LIMIT 1000;
          END$$

      DELIMITER ;

                                           EXAMPLE


    ○ use employees;
      drop procedure if exists emp_salary;

      delimiter //
      create procedure emp_salary(IN p_emp_no INTEGER)
      begin
          select e.first_name, e.last_name, s.salary , s.from_date, s.to_date
      from employees e
          join
             salaries s on e.emp_no = s.emp_no
      where
            e.emp_no = p_emp_no;
      end //

      delimiter ;

    call emp_salary(10001);



                                       Implement a parameterized procedure


    ○ set @p_emp_no = 0;
      call employees.emp_info('Bezalel', 'Simmel', @p_emp_no);
      select @p_emp_no;



 */



public class INPUT_PARAMETER_ {
}
