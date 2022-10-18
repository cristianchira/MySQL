package SQL_STATEMENTS.DML.DELETE;

// https://dev.mysql.com/doc/refman/8.0/en/delete.html


/*                                                  DELETE Statement


   ○ DELETE is a DML statement that

                  * remove rows from a table.

    ○ functions similarly with truncate

                   * but unlike TRUNCATE where delete all records

                   * with DELETE

                                 - you can specify precisely what you would like to be removed



    ○ A DELETE statement can start with a WITH clause to define common table expressions accessible within the DELETE.

                   * See Section 13.2.15, “WITH (Common Table Expressions)”.



                                                              SQL


    ○ DELETE FROM table_name
       WHERE conditions;

    ○ ON DELETE CASCADE

                   * if a specific value from the parent table’s primary key has been deleted,

                   * all the records from the child table referring to this value

                                 * will be removed as well




                                            Single-Table Syntax


DELETE [LOW_PRIORITY] [QUICK] [IGNORE] FROM tbl_name [[AS] tbl_alias]
    [PARTITION (partition_name [, partition_name] ...)]
    [WHERE where_condition]
    [ORDER BY ...]
    [LIMIT row_count]





 */


public class Delete_ {
}
