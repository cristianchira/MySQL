package SQL_STATEMENTS.DML.INSERT;

// https://dev.mysql.com/doc/refman/8.0/en/insert.html


/*                                                    Insert Statement


    ○ is used to insert data in TABLES

    ○ INSERT

                  * inserts new rows into an existing table.


                                          Insert statements types


    1. INSERT ... VALUES

    2. INSERT ... VALUES ROW()

    3. INSERT ... SET

    4. INSERT ...SELECT

                   * https://dev.mysql.com/doc/refman/8.0/en/insert-select.html

                   * form inserts rows selected from another table or tables


    5.  INSERT ... TABLE

                  * in MySQL 8.0.19 and later to insert rows from a single table.

    6. INSERT with an ON DUPLICATE KEY UPDATE

                * https://dev.mysql.com/doc/refman/8.0/en/insert-on-duplicate.html

                 * clause enables existing rows to be updated

                 * if a row to be inserted would cause a duplicate value in a UNIQUE index or PRIMARY KEY.

         In MySQL 8.0.19 and later,

                 * a row alias with one or more optional column aliases can be used with ON DUPLICATE KEY UPDATE

                 * to refer to the row to be inserted.

    7. DELAYED

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

public class Insert_ {
}
