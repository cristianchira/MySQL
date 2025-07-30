package _2_SQL_Statements._2_DML._1_SELECT._14_GROUP_BY;

// MArtin G. GRPOP BY
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345180#overview


/*                                       GROUP BY


    ○ SELECT column_name(s)
      FROM table_name
      WHERE conditions
          GROUP BY column_name(s)
       ORDER BY column_name(s);

    ○ Example
      SELECT first_name, count(first_name)
      FROM employees
          GROUP BY first_name
      ORDER BY first_name DESC;

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
                             - in the SELECT statement!

                  * GROUP BY is one of the most powerful and useful tools in SQL

    ○ In most cases, when you need an Aggregate Functions,
                * you must add a GROUP BY clause in your query too.

    ○ Example
                * Assume you need a list composed of two fields.
                            - The first must contain a distinct first name of the employee,
                            - and the second the number of times this name
                                        • is encountered in our database.

                * SELECT
                      first_name, count(first_name) as appearance
                  FROM
                      employees
                  GROUP BY first_name;


 */


public class DB_GRPUP_BY {
}
