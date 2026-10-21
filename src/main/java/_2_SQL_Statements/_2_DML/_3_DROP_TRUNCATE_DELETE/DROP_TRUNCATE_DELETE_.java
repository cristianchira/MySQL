package _2_SQL_Statements._2_DML._3_DROP_TRUNCATE_DELETE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8376492#questions


/*                                                              DROP_TRUNCATE_DELETE


    ○ DROP

                  * you won’t be able to roll back

                                - to its initial state,

                                - or to the last COMMIT statement

                   * use DROP TABLE only when you are sure you

                                 - aren’t going to use the table in question anymore


    ○ TRUNCATE

                 * ~ DELETE without WHERE

                 * remove all records from a table

                                 - but it's structure REMAINS INTACT.

                 * AUTOINCREMENT value

                                 - WILL BE RESET and start from position 1


    ○ Delete

               * Remove RECORDS row by row

               * if where condition is omitted

                                 - will have same effect like TRUNCATE


                                          TRUNCATE vs DELETE without WHERE


    ○ The SQL optimizer will implement

              * different programmatic approaches

                            - when we are using TRUNCATE or DELETE

             * TRUNCATE delivers the output

                             - MUCH QUICKER than DELETE row by row

             * Auto-increment values are not reset with DELETE

 */


/*
TOPIC:
    DELETE, TRUNCATE și DROP nu lasă aceeași stare: DELETE scoate rânduri, TRUNCATE lasă definiția goală, DROP scoate obiectul.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să alegi ce rămâne din sales. Cele trei propoziții nu sunt sinonime.
MECHANISM:
    Serverul tratează DELETE ca DML, TRUNCATE și DROP ca DDL. TRUNCATE resetează AUTO_INCREMENT și face commit implicit. DELETE fără WHERE nu resetează contorul InnoDB la fel.
LOOKALIKES:
    Cele trei nume din folder. Le separă ce rămâne: rânduri, definiție sau nimic.
BOOK THEORY:
    Manualul 9.7, 15.2.2 DELETE, deschis în tur: DELETE scoate rânduri. Fără WHERE le scoate pe toate. În InnoDB se poate da înapoi dacă tranzacția nu s-a închis. Nu este TRUNCATE. Manualul 9.7, 15.1.44 TRUNCATE, deschis în tur: TRUNCATE este DDL, commit implicit, resetează AUTO_INCREMENT, nu se dă înapoi ca DELETE.  Comentariul de curs spune că DELETE fără WHERE are același efect ca TRUNCATE. Manualul le desparte pe commit, trigger și AUTO_INCREMENT.
BOOK EXAMPLE:
    deleteOnePurchase
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_3_DROP_TRUNCATE_DELETE/DROP_TRUNCATE_DELETE_.java
BREAK LAB:
    truncateResetsTheCounter ține TRUNCATE TABLE sales. Manualul 9.7: TRUNCATE face commit implicit și resetează AUTO_INCREMENT. DELETE fără WHERE nu este acel mecanism.
TRADE-OFFS:
    TRUNCATE când vrei gol și contor resetat. DROP când vrei să dispară obiectul.
ANTI-PATTERNS:
    TRUNCATE ca să scoți purchase_number 1. Pleacă toate rândurile.
STAFF QUESTIONS:
    deleteOnePurchase și truncateResetsTheCounter. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Retragi o cutie: DELETE. Golești tura: TRUNCATE. Scoți tabela de probă: DROP.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă DELETE FROM sales WHERE purchase_number = 1?
   Capcană: DELETE fără WHERE și TRUNCATE lasă aceeași stare, inclusiv contorul.
   Cheie: deleteOnePurchase: rândul 1 dispare și sales rămâne, cu restul rândurilor și cu definiția. fără să le desparți, TRUNCATE sau DROP fac altceva decât scoaterea rândului 1
   Demonstrație: deleteOnePurchase
2. Enunț: Ce rămâne în tabelă sau în sesiune după DELETE FROM sales WHERE purchase_number = 1?
   Capcană: DROP lasă definiția ca să poți insera din nou.
   Cheie: Efectul este: rândul 1 dispare și sales rămâne, cu restul rândurilor și cu definiția. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: deleteOnePurchase
3. Enunț: De ce DELETE FROM sales WHERE purchase_number = 1 nu este vecinul său?
   Capcană: Toate trei se dau înapoi cu ROLLBACK.
   Cheie: Cele trei nume din folder. Le separă ce rămâne: rânduri, definiție sau nimic.
   Demonstrație: deleteOnePurchase
4. Enunț: Cum eșuează TRUNCATE TABLE sales?
   Capcană: TRUNCATE poate avea WHERE purchase_number = 1.
   Cheie: truncateResetsTheCounter: Manualul 9.7: TRUNCATE face commit implicit și resetează AUTO_INCREMENT. DELETE fără WHERE nu este acel mecanism.
   Demonstrație: truncateResetsTheCounter
5. Enunț: Când nu mai trimiți DELETE FROM sales WHERE purchase_number = 1?
   Capcană: Ca să scoți un rând, TRUNCATE este mai rapid și la fel de precis.
   Cheie: TRUNCATE când vrei gol și contor resetat. DROP când vrei să dispară obiectul.
   Demonstrație: deleteOnePurchase

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE scoți un rând și păstrezi obiectul și contorul -> deleteOnePurchase -> DELETE FROM sales WHERE purchase_number = 1 -> nu golește tabela și nu o scoate din schemă
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> truncateResetsTheCounter -> TRUNCATE TABLE sales -> Manualul 9.7: TRUNCATE face commit implicit și resetează AUTO_INCREMENT. DELETE fără WHERE nu este acel mecanism.
    3. Granița
           -> Trei propoziții, trei stări. Numele folderului nu le face interschimbabile.
    4. Alternativa
           -> Cele trei nume din folder. Le separă ce rămâne: rânduri, definiție sau nimic.
    5. Anti-patternul
           -> TRUNCATE ca să scoți purchase_number 1. Pleacă toate rândurile.
    6. Dovada
           -> deleteOnePurchase ține propoziția. truncateResetsTheCounter ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DELETE scoate rânduri și poate avea WHERE. TRUNCATE lasă definiția, resetează contorul, commit implicit. DROP scoate obiectul.
*/

