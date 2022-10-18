package JOINs.SELF_JOIN;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8565170#questions/3352670


/*                                          SQL Self Join


    ○ Applied when a table must join itself

                  * if you’d like to combine CERTAIN ROWS of a table

                  * with OTHER ROWS of the same table,

                                - you need a self-join

    ○ Using ALIASES is OBLIGATORY -------------------------------------------------------------------------------------

    ○ These REFERENCES to the original table

                 * let you use different blocks of the available data

    ○ You can either

                 * filter both in the join,

                 * or you can filter

                               - one of them in the WHERE clause,

                               - and the other one – in the join

    ○ In a self-join statement, you will have to comply  with the same

                 * logical

                 * and syntactic structure

    ○ The 2 tables will be identical

                  * to the table you’ll be using in the self-join

    ○ You can think of them as virtual projections

                 * of the underlying, base table

    ○ The self-join will

                 * reference both implied tables

                 * and will treat them as two separate tables in its operations

    ○ The data used will come

                 * from a single source,

                 * which is the underlying table that stores data physically


                                               EXAMPLE


    ○ SELECT
          e1.*
      FROM
          emp_manager e1

        JOIN   emp_manager e2  ON  e1.emp_no = e2.manager_no

      GROUP BY emp_no;


                                               When is it appropriate to use self-joins?


    ○ When a column table is referenced in the same table

 */


public class SELF_JOIN_ {
}
