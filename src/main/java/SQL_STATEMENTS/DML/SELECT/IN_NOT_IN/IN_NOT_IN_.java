package SQL_STATEMENTS.DML.SELECT.IN_NOT_IN;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345098#overview


/*                                                         IN   NOT IN


    ○ IN operator allows SQL to

                    * RETURN the names written in parentheses

                                  - if they exist in our table.


    ○ IN structure

                   * is quicker than

                   *  the OR structure.

    ○ when many conditions are applied in a query

                  * IN operator is preferred


                                         IN  query


    ○ SELECT * FROM employees
       WHERE first_name
       IN ('Cathie' , 'Mark', 'Nathan');


                                             NOT IN   query


 ○ SELECT * FROM employees
       WHERE first_name
       NOT IN ('Cathie' , 'Mark', 'Nathan');



 */

public class IN_NOT_IN_ {
}
