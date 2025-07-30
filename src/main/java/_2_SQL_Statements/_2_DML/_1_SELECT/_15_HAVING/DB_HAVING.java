package _2_SQL_Statements._2_DML._1_SELECT._15_HAVING;

// MArtin G. Having
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345186#overview


/*                                       HAVING


    ▬▬▐ Refines the output from records that do not satisfy a certain condition
                  * frequently implemented with GROUP BY
                  * Having needs to be inserted between the group by and order by clauses.
                  * HAVING is like WHERE but applied to the GROUP BY block

    ○ Syntax
                  * SELECT column_name(s)
                    FROM table_name
                    WHERE conditions (not mandatory)
                    GROUP BY column_name(s)
                        HAVING conditions
                    ORDER BY column_name(s)
                    LIMIT n;


    ○ Example

                   * SELECT first_name, last_name, COUNT(gender) as appearance
                     FROM employees
                     WHERE gender = 'M'
                     GROUP BY first_name, last_name
                     HAVING COUNT(*) > 1
                     ORDER BY first_name, last_name desc
                     limit 12;


                             ▬▬ WHERE vs. HAVING ▬▬


    ○ After HAVING,
                  * you can have a condition with an Aggregate Functions,

    ○ While WHERE
                 * cannot use Aggregate Functions within its conditions


 */

public class DB_HAVING {
}
