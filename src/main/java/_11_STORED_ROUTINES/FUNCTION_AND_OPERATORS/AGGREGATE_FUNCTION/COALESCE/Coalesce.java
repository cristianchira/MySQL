package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION.COALESCE;

// Martin G. IF NULL() and COALESCE()
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8400644#questions

/*                                            Coalesce()


    ○ select
          dept_no, ifnull(dept_name,'Department name not provided') as dept_name
      from

          departments_dup;


    ○ Coalesce(expression_1, expression_2 , expression_n)

                  * can have multiple parameters

                                - think at Coalesce like a IFNULL  with more than 2 parameters


                  * Coalesce will always return a single value

                                - of the ones we have within parentheses

                                - and this value will be the first non-null value of the list

                                - reading the values from the left to right

                  * COALESCE and IFNULL does not make any change TO THE DATA SET

                                - only create an uotput instead null value

    ○ EXAMPLE


                 1. with one parameter

                                SELECT  dept_no,  dept_name,

                                COALESCE('Department manager') as fake_column

                                 FROM  departments_dup;

                  2. with 2 parameters works like IFNULL

                                 SELECT   dept_no,

                                 COALESCE(dept_name, 'Department name not provided') as dept_name

                                 FROM  departments_dup;

                  2. with multiple parameters

                                 SELECT   dept_no, dept_name,

                                 COALESCE(dept_manager,dept_name, 'N/A') as dept_name

                                 FROM  departments_dup;

                             If dept_manager is null will take dept_name and where dept name is null will take third parameter 'N/A'.



    ○ Here we will study something a bit more sophisticated.

                   * IFNULL()

                   * and COALESCE()

           are among the advanced SQL functions in the toolkit of SQL professionals.

    ○ They are used when

                  * null values are dispersed in your data table

                  * and you would like to substitute the null values with another value.

    ○ So, let’s adjust the “Departments” duplicate in a way that suits the purposes of the next video,

                   * in which we will work with

                                 - IF NULL()

                                 - and COALESCE().


                                                    IFNULL()


    ○ IFNULL(expression_1, expression_2 )

                  * return the first of two indicated values

                                - if the data value found in the table is not null

                  * and returns the second value if there is the null value

    ○ IFNULL

                  * works with precisely 2 parameters


    ○ EXAMPLE:

                  * SELECT  dept_no,

                    IFNULL(dept_name,'Department name not provided') as dept_name

                    FROM  departments_dup;


 */


public class Coalesce {
}
