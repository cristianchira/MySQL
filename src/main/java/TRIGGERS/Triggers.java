package TRIGGERS;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/25178416#overview


/*                                            MySQL Triggers


    ○ A trigger is a MySQL object

                  * that can “trigger” a specific action or calculation

                                - ‘before’

                                - or ‘after’

                        an INSERT, UPDATE, or DELETE

                                - statement has been executed

    ○ System functions = built-in functions

                 * often applied in practice,

                 * they provide data about the moment of the execution of a certain query


    ○ A type of Stored Program,

                  * associated with a table,

                  * that will be activated automatically once a specific event occurs

    ○ This event must be

                  * related to the associated table

                  * and represented by one of the following three DML statements:

                                - INSERT

                                - UPDATE

                                - DELETE


                                      SQL  -- BEFORE INSERT


    ○ use employees;

       COMMIT;

      # Before insert
       DELIMITER $$

       CREATE TRIGGER  before_salary_insert
       BEFORE INSERT ON salary
       FOR EACH ROW
       BEGIN

            IF NEW.salary < 0  THEN
            SET NEW.salary =0;
            END IF;

      END$$

     DELIMITER ;


                                    SQL -- BEFORE UPDATE


    ○  # Before update
         DELIMITER $$

         CREATE TRIGGER  before_salaries_update

         BEFORE UPDATE ON salaries
         FOR EACH ROW
         BEGIN

            IF NEW.salary < 0  THEN
            SET NEW.salary = OLD.salary;
            END IF;

    END$$

DELIMITER ;


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

    DELETE FROMsalaries where salary=99393;

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


public class Triggers {
}
