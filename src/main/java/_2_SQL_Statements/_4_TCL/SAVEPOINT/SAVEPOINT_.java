package _2_SQL_Statements._4_TCL.SAVEPOINT;

// https://dev.mysql.com/doc/refman/8.0/en/savepoint.html


/*                           SAVEPOINT, ROLLBACK TO SAVEPOINT, and RELEASE SAVEPOINT Statements


                                                    Syntax

SAVEPOINT identifier
ROLLBACK [WORK] TO [SAVEPOINT] identifier
RELEASE SAVEPOINT identifier

    ○ InnoDB supports the SQL statements

                   * SAVEPOINT,

                   * ROLLBACK TO SAVEPOINT,

                   * RELEASE SAVEPOINT

                   * and the optional WORK keyword for ROLLBACK.

    ○ SAVEPOINT

                   * statement sets a named transaction savepoint with a name of identifier.

                                 - If the current transaction has a savepoint with the same name,

                                 - the old savepoint is deleted and a new one is set.

    ○ ROLLBACK TO SAVEPOINT

                    * statement rolls back a transaction to the named savepoint without terminating the transaction.

                                 - Modifications that the current transaction made to rows

                                 - after the savepoint was set are undone in the rollback,

                                 - but InnoDB does not release the row locks that were stored in memory after the savepoint.

                         (For a new inserted row, the lock information is carried by the transaction ID stored in the row;

                                 - the lock is not separately stored in memory.

                         In this case, the row lock is released in the undo.)

                                  - Savepoints that were set at a later time than the named savepoint are deleted.

    ○ If ROLLBACK TO SAVEPOINT

                     * statement returns the following error,

                     * it means that no savepoint with the specified name exists:

ERROR 1305 (42000): SAVEPOINT identifier does not exist


    ○ RELEASE SAVEPOINT

                    * statement removes the named savepoint from the set of savepoints of the current transaction.

             No commit or rollback occurs.

                     * It is an error if the savepoint does not exist.

    ○ All savepoints of the current transaction are deleted

                     * if you execute

                                   - a COMMIT,

                                   - or a ROLLBACK

                           that does not name a savepoint.

    ○ A new savepoint level is created when

                    * a stored function is invoked

                    * or a trigger is activated.

            The savepoints on previous levels

                    * become unavailable

                    * and thus do not conflict with savepoints on the new level.

             When the function or trigger terminates,

                    * any savepoints it created are released

                    * and the previous savepoint level is restored.





 */

/*
TOPIC:
    SAVEPOINT before_raise pune un semn; ROLLBACK TO before_raise anulează doar ce s-a scris după semn și lasă tranzacția deschisă.
FAMILIE:
    Transaction
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu pierzi toată tranzacția când ultima modificare este greșită. Lacătele de rând luate după semn pot rămâne.
MECHANISM:
    InnoDB execută SAVEPOINT. ROLLBACK TO nu închide tranzacția și nu eliberează lacătele de rând ținute în memorie după semn. COMMIT șterge semnele.
LOOKALIKES:
    ROLLBACK fără TO anulează toată tranzacția și o închide. RELEASE SAVEPOINT scoate semnul și nu anulează rânduri.
BOOK THEORY:
    Manualul 9.7, 15.3.4 SAVEPOINT, deschis în tur: ROLLBACK TO nu închide tranzacția și nu eliberează lacătele de rând ținute în memorie după savepoint. COMMIT șterge savepoint-urile. 
BOOK EXAMPLE:
    rollbackToKeepsTheTransaction
REPO DEMO:
    src/main/java/_2_SQL_Statements/_4_TCL/SAVEPOINT/SAVEPOINT_.java
BREAK LAB:
    rollbackClosesEverything ține ROLLBACK. ROLLBACK fără TO închide tranzacția. ROLLBACK TO o lasă deschisă. Lacătele de după semn nu se eliberează toate la ROLLBACK TO.
TRADE-OFFS:
    ROLLBACK când toată tranzacția trebuie să dispară. RELEASE SAVEPOINT când vrei să ștergi semnul și să păstrezi rândurile.
ANTI-PATTERNS:
    ROLLBACK când voiai doar întoarcerea la before_raise. Pleacă toată tranzacția.
STAFF QUESTIONS:
    rollbackToKeepsTheTransaction și rollbackClosesEverything. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Într-o recepție deschisă, SAVEPOINT before_raise înainte de cantitatea greșită. ROLLBACK TO o scoate și lasă liniile de dinainte.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă ROLLBACK TO before_raise?
   Capcană: ROLLBACK TO închide tranzacția, ca ROLLBACK.
   Cheie: rollbackToKeepsTheTransaction: rândurile scrise după before_raise revin, iar tranzacția rămâne deschisă. fără SAVEPOINT, ROLLBACK întoarce tot ce era necomis și închide tranzacția
   Demonstrație: rollbackToKeepsTheTransaction
2. Enunț: Ce rămâne în tabelă sau în sesiune după ROLLBACK TO before_raise?
   Capcană: ROLLBACK TO eliberează toate lacătele luate după semn.
   Cheie: Efectul este: rândurile scrise după before_raise revin, iar tranzacția rămâne deschisă. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: rollbackToKeepsTheTransaction
3. Enunț: De ce ROLLBACK TO before_raise nu este vecinul său?
   Capcană: Dacă semnul lipsește, serverul îl creează și continuă.
   Cheie: ROLLBACK fără TO anulează toată tranzacția și o închide. RELEASE SAVEPOINT scoate semnul și nu anulează rânduri.
   Demonstrație: rollbackToKeepsTheTransaction
4. Enunț: Cum eșuează ROLLBACK?
   Capcană: RELEASE SAVEPOINT anulează rândurile de după semn.
   Cheie: rollbackClosesEverything: ROLLBACK fără TO închide tranzacția. ROLLBACK TO o lasă deschisă. Lacătele de după semn nu se eliberează toate la ROLLBACK TO.
   Demonstrație: rollbackClosesEverything
5. Enunț: Când nu mai trimiți ROLLBACK TO before_raise?
   Capcană: COMMIT păstrează savepoint-ul pentru tranzacția următoare.
   Cheie: ROLLBACK când toată tranzacția trebuie să dispară. RELEASE SAVEPOINT când vrei să ștergi semnul și să păstrezi rândurile.
   Demonstrație: rollbackToKeepsTheTransaction

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei să anulezi doar după semn -> rollbackToKeepsTheTransaction -> ROLLBACK TO before_raise -> nu eliberează lacătele de rând ținute în memorie după savepoint și eșuează dacă semnul lipsește
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> rollbackClosesEverything -> ROLLBACK -> ROLLBACK fără TO închide tranzacția. ROLLBACK TO o lasă deschisă. Lacătele de după semn nu se eliberează toate la ROLLBACK TO.
    3. Granița
           -> SAVEPOINT este propoziția. Un marcaj în editor nu este semnul din tranzacție.
    4. Alternativa
           -> ROLLBACK fără TO anulează toată tranzacția și o închide. RELEASE SAVEPOINT scoate semnul și nu anulează rânduri.
    5. Anti-patternul
           -> ROLLBACK când voiai doar întoarcerea la before_raise. Pleacă toată tranzacția.
    6. Dovada
           -> rollbackToKeepsTheTransaction ține propoziția. rollbackClosesEverything ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> ROLLBACK TO anulează după semn și lasă tranzacția deschisă. ROLLBACK o închide pe toată. Lacătele de după semn pot rămâne. ERROR 1305 înseamnă că semnul lipsește.
*/

