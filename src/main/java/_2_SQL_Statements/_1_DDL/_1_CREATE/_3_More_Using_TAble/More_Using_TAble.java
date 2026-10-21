package _2_SQL_Statements._1_DDL._1_CREATE._3_More_Using_TAble;

//Martin G. Additional note using table
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339076#questions

/*                             More using table


    ○ query

                * a command you write in SQL with the idea of either retrieving

                            - information from the database on which you are working,

                    or,alternatively,

                            - to insert,

                            - update,

                            - or delete data from it

    ○ We say that run a type of query

               * Example

                           - we run a create query

*/

/*
TOPIC:
    O interogare este propoziția pe care o execută serverul; cuvântul query din curs nu mută rânduri până nu există SQL.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu confunzi numele de curs cu efectul. După o propoziție de citire, tabela sales rămâne cu aceleași rânduri.
MECHANISM:
    Serverul execută SELECT la sfârșitul propoziției și întoarce rânduri de rezultat. Nu deschide o tabelă nouă doar pentru că ai numit textul query.
LOOKALIKES:
    CREATE TABLE schimbă schema. Cuvântul query din comentariul de curs nu este o propoziție.
BOOK THEORY:
    PDF-ul More+on+Using+Tables.pdf din folder a fost deschis în tur. Pagina CREATE TABLE și pagina SELECT din manualul 9.7 au fost deschise: SELECT citește, CREATE TABLE definește. Cursul spune că un query fie citește, fie scrie; mecanismul este propoziția concretă, nu eticheta.
BOOK EXAMPLE:
    selectPurchaseReadsRows
REPO DEMO:
    src/main/java/_2_SQL_Statements/_1_DDL/_1_CREATE/_3_More_Using_TAble/More_Using_TAble.java
BREAK LAB:
    theWordQueryIsNotSql ține query. Serverul respinge cuvântul. Tabela sales rămâne neatinsă.
TRADE-OFFS:
    CREATE TABLE când sales lipsește. INSERT când vrei un rând nou, nu o citire.
ANTI-PATTERNS:
    Să trimiți query la server. Nu se citește niciun purchase_number.
STAFF QUESTIONS:
    selectPurchaseReadsRows și theWordQueryIsNotSql. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un raport de loturi cere numerele, nu cuvântul raport. SELECT lot_no FROM lots citește. CREATE TABLE lots face obiectul și lasă raportul gol.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă propoziția față de cuvântul query?
   Capcană: Dacă ai scris query în comentariu, serverul a și citit sales.
   Cheie: selectPurchaseReadsRows este textul pe care serverul îl poate rula. Cuvântul query nu întoarce purchase_number.
   Demonstrație: selectPurchaseReadsRows
2. Enunț: Ce se întâmplă cu rândurile din sales la sfârșitul lui SELECT?
   Capcană: SELECT copiază rândurile într-o tabelă de rezultat stocată.
   Cheie: Apar rânduri de rezultat. Tabela sales rămâne. Nu se ia un lacăt după ce rezultatul a plecat.
   Demonstrație: selectPurchaseReadsRows
3. Enunț: De ce nu este CREATE TABLE?
   Capcană: Ambele se numesc query în curs, deci au același efect.
   Cheie: CREATE TABLE face obiectul și lasă zero rânduri. SELECT cere obiectul și întoarce valorile. Aceeași etichetă nu este același mecanism.
   Demonstrație: selectPurchaseReadsRows
4. Enunț: Cum eșuează dacă trimiți doar query?
   Capcană: Serverul tratează query ca pe un SELECT * implicit.
   Cheie: theWordQueryIsNotSql nu este propoziție. Nu apare un rezultat și sales nu se schimbă.
   Demonstrație: theWordQueryIsNotSql
5. Enunț: Când citești și când creezi tabela?
   Capcană: Întâi trimiți query, iar serverul alege singur dacă citește sau creează.
   Cheie: SELECT când sales există și vrei valorile. CREATE TABLE când obiectul lipsește. Nu trimite eticheta din curs.
   Demonstrație: selectPurchaseReadsRows

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei numerele din sales, nu eticheta din curs -> selectPurchaseReadsRows -> SELECT purchase_number FROM sales -> nu garantează că tabela sales există și nu schimbă rândurile
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> theWordQueryIsNotSql -> query -> Serverul respinge cuvântul. Tabela sales rămâne neatinsă.
    3. Granița
           -> Propoziția este mecanismul. Cuvântul query și click-ul de run din Workbench nu sunt efectul.
    4. Alternativa
           -> CREATE TABLE schimbă schema. Cuvântul query din comentariul de curs nu este o propoziție.
    5. Anti-patternul
           -> Să trimiți query la server. Nu se citește niciun purchase_number.
    6. Dovada
           -> selectPurchaseReadsRows ține propoziția. theWordQueryIsNotSql ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> SELECT purchase_number FROM sales citește și lasă rândurile. Cuvântul query nu este propoziția. CREATE TABLE schimbă schema, nu rezultatul.
*/

public class More_Using_TAble {
    /*
    🟢 Începător
        Ce este            rezultatul are numerele de cumpărare, iar rândurile din sales rămân
        De ce e nevoie     fără propoziție, eticheta query din curs nu întoarce niciun purchase_number
    🟡 Producție
        Cum se scrie       SELECT purchase_number FROM sales
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este CREATE TABLE sales (purchase_number INT), care face obiectul și nu citește un rând
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE sales (purchase_number INT), care face obiectul și nu citește un rând.
    */
    // Începător: selectPurchaseReadsRows păstrează propoziția pentru rezultatul are numerele de cumpărare, iar rândurile din sales rămân
    // Producție: public static String selectPurchaseReadsRows(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. SELECT nu este un nume de folder și nu garantează că sales există.
    public static String selectPurchaseReadsRows() {
        // Începător: rezultatul are numerele de cumpărare, iar rândurile din sales rămân
        // Producție: SELECT purchase_number FROM sales. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: SELECT nu este un nume de folder și nu garantează că sales există. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT purchase_number FROM sales";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide selectPurchaseReadsRows. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul nu primește o propoziție și nu întoarce rânduri
        De ce e nevoie     fără textul greșit, pare că a spune query este deja o citire
    🟡 Producție
        Cum se scrie       query
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul nu primește o propoziție și nu întoarce rânduri
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE sales (purchase_number INT), care face obiectul și nu citește un rând.
    */
    // Începător: theWordQueryIsNotSql păstrează textul greșit pentru serverul nu primește o propoziție și nu întoarce rânduri
    // Producție: public static String theWordQueryIsNotSql(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Serverul respinge cuvântul. Tabela sales rămâne neatinsă.
    public static String theWordQueryIsNotSql() {
        // Începător: serverul nu primește o propoziție și nu întoarce rânduri
        // Producție: query. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Serverul respinge cuvântul. Tabela sales rămâne neatinsă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "query";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide theWordQueryIsNotSql. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
