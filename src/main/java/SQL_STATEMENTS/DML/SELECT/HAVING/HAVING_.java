package SQL_STATEMENTS.DML.SELECT.HAVING;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345186#overview


/*                                                           HAVING


    ○ refines the output from records that do not satisfy a certain condition

                  * frequently implemented with GROUP BY

                  * HAVING is like WHERE but applied to the GROUP BY block


                                                   HAVING query


    ○ SELECT column_name(s)

      FROM table_name

      WHERE conditions

      GROUP BY column_name(s)

      HAVING conditions

      ORDER BY column_name(s);


                                                      WHERE vs. HAVING


    ○ after HAVING,

                  * you can have a condition with an aggregate function,

    ○ while WHERE

                 * cannot use aggregate functions within its conditions


 */

public class HAVING_ {
}
