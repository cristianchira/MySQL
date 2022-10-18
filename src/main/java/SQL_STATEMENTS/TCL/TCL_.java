package SQL_STATEMENTS.TCL;

// https://dev.mysql.com/doc/refman/8.0/en/sql-transactional-statements.html


/*                                     Transaction Control Language


    ○ Not every change you make to a database is saved automatically

    ○ The statement to use in such cases is COMMIT.

               * It works only for changes made with the help of the

                             - INSERT,

                              - DELETE,

                              - or UPDATE clauses.

                      COMMIT will save

                              - the changes you've made permanently on the database

                              - and will let other users have access to the modified version of the database.

    ○ COMMIT statement

                  * committed states can accrue(accumulate)

    ○ ROLLBACK clause

                   * the clause that will let you to

                                 - undo any changes you have made

                                 - but don't want to be saved permanently.

                          To apply this command,

                                 - type ROLLBACK;

 ○ The clause that will let you to

                   * undo any changes you have made

                   * but don't want to be saved permanently.

           To apply this command,

                    * type ROLLBACK;

    ○ Example ---------------------------------------------------------------------------------------------------------

           UPDATE customers
           SET last_name = 'Johnson'
           WHERE customer_id =4
           COMMIT;

           ROLLBACK;

    ○ ROLLBACK will revert until  the last committed state,

                  * and all the changes  made in the meantime will be REMOVED.

    ○ There can be multiple statements executed,

                  * and they will all be undone.


                               Transactional and Locking Statements


    1. START TRANSACTION, COMMIT, and ROLLBACK Statements

                * https://dev.mysql.com/doc/refman/8.0/en/commit.html

    2. Statements That Cannot Be Rolled Back

                * https://dev.mysql.com/doc/refman/8.0/en/cannot-roll-back.html

    3. Statements That Cause an Implicit Commit

                * https://dev.mysql.com/doc/refman/8.0/en/implicit-commit.html

    4. SAVEPOINT, ROLLBACK TO SAVEPOINT, and RELEASE SAVEPOINT Statements

                * https://dev.mysql.com/doc/refman/8.0/en/savepoint.html

    5. LOCK INSTANCE FOR BACKUP and UNLOCK INSTANCE Statements

               * https://dev.mysql.com/doc/refman/8.0/en/lock-instance-for-backup.html

    6. LOCK TABLES and UNLOCK TABLES Statements

               * https://dev.mysql.com/doc/refman/8.0/en/lock-tables.html

    7. SET TRANSACTION Statement

              * https://dev.mysql.com/doc/refman/8.0/en/set-transaction.html

    8. XA Transactions

              * https://dev.mysql.com/doc/refman/8.0/en/xa.html



 */

public class TCL_ {
}
