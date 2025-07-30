package _2_SQL_Statements._4_TCL.SAVEPOINT;

// https://dev.mysql.com/doc/refman/8.0/en/savepoint.html


/*                           SAVEPOINT, ROLLBACK TO SAVEPOINT, and RELEASE SAVEPOINT Statements


                                                    Syntax

SAVEPOINT identifier
ROLLBACK [WORK] TO [SAVEPOINT] identifier
RELEASE SAVEPOINT identifier

    ○ InnoDB supports the SQL statements

                   * SAVEPOINT,

                   * ROLLBACK TO SAVEPOINT,

                   * RELEASE SAVEPOINT

                   * and the optional WORK keyword for ROLLBACK.

    ○ SAVEPOINT

                   * statement sets a named transaction savepoint with a name of identifier.

                                 - If the current transaction has a savepoint with the same name,

                                 - the old savepoint is deleted and a new one is set.

    ○ ROLLBACK TO SAVEPOINT

                    * statement rolls back a transaction to the named savepoint without terminating the transaction.

                                 - Modifications that the current transaction made to rows

                                 - after the savepoint was set are undone in the rollback,

                                 - but InnoDB does not release the row locks that were stored in memory after the savepoint.

                         (For a new inserted row, the lock information is carried by the transaction ID stored in the row;

                                 - the lock is not separately stored in memory.

                         In this case, the row lock is released in the undo.)

                                  - Savepoints that were set at a later time than the named savepoint are deleted.

    ○ If ROLLBACK TO SAVEPOINT

                     * statement returns the following error,

                     * it means that no savepoint with the specified name exists:

ERROR 1305 (42000): SAVEPOINT identifier does not exist


    ○ RELEASE SAVEPOINT

                    * statement removes the named savepoint from the set of savepoints of the current transaction.

             No commit or rollback occurs.

                     * It is an error if the savepoint does not exist.

    ○ All savepoints of the current transaction are deleted

                     * if you execute

                                   - a COMMIT,

                                   - or a ROLLBACK

                           that does not name a savepoint.

    ○ A new savepoint level is created when

                    * a stored function is invoked

                    * or a trigger is activated.

            The savepoints on previous levels

                    * become unavailable

                    * and thus do not conflict with savepoints on the new level.

             When the function or trigger terminates,

                    * any savepoints it created are released

                    * and the previous savepoint level is restored.





 */

public class SAVEPOINT_ {
}
