package _2_SQL_Statements._4_TCL.ROLLBACK;

// https://dev.mysql.com/doc/refman/8.0/en/commit.html


/*                                                  ROLLBACK statement


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

    ○ ROLLBACK will REVERT:

                  * until the last COMMITTED state,

                  * and all the changes  made in the meantime will be REMOVED.

    ○ There can be multiple statements executed,

                  * and they will all be undone.

 */


public class ROLLBACK_ {
}
