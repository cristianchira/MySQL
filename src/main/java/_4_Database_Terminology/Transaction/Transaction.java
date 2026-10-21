package _4_Database_Terminology.Transaction;

// https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_transaction


/*                                   Database Transactions


    ▬▬▐ A database transaction is a sequence of one or more operations executed as a
                * single,
                * logical
         unit of work.These operations can include
                * reading from,
                * writing to,
                * updating,
                * or deleting
        data within the database.

    ○ Transactions are a crucial concept in database management systems, as they ensure
                * data consistency,
                * integrity,
                * and reliability
        in the face of
                * concurrent access,
                * system failures, or other unexpected events.


                ▬▬ Database transactions are guided by four key properties,
                        known as the ACID properties: ▬▬


    1. Atomicity:
                * A transaction is atomic, meaning that either all of its operations are executed
                      successfully, or none of them are. If any part of the transaction fails,
                      the entire transaction is rolled back, and the database is returned to
                      its previous state before the transaction started.

    2. Consistency:
                * A transaction ensures that the database transitions from one consistent state to
                      another. This means that before a transaction begins, the database is in a
                      consistent state, and after the transaction completes successfully, the database
                      is again in a consistent state. Any integrity constraints and business
                      rules defined for the database are maintained.

    3. Isolation:
                 * Each transaction is isolated from other concurrent transactions. This means that the
                       intermediate states of a transaction are not visible to other transactions, and
                       the effects of a transaction are not visible to other transactions until the
                       transaction is committed.

    4. Durability:
                 * Once a transaction is committed, its effects are permanent and will persist in the
                       database, even in the case of a system failure or crash. This is typically
                       achieved by writing transaction logs to non-volatile storage.

    ○ Database transactions are vital for maintaining the integrity of a database and ensuring that the
          data remains consistent and reliable, regardless of any concurrent activity or system issues.

    ○ Transactions can be classified into different types based on their characteristics, behavior, or
           the operations they perform.


                           ▬▬ Here are some common types of transactions: ▬▬


    1. Flat Transactions:
                * Flat transactions, also known as traditional or simple transactions, follow the ACID
                      properties (Atomicity, Consistency, Isolation, Durability). They have a well-defined
                      beginning and end, and either all the operations within the transaction are executed,
                      or none are. Flat transactions are widely used in conventional database management
                      systems.

    2. Long-Lived Transactions:
                 * These transactions have an extended duration and involve multiple steps, possibly
                       with user interactions or external system interactions. Long-lived transactions
                       are common in workflow management systems or business process management systems,
                       where various tasks need to be coordinated over a longer period.

    3. Nested Transactions:
                  * Nested transactions are transactions that contain other transactions within them,
                        forming a hierarchical structure. Nested transactions allow better management
                        of complex, multi-step operations by isolating and controlling the execution
                        of the inner transactions. If an inner transaction fails, it can be rolled
                        back without affecting the entire outer transaction.

    4. Distributed Transactions:
                  * Distributed transactions involve multiple resources or databases, which may be
                        located on different machines or systems. These transactions require
                        coordination between the participating resources to ensure that all parts of
                        the transaction are executed consistently and reliably. Two-phase
                        commit (2PC) and three-phase commit (3PC) protocols are common methods for
                        managing distributed transactions.

    5. Autonomous Transactions:
                   * Autonomous transactions are independent transactions that can be called from within
                         another transaction. They have their own separate commit and rollback scope,
                         and their outcome does not affect the calling transaction. This type of
                         transaction is useful when you need to perform actions such as logging or
                         auditing, which should not be rolled back even if the calling transaction fails.

    6. Compensating Transactions:
                    * Compensating transactions are used to undo the effects of a previously committed
                          transaction, typically in long-lived or distributed transactions. Instead
                          of rolling back the original transaction, a compensating transaction is
                          executed to reverse its effects, maintaining data consistency and integrity.
                          This approach is useful in scenarios where rolling back a transaction is not
                          feasible or would be too disruptive.


                                       ▬▬ Some terms used in transactions ▬▬


    1. Transaction ID, also known as a Transaction Identifier or Transaction Reference Number,
                * is a unique identifier assigned to a transaction within a database management system
                      or other transactional systems.
         The Transaction ID serves several purposes:
                1. Uniqueness:
                            - It uniquely identifies a specific transaction, allowing users or systems
                                  to reference, track, or query it when needed. This is particularly
                                  useful for audit trails, logging, debugging, or transaction management.

                2. Traceability:
                             - The Transaction ID helps trace the sequence of operations and changes
                                   made within a transaction, which can be essential for debugging,
                                   troubleshooting, or analyzing the history of data modifications.

                3. Concurrency Control:
                              - In a multi-user environment, Transaction IDs are used by the system
                                    to manage concurrent access and isolation levels between transactions. This ensures that each transaction is isolated from others and helps maintain data consistency and integrity.

                 4. Recovery:
                               - Transaction IDs play a vital role in the recovery process after a
                                     system failure or crash. They help identify committed and
                                     uncommitted transactions, allowing the system to roll back
                                     uncommitted transactions and ensure data consistency.

                 5. Coordination:
                                 - In distributed systems, Transaction IDs are used to coordinate
                                      transactions across multiple resources, such as databases,
                                      message queues, or other services. They help maintain consistency
                                      across these resources by ensuring that all participating resources either commit or roll back their part of the transaction.

    ○ Transaction IDs are generated and managed by the database management system, transaction manager,
           or other transactional systems. They are typically assigned when
                * a transaction begins
                * and can be included
                            - in logs,
                            - audit trails,
                            - or other records related to the
                    transaction's lifecycle.

    2. Transaction Log:
                * A transaction log is a record of all changes made to a database during a transaction.
                      It includes information such as the
                            - transaction ID,
                            - operations performed,
                            - affected data, timestamps,
                            - and more.
          Transaction logs are essential for
                 * recovery processes,
                 * audit trails,
                 * and debugging.

    3. Audit trail
                 * is a chronological record of events and activities, usually related to a specific
                       system, application, or process. It provides a way to track and document the
                             - history of actions,
                             - modifications, and changes made
                                         • by users,
                                         • applications,
                                         • or systems.
           Audit trails are essential for
                  * security,
                  * compliance,
                  * troubleshooting,
                  * and monitoring purposes.
            In the context of a database management system, an audit trail records actions such as:
                  1. User access:
                              - Logins,
                              - logouts,
                              - failed login attempts,
                              - and user account modifications.
                   2. Data modifications:
                               - Insertions,
                               - updates,
                               - deletions,
                               - and other changes made to the data.
                    3. Schema changes:
                               - Modifications to the database structure, such as
                                           • adding, altering, or dropping tables, columns, or indexes.
                    4. Permission changes:
                               - Granting, revoking, or modifying user permissions and access rights.
                    5. Transaction information:
                               - Details about transactions, including
                                           • transaction IDs,
                                           • timestamps,
                                           • and whether they were committed or rolled back.
                     6. System events:
                                - Backup and recovery operations, system errors, and other critical
                                     events.


             ▬▬ Methods for implementing audit trails in a database management system: ▬▬


    1. Built-in auditing features:
                * Many database management systems provide built-in auditing capabilities that can be
                      enabled or configured to capture the desired level of detail. Examples include
                            - Oracle's auditing features,
                            - SQL Server's SQL Server Audit,
                            - and MySQL's Enterprise Audit plugin.

    2. Custom triggers:
                 * You can create custom triggers in the database to capture specific events and store
                       them in dedicated audit tables. For instance, you can create triggers to
                             - log changes made to specific tables,
                             - along with information about
                                         • the user, timestamp, and modified data.

     3. Middleware or application-level logging:
                  * Implement auditing at the application level using logging frameworks or middleware
                        components. This approach provides more control over what to log and how to
                        store the audit records but may require more development effort.

     4. External monitoring tools:
                   * Use specialized database monitoring and auditing tools to capture events and
                         generate audit trails. These tools often provide advanced features, such as
                               - real-time monitoring,
                               - alerting,
                               - and reporting.
            Example of these tools:
                   1. SolarWinds Database Performance Analyzer (DPA)
                   2. ManageEngine Applications Manager
                   3. Idera SQL Diagnostic Manager
                   4. Redgate SQL Monitor
                   5. SentryOne SQL Sentry

    ○ When implementing audit trails, it's crucial to consider factors such as
                * performance impact, storage requirements, and data protection.
        Ensure that audit trails are
                * secure, tamper-proof, and accessible only to authorized users,
        as they may contain sensitive information.






 */

