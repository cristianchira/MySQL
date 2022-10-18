package Constraints.Primary_Key;

// https://dev.mysql.com/doc/refman/8.0/en/constraint-primary-key.html


/*                           Primary Key Constraint


    ○ The PRIMARY KEY constraint uniquely identifies each record in a table.

    ○ Primary keys must contain UNIQUE values, and cannot contain NULL values.

    ○ A table can have only ONE primary key;

                  * and in the table,

                  * this primary key can consist of single or multiple columns (fields).

    ○ Normally, errors occur for data-change statements (such as INSERT or UPDATE)

                  * that would violate

                                - primary-key,

                                - unique-key,

                                - or foreign-key constraints.

    ○ If you are using a

                  * transactional storage engine such as InnoDB,

                                 - MySQL automatically ROLLS BACK the statement.

                  * If you are using a non-transactional storage engine,

                                - MySQL STOPS PROCESSING the statement at the row for which the error occurred

                                - and LEAVES any remaining rows unprocessed.


                                         ADD Primary key


    ○  ALTER TABLE departments_dup
       ADD PRIMARY KEY(dept_no);


                                   DROP Primary key


    ○  ALTER TABLE departments_dup
       DROP PRIMARY KEY(dept_no);


 */


public class Primary_Key {
}
