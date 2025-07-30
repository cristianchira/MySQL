package _12_DB_Optimization.INDEX_OPTIMIZATION;

// https://dev.mysql.com/doc/refman/8.0/en/create-index.html

// https://dev.mysql.com/doc/refman/8.0/en/mysql-indexes.html



/*                                           INDEX


    ▬▬▐ Index of a table:
                 * an organizational unit that helps retrieve data more easily
                 * it takes more time to update a table because indexes must be updated, too,
                 * and that's time-consuming.


                             ▬▬ How MySQL Uses Indexes ▬▬


    ○ Indexes are used to
                   * find rows with specific column values quickly.
           Without an index, MySQL must begin with the first row
                   * and then read through the entire table to find the relevant rows.
           The larger the table, the more this costs.
                   * If the table has an index for the columns in question,
                   * MySQL can quickly determine the position to seek to in the middle of the data file
                   * without having to look at all the data.
           This is much faster than reading every row sequentially.



                              ▬▬ Optimization and Indexes ▬▬


    1. How MySQL Uses Indexes
                   * https://dev.mysql.com/doc/refman/8.0/en/mysql-indexes.html

    2. Primary Key Optimization
                   * https://dev.mysql.com/doc/refman/8.0/en/primary-key-optimization.html

    3. SPATIAL Index Optimization
                   * https://dev.mysql.com/doc/refman/8.0/en/spatial-index-optimization.html

    4. Foreign Key Optimization
                   * https://dev.mysql.com/doc/refman/8.0/en/foreign-key-optimization.html

    5. Column Indexes
                   * https://dev.mysql.com/doc/refman/8.0/en/column-indexes.html

    6. Multiple-Column Indexes
                   * https://dev.mysql.com/doc/refman/8.0/en/multiple-column-indexes.html

    7. Verifying Index Usage
                   * https://dev.mysql.com/doc/refman/8.0/en/verifying-index-usage.html

    8. InnoDB and MyISAM Index Statistics Collection
                   * https://dev.mysql.com/doc/refman/8.0/en/index-statistics.html

    9. Comparison of B-Tree and Hash Indexes
                   * https://dev.mysql.com/doc/refman/8.0/en/index-btree-hash.html

     10. Use of Index Extensions
                   * https://dev.mysql.com/doc/refman/8.0/en/index-extensions.html

    11. Optimizer Use of Generated Column Indexes

                   * https://dev.mysql.com/doc/refman/8.0/en/generated-column-index-optimizations.html

    12. Invisible Indexes

                   * https://dev.mysql.com/doc/refman/8.0/en/invisible-indexes.html

    13. Descending Indexes

                   * https://dev.mysql.com/doc/refman/8.0/en/descending-indexes.html

    14. Indexed Lookups from TIMESTAMP Columns

                   * https://dev.mysql.com/doc/refman/8.0/en/timestamp-lookups.html


                                  ▬▬ Index are stored in: ▬▬


    ○  https://dev.mysql.com/doc/refman/8.0/en/index-btree-hash.html


    ○ B-trees             -- https://dev.mysql.com/doc/refman/8.0/en/index-btree-hash.html#btree-index-characteristics

                  * PRIMARY KEY,
                  * UNIQUE,
                  * INDEX,
                  * and FULLTEXT

    ○ Hash index

                  * https://dev.mysql.com/doc/refman/8.0/en/index-btree-hash.html#hash-index-characteristics


 */

public class Index_Optimization {
}
