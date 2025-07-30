package _7_Data_Types._0_Data_Types_Default_Values;

// Data Type Default Values
//https://dev.mysql.com/doc/refman/8.0/en/data-type-defaults.html


/*                        Data Type Default Values

    ○ Data type specifications can have

                1. explicit

                2. or implicit

        default values.

    ○ A DEFAULT value clause in a data type specification explicitly indicates a default value for a column.
          Examples:

                * CREATE TABLE t1 (
                   i     INT DEFAULT -1,
                   c     VARCHAR(10) DEFAULT '',
                   price DOUBLE(16,2) DEFAULT 0.00
                  );


                                        Default values


    1. INT - 0

    2.FLOAT - 0

    3. DECIMAL - 0

    4.DOUBLE - 0

    5. VARCHAR - ''

    6.CHAR - ''

    7. TEXT - NULL

    8. DATE - NULL

    9. TIME - NULL

    10. DATETIME - NULL

    11. TIMESTAMP - NULL (or current timestamp, depending on the database system)

    12. BLOB - NULL

    13. CLOB - NULL

    14. LONGBLOB - NULL

    15. LONGTEXT - NULL

    16.ENUM - First value listed in the type definition

    17 SET - Empty set (no values)

    18.JSON - NULL

*/

public class Data_Types_Default_Values {
}
