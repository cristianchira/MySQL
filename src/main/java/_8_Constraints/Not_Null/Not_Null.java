package _8_Constraints.Not_Null;

//Martin G. NOT NULL
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8400660#questions

//NULL Values
// https://dev.mysql.com/doc/refman/8.0/en/null-values.html


/*                                            not null


    ○ Null value is as a missing value

                  * 0 or NONE

                                - is assigned by user

                 * NULL

                               - is assigned by computer

    ○ doesn't accept null data value

    ○ the not null restriction is applied

                  * through the NOT NULL Constraint

    ○ when you insert values in the table

                  * you cannot leave the respective field empty

    ○ If yuo leave it empty

                 * MySql will signal an error


    (
    ...
    company_name varchar(255) not null,
    ...
    )

                                      ADD  not null


    ○ ALTER TABLE companies
       CHANGE COLUMN company_name company_name varchar(255) NOT NULL;


    ○ ALTER TABLE companies
       MODIFY company_name varchar(255) NOT NULL;


                                  DROP  not null


    ○ ALTER TABLE companies
       CHANGE COLUMN company_name company_name varchar(255) NULL;

  ○ ALTER TABLE companies
     MODIFY company_name varchar(255) NULL;



 */

public class Not_Null {
}
