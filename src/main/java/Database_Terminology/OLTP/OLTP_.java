package Database_Terminology.OLTP;

// https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_oltp


/*                                                                OLTP


    ○ Acronym for “Online Transaction Processing”.

                  * A database system, or a database application,

                  * that runs a workload with many transactions,

                  * with frequent writes as well as reads,

                  * typically affecting small amounts of data at a time.


    ○ For example,

                  * an airline reservation system

                  * or an application that processes bank deposits.

          The data might be organized in normalized form for a balance between

                  * DML (insert/update/delete) efficiency

                  * and query efficiency.

          Contrast with Data Warehouse.

    ○ With its row-level locking and transactional capability,

                 *InnoDB is the ideal storage engine for MySQL tables used in OLTP applications.


 */

public class OLTP_ {
}
