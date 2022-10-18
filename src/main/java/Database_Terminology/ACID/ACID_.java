package Database_Terminology.ACID;

// https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_acid


/*                                                ACID


    ○ An acronym standing for

                   * ATOMICITY,

                   * CONSISTENCY,

                   * ISOLATION,

                   * and DURABILITY.

            These properties are all

                   * desirable in a database system,

                   * and are all closely tied to the notion of a transaction.

    ○ The transactional features of InnoDB adhere to the ACID principles.


                                                          Transaction ID


    ○ An internal field associated with each row.

                  * This field is physically changed by

                                - INSERT,

                                 - UPDATE,

                                 - and DELETE

                         operations to record which transaction has locked the row.


                                                      Transactions are


    1. Atomic units of work that can be: ------------------------------------------------------------------------------

                    * committed

                    * or rolled back.

          When a transaction makes multiple changes to the database,

                  * either all the changes succeed when the transaction is Committed,

                  * or all the changes are undone when the transaction is Rolled Back.

    2. The database remains in a CONSISTENT state at all times ---------------------------------------------------------

                  *  after each commit or rollback,

                  * and while transactions are in progress.

          If related data is being updated across multiple tables,

                  * queries see either

                                - all old values

                                - or all new values,

                        not a mix of old and new values.

    3. Transactions are protected (isolated) ---------------------------------------------------------------------------

                  * from each other while they are in progress;

                  * they cannot INTERFERE with each other

                  * or SEE each other's uncommitted data.

          This isolation is achieved through the

                  * LOCKING mechanism.

          Experienced users can ADJUST the isolation level,

                   * trading off less protection

                                 - in favor of increased performance and concurrency,

                                 - when they can be sure that the transactions

                                 - really do not interfere with each other.

    4. The results of transactions are DURABLE:  ----------------------------------------------------------------------

                   * once a commit operation succeeds,

                   * the changes made by that transaction are safe from

                                 - power failures,

                                 - system crashes,

                                 - race conditions,

                                 - or other potential dangers

                         that many non-database applications are vulnerable to.

                                 - Durability typically involves writing to

                                 - disk storage,

                                 - with a certain amount of redundancy to protect against

                                 - power failures

                                 - or software crashes during write operations.

    ○ In InnoDB, the double write buffer assists with durability.








 */

public class ACID_ {
}
