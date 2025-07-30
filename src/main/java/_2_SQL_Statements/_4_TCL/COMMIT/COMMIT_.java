package _2_SQL_Statements._4_TCL.COMMIT;

// https://dev.mysql.com/doc/refman/8.0/en/commit.html


/*                                                       COMMIT statement


    ○ Not every change you make to a database is saved automatically

    ○ The statement to use in such cases is COMMIT.

               * It works only for changes made with the help of the

                             - INSERT,

                              - DELETE,

                              - or UPDATE clauses.

                      COMMIT will save

                              - the changes(transaction) you've made permanently on the database

                              - changes cannot be undone

                              - and will let other users have access to the modified version of the database.

    ○ COMMIT statement

                  * committed states can accrue(accumulate)


                                      These statements provide control over use of transactions:


    1. START TRANSACTION

                  *  or BEGIN start a new transaction.

    2.  COMMIT

                  * commits the current transaction,

                  * making its changes permanent.

    3. ROLLBACK

                  * rolls back the current transaction,

                  * canceling its changes.

    4. SET autocommit

                  * disables

                  * or enables

          the default autocommit mode for the current session.

    ○ By default, MySQL runs with autocommit mode enabled. ------------------------------------------------------------

                  * This means that,

                                - when not otherwise inside a transaction,

                                - each statement is atomic,

                                - as if it were surrounded by START TRANSACTION and COMMIT.

                         You cannot use ROLLBACK to undo the effect;

                                - however, if an error occurs during statement execution,

                                - the statement is rolled back.

    ○ To disable autocommit mode implicitly for a single series of statements,

                   * use the START TRANSACTION statement:

START TRANSACTION;
SELECT @A:=SUM(salary) FROM table1 WHERE type=1;
UPDATE table2 SET summary=@A WHERE type=1;
COMMIT;
With START TRANSACTION,

    ○ autocommit remains disabled until you end the transaction with COMMIT or ROLLBACK.

                    * The autocommit mode then reverts to its previous state.




 */

public class COMMIT_ {
}
