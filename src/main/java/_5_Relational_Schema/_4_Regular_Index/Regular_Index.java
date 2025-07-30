package _5_Relational_Schema._4_Regular_Index;

/*                     A non-unique index (also called a Regular Index)


                                Ways to create Regular Index


    1. Create a Regular Index key on an existing table

                * CREATE INDEX email_index ON users (email_address);

    2. Create a regular index key on a new table using index or key:

                1.  Using the CREATE INDEX statement

                            - CREATE TABLE table_name (
                              -- Column definitions
                              INDEX index_name (column_name)
                              );


                2. Using the KEY index

                            - CREATE TABLE table_name (
                              -- Column definitions
                              KEY index_name (column_name)
                              );

    ○ is an index created on one or more columns in a table

                * without enforcing uniqueness for the indexed columns.

        It means that multiple rows in the table

                * can have the same values in the indexed columns.

        The primary purpose of a non-unique index is to

                * improve the performance of queries that involve the indexed columns,

                            - such as searching,

                            - filtering,

                            - joining,

                            - or sorting operations.

         In the example provided earlier, a non-unique index is defined

                * on the customer_id column of the sales table:

                            - KEY `customer_id` (`customer_id`),

                * This index will help the database management system

                             - quickly find and retrieve rows in the sales table

                             - that have specific customer_id values

                             - or are involved in join operations with another table

                                         • on the customer_id column.

                 * Although the index allows duplicate customer_id values in the sales table,

                              - it still significantly improves the performance of relevant queries.


                                                        NOTE


    ○ Keep in mind that

                * while indexes can speed up query performance,

                * they also have some trade-offs.

        They

                 * increase the storage space required for the table

                 * and can slow down

                             - insert,

                             - update,

                             - or delete

                 operations because the index

                             - needs to be updated when the table data changes.

        It's essential to create indexes

                  * judiciously

         and focus on columns that are

                  * frequently used in queries to balance performance benefits and overheads.

 */

public class Regular_Index {
}
