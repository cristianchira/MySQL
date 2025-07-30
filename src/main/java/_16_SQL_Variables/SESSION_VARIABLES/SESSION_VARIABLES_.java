package _16_SQL_Variables.SESSION_VARIABLES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8745698#questions


/*                                                Session Variables


    ○ A variable that exists only for

                  * the SESSION in which you are operating

    ○ It is defined on our server,

                  * and it lives there

                  * it is visible to the connection being used only


                                                      Session


    ○ A series of

                  * information exchange interactions,

                  * or a dialogue, between

                                - a computer

                                - and a user

    ○ A SESSION

                  * BEGINS at a certain point in time

                                - a session comes out because of a connection that has been made successfully.

                  * TERMINATES at another,later point.

                                - once you close the session tab,

                                - you will end the established connection

                                - and terminate the MySQL session.


    ○ If there are 100 users connected to the server at the same time,

                  * this means there will be 100 connections in 100 sessions,

                                - no more and no less.

    ○ However, you will be the only one who can see

                  * the variable you have created in your session.


    ○ EXAMPLE

                 * e.g. a dialogue between the MySQL server

                 * and a client application like MySQL Workbench


                                                     SQL


    ○ SET @var_name = value;

 */

public class SESSION_VARIABLES_ {
}
