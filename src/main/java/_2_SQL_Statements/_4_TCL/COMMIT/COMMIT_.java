package _2_SQL_Statements._4_TCL.COMMIT;

// https://dev.mysql.com/doc/refman/8.0/en/commit.html


/*                                                       COMMIT statement


    ○ Implicit, autocommit este 1, deci propoziția este deja durabilă la sfârșitul ei dacă nu ai deschis START TRANSACTION. COMMIT închide tranzacția curentă și nu este limitat la INSERT, DELETE și UPDATE.
    HISTORICAL: Not every change you make to a database is saved automatically
    HISTORICAL: The statement to use in such cases is COMMIT.
    HISTORICAL: It works only for changes made with the help of the INSERT, DELETE, or UPDATE clauses.
    Regula citită în tur: MySQL 9.7 Reference Manual, 15.3.1. Același fișier spune mai jos, corect, că autocommit este pornit implicit.

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

/*
TOPIC:
    Cu autocommit 1, UPDATE este deja durabil la sfârșitul lui; COMMIT închide tranzacția deschisă cu START TRANSACTION.
FAMILIE:
    Transaction
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să rămână schimbarea după ce ai deschis tranzacția. Fără START TRANSACTION, COMMIT nu este momentul în care un UPDATE singur se salvează.
MECHANISM:
    Serverul, implicit, face fiecare propoziție atomică. START TRANSACTION oprește autocommit până la COMMIT sau ROLLBACK. COMMIT face schimbarea durabilă.
LOOKALIKES:
    ROLLBACK anulează tranzacția deschisă. Un UPDATE cu autocommit 1 nu așteaptă COMMIT.
BOOK THEORY:
    Manualul 9.7, 15.3.1, deschis în tur: autocommit este 1 implicit. COMMIT face durabilă tranzacția deschisă. ROLLBACK o anulează. Un UPDATE deja închis de autocommit nu se întoarce.  Comentariul de curs are deja HISTORICAL pe ideea că nu orice schimbare se salvează singură.
BOOK EXAMPLE:
    commitOpenTransaction
REPO DEMO:
    src/main/java/_2_SQL_Statements/_4_TCL/COMMIT/COMMIT_.java
BREAK LAB:
    rollbackDropsTheOpenChange ține ROLLBACK. ROLLBACK anulează. COMMIT face durabil. Cu autocommit 1, propoziția singură nu așteaptă niciuna.
TRADE-OFFS:
    ROLLBACK când schimbarea deschisă trebuie să dispară. Lași autocommit 1 când fiecare propoziție trebuie să rămână singură.
ANTI-PATTERNS:
    Să crezi că UPDATE-ul din curs așteaptă COMMIT deși autocommit este 1.
STAFF QUESTIONS:
    commitOpenTransaction și rollbackDropsTheOpenChange. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Închizi o mișcare de stoc deschisă cu START TRANSACTION prin COMMIT. ROLLBACK o scoate. O singură propoziție cu autocommit 1 nu așteaptă.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă COMMIT?
   Capcană: Orice UPDATE așteaptă COMMIT, pentru că autocommit este oprit implicit.
   Cheie: commitOpenTransaction: tranzacția deschisă devine durabilă și se închide. fără COMMIT, după START TRANSACTION schimbarea InnoDB încă se poate da înapoi
   Demonstrație: commitOpenTransaction
2. Enunț: Ce rămâne în tabelă sau în sesiune după COMMIT?
   Capcană: COMMIT anulează ultimul UPDATE.
   Cheie: Efectul este: tranzacția deschisă devine durabilă și se închide. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: commitOpenTransaction
3. Enunț: De ce COMMIT nu este vecinul său?
   Capcană: ROLLBACK salvează tranzacția.
   Cheie: ROLLBACK anulează tranzacția deschisă. Un UPDATE cu autocommit 1 nu așteaptă COMMIT.
   Demonstrație: commitOpenTransaction
4. Enunț: Cum eșuează ROLLBACK?
   Capcană: COMMIT funcționează doar pentru INSERT, DELETE și UPDATE, nu și ca închidere de tranzacție.
   Cheie: rollbackDropsTheOpenChange: ROLLBACK anulează. COMMIT face durabil. Cu autocommit 1, propoziția singură nu așteaptă niciuna.
   Demonstrație: rollbackDropsTheOpenChange
5. Enunț: Când nu mai trimiți COMMIT?
   Capcană: Ca să rămână o schimbare, e de ajuns să închizi Workbench.
   Cheie: ROLLBACK când schimbarea deschisă trebuie să dispară. Lași autocommit 1 când fiecare propoziție trebuie să rămână singură.
   Demonstrație: commitOpenTransaction

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE ai deschis START TRANSACTION și vrei ca schimbarea să rămână -> commitOpenTransaction -> COMMIT -> nu salvează un UPDATE care autocommit l-a închis deja și nu anulează DDL cu commit implicit
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> rollbackDropsTheOpenChange -> ROLLBACK -> ROLLBACK anulează. COMMIT face durabil. Cu autocommit 1, propoziția singură nu așteaptă niciuna.
    3. Granița
           -> COMMIT este propoziția. Butonul de save din client nu este tranzacția.
    4. Alternativa
           -> ROLLBACK anulează tranzacția deschisă. Un UPDATE cu autocommit 1 nu așteaptă COMMIT.
    5. Anti-patternul
           -> Să crezi că UPDATE-ul din curs așteaptă COMMIT deși autocommit este 1.
    6. Dovada
           -> commitOpenTransaction ține propoziția. rollbackDropsTheOpenChange ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> autocommit 1 face propoziția durabilă singură. COMMIT închide tranzacția deschisă cu START TRANSACTION. ROLLBACK o anulează.
*/

