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



/*
TOPIC:
    SELECT ... FOR UPDATE ține lacăt pe rândurile citite în tranzacție; lacătul de tabelă ține toată tabela, nu doar emp_no 10001.
FAMILIE:
    Transaction
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu lase altă tranzacție să schimbe angajatul 10001 până închizi. După COMMIT, lacătul nu rămâne.
MECHANISM:
    InnoDB pune lacătul de rând pe index la FOR UPDATE, în tranzacția deschisă. Fără index care izolază rândul, poate ține mai mult decât rândul numit. LOCK TABLES ține tabela.
LOOKALIKES:
    LOCK TABLES employees WRITE ține toată tabela. Un SELECT fără FOR UPDATE, la REPEATABLE READ, nu ia același lacăt de scriere.
BOOK THEORY:
    Comentariul de curs desparte lacătul de latch. Pagina de lacăt InnoDB nu a fost adusă integral: NERULAT AICI pentru URL-ul de locking. Propoziția FOR UPDATE este în sintaxa SELECT 9.7 deschisă: FOR UPDATE stă la sfârșitul SELECT.
    HISTORICAL: pagina 17.7.1 a fost adusă după aceea. SELECT ... FOR UPDATE ia un lacăt de intenție IX pe tabelă și un lacăt de înregistrare pe index. LOCK TABLES ... WRITE ia lacătul de tabelă. La o căutare pe index neunic, InnoDB poate ține și golul dinaintea înregistrării.
BOOK EXAMPLE:
    forUpdateLocksTheEmployee
REPO DEMO:
    src/main/java/_4_Database_Terminology/Row_Lock/Row_Lock_.java
BREAK LAB:
    lockTablesHoldsTheWholeTable ține LOCK TABLES employees WRITE. LOCK TABLES este lacăt de tabelă. FOR UPDATE pe cheie este lacătul de rând, cât timp indexul izolază rândul.
TRADE-OFFS:
    LOCK TABLES când chiar vrei toată tabela și accepți că blochezi și celelalte rânduri.
ANTI-PATTERNS:
    LOCK TABLES ca să păzești un angajat. Ții toată tabela.
STAFF QUESTIONS:
    forUpdateLocksTheEmployee și lockTablesHoldsTheWholeTable. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Rezervi un loc: SELECT seat_id FROM seat WHERE seat_id = 14 FOR UPDATE în tranzacția deschisă. LOCK TABLES ar bloca toată sala.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 FOR UPDATE?
   Capcană: FOR UPDATE ține lacătul și după COMMIT.
   Cheie: forUpdateLocksTheEmployee: în tranzacția deschisă, rândul 10001 este ținut până la COMMIT sau ROLLBACK. fără FOR UPDATE, o citire obișnuită nu ține rândul pentru scrierea care urmează
   Demonstrație: forUpdateLocksTheEmployee
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 FOR UPDATE?
   Capcană: LOCK TABLES employees WRITE ține doar emp_no 10001.
   Cheie: Efectul este: în tranzacția deschisă, rândul 10001 este ținut până la COMMIT sau ROLLBACK. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: forUpdateLocksTheEmployee
3. Enunț: De ce SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 FOR UPDATE nu este vecinul său?
   Capcană: Cu autocommit 1, FOR UPDATE ține rândul până la propoziția următoare.
   Cheie: LOCK TABLES employees WRITE ține toată tabela. Un SELECT fără FOR UPDATE, la REPEATABLE READ, nu ia același lacăt de scriere.
   Demonstrație: forUpdateLocksTheEmployee
4. Enunț: Cum eșuează LOCK TABLES employees WRITE?
   Capcană: Lacătul de rând este un latch de memorie.
   Cheie: lockTablesHoldsTheWholeTable: LOCK TABLES este lacăt de tabelă. FOR UPDATE pe cheie este lacătul de rând, cât timp indexul izolază rândul.
   Demonstrație: lockTablesHoldsTheWholeTable
5. Enunț: Când nu mai trimiți SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 FOR UPDATE?
   Capcană: Fără index, FOR UPDATE ține mereu exact un rând.
   Cheie: LOCK TABLES când chiar vrei toată tabela și accepți că blochezi și celelalte rânduri.
   Demonstrație: forUpdateLocksTheEmployee

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei să ții angajatul 10001 până închizi tranzacția -> forUpdateLocksTheEmployee -> SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 FOR UPDATE -> nu garantează că fără index pe emp_no ții doar acel rând și nu ține lacătul după COMMIT
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> lockTablesHoldsTheWholeTable -> LOCK TABLES employees WRITE -> LOCK TABLES este lacăt de tabelă. FOR UPDATE pe cheie este lacătul de rând, cât timp indexul izolază rândul.
    3. Granița
           -> FOR UPDATE este clauza. Un lacăt desenat în client nu ține rândul.
    4. Alternativa
           -> LOCK TABLES employees WRITE ține toată tabela. Un SELECT fără FOR UPDATE, la REPEATABLE READ, nu ia același lacăt de scriere.
    5. Anti-patternul
           -> LOCK TABLES ca să păzești un angajat. Ții toată tabela.
    6. Dovada
           -> forUpdateLocksTheEmployee ține propoziția. lockTablesHoldsTheWholeTable ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> FOR UPDATE ține rândul în tranzacția deschisă. LOCK TABLES ține tabela. Fără index potrivit, InnoDB poate ține mai mult decât rândul numit. După COMMIT, lacătul nu rămâne.
*/

