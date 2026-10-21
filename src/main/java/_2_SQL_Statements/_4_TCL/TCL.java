package _2_SQL_Statements._4_TCL;

// Martin G. Transactional and Locking Statements
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338040#overview

// Transactional and Locking Statements
// https://dev.mysql.com/doc/refman/8.0/en/sql-transactional-statements.html


/*                                     Transaction Control Language


    ○ Implicit, autocommit este 1: fiecare propoziție este ea însăși o tranzacție și rămâne la sfârșitul ei. COMMIT explicit închide tranzacția deschisă cu START TRANSACTION. Nu este limitat la INSERT, DELETE și UPDATE. DDL pe tabelă netemporară și GRANT fac commit implicit, iar un ROLLBACK ulterior nu le anulează.
    HISTORICAL: Not every change you make to a database is saved automatically
    HISTORICAL: It works only for changes made with the help of the INSERT, DELETE, or UPDATE clauses.
    Regula citită în tur: MySQL 9.7 Reference Manual, 15.3.1 START TRANSACTION, COMMIT, and ROLLBACK Statements și 15.3.3 Statements That Cause an Implicit Commit.

    1. COMMIT.

               * COMMIT will save

                              - the transaction on the database

                                          • transaction cannot be undone
               * Example

                             - UPDATE customers
                               SET last_name = 'Johnson'
                               WHERE customer_id =4
                               COMMIT;
    HISTORICAL: exemplul de mai sus închide UPDATE cu COMMIT fără START TRANSACTION. Cu autocommit pornit, UPDATE este deja durabil la sfârșitul lui, iar COMMIT nu este momentul salvării.
    Regula citită în tur: MySQL 9.7 Reference Manual, 15.3.1. By default, autocommit mode is enabled, and each statement is atomic.


    2. ROLLBACK clause

                   * the clause that will let you to

                               - undo any changes you have made

                               - but don't want to be saved permanently.

                       To apply this command,

                               - type ROLLBACK;

                  * ROLLBACK anulează schimbările InnoDB încă necomise din tranzacția deschisă. Nu anulează CREATE TABLE netemporar, GRANT sau un UPDATE deja comis. Pe un motor netranzacțional, schimbarea rămâne și serverul avertizează că rollback-ul nu este complet.
                  HISTORICAL: ROLLBACK will revert until the last committed state, and all the changes made in the meantime will be REMOVED.
                  Regula citită în tur: MySQL 9.7 Reference Manual, 15.3.1 și 15.3.3. Avertismentul pentru motorul netranzacțional este ER_WARNING_NOT_COMPLETE_ROLLBACK.

                  * There can be multiple statements executed,

                              - and they will all be undone.


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

/*
TOPIC:
    TCL spune când schimbarea rămâne: COMMIT o face durabilă, ROLLBACK o anulează, iar autocommit 1 face fiecare propoziție o tranzacție.
FAMILIE:
    Transaction
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu tratezi orice propoziție ca și cum ar aștepta COMMIT. Starea se vede la închiderea tranzacției deschise sau la sfârșitul propoziției dacă autocommit este 1.
MECHANISM:
    Serverul pornește cu autocommit 1. START TRANSACTION deschide una care se închide la COMMIT sau ROLLBACK. DDL cu commit implicit nu stă la mijloc ca un UPDATE.
LOOKALIKES:
    Un UPDATE cu autocommit 1 este deja închis. SAVEPOINT cere o tranzacție deschisă.
BOOK THEORY:
    Manualul 9.7, 15.3.1, deschis în tur: autocommit este 1 implicit. COMMIT face durabilă tranzacția deschisă. ROLLBACK o anulează. Un UPDATE deja închis de autocommit nu se întoarce.  Comentariul din TCL.java are deja HISTORICAL pe ideea că nu orice schimbare așteaptă COMMIT.
BOOK EXAMPLE:
    commitMakesItDurable
REPO DEMO:
    src/main/java/_2_SQL_Statements/_4_TCL/TCL.java
BREAK LAB:
    setAutocommitOffIsNotCommit ține SET autocommit = 0. SET autocommit = 0 schimbă sesiunea. COMMIT închide tranzacția. Nu sunt aceeași propoziție.
TRADE-OFFS:
    SET autocommit = 0 când mai multe propoziții trebuie să stea în aceeași tranzacție, apoi COMMIT.
ANTI-PATTERNS:
    SET autocommit = 0 ca să salvezi. Nu ai închis tranzacția.
STAFF QUESTIONS:
    commitMakesItDurable și setAutocommitOffIsNotCommit. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O sesiune de inventar cu mai multe linii oprește autocommit, apoi COMMIT la sfârșitul fișei. O linie singură cu autocommit 1 rămâne fără COMMIT separat.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă COMMIT?
   Capcană: Implicit, nimic nu se salvează până la COMMIT.
   Cheie: commitMakesItDurable: tranzacția deschisă rămâne durabilă. fără să știi autocommit, crezi că orice UPDATE stă necomis până la COMMIT
   Demonstrație: commitMakesItDurable
2. Enunț: Ce rămâne în tabelă sau în sesiune după COMMIT?
   Capcană: SET autocommit = 0 face COMMIT.
   Cheie: Efectul este: tranzacția deschisă rămâne durabilă. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: commitMakesItDurable
3. Enunț: De ce COMMIT nu este vecinul său?
   Capcană: ROLLBACK salvează.
   Cheie: Un UPDATE cu autocommit 1 este deja închis. SAVEPOINT cere o tranzacție deschisă.
   Demonstrație: commitMakesItDurable
4. Enunț: Cum eșuează SET autocommit = 0?
   Capcană: TRUNCATE așteaptă COMMIT ca un INSERT.
   Cheie: setAutocommitOffIsNotCommit: SET autocommit = 0 schimbă sesiunea. COMMIT închide tranzacția. Nu sunt aceeași propoziție.
   Demonstrație: setAutocommitOffIsNotCommit
5. Enunț: Când nu mai trimiți COMMIT?
   Capcană: Ca să ții mai multe propoziții împreună, este de ajuns să nu închizi fereastra.
   Cheie: SET autocommit = 0 când mai multe propoziții trebuie să stea în aceeași tranzacție, apoi COMMIT.
   Demonstrație: commitMakesItDurable

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE tranzacția este deschisă și vrei să rămână -> commitMakesItDurable -> COMMIT -> nu schimbă autocommit-ul implicit al altor sesiuni
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> setAutocommitOffIsNotCommit -> SET autocommit = 0 -> SET autocommit = 0 schimbă sesiunea. COMMIT închide tranzacția. Nu sunt aceeași propoziție.
    3. Granița
           -> COMMIT, ROLLBACK și SET autocommit sunt propoziții. Un checkbox de auto-commit în client trebuie să trimită propoziția.
    4. Alternativa
           -> Un UPDATE cu autocommit 1 este deja închis. SAVEPOINT cere o tranzacție deschisă.
    5. Anti-patternul
           -> SET autocommit = 0 ca să salvezi. Nu ai închis tranzacția.
    6. Dovada
           -> commitMakesItDurable ține propoziția. setAutocommitOffIsNotCommit ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> autocommit 1 închide fiecare propoziție. COMMIT închide tranzacția deschisă. ROLLBACK o anulează. SET autocommit = 0 nu este salvarea.
*/

