package _2_SQL_Statements._2_DML._1_SELECT._6_IN_NOT_IN;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345098#overview


/*                                    IN NOT IN

                                 ▬▬ IN query ▬▬


    ▬▬▐ IN operator in SQL can be used to
                * replace multiple OR operators when checking if
                            - a column's value matches
                            - any value in a given set.

    ○ SELECT * FROM employees
       WHERE first_name
       IN ('Cathie' , 'Mark', 'Nathan');


                                          ▬▬ NOT IN query ▬▬


    ○ SELECT * FROM employees
       WHERE first_name
       NOT IN ('Cathie' , 'Mark', 'Nathan');



                                         ▬▬ NOTE ▬▬


    ○ IN structure
                   * is quicker than
                   *  the OR structure.
           When applied to larger data sets,
                   * it will grow(differences between execution time)
           Minimizing execution time on query
                   * is a primary task for SQL professionals.

    ○ when many conditions are applied in a query
                  * IN operator is preferred


 */

public class DB_IN_NOT_IN {
}