public class DROP_TRUNCATE_DELETE_ {
    /*
    🟢 Începător
        Ce este            rândul 1 dispare și sales rămâne, cu restul rândurilor și cu definiția
        De ce e nevoie     fără să le desparți, TRUNCATE sau DROP fac altceva decât scoaterea rândului 1
    🟡 Producție
        Cum se scrie       DELETE FROM sales WHERE purchase_number = 1
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este TRUNCATE TABLE sales, care golește tot și resetează contorul
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu TRUNCATE TABLE sales, care golește tot și resetează contorul.
    */
    // Începător: deleteOnePurchase păstrează propoziția pentru rândul 1 dispare și sales rămâne, cu restul rândurilor și cu definiția
    // Producție: public static String deleteOnePurchase(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DELETE fără WHERE nu este TRUNCATE: nu resetează AUTO_INCREMENT și se poate da înapoi într-o tranzacție InnoDB.
    public static String deleteOnePurchase() {
        // Începător: rândul 1 dispare și sales rămâne, cu restul rândurilor și cu definiția
        // Producție: DELETE FROM sales WHERE purchase_number = 1. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: DELETE fără WHERE nu este TRUNCATE: nu resetează AUTO_INCREMENT și se poate da înapoi într-o tranzacție InnoDB. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "DELETE FROM sales WHERE purchase_number = 1";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide deleteOnePurchase. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            nu rămâne niciun rând, contorul pornește de la capăt și definiția rămâne
        De ce e nevoie     fără textul greșit, același efect din curs pare regula de pe 9.7
    🟡 Producție
        Cum se scrie       TRUNCATE TABLE sales
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care nu rămâne niciun rând, contorul pornește de la capăt și definiția rămâne
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu TRUNCATE TABLE sales, care golește tot și resetează contorul.
    */
    // Începător: truncateResetsTheCounter păstrează textul greșit pentru nu rămâne niciun rând, contorul pornește de la capăt și definiția rămâne
    // Producție: public static String truncateResetsTheCounter(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Manualul 9.7: TRUNCATE face commit implicit și resetează AUTO_INCREMENT. DELETE fără WHERE nu este acel mecanism.
    public static String truncateResetsTheCounter() {
        // Începător: nu rămâne niciun rând, contorul pornește de la capăt și definiția rămâne
        // Producție: TRUNCATE TABLE sales. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Manualul 9.7: TRUNCATE face commit implicit și resetează AUTO_INCREMENT. DELETE fără WHERE nu este acel mecanism. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "TRUNCATE TABLE sales";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide truncateResetsTheCounter. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
