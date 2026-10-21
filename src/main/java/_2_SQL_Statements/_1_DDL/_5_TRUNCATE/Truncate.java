package _2_SQL_Statements._1_DDL._5_TRUNCATE;


// https://dev.mysql.com/doc/refman/8.0/en/truncate-table.html


/*                                            Truncate table statement


    ○ TRUNCATE [TABLE] tbl_name

    ○ TRUNCATE TABLE empties a table completely.

                  * It requires the DROP privilege.

          Logically, TRUNCATE TABLE

                  * is similar to a DELETE statement that deletes all rows,

                  * or a sequence of DROP TABLE

                  * and CREATE TABLE statements.



 */

/*
TOPIC:
    TRUNCATE TABLE sales golește tabela, lasă definiția, resetează AUTO_INCREMENT și nu se dă înapoi ca un DELETE.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să dispară toate rândurile și să rămână sales. După propoziție, tabela este goală și numărul automat pornește de la început.
MECHANISM:
    Serverul execută TRUNCATE ca DDL: commit implicit, nu pornește trigger-ele ON DELETE, eșuează dacă altă tabelă InnoDB o referă prin FOREIGN KEY.
LOOKALIKES:
    DELETE FROM sales șterge rând cu rând, poate avea WHERE și, într-o tranzacție InnoDB, se poate da înapoi. DROP TABLE sales scoate și definiția.
BOOK THEORY:
    Manualul 9.7, 15.1.44, deschis în tur: TRUNCATE golește tabela, cere privilegiul DROP, ocolește ștergerea DML, nu aprinde ON DELETE, nu se dă înapoi ca DML, face commit implicit și resetează AUTO_INCREMENT. PDF local de truncate nu a fost separat de comentariul de curs, care spune același lucru și rămâne.
BOOK EXAMPLE:
    truncateSalesKeepsDefinition
REPO DEMO:
    src/main/java/_2_SQL_Statements/_1_DDL/_5_TRUNCATE/Truncate.java
BREAK LAB:
    deleteAllStillRollsBack ține DELETE FROM sales. DELETE este DML. Manualul 9.7 spune că TRUNCATE nu se dă înapoi ca o operație DML și resetează AUTO_INCREMENT.
TRADE-OFFS:
    DELETE FROM sales WHERE ... când trebuie să rămână o parte din rânduri sau să poți da ROLLBACK. DROP când trebuie să dispară și definiția.
ANTI-PATTERNS:
    DELETE FROM sales crezând că ai resetat AUTO_INCREMENT și că nu mai poți da înapoi.
STAFF QUESTIONS:
    truncateSalesKeepsDefinition și deleteAllStillRollsBack. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Tabela de scanări de la poartă trebuie golită la miezul nopții, dar definiția rămâne pentru tura următoare. TRUNCATE TABLE gate_scan o golește. DELETE ar putea fi dat înapoi și ar lăsa contorul.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă TRUNCATE TABLE sales?
   Capcană: TRUNCATE este un DELETE fără WHERE, deci ROLLBACK aduce rândurile.
   Cheie: truncateSalesKeepsDefinition: sales rămâne, fără rânduri, iar AUTO_INCREMENT se întoarce la început. fără TRUNCATE, DELETE fără WHERE într-o tranzacție încă se poate da înapoi și poate lăsa AUTO_INCREMENT mai departe
   Demonstrație: truncateSalesKeepsDefinition
2. Enunț: Ce rămâne în tabelă sau în sesiune după TRUNCATE TABLE sales?
   Capcană: AUTO_INCREMENT rămâne la ultimul număr, ca la DELETE.
   Cheie: Efectul este: sales rămâne, fără rânduri, iar AUTO_INCREMENT se întoarce la început. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: truncateSalesKeepsDefinition
3. Enunț: De ce TRUNCATE TABLE sales nu este vecinul său?
   Capcană: DROP TABLE sales este golirea, pentru că nu mai vezi rânduri.
   Cheie: DELETE FROM sales șterge rând cu rând, poate avea WHERE și, într-o tranzacție InnoDB, se poate da înapoi. DROP TABLE sales scoate și definiția.
   Demonstrație: truncateSalesKeepsDefinition
4. Enunț: Cum eșuează DELETE FROM sales?
   Capcană: DELETE FROM sales resetează contorul și nu poate fi dat înapoi.
   Cheie: deleteAllStillRollsBack: DELETE este DML. Manualul 9.7 spune că TRUNCATE nu se dă înapoi ca o operație DML și resetează AUTO_INCREMENT.
   Demonstrație: deleteAllStillRollsBack
5. Enunț: Când nu mai trimiți TRUNCATE TABLE sales?
   Capcană: Ca să golești o tabelă referită de un copil, TRUNCATE este mereu mai sigur decât DELETE.
   Cheie: DELETE FROM sales WHERE ... când trebuie să rămână o parte din rânduri sau să poți da ROLLBACK. DROP când trebuie să dispară și definiția.
   Demonstrație: truncateSalesKeepsDefinition

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei sales goală, cu definiția păstrată, și nu vrei să dai înapoi -> truncateSalesKeepsDefinition -> TRUNCATE TABLE sales -> nu garantează un număr de rânduri afectate și nu trece peste un FOREIGN KEY care o referă
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> deleteAllStillRollsBack -> DELETE FROM sales -> DELETE este DML. Manualul 9.7 spune că TRUNCATE nu se dă înapoi ca o operație DML și resetează AUTO_INCREMENT.
    3. Granița
           -> TRUNCATE este DDL pe server. Un buton de empty în client nu este propoziția.
    4. Alternativa
           -> DELETE FROM sales șterge rând cu rând, poate avea WHERE și, într-o tranzacție InnoDB, se poate da înapoi. DROP TABLE sales scoate și definiția.
    5. Anti-patternul
           -> DELETE FROM sales crezând că ai resetat AUTO_INCREMENT și că nu mai poți da înapoi.
    6. Dovada
           -> truncateSalesKeepsDefinition ține propoziția. deleteAllStillRollsBack ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> TRUNCATE lasă definiția, golește, resetează AUTO_INCREMENT și face commit implicit. DELETE poate avea WHERE și se poate da înapoi în InnoDB. DROP scoate obiectul.
*/

