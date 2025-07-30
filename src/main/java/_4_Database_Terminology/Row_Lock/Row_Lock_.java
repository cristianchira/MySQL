package _4_Database_Terminology.Row_Lock;


// https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_lock


/*                                                              lock


    ○ The high-level notion of an object that controls access to

                   * a resource, such as

                                 - a table,

                                 - row,

                                 - or internal data structure,

                                 - as part of a locking strategy.


    ○ For intensive performance tuning,

                   * you might delve into the actual structures that implement locks, such as

                                 - mutexes

                                              • https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_mutex

                                              • mutex

                                                Informal abbreviation for “mutex variable”. (Mutex itself is short for
                                                    “mutual exclusion”.) The low-level object that InnoDB uses to represent
                                                     and enforce exclusive-access locks to internal in-memory data structures.
                                                     Once the lock is acquired, any other process, thread, and so on is
                                                     prevented from acquiring the same lock. Contrast with rw-locks,
                                                     which InnoDB uses to represent and enforce shared-access locks to
                                                     internal in-memory data structures. Mutexes and rw-locks are known
                                                     collectively as latches.

                                 - and latches.

                                              • https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_latch

                                              • latch

                                                 A lightweight structure used by InnoDB to implement a lock for its own
                                                     internal memory structures, typically held for a brief time measured
                                                     in milliseconds or microseconds. A general term that includes
                                                     both mutexes (for exclusive access) and rw-locks (for shared access).
                                                     Certain latches are the focus of InnoDB performance tuning. Statistics
                                                     about latch use and contention are available through the
                                                     Performance Schema interface.



 */



public class Row_Lock_ {
}
