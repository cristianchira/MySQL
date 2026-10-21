package _2_SQL_Statements._1_DDL._3_DROP;

// https://dev.mysql.com/doc/refman/8.0/en/drop-database.html



/*                                       DROP Statements


    ○ DROP object_type object_name;

                                               Drop database


    ○ DROP {DATABASE | SCHEMA} [IF EXISTS] db_name

    ○ https://dev.mysql.com/doc/refman/8.0/en/drop-database.html


                                                Drop table


    ○ DROP [TEMPORARY] TABLE [IF EXISTS]  tbl_name

       [, tbl_name] ...
       [RESTRICT | CASCADE]

    ○ https://dev.mysql.com/doc/refman/8.0/en/drop-table.html


 */

/*
TOPIC:
    DROP TABLE scoate obiectul sales; definiția nu mai este acolo, spre deosebire de TRUNCATE, care lasă tabela goală.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să dispară tabela, nu doar rândurile. După propoziție, sales nu mai poate fi citită.
MECHANISM:
    Serverul execută DROP TABLE la sfârșitul propoziției și scoate obiectul. IF EXISTS nu eșuează când numele lipsește.
LOOKALIKES:
    TRUNCATE TABLE sales lasă definiția și zero rânduri. DELETE FROM sales lasă definiția și, în InnoDB, se poate da înapoi dacă tranzacția e deschisă.
BOOK THEORY:
    Manualul 9.7, 15.1.44 TRUNCATE TABLE, deschis în tur: TRUNCATE lasă tabela, o golește, resetează AUTO_INCREMENT și face commit implicit. DROP scoate obiectul. Pagina DROP TABLE ca URL separat este NERULAT AICI. Comentariul de curs păstrează sintaxa DROP.
BOOK EXAMPLE:
    dropSalesRemovesTheObject
REPO DEMO:
    src/main/java/_2_SQL_Statements/_1_DDL/_3_DROP/Drop.java
BREAK LAB:
    truncateLeavesTheName ține TRUNCATE TABLE sales. TRUNCATE nu este DROP. Numele sales rămâne în schemă.
TRADE-OFFS:
    TRUNCATE când vrei sales goală și păstrezi privilegiile și definiția. DELETE când vrei doar unele rânduri și, în InnoDB, posibilitatea de ROLLBACK.
ANTI-PATTERNS:
    TRUNCATE TABLE sales când voiai să dispară numele. Următorul CREATE TABLE sales eșuează.
STAFF QUESTIONS:
    dropSalesRemovesTheObject și truncateLeavesTheName. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O tabelă de probe temporare lab_tmp trebuie să dispară după export. DROP TABLE lab_tmp scoate obiectul. TRUNCATE l-ar lăsa gol pentru următorul import din greșeală.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă DROP TABLE sales?
   Capcană: DROP TABLE lasă definiția și șterge doar rândurile, ca să poți insera imediat.
   Cheie: dropSalesRemovesTheObject: obiectul sales dispare, împreună cu rândurile și cu definiția. fără DROP, TRUNCATE ar lăsa sales goală și un CREATE TABLE sales ar eșua fiindcă numele există
   Demonstrație: dropSalesRemovesTheObject
2. Enunț: Ce rămâne în tabelă sau în sesiune după DROP TABLE sales?
   Capcană: Poți da ROLLBACK și sales revine cu rândurile, ca la DELETE.
   Cheie: Efectul este: obiectul sales dispare, împreună cu rândurile și cu definiția. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: dropSalesRemovesTheObject
3. Enunț: De ce DROP TABLE sales nu este vecinul său?
   Capcană: TRUNCATE și DROP sunt același lucru pentru că ambele golesc.
   Cheie: TRUNCATE TABLE sales lasă definiția și zero rânduri. DELETE FROM sales lasă definiția și, în InnoDB, se poate da înapoi dacă tranzacția e deschisă.
   Demonstrație: dropSalesRemovesTheObject
4. Enunț: Cum eșuează TRUNCATE TABLE sales?
   Capcană: TRUNCATE TABLE sales scoate numele din schemă.
   Cheie: truncateLeavesTheName: TRUNCATE nu este DROP. Numele sales rămâne în schemă.
   Demonstrație: truncateLeavesTheName
5. Enunț: Când nu mai trimiți DROP TABLE sales?
   Capcană: Ca să dispară tabela, trimiți DELETE FROM sales fără WHERE.
   Cheie: TRUNCATE când vrei sales goală și păstrezi privilegiile și definiția. DELETE când vrei doar unele rânduri și, în InnoDB, posibilitatea de ROLLBACK.
   Demonstrație: dropSalesRemovesTheObject

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE nu mai vrei obiectul sales -> dropSalesRemovesTheObject -> DROP TABLE sales -> nu garantează că un nume calificat din altă sesiune nu mai are un plan în cache și nu anulează un DELETE deja comis
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> truncateLeavesTheName -> TRUNCATE TABLE sales -> TRUNCATE nu este DROP. Numele sales rămâne în schemă.
    3. Granița
           -> DROP TABLE este DDL. Ștergerea din arborele Workbench este click până când trimite DROP.
    4. Alternativa
           -> TRUNCATE TABLE sales lasă definiția și zero rânduri. DELETE FROM sales lasă definiția și, în InnoDB, se poate da înapoi dacă tranzacția e deschisă.
    5. Anti-patternul
           -> TRUNCATE TABLE sales când voiai să dispară numele. Următorul CREATE TABLE sales eșuează.
    6. Dovada
           -> dropSalesRemovesTheObject ține propoziția. truncateLeavesTheName ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DROP TABLE sales scoate obiectul. TRUNCATE lasă definiția și resetează AUTO_INCREMENT. DELETE scoate rânduri și poate sta într-o tranzacție InnoDB.
*/