public class SAVEPOINT_ {
    /*
    🟢 Începător
        Ce este            rândurile scrise după before_raise revin, iar tranzacția rămâne deschisă
        De ce e nevoie     fără SAVEPOINT, ROLLBACK întoarce tot ce era necomis și închide tranzacția
    🟡 Producție
        Cum se scrie       ROLLBACK TO before_raise
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este ROLLBACK, care nu numește un semn și închide tranzacția
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ROLLBACK, care nu numește un semn și închide tranzacția.
    */
    // Începător: rollbackToKeepsTheTransaction păstrează propoziția pentru rândurile scrise după before_raise revin, iar tranzacția rămâne deschisă
    // Producție: public static String rollbackToKeepsTheTransaction(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Dacă semnul nu există, serverul răspunde ERROR 1305 SAVEPOINT nu există și nu anulează nimic din această propoziție.
    public static String rollbackToKeepsTheTransaction() {
        // Începător: rândurile scrise după before_raise revin, iar tranzacția rămâne deschisă
        // Producție: ROLLBACK TO before_raise. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Dacă semnul nu există, serverul răspunde ERROR 1305 SAVEPOINT nu există și nu anulează nimic din această propoziție. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ROLLBACK TO before_raise";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide rollbackToKeepsTheTransaction. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            toată tranzacția deschisă se anulează și se închide, nu doar bucata de după semn
        De ce e nevoie     fără textul greșit, ROLLBACK pare ROLLBACK TO
    🟡 Producție
        Cum se scrie       ROLLBACK
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care toată tranzacția deschisă se anulează și se închide, nu doar bucata de după semn
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ROLLBACK, care nu numește un semn și închide tranzacția.
    */
    // Începător: rollbackClosesEverything păstrează textul greșit pentru toată tranzacția deschisă se anulează și se închide, nu doar bucata de după semn
    // Producție: public static String rollbackClosesEverything(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. ROLLBACK fără TO închide tranzacția. ROLLBACK TO o lasă deschisă. Lacătele de după semn nu se eliberează toate la ROLLBACK TO.
    public static String rollbackClosesEverything() {
        // Începător: toată tranzacția deschisă se anulează și se închide, nu doar bucata de după semn
        // Producție: ROLLBACK. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: ROLLBACK fără TO închide tranzacția. ROLLBACK TO o lasă deschisă. Lacătele de după semn nu se eliberează toate la ROLLBACK TO. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ROLLBACK";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide rollbackClosesEverything. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
