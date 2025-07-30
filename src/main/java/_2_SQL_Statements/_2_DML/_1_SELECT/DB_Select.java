package _2_SQL_Statements._2_DML._1_SELECT;

//  https://dev.mysql.com/doc/refman/8.0/en/select.html

/*                                       SELECT Statement


    ▬▬▐ Allows you to extract a fraction of the entire data set
                   * used to retrieve data from database objects, like tables
                   * used to query data from a database


                                    Basic select statement


    1. SELECT column_1, column_2, … column_n
       FROM table_name;

    2. SELECT * FROM table_name


                                       COMPLETE SELECT STATEMENT


                      SELECT
    [ALL | DISTINCT | DISTINCTROW ]
    [HIGH_PRIORITY]
    [STRAIGHT_JOIN]
    [SQL_SMALL_RESULT] [SQL_BIG_RESULT] [SQL_BUFFER_RESULT]
    [SQL_NO_CACHE] [SQL_CALC_FOUND_ROWS]
    select_expr [, select_expr] ...
    [into_option]

    [FROM table_references
      [PARTITION partition_list]]

    [WHERE where_condition]

    [GROUP BY {col_name | expr | position}, ... [WITH ROLLUP]]

    [HAVING where_condition]

    [WINDOW window_name AS (window_spec)
        [, window_name AS (window_spec)] ...]

    [ORDER BY {col_name | expr | position}
      [ASC | DESC], ... [WITH ROLLUP]]

    [LIMIT {[offset,] row_count | row_count OFFSET offset}]

    [into_option]
    [FOR {UPDATE | SHARE}
        [OF tbl_name [, tbl_name] ...]
        [NOWAIT | SKIP LOCKED]
      | LOCK IN SHARE MODE]
    [into_option]

into_option: {
    INTO OUTFILE 'file_name'
        [CHARACTER SET charset_name]
        export_options
  | INTO DUMPFILE 'file_name'
  | INTO var_name [, var_name] ...
}





*/

public class DB_Select {
}