public class COMMIT_ {
    /*
    🟢 Începător
        Ce este            tranzacția deschisă devine durabilă și se închide
        De ce e nevoie     fără COMMIT, după START TRANSACTION schimbarea InnoDB încă se poate da înapoi
    🟡 Producție
        Cum se scrie       COMMIT
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este ROLLBACK, care anulează tranzacția deschisă
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ROLLBACK, care anulează tranzacția deschisă.
    */
    // Începător: commitOpenTransaction păstrează propoziția pentru tranzacția deschisă devine durabilă și se închide
    // Producție: public static String commitOpenTransaction(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. COMMIT după un UPDATE cu autocommit 1 nu este salvarea acelui UPDATE: el era deja închis.
    public static String commitOpenTransaction() {
        // Începător: tranzacția deschisă devine durabilă și se închide
        // Producție: COMMIT. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: COMMIT după un UPDATE cu autocommit 1 nu este salvarea acelui UPDATE: el era deja închis. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "COMMIT";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide commitOpenTransaction. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            schimbarea InnoDB încă necomisă din tranzacția deschisă dispare
        De ce e nevoie     fără textul greșit, ROLLBACK pare tot o salvare
    🟡 Producție
        Cum se scrie       ROLLBACK
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care schimbarea InnoDB încă necomisă din tranzacția deschisă dispare
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ROLLBACK, care anulează tranzacția deschisă.
    */
    // Începător: rollbackDropsTheOpenChange păstrează textul greșit pentru schimbarea InnoDB încă necomisă din tranzacția deschisă dispare
    // Producție: public static String rollbackDropsTheOpenChange(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. ROLLBACK anulează. COMMIT face durabil. Cu autocommit 1, propoziția singură nu așteaptă niciuna.
    public static String rollbackDropsTheOpenChange() {
        // Începător: schimbarea InnoDB încă necomisă din tranzacția deschisă dispare
        // Producție: ROLLBACK. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: ROLLBACK anulează. COMMIT face durabil. Cu autocommit 1, propoziția singură nu așteaptă niciuna. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ROLLBACK";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide rollbackDropsTheOpenChange. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
