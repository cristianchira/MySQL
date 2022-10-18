package SQL_STATEMENTS.DML.SELECT.GROUP_BY;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345180#overview


/*                                                         GROUP BY


    ○ When working in SQL,

                  * results can be grouped according to a specific

                                - field

                                - or fields

    ○ GROUP BY must be PLACED:

                 * immediately after the WHERE conditions, if any,

                 * and just before the ORDER BY clause

    ○ In most cases, when you need an Aggregate Function,

                 * you must add a GROUP BY clause in your query, too.

    ○ GROUP BY:

                 * Always include the field you have grouped your results by

                 * in the SELECT statement!

    ○ GROUP BY:

                * is one of the most powerful and useful tools in SQL


                                                       GROUP BY    query


    ○ SELECT column_name(s)  FROM table_name

       WHERE conditions

       GROUP BY column_name(s)

       ORDER BY column_name(s);


    ○ Example

      SELECT first_name, count(first_name)

      FROM employees

      GROUP BY first_name

      ORDER BY first_name DESC;





 */


public class GROUP_BY_ {
}
