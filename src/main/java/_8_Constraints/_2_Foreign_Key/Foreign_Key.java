package _8_Constraints._2_Foreign_Key;

// https://dev.mysql.com/doc/refman/8.0/en/create-table-foreign-keys.html


/*                                FOREIGN KEY Constraints


    ○ which permit cross-referencing related data across tables.(Points to a column of another table so links the 2 table)

    ○ A foreign key relationship involves a

                   * parent table

                                 - that holds the initial column values,

                   * and a child table

                                 - with column values that reference the parent column values.

                    * a foreign key constraint is defined on the child table.

                    * a foreign key in SQL is defined through a foreign key constraint


    ○ A type of pointer relationship, between rows in separate InnoDB tables.

                    * The foreign key relationship is defined on one column in both

                                 - the parent table

                                 - and the child table.

    ○ In addition to enabling fast lookup of related information,

                   * foreign keys help to enforce referential integrity,

                   * by preventing any of these pointers from becoming invalid as data is

                                 - inserted,

                                 - updated,

                                 - and deleted.

                         This enforcement mechanism is a type of constraint.

    ○ A row that points to another table cannot be inserted

                   * if the associated foreign key value does not exist in the other table.

           If a row

                   * is deleted

                   * or its foreign key value changed,

                   * and rows in another table point to that foreign key value,

                   * the foreign key can be set up to prevent the deletion,

                   * cause the corresponding column values in the other table to

                                 - become null,

                                 - or automatically delete the corresponding rows in the other table.


[CONSTRAINT [symbol]] FOREIGN KEY

    which help keep the related data consistent.(maintains the referential integrity within the database)

    [index_name] (col_name, ...)
    REFERENCES tbl_name (col_name,...)
    [ON DELETE reference_option]
    [ON UPDATE reference_option]

reference_option:
    RESTRICT | CASCADE | SET NULL | NO ACTION | SET DEFAULT

    child table(where is defined FK)   = referencing table

    parent table = referenced table




 */

public class Foreign_Key {
}