/*
TOPIC:
    START TRANSACTION deschide tranzacția; până la COMMIT sau ROLLBACK, un UPDATE InnoDB încă nu este durabil.
FAMILIE:
    Transaction
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să ții mai multe schimbări împreună. Cu autocommit 1, o propoziție singură nu așteaptă START TRANSACTION.
MECHANISM:
    Serverul execută START TRANSACTION și oprește autocommit până la închidere. BEGIN într-o procedură este blocul BEGIN END, nu tranzacția; acolo folosești START TRANSACTION.
LOOKALIKES:
    COMMIT închide și face durabil. SET autocommit = 1 este modul, nu deschiderea unei tranzacții mai lungi.
BOOK THEORY:
    Manualul 9.7, 15.3.1, deschis în tur: START TRANSACTION pornește tranzacția. În programe stocate, BEGIN este blocul compus. autocommit este 1 implicit.
BOOK EXAMPLE:
    startTransactionHoldsTheChange
REPO DEMO:
    src/main/java/_4_Database_Terminology/Transaction/Transaction.java
BREAK LAB:
    commitClosesWhatStartOpened ține COMMIT. COMMIT închide. START TRANSACTION deschide. BEGIN în rutină este alt BEGIN.
TRADE-OFFS:
    Lași autocommit 1 când fiecare propoziție trebuie să rămână singură.
ANTI-PATTERNS:
    COMMIT ca să începi. Tranzacția se închide, nu se deschide.
STAFF QUESTIONS:
    startTransactionHoldsTheChange și commitClosesWhatStartOpened. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O recepție cu trei linii începe cu START TRANSACTION și se închide cu COMMIT. O linie singură poate rămâne pe autocommit.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă START TRANSACTION?
   Capcană: COMMIT deschide tranzacția.
   Cheie: startTransactionHoldsTheChange: sesiunea ține deschisă o tranzacție până la COMMIT sau ROLLBACK. fără START TRANSACTION, autocommit 1 închide fiecare UPDATE singur
   Demonstrație: startTransactionHoldsTheChange
2. Enunț: Ce rămâne în tabelă sau în sesiune după START TRANSACTION?
   Capcană: BEGIN dintr-o procedură este mereu START TRANSACTION.
   Cheie: Efectul este: sesiunea ține deschisă o tranzacție până la COMMIT sau ROLLBACK. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: startTransactionHoldsTheChange
3. Enunț: De ce START TRANSACTION nu este vecinul său?
   Capcană: autocommit 1 lasă UPDATE necomis până la COMMIT.
   Cheie: COMMIT închide și face durabil. SET autocommit = 1 este modul, nu deschiderea unei tranzacții mai lungi.
   Demonstrație: startTransactionHoldsTheChange
4. Enunț: Cum eșuează COMMIT?
   Capcană: START TRANSACTION șterge rânduri.
   Cheie: commitClosesWhatStartOpened: COMMIT închide. START TRANSACTION deschide. BEGIN în rutină este alt BEGIN.
   Demonstrație: commitClosesWhatStartOpened
5. Enunț: Când nu mai trimiți START TRANSACTION?
   Capcană: Ca să grupezi propoziții, este de ajuns autocommit 1.
   Cheie: Lași autocommit 1 când fiecare propoziție trebuie să rămână singură.
   Demonstrație: startTransactionHoldsTheChange

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE mai multe propoziții trebuie să rămână sau să cadă împreună -> startTransactionHoldsTheChange -> START TRANSACTION -> nu face durabilă schimbarea; asta este COMMIT
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> commitClosesWhatStartOpened -> COMMIT -> COMMIT închide. START TRANSACTION deschide. BEGIN în rutină este alt BEGIN.
    3. Granița
           -> START TRANSACTION este propoziția. Un mod de tranzacție din JDBC poate trimite el propoziția; nu o înlocuiește cu un nume de metodă fără efect.
    4. Alternativa
           -> COMMIT închide și face durabil. SET autocommit = 1 este modul, nu deschiderea unei tranzacții mai lungi.
    5. Anti-patternul
           -> COMMIT ca să începi. Tranzacția se închide, nu se deschide.
    6. Dovada
           -> startTransactionHoldsTheChange ține propoziția. commitClosesWhatStartOpened ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> START TRANSACTION deschide. COMMIT face durabil. ROLLBACK anulează. autocommit 1 face propoziția o tranzacție. BEGIN în procedură nu este START TRANSACTION.
*/

