package SQL_STATEMENTS.DML.SELECT.LIKE_NOT_LIKE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345106#overview


/*                                                      LIKE


    ○ Sometimes you will need to look for a specific pattern in a column.

    ○ Then use the LIKE operator.

    ○ It is succeeded by parentheses

                   * within which the pattern must be indicated.

    ○ That's why the implemented technique is called

                  * pattern matching.


   ○ all search are case-insensitive


                                                 LIKE  query   %


    ○ The percentage sign is necessary for matching a sequence of characters.

     1. SELECT * FROM employees  WHERE first_name

        LIKE ('Dek%');

     2. LIKE ('%sr%');

     3. LIKE ('%sr);

                                                  LIKE  query   _


    ○ underscore helps you

                  * match a single character.

    ○ If you would like to fetch all names,

                  * starting with Mar

                  *and written with four letters,

          you would type:


    SELECT * FROM employees  WHERE first_name

        LIKE ('Mar_');


                                                         NOT LIKE


    ○ is just opposite to LIKE

 */

public class LIKE_ {
}
