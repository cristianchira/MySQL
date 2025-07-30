package _8_Constraints._3_Indexes;

//  https://dev.mysql.com/doc/refman/8.0/en/mysql-indexes.html
// https://dev.mysql.com/doc/search/?d=201&p=1&q=Indexes

//https://dev.mysql.com/doc/refman/8.0/en/create-index.html


/*                                   Index of a table


    ○ the index of a table functions like the index of a book

                   *  data is taken from a column of the table

                   * and is stored in a certain order in a distinct place,

                             called an index.

    ○ we can use an index that

                 * will increase the speed of searches related to a table

    ○ an organizational unit that help retrieve data more easily.

    ○ Of course, this doesn't come without a cost.

                   * It takes more time to update a table

                   * because indexes must be updated, too,

                   * and that's time-consuming.

    ○ Primary and Unique keys are MySQL indexes


                                       SQL Index


    ○ composite index
                  * is applied on multiple columns not only one

    ○ CREATE INDEX index_name
       ON table_name (column_1, column_2, …);

    ○ The parentheses serve as to indicate the column names on which our search will be based.

                   * It will be speeded up and the data will be filtered in a quicker way.

                   * Speaking technically, the idea is to choose columns so your search will be optimized.

                   * These must be fields from your data table.

                   * You will search frequently.




                                                  SQL Example


    ○ create index i_composite  on employees(first_name, last_name);

    ○   drop index i_composite on employees;

         ALTER TABLE employees

         DROP INDEX i_hire_date;

    ○ show index from employees;



                                                CREATE INDEX  FULL Statement


CREATE [UNIQUE | FULLTEXT | SPATIAL] INDEX index_name
    [index_type]
    ON tbl_name (key_part,...)
    [index_option]
    [algorithm_option | lock_option] ...

key_part: {col_name [(length)] | (expr)} [ASC | DESC]

index_option: {
    KEY_BLOCK_SIZE [=] value
  | index_type
  | WITH PARSER parser_name
  | COMMENT 'string'
  | {VISIBLE | INVISIBLE}
  | ENGINE_ATTRIBUTE [=] 'string'
  | SECONDARY_ENGINE_ATTRIBUTE [=] 'string'
}

index_type:
    USING {BTREE | HASH}

algorithm_option:
    ALGORITHM [=] {DEFAULT | INPLACE | COPY}

lock_option:
    LOCK [=] {DEFAULT | NONE | SHARED | EXCLUSIVE}


                                            ADD UNIQUE KEY


           ALTER TABLE table_name
           ADD UNIQUE KEY  (column_name)


                                        DROP UNIQUE KEY


            ALTER TABLE table_name
            DROP index column_name

   will be index instead of Unique Key and column name without parenthesis.

 */

public class Indexes {
}
