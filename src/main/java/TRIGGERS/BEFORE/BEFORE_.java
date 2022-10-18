package TRIGGERS.BEFORE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/25178416#overview


/*                                   SQL  -- BEFORE INSERT


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



                                                      EXERCISE BEFORE INSERT


    ○ Task

                  * Create a trigger that checks if

                                -the hire date of an employee is higher

                                - than the current date.

                        If true,

                                 - set this date to be the current date.

                        Format the output appropriately (YY-MM-DD).


    ○ Solution


    DELIMITER $$

    CREATE TRIGGER trig_hire_date

    BEFORE INSERT ON employees

    FOR EACH ROW

    BEGIN

                IF NEW.hire_date > date_format(sysdate(), '%Y-%m-%d') THEN

                                SET NEW.hire_date = date_format(sysdate(), '%Y-%m-%d');

                END IF;

    END $$

    DELIMITER ;



INSERT employees VALUES ('999904', '1970-01-31', 'John', 'Johnson', 'M', '2025-01-01');

SELECT

    *

FROM

    employees

ORDER BY emp_no DESC;




 */

public class BEFORE_ {
}
