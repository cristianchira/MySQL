package Relational_Schema.Unique_Key;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338068#questions

// https://dev.mysql.com/doc/refman/8.0/en/constraint-primary-key.html


/*                                           Unique Key


    ○ used whenever you would like to specify that you

                  * don't want to see duplicate data in a given field

    ○ can contain null values

    ○ can be several unique key in a table(relational schema)

    ○ one unique key can be applied to

                  * one column

                  * multiple columns


                                              Unique constraint


    ○ UNIQUE KEY (`email_address`)

    ○ Unique keys are implemented on SQL

                  * through a constraint

                                - UNIQUE constraint

    ○ If you attempt to insert an already existing, duplicate value in the unique column,

                   * SQL will display an error


                                                      Full example




*/


public class Unique_Key_And_Null_Values {
}
