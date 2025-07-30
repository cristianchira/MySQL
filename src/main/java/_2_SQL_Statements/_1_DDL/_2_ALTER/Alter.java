package _2_SQL_Statements._1_DDL._2_ALTER;

// https://dev.mysql.com/doc/refman/8.0/en/alter-database.html


/*                                               ALTER statement


    1. ADD COLUMN

               * ALTER TABLE table_name
                  ADD COLUMN new_column_name data_type
                  AFTER existing_column_name;

               * ALTER TABLE table_name
                 ADD COLUMN new_column_name data_type
                 FIRST;


    2. DROP

               * ALTER TABLE table_name
                 DROP COLUMN column_name;


    3. RENAME COLUMN

                * ALTER TABLE table_name
                  CHANGE COLUMN old_column_name new_column_name column_definition;

                * Example

                            - ALTER TABLE customers CHANGE COLUMN email user_email VARCHAR(255);



   4. MODIFY

                * ALTER TABLE table_name MODIFY COLUMN column_name new_definition;

                * Example

                            - ALTER TABLE employees
                              MODIFY COLUMN salary DECIMAL(10,2)
                              CHECK (salary >= 0 and salary <= 100000);

                 * This will modify the salary column to

                             - change its data type to DECIMAL(10,2)

                             - and add a CHECK constraint to ensure that

                             - the salary value is between 0 and 100000.

                     Note that any indexes or constraints that reference the column

                             - may also need to be modified accordingly.

 */


public class Alter {
}
