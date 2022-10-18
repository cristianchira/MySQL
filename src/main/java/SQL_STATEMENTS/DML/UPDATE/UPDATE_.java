package SQL_STATEMENTS.DML.UPDATE;

// https://dev.mysql.com/doc/refman/8.0/en/update.html


/*                                              UPDATE Statement



    ○ UPDATE is a DML statement

                   * that modifies rows in a table.

                   * we do not have to update each value of the record of interest

                   * we can still say we have updated the specific record

                   * if you don’t provide a WHERE condition,

                                 - ALL rows of the table will be UPDATED


    ○ An UPDATE statement can start with a

                   * WITH clause to define common table expressions accessible within the UPDATE.

                   * https://dev.mysql.com/doc/refman/8.0/en/with.html


                                                 Complete UPDATE STATEMENT


UPDATE [LOW_PRIORITY] [IGNORE] table_reference
    SET assignment_list
    [WHERE where_condition]
    [ORDER BY ...]
    [LIMIT row_count]

value:
    {expr | DEFAULT}

assignment:
    col_name = value

assignment_list:
    assignment [, assignment] ...


                                                            SQL


 ○ UPDATE sales
   SET date_of_purchase = '2019-12-12'
   WHERE purchase_number=1;


 */


public class UPDATE_ {
}
