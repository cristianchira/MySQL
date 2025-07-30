package _15_TRIGGERS.AFTER;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/25178416#overview


/*                                                     Exercise


    ○ a new employee has been promoted to a manager

                   * annual salary should immediately increase with 20000

                                 - than the higher annual salary he earned until that moment

                  * a new record in dept_manager TABLE.

                  * create a trigger that will apply several modifications to the salaries table

                  * once the relevant record in the department manager table has been inserted.

         More precisely, our subtasks are make sure that

                  * the end date of the previously highest salary contract of that employee

                  * is the one from the execution of the insert statement.

                  * Insert a new record in the salaries table about the same employee that reflects their
                                - next contract as a manager.

         A start date the same as the new.from date from the newly inserted record in department manager a salary
                  *  equal to 20,000 higher than their highest ever salary

                  * and let that be a contract of indefinite duration.

         You can display that as a contract ending on the 1st of January in the year 9999.


                                  SQL  -- AFTER INSERT


    ○ DROP TRIGGER IF EXISTS trig_ins_dept_mng;

      DELIMITER $$

      CREATE TRIGGER trig_ins_dept_mng
      AFTER INSERT ON dept_manager
      FOR EACH ROW
      BEGIN

          DECLARE v_curr_salary int;

          SELECT  MAX(salary)
          INTO v_curr_salary FROM  salaries
          WHERE
              emp_no = new.emp_no;

     IF v_curr_salary IS NOT NULL THEN

          UPDATE salaries
          SET to_date = sysdate()

          WHERE  emp_no = NEW.emp_no and to_date = NEW.to_date;
          INSERT INTO salaries
          VALUES (new.emp_no, v_curr_salary + 20000, new.from_date,new.to_date);
    END IF;

    END $$

    DELIMITER ;

    DELETE FROM dept_manager WHERE emp_no = 111534;

    DELETE FROM salaries where salary=99393;

    INSERT INTO dept_manager VALUES(111534,'d009',date_format(sysdate(), '%y-%m-%d'),'9999-01-010');

SELECT
    *
FROM
    dept_manager
WHERE
    emp_no = 111534;

SELECT
    *
FROM
    salaries
WHERE
    emp_no = 111534;






 */


public class AFTER_ {
}
