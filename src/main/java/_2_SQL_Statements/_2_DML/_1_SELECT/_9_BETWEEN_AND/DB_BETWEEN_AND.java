package _2_SQL_Statements._2_DML._1_SELECT._9_BETWEEN_AND;

// Martin G. BETWEEN... AND
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345114#overview


/*                                            BETWEEN ... AND


    ○ Helps us designate the interval to which a given value belongs
                  * Not used only for date values
                  * It could also be applied to strings and numbers
                  * It is always used in combination with the AND operator.

    ○ SELECT * FROM employees
      WHERE hire_date
      BETWEEN '1990-01-01' AND '2000-01-01';

                  * '1990-01-01' AND '2000-01-01'
                                - WILL be included in the retrieved list of records


                                          ▬▬ NOT BETWEEN… AND… ▬▬


    ○ Will refer to an interval composed of two parts:
                  * an interval below the first value indicated
                  * a second interval above the second value


    ○ SELECT * FROM employees
       WHERE hire_date
       NOT BETWEEN '1990-01-01' AND '2000-01-01';

                 * '1990-01-01' AND '2000-01-01’
                                - ARE NOT included in the intervals


 */



public class DB_BETWEEN_AND {
}
