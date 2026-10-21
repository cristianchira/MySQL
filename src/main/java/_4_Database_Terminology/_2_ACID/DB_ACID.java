package _4_Database_Terminology._2_ACID;

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


                                  ▬▬ Transactions are ▬▬


    1. Atomic units of work that can be:
                    * committed
                    * or rolled back.
          When a transaction makes multiple changes to the database,
                  * either all the changes succeed when the transaction is Committed,
                  * or all the changes are undone when the transaction is Rolled Back.

    2. The database remains in a CONSISTENT state at all times
                  *  after each commit or rollback,
                  * and while transactions are in progress.
          If related data is being updated across multiple tables,
                  * queries see either
                                - all old values
                                - or all new values,
                        not a mix of old and new values.

    3. Transactions are protected (isolated)
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

    4. The results of transactions are DURABLE:
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

/*
TOPIC:
    ACID este proprietatea tranzacției InnoDB, nu un comutator: cu autocommit 1, fiecare propoziție este deja o tranzacție.
FAMILIE:
    Transaction
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu spui ACID în loc de COMMIT. Durabilitatea se vede când tranzacția se închide.
MECHANISM:
    Serverul ține atomicitatea pe tranzacție. autocommit 1 face propoziția o tranzacție. START TRANSACTION leagă mai multe propoziții până la COMMIT sau ROLLBACK.
LOOKALIKES:
    SET autocommit = 1 nu este cuvântul ACID. SHOW ENGINES spune dacă motorul are Transactions.
BOOK THEORY:
    Manualul 9.7, 15.3.1, deschis în tur: autocommit este 1 implicit. COMMIT face durabilă tranzacția deschisă. ROLLBACK o anulează. Un UPDATE deja închis de autocommit nu se întoarce.  Comentariul de curs leagă ACID de tranzacția InnoDB. Rămâne. Nu este un înlocuitor pentru propoziție.
BOOK EXAMPLE:
    commitIsTheDurablePoint
REPO DEMO:
    src/main/java/_4_Database_Terminology/_2_ACID/DB_ACID.java
BREAK LAB:
    theWordAcidIsNotAStatement ține ACID. ACID nu este SQL. COMMIT sau ROLLBACK închid tranzacția deschisă.
TRADE-OFFS:
    SHOW ENGINES când trebuie să vezi dacă motorul are Transactions înainte să te bazezi pe ROLLBACK.
ANTI-PATTERNS:
    Să trimiți ACID. Nu se închide nicio tranzacție.
STAFF QUESTIONS:
    commitIsTheDurablePoint și theWordAcidIsNotAStatement. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O mișcare de stoc rămâne la COMMIT, nu pentru că ai spus că sistemul este ACID.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă COMMIT?
   Capcană: ACID este o propoziție pe care o trimiți ca să salvezi.
   Cheie: commitIsTheDurablePoint: tranzacția deschisă devine durabilă; cuvântul ACID singur nu închide nimic. fără COMMIT sau fără autocommit, schimbarea InnoDB deschisă nu este încă durabilă
   Demonstrație: commitIsTheDurablePoint
2. Enunț: Ce rămâne în tabelă sau în sesiune după COMMIT?
   Capcană: autocommit 1 înseamnă că ACID este oprit.
   Cheie: Efectul este: tranzacția deschisă devine durabilă; cuvântul ACID singur nu închide nimic. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: commitIsTheDurablePoint
3. Enunț: De ce COMMIT nu este vecinul său?
   Capcană: SHOW ENGINES și ACID sunt aceeași coloană, scrisă ACID.
   Cheie: SET autocommit = 1 nu este cuvântul ACID. SHOW ENGINES spune dacă motorul are Transactions.
   Demonstrație: commitIsTheDurablePoint
4. Enunț: Cum eșuează ACID?
   Capcană: ROLLBACK nu ține de atomicitate.
   Cheie: theWordAcidIsNotAStatement: ACID nu este SQL. COMMIT sau ROLLBACK închid tranzacția deschisă.
   Demonstrație: theWordAcidIsNotAStatement
5. Enunț: Când nu mai trimiți COMMIT?
   Capcană: Dacă motorul nu are tranzacții, ACID tot aduce rândul înapoi.
   Cheie: SHOW ENGINES când trebuie să vezi dacă motorul are Transactions înainte să te bazezi pe ROLLBACK.
   Demonstrație: commitIsTheDurablePoint

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei durabilitatea unei tranzacții deschise -> commitIsTheDurablePoint -> COMMIT -> nu face un motor fără Transactions să țină ROLLBACK
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> theWordAcidIsNotAStatement -> ACID -> ACID nu este SQL. COMMIT sau ROLLBACK închid tranzacția deschisă.
    3. Granița
           -> ACID este proprietatea. Propoziția este COMMIT, ROLLBACK sau propoziția închisă de autocommit.
    4. Alternativa
           -> SET autocommit = 1 nu este cuvântul ACID. SHOW ENGINES spune dacă motorul are Transactions.
    5. Anti-patternul
           -> Să trimiți ACID. Nu se închide nicio tranzacție.
    6. Dovada
           -> commitIsTheDurablePoint ține propoziția. theWordAcidIsNotAStatement ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> ACID descrie tranzacția. autocommit 1 face fiecare propoziție o tranzacție. COMMIT o închide când ai deschis-o. Cuvântul ACID nu este propoziția.
*/

public class DB_ACID {
    /*
    🟢 Începător
        Ce este            tranzacția deschisă devine durabilă; cuvântul ACID singur nu închide nimic
        De ce e nevoie     fără COMMIT sau fără autocommit, schimbarea InnoDB deschisă nu este încă durabilă
    🟡 Producție
        Cum se scrie       COMMIT
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este SET autocommit = 1, care face fiecare propoziție ulterioară o tranzacție, dar nu închide el însuși una deja deschisă ca COMMIT
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SET autocommit = 1, care face fiecare propoziție ulterioară o tranzacție, dar nu închide el însuși una deja deschisă ca COMMIT.
    */
    // Începător: commitIsTheDurablePoint păstrează propoziția pentru tranzacția deschisă devine durabilă; cuvântul ACID singur nu închide nimic
    // Producție: public static String commitIsTheDurablePoint(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. A spune că baza este ACID nu ține loc de ROLLBACK când ai greșit în tranzacția deschisă.
    public static String commitIsTheDurablePoint() {
        // Începător: tranzacția deschisă devine durabilă; cuvântul ACID singur nu închide nimic
        // Producție: COMMIT. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: A spune că baza este ACID nu ține loc de ROLLBACK când ai greșit în tranzacția deschisă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "COMMIT";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide commitIsTheDurablePoint. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul nu primește o propoziție și nu închide tranzacția
        De ce e nevoie     fără textul greșit, acronimul pare comutatorul
    🟡 Producție
        Cum se scrie       ACID
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul nu primește o propoziție și nu închide tranzacția
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SET autocommit = 1, care face fiecare propoziție ulterioară o tranzacție, dar nu închide el însuși una deja deschisă ca COMMIT.
    */
    // Începător: theWordAcidIsNotAStatement păstrează textul greșit pentru serverul nu primește o propoziție și nu închide tranzacția
    // Producție: public static String theWordAcidIsNotAStatement(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. ACID nu este SQL. COMMIT sau ROLLBACK închid tranzacția deschisă.
    public static String theWordAcidIsNotAStatement() {
        // Începător: serverul nu primește o propoziție și nu închide tranzacția
        // Producție: ACID. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: ACID nu este SQL. COMMIT sau ROLLBACK închid tranzacția deschisă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ACID";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide theWordAcidIsNotAStatement. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
