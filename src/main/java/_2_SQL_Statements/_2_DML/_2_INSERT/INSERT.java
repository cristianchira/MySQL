package _2_SQL_Statements._2_DML._2_INSERT;

// https://dev.mysql.com/doc/refman/8.0/en/insert.html


/*                                           Insert Statement


    ○ is used to insert data in TABLES

    ○ INSERT

                  * inserts new rows into an existing table.


                                          Insert statements types


    1.INSERT ... VALUES:

                * This is the most basic form of the INSERT statement.

                * It inserts a single row of data into a table, specifying the values for each column.

                            - INSERT INTO table_name (column1, column2, column3)
                               VALUES (value1, value2, value3);

    2. INSERT ... VALUES ROW():

                 * This is a variation of the INSERT statement,

                 * which allows you to insert multiple rows of data into a table in a single query.

                 * The ROW() function is used to specify each row's values.

                              - INSERT INTO table_name (column1, column2, column3)
                                VALUES ROW(value1, value2, value3), ROW(value4, value5, value6);

         Note that not all database systems support this syntax.

                  * In some databases, you can simply use multiple VALUES without the ROW() function:

                              - INSERT INTO table_name (column1, column2, column3)
                                VALUES (value1, value2, value3), (value4, value5, value6);
    3.INSERT ... SET:

                 * This variation of the INSERT statement allows you to insert a single row of data

                 * into a table by setting the values for each column in a more verbose syntax.
                       It's often used in MySQL.

                              - INSERT INTO table_name SET column1 = value1, column2 = value2, column3 = value3;

    4.INSERT ... SELECT:

                  * This form of the INSERT statement allows you to insert data into a table based on

                  * the results of a SELECT query.

          This is useful when you want to

                  * copy data from one table to another

                  * or insert data based on some condition.

                              - INSERT INTO table_name (column1, column2, column3)
                                SELECT column1, column2, column3 FROM source_table WHERE some_condition;

           This query

                  * will insert the rows returned by the SELECT statement

                  * into the target table (table_name).

                   * https://dev.mysql.com/doc/refman/8.0/en/insert-select.html


    5.  INSERT ... TABLE

                  * in MySQL 8.0.19 and later to insert rows from a single table.

    6. INSERT with an ON DUPLICATE KEY UPDATE

                 * https://dev.mysql.com/doc/refman/8.0/en/insert-on-duplicate.html

                 * This MySQL-specific extension allows you to update existing rows
                       if the insertion of a new row would cause a duplicate value in
                       a UNIQUE index or PRIMARY KEY.

         In MySQL 8.0.19 and later,

                 * you can use a row alias with optional column aliases to refer to
                       the row being inserted with ON DUPLICATE KEY UPDATE.

                             - INSERT INTO table_name (column1, column2, ...)
                               VALUES (value1, value2, ...)
                               ON DUPLICATE KEY UPDATE column1 = new_value1, column2 = new_value2, ...;


    7. DELAYED   -- deprecated

                 *  keyword is accepted but ignored by the server.

                 *  https://dev.mysql.com/doc/refman/8.0/en/insert-delayed.html


                                            Complete insert statements


                INSERT [LOW_PRIORITY | DELAYED | HIGH_PRIORITY] [IGNORE]

    [INTO] tbl_name

    [PARTITION (partition_name [, partition_name] ...)]
    [(col_name [, col_name] ...)]

    { {VALUES | VALUE} (value_list) [, (value_list)] ... }

    [AS row_alias[(col_alias [, col_alias] ...)]]

    [ON DUPLICATE KEY UPDATE assignment_list]

INSERT [LOW_PRIORITY | DELAYED | HIGH_PRIORITY] [IGNORE]

    [INTO] tbl_name

    [PARTITION (partition_name [, partition_name] ...)]
    SET assignment_list
    [AS row_alias[(col_alias [, col_alias] ...)]]
    [ON DUPLICATE KEY UPDATE assignment_list]

INSERT [LOW_PRIORITY | HIGH_PRIORITY] [IGNORE]
    [INTO] tbl_name

    [PARTITION (partition_name [, partition_name] ...)]
    [(col_name [, col_name] ...)]
    { SELECT ...
      | TABLE table_name
      | VALUES row_constructor_list
    }
    [ON DUPLICATE KEY UPDATE assignment_list]

value:
    {expr | DEFAULT}

value_list:
    value [, value] ...

row_constructor_list:
    ROW(value_list)[, ROW(value_list)][, ...]

assignment:
    col_name =
          value
        | [row_alias.]col_name
        | [tbl_name.]col_name
        | [row_alias.]col_alias

assignment_list:
    assignment [, assignment] ...




 */

public class INSERT {
}
