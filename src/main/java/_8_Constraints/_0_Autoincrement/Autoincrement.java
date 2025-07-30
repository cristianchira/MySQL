package _8_Constraints._0_Autoincrement;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339104#notes   5:23


/*                         Autoincrement constraint MySQL


    ○ Autoincrement constraint can be applied to:
                  * PRIMARY KEY
                  * UNIQUE KEY
                  * INDEX

   ○ Cannot apply to all in the same time

   ○ Example
               * create table student
                 (
                     id int unsigned auto_increment primary key,
                     first_name varchar(45) null,
                     last_name  varchar(45) null,
                     email      varchar(45) null
                  )


                                ▬▬ Reset counter ▬▬


    1. TRUNCATE TABLE table_name
                * automatically resets the AUTO_INCREMENT counter

    2. ALTER TABLE student AUTO_INCREMENT = 1;
                * This command sets the AUTO_INCREMENT counter to 1,or whatever value you replace 1 with.
        This might not behave as expected
                * if there are existing rows with IDs higher than the number you set,
                * since AUTO_INCREMENT ensures new rows get a higher ID than any existing row.
        If you want to start AUTO_INCREMENT from 1,
                * you would need to delete the rows in the table
                * or ensure that the AUTO_INCREMENT value is greater than the maximum existing id.

    ○ However, always be careful with TRUNCATE and ALTER commands on your production database as
                * these changes are permanent and can't be undone.


 */


public class Autoincrement {
}
