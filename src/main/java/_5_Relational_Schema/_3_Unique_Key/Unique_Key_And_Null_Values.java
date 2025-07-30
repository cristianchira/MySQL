package _5_Relational_Schema._3_Unique_Key;

// Martin G. Unique Key
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338068#questions


// PRIMARY KEY and UNIQUE Index Constraints
// https://dev.mysql.com/doc/refman/8.0/en/constraint-primary-key.html


/*                                           Unique Key


    1. Create unique key on existing table

                *  ALTER TABLE table_name
                   ADD UNIQUE KEY index_name (column_name);

    2. When create new table:

                * with declared name   -- best practice

                            - CREATE TABLE users (
                              id INT PRIMARY KEY,
                              username VARCHAR(255),
                              email_address VARCHAR(255),
                              UNIQUE KEY unique_email_address (email_address)
                              );

                 * without declared name  -- MySQL will automatically generate a unique index name

                            - CREATE TABLE users (
                              id INT PRIMARY KEY,
                              username VARCHAR(255),
                              email_address VARCHAR(255),
                              UNIQUE (email_address)
                            );



    ○ used whenever you would like to specify that you

                  * don't want to see duplicate data in a given field

    ○ can contain null values    --------------------------------------------------------------------

    ○ can be several unique key in a table(relational schema)

    ○ one unique key can be applied to

                  * one column

                  * multiple columns


                                              Unique constraint


    ○  CONSTRAINT unique_email_address UNIQUE (`email_address`)

    ○ Unique keys are implemented on SQL

                  * through a constraint

                                - UNIQUE constraint

    ○ If you attempt to insert an already existing, duplicate value in the unique column,

                   * SQL will display an error




*/


public class Unique_Key_And_Null_Values {
}