public class Drop {
    /*
    🟢 Începător
        Ce este            obiectul sales dispare, împreună cu rândurile și cu definiția
        De ce e nevoie     fără DROP, TRUNCATE ar lăsa sales goală și un CREATE TABLE sales ar eșua fiindcă numele există
    🟡 Producție
        Cum se scrie       DROP TABLE sales
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este TRUNCATE TABLE sales, care lasă definiția
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu TRUNCATE TABLE sales, care lasă definiția.
    */
    // Începător: dropSalesRemovesTheObject păstrează propoziția pentru obiectul sales dispare, împreună cu rândurile și cu definiția
    // Producție: public static String dropSalesRemovesTheObject(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DROP nu se dă înapoi ca un DELETE InnoDB. IF EXISTS schimbă eșecul când tabela lipsește, nu aduce tabela înapoi.
    public static String dropSalesRemovesTheObject() {
        // Începător: obiectul sales dispare, împreună cu rândurile și cu definiția
        // Producție: DROP TABLE sales. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: DROP nu se dă înapoi ca un DELETE InnoDB. IF EXISTS schimbă eșecul când tabela lipsește, nu aduce tabela înapoi. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "DROP TABLE sales";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide dropSalesRemovesTheObject. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            sales rămâne, goală, iar un CREATE TABLE sales cu același nume eșuează
        De ce e nevoie     fără textul greșit, pare că a goli tabela scoate și numele
    🟡 Producție
        Cum se scrie       TRUNCATE TABLE sales
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care sales rămâne, goală, iar un CREATE TABLE sales cu același nume eșuează
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu TRUNCATE TABLE sales, care lasă definiția.
    */
    // Începător: truncateLeavesTheName păstrează textul greșit pentru sales rămâne, goală, iar un CREATE TABLE sales cu același nume eșuează
    // Producție: public static String truncateLeavesTheName(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. TRUNCATE nu este DROP. Numele sales rămâne în schemă.
    public static String truncateLeavesTheName() {
        // Începător: sales rămâne, goală, iar un CREATE TABLE sales cu același nume eșuează
        // Producție: TRUNCATE TABLE sales. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: TRUNCATE nu este DROP. Numele sales rămâne în schemă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "TRUNCATE TABLE sales";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide truncateLeavesTheName. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