public class Transaction {
    /*
    🟢 Începător
        Ce este            sesiunea ține deschisă o tranzacție până la COMMIT sau ROLLBACK
        De ce e nevoie     fără START TRANSACTION, autocommit 1 închide fiecare UPDATE singur
    🟡 Producție
        Cum se scrie       START TRANSACTION
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este COMMIT, care închide și face durabil
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu COMMIT, care închide și face durabil.
    */
    // Începător: startTransactionHoldsTheChange păstrează propoziția pentru sesiunea ține deschisă o tranzacție până la COMMIT sau ROLLBACK
    // Producție: public static String startTransactionHoldsTheChange(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. BEGIN într-o procedură nu deschide tranzacția. Deschizi cu START TRANSACTION.
    public static String startTransactionHoldsTheChange() {
        // Începător: sesiunea ține deschisă o tranzacție până la COMMIT sau ROLLBACK
        // Producție: START TRANSACTION. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: BEGIN într-o procedură nu deschide tranzacția. Deschizi cu START TRANSACTION. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "START TRANSACTION";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide startTransactionHoldsTheChange. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            închide tranzacția deschisă; nu o deschide
        De ce e nevoie     fără textul greșit, COMMIT pare începutul
    🟡 Producție
        Cum se scrie       COMMIT
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care închide tranzacția deschisă; nu o deschide
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu COMMIT, care închide și face durabil.
    */
    // Începător: commitClosesWhatStartOpened păstrează textul greșit pentru închide tranzacția deschisă; nu o deschide
    // Producție: public static String commitClosesWhatStartOpened(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. COMMIT închide. START TRANSACTION deschide. BEGIN în rutină este alt BEGIN.
    public static String commitClosesWhatStartOpened() {
        // Începător: închide tranzacția deschisă; nu o deschide
        // Producție: COMMIT. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: COMMIT închide. START TRANSACTION deschide. BEGIN în rutină este alt BEGIN. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "COMMIT";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide commitClosesWhatStartOpened. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
