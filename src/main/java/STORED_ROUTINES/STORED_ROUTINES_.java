package STORED_ROUTINES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692222#questions



/*                                                     STORED ROUTINES


    ○ Routine (in a context other than computer science)

                  * a usual, fixed action,

                  * or series of actions, repeated periodically


    ○ Stored Routine


                  * an SQL statement,

                  * or a set of SQL statements,

          that can be STORED on the database server

    ○ Whenever a user needs to run the query in question, they can

                  * call,

                  * reference,

                  * or invoke the routine

    ○ This routine can bring the desired result multiple times


                                      Stored routines


         Stored procedures                                                    Functions

             = procedures                                                 = user-defined functions
                                                                           ≠ built-in functions
                                                                           (aggregate functions,
                                                                            datetime functions)


                                        TECHNICAL DIFFERENCES


           Stored PROCEDURE                                                User-defined FUNCTION


       ○ Does not return a                                             ○ Returns a value.
            value.


       ○ CALL procedure;                                               ○ SELECT function;


       ○ Including a PROCEDURE in a SELECT                             ○ You can easily include a function
              statement is impossible                                      as one of the columns inside a SELECT
                                                                              statement.

                                                                                       - set @v_emp_no=10001;
                                                                                         SELECT
                                                                                             e.emp_no,
                                                                                             e.first_name,
                                                                                             e.last_name,
                                                                                             F_EMP_AVG_SALARY(@V_emp_no) AS avg_salaru
                                                                                         FROM
                                                                                             employees e
                                                                                         WHERE
                                                                                              @v_emp_no = e.emp_no;




                                         CONCEPTUAL DIFFERENCES


       ○ Can have multiple OUT                                        ○ can return a single value only
             parameters.


       ○  Can INSERT UPDATE DELETE                                   ○ Shouldn't br use to INSERT UPDATE DELETE
                                                                        it should return some value.

       ○ If you need to obtain MORE THAN ONE VALUE as a result of a calculation,

                    * you are better off using a PROCEDURE.

       ○ If you need to just ONE VALUE to be returned,

                    * then you can use a FUNCTION


 */
public class STORED_ROUTINES_ {
}
