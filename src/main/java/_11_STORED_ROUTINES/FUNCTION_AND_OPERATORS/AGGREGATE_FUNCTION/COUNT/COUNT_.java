package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION.COUNT;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8376506#questions

// https://dev.mysql.com/doc/refman/8.0/en/counting-rows.html



/*                                         COUNT()   -- aggregate function


    ○ SELECT COUNT(column_name)
      FROM table_name;


    ○ they gather data from many rows of a table,

                    * then aggregate it into a single value

                    * applicable to both numeric and non-numeric data

                    * you shouldn’t leave white space before opening the parentheses

                     * They ignore null values unless told not to.


                    INPUT                                     OUTPUT

   ○ the information contained                   ○ the single value they provide
          in multiple rows


                          COUNT(DISTINCT column_name)


    ○ helps us find the number of times unique values

                   * are encountered in a given column


                                          COUNT(*)


    ○ Returns the number of all rows of the table,

                  * values NULL included


                                   Why do these functions exist?


    ○ they are a response to the information requirements of a

                   * company’s different organizational levels

                   * top management executives are typically interested in

                                 - summarized figures and rarely in detailed data



                                                  SQL


    ○ SELECT species, sex, COUNT(*) FROM pet
       WHERE species = 'dog' OR species = 'cat'
       GROUP BY species, sex;




 */


public class COUNT_ {
}
