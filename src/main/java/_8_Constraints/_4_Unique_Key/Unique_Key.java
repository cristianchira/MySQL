package _8_Constraints._4_Unique_Key;

// https://dev.mysql.com/doc/refman/8.0/en/partitioning-limitations-partitioning-keys-unique-keys.html


/*                                           Unique key


    ○ Used whenever you would like to specify you

                  * don't want to see duplicate data in a given field.

   ○ Ensured that all values in a column(or a set of columns) are different

                  * unique keys are implemented in sql through a constraint:


   ○  UNIQUE KEY (col1, col3),
      UNIQUE KEY (col2, col4)


                                          ADD Unique Key


    1. ALTER TABLE departments_dup
        ADD UNIQUE(dept_name);

    2. ALTER TABLE departments_dup
       ADD CONSTRAINT UC UNIQUE(dept_name);



                                          DROP Unique key


     1. ALTER TABLE departments_dup
        DROP INDEX [CONSTRAINT] dept_name;
        HISTORICAL: rândul cu [CONSTRAINT] nu este sintaxa pe care serverul o execută. UNIQUE se scoate ca index: ALTER TABLE departments_dup DROP INDEX dept_name.


     2.ALTER TABLE departments_dup
       DROP INDEX [CONSTRAINT] UC;
       HISTORICAL: [CONSTRAINT] nu face parte din DROP INDEX. Propoziția este ALTER TABLE departments_dup DROP INDEX UC.




 */

public class Unique_Key {
}
