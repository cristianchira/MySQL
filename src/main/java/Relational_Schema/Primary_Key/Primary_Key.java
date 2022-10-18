package Relational_Schema.Primary_Key;

// https://dev.mysql.com/doc/refman/5.7/en/primary-key-optimization.html

/*                                                Primary key

    ○ a column(or a set of column)

                  * whose value exists and is unique for every record in a table.

    ○ Each table can have one and only one primary key in one table,

                   * you cannot have three or four primary keys.

    ○ Primary keys are the unique identifier of a table

    ○ Primary key

                   * may be composed of a set of columns(ex two columns)

   ○ Another crucial feature of primary keys is

                   * they cannot contain null values

   ○ Not all the table you will work with will have the primary key


                                             Primary Key Optimization


    The primary key for a table represents

        the column or set of columns that you use in your most vital queries.
        It has an associated index, for fast query performance.
         Query performance benefits from the NOT NULL optimization, because it cannot include any NULL values. With the
         InnoDB storage engine, the table data is physically organized

             to do ultra-fast lookups and sorts based on the primary key column or columns.

    If your table is big and important, but does not have an obvious column or set of columns to use as a primary key,

        you might create a

            separate column with auto-increment values to use as the primary key.

        These unique IDs can serve as pointers to corresponding rows in other tables when you join tables using foreign keys.

 */


public class Primary_Key {
}