public class TCL {
    /*
    🟢 Începător
        Ce este            tranzacția deschisă rămâne durabilă
        De ce e nevoie     fără să știi autocommit, crezi că orice UPDATE stă necomis până la COMMIT
    🟡 Producție
        Cum se scrie       COMMIT
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este SET autocommit = 0, care lasă sesiunea fără închidere automată până o schimbi
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SET autocommit = 0, care lasă sesiunea fără închidere automată până o schimbi.
    */
    // Începător: commitMakesItDurable păstrează propoziția pentru tranzacția deschisă rămâne durabilă
    // Producție: public static String commitMakesItDurable(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. COMMIT nu anulează. ROLLBACK nu salvează.
    public static String commitMakesItDurable() {
        // Începător: tranzacția deschisă rămâne durabilă
        // Producție: COMMIT. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: COMMIT nu anulează. ROLLBACK nu salvează. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "COMMIT";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide commitMakesItDurable. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            sesiunea nu mai închide singură propozițiile, dar schimbarea deschisă nu a devenit durabilă
        De ce e nevoie     fără textul greșit, oprirea autocommit pare salvarea
    🟡 Producție
        Cum se scrie       SET autocommit = 0
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care sesiunea nu mai închide singură propozițiile, dar schimbarea deschisă nu a devenit durabilă
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SET autocommit = 0, care lasă sesiunea fără închidere automată până o schimbi.
    */
    // Începător: setAutocommitOffIsNotCommit păstrează textul greșit pentru sesiunea nu mai închide singură propozițiile, dar schimbarea deschisă nu a devenit durabilă
    // Producție: public static String setAutocommitOffIsNotCommit(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. SET autocommit = 0 schimbă sesiunea. COMMIT închide tranzacția. Nu sunt aceeași propoziție.
    public static String setAutocommitOffIsNotCommit() {
        // Începător: sesiunea nu mai închide singură propozițiile, dar schimbarea deschisă nu a devenit durabilă
        // Producție: SET autocommit = 0. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: SET autocommit = 0 schimbă sesiunea. COMMIT închide tranzacția. Nu sunt aceeași propoziție. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SET autocommit = 0";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide setAutocommitOffIsNotCommit. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