public class Truncate {
    /*
    🟢 Începător
        Ce este            sales rămâne, fără rânduri, iar AUTO_INCREMENT se întoarce la început
        De ce e nevoie     fără TRUNCATE, DELETE fără WHERE într-o tranzacție încă se poate da înapoi și poate lăsa AUTO_INCREMENT mai departe
    🟡 Producție
        Cum se scrie       TRUNCATE TABLE sales
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este DELETE FROM sales, care este DML și poate avea WHERE
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu DELETE FROM sales, care este DML și poate avea WHERE.
    */
    // Începător: truncateSalesKeepsDefinition păstrează propoziția pentru sales rămâne, fără rânduri, iar AUTO_INCREMENT se întoarce la început
    // Producție: public static String truncateSalesKeepsDefinition(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. TRUNCATE nu trece dacă un FOREIGN KEY din altă tabelă InnoDB referă sales. Nu întoarce un număr adevărat de rânduri șterse.
    public static String truncateSalesKeepsDefinition() {
        // Începător: sales rămâne, fără rânduri, iar AUTO_INCREMENT se întoarce la început
        // Producție: TRUNCATE TABLE sales. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: TRUNCATE nu trece dacă un FOREIGN KEY din altă tabelă InnoDB referă sales. Nu întoarce un număr adevărat de rânduri șterse. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "TRUNCATE TABLE sales";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide truncateSalesKeepsDefinition. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            într-o tranzacție InnoDB deschisă, rândurile pot reveni la ROLLBACK și AUTO_INCREMENT nu se resetează ca la TRUNCATE
        De ce e nevoie     fără textul greșit, pare că DELETE fără WHERE este deja TRUNCATE
    🟡 Producție
        Cum se scrie       DELETE FROM sales
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care într-o tranzacție InnoDB deschisă, rândurile pot reveni la ROLLBACK și AUTO_INCREMENT nu se resetează ca la TRUNCATE
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu DELETE FROM sales, care este DML și poate avea WHERE.
    */
    // Începător: deleteAllStillRollsBack păstrează textul greșit pentru într-o tranzacție InnoDB deschisă, rândurile pot reveni la ROLLBACK și AUTO_INCREMENT nu se resetează ca la TRUNCATE
    // Producție: public static String deleteAllStillRollsBack(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DELETE este DML. Manualul 9.7 spune că TRUNCATE nu se dă înapoi ca o operație DML și resetează AUTO_INCREMENT.
    public static String deleteAllStillRollsBack() {
        // Începător: într-o tranzacție InnoDB deschisă, rândurile pot reveni la ROLLBACK și AUTO_INCREMENT nu se resetează ca la TRUNCATE
        // Producție: DELETE FROM sales. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: DELETE este DML. Manualul 9.7 spune că TRUNCATE nu se dă înapoi ca o operație DML și resetează AUTO_INCREMENT. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "DELETE FROM sales";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide deleteAllStillRollsBack. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
