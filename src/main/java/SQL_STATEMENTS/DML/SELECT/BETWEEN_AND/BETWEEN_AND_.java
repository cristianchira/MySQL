package SQL_STATEMENTS.DML.SELECT.BETWEEN_AND;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345114#overview


/*                                                      BETWEEN ... AND


    ○ Helps us designate the interval to which a given value belongs

                  *  not used only for date values

                  * could also be applied to strings and numbers


    ○ SELECT * FROM employees

      WHERE hire_date

      BETWEEN '1990-01-01' AND '2000-01-01';


                  * '1990-01-01' AND '2000-01-01'

                                - WILL be included in the retrieved list of records


                                                  NOT BETWEEN… AND…

    ○ Will refer to an interval composed of two parts:

                  * an interval below the first value indicated

                  * a second interval above the second value


    ○ SELECT * FROM employees
       WHERE hire_date
       NOT BETWEEN '1990-01-01' AND '2000-01-01';

                 * '1990-01-01' AND '2000-01-01’

                                - ARE NOT included in the intervals


 */



public class BETWEEN_AND_ {
}