public class Row_Lock_ {
    /*
    🟢 Începător
        Ce este            în tranzacția deschisă, rândul 10001 este ținut până la COMMIT sau ROLLBACK
        De ce e nevoie     fără FOR UPDATE, o citire obișnuită nu ține rândul pentru scrierea care urmează
    🟡 Producție
        Cum se scrie       SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 FOR UPDATE
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este LOCK TABLES employees WRITE
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu LOCK TABLES employees WRITE.
    */
    // Începător: forUpdateLocksTheEmployee păstrează propoziția pentru în tranzacția deschisă, rândul 10001 este ținut până la COMMIT sau ROLLBACK
    // Producție: public static String forUpdateLocksTheEmployee(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. FOR UPDATE fără tranzacție deschisă, cu autocommit 1, se închide odată cu propoziția și nu ține rândul pentru propoziția următoare.
    public static String forUpdateLocksTheEmployee() {
        // Începător: în tranzacția deschisă, rândul 10001 este ținut până la COMMIT sau ROLLBACK
        // Producție: SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 FOR UPDATE. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: FOR UPDATE fără tranzacție deschisă, cu autocommit 1, se închide odată cu propoziția și nu ține rândul pentru propoziția următoare. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 FOR UPDATE";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide forUpdateLocksTheEmployee. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            este ținută toată tabela employees, nu doar angajatul 10001
        De ce e nevoie     fără textul greșit, orice lacăt pare de rând
    🟡 Producție
        Cum se scrie       LOCK TABLES employees WRITE
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care este ținută toată tabela employees, nu doar angajatul 10001
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu LOCK TABLES employees WRITE.
    */
    // Începător: lockTablesHoldsTheWholeTable păstrează textul greșit pentru este ținută toată tabela employees, nu doar angajatul 10001
    // Producție: public static String lockTablesHoldsTheWholeTable(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. LOCK TABLES este lacăt de tabelă. FOR UPDATE pe cheie este lacătul de rând, cât timp indexul izolază rândul.
    public static String lockTablesHoldsTheWholeTable() {
        // Începător: este ținută toată tabela employees, nu doar angajatul 10001
        // Producție: LOCK TABLES employees WRITE. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: LOCK TABLES este lacăt de tabelă. FOR UPDATE pe cheie este lacătul de rând, cât timp indexul izolază rândul. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "LOCK TABLES employees WRITE";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide lockTablesHoldsTheWholeTable. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
