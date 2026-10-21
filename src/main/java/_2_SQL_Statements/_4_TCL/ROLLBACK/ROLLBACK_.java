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


/*
TOPIC:
    ROLLBACK anulează schimbările InnoDB încă necomise din tranzacția deschisă; nu anulează un UPDATE deja închis de autocommit.
FAMILIE:
    Transaction
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să dispară corecția pe care nu o vrei, dacă tranzacția este încă deschisă. Un COMMIT deja făcut nu se desface.
MECHANISM:
    Serverul execută ROLLBACK și readuce rândurile InnoDB la începutul tranzacției deschise. Cu autocommit 1, UPDATE s-a închis singur și ROLLBACK nu îl mai are.
LOOKALIKES:
    COMMIT face schimbarea durabilă. ROLLBACK TO savepoint anulează doar până la semn și lasă tranzacția deschisă.
BOOK THEORY:
    Manualul 9.7, 15.3.1, deschis în tur: autocommit este 1 implicit. COMMIT face durabilă tranzacția deschisă. ROLLBACK o anulează. Un UPDATE deja închis de autocommit nu se întoarce.  Exemplul din curs face UPDATE, COMMIT, apoi ROLLBACK. După COMMIT, ROLLBACK nu mai are acea schimbare.
BOOK EXAMPLE:
    rollbackOpenTransaction
REPO DEMO:
    src/main/java/_2_SQL_Statements/_4_TCL/ROLLBACK/ROLLBACK_.java
BREAK LAB:
    commitThenRollbackChangesNothing ține COMMIT. După COMMIT, tranzacția s-a închis. ROLLBACK următor nu are acea schimbare.
TRADE-OFFS:
    COMMIT când schimbarea trebuie să rămână. ROLLBACK TO când vrei să păstrezi tranzacția și să anulezi doar după un semn.
ANTI-PATTERNS:
    UPDATE, COMMIT, ROLLBACK ca să anulezi UPDATE-ul. După COMMIT, data rămâne.
STAFF QUESTIONS:
    rollbackOpenTransaction și commitThenRollbackChangesNothing. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Anulezi o ajustare de stoc încă deschisă cu ROLLBACK. Dacă ai dat COMMIT, cantitatea rămâne.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă ROLLBACK?
   Capcană: ROLLBACK de după COMMIT aduce înapoi last_name.
   Cheie: rollbackOpenTransaction: schimbările InnoDB necomise din tranzacția curentă dispar. fără ROLLBACK, după START TRANSACTION rândurile schimbate încă stau necomise
   Demonstrație: rollbackOpenTransaction
2. Enunț: Ce rămâne în tabelă sau în sesiune după ROLLBACK?
   Capcană: ROLLBACK șterge tabela.
   Cheie: Efectul este: schimbările InnoDB necomise din tranzacția curentă dispar. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: rollbackOpenTransaction
3. Enunț: De ce ROLLBACK nu este vecinul său?
   Capcană: Cu autocommit 1, ROLLBACK anulează ultimul UPDATE.
   Cheie: COMMIT face schimbarea durabilă. ROLLBACK TO savepoint anulează doar până la semn și lasă tranzacția deschisă.
   Demonstrație: rollbackOpenTransaction
4. Enunț: Cum eșuează COMMIT?
   Capcană: ROLLBACK TO și ROLLBACK închid la fel tranzacția.
   Cheie: commitThenRollbackChangesNothing: După COMMIT, tranzacția s-a închis. ROLLBACK următor nu are acea schimbare.
   Demonstrație: commitThenRollbackChangesNothing
5. Enunț: Când nu mai trimiți ROLLBACK?
   Capcană: Ca să anulezi, este de ajuns să închizi conexiunea după COMMIT.
   Cheie: COMMIT când schimbarea trebuie să rămână. ROLLBACK TO când vrei să păstrezi tranzacția și să anulezi doar după un semn.
   Demonstrație: rollbackOpenTransaction

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE tranzacția este deschisă și schimbarea trebuie să dispară -> rollbackOpenTransaction -> ROLLBACK -> nu anulează DDL cu commit implicit și nu anulează un UPDATE cu autocommit 1
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> commitThenRollbackChangesNothing -> COMMIT -> După COMMIT, tranzacția s-a închis. ROLLBACK următor nu are acea schimbare.
    3. Granița
           -> ROLLBACK este propoziția. Undo din editor nu este tranzacția.
    4. Alternativa
           -> COMMIT face schimbarea durabilă. ROLLBACK TO savepoint anulează doar până la semn și lasă tranzacția deschisă.
    5. Anti-patternul
           -> UPDATE, COMMIT, ROLLBACK ca să anulezi UPDATE-ul. După COMMIT, data rămâne.
    6. Dovada
           -> rollbackOpenTransaction ține propoziția. commitThenRollbackChangesNothing ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> ROLLBACK anulează tranzacția deschisă pe InnoDB. Nu anulează ce autocommit sau COMMIT a închis. ROLLBACK TO lasă tranzacția deschisă.
*/

public class ROLLBACK_ {
    /*
    🟢 Începător
        Ce este            schimbările InnoDB necomise din tranzacția curentă dispar
        De ce e nevoie     fără ROLLBACK, după START TRANSACTION rândurile schimbate încă stau necomise
    🟡 Producție
        Cum se scrie       ROLLBACK
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este COMMIT, care le face durabile
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu COMMIT, care le face durabile.
    */
    // Începător: rollbackOpenTransaction păstrează propoziția pentru schimbările InnoDB necomise din tranzacția curentă dispar
    // Producție: public static String rollbackOpenTransaction(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. ROLLBACK după COMMIT nu aduce înapoi data deja durabilă.
    public static String rollbackOpenTransaction() {
        // Începător: schimbările InnoDB necomise din tranzacția curentă dispar
        // Producție: ROLLBACK. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: ROLLBACK după COMMIT nu aduce înapoi data deja durabilă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ROLLBACK";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide rollbackOpenTransaction. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            schimbarea deschisă rămâne și un ROLLBACK de după nu o mai scoate
        De ce e nevoie     fără textul greșit, ordinea COMMIT apoi ROLLBACK din curs pare că întoarce data
    🟡 Producție
        Cum se scrie       COMMIT
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care schimbarea deschisă rămâne și un ROLLBACK de după nu o mai scoate
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu COMMIT, care le face durabile.
    */
    // Începător: commitThenRollbackChangesNothing păstrează textul greșit pentru schimbarea deschisă rămâne și un ROLLBACK de după nu o mai scoate
    // Producție: public static String commitThenRollbackChangesNothing(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. După COMMIT, tranzacția s-a închis. ROLLBACK următor nu are acea schimbare.
    public static String commitThenRollbackChangesNothing() {
        // Începător: schimbarea deschisă rămâne și un ROLLBACK de după nu o mai scoate
        // Producție: COMMIT. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: După COMMIT, tranzacția s-a închis. ROLLBACK următor nu are acea schimbare. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "COMMIT";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide commitThenRollbackChangesNothing. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
