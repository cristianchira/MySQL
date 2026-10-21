package _2_SQL_Statements._2_DML;

//Martin G. DML
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338036#overview

//DML
//  https://dev.mysql.com/doc/refman/8.0/en/sql-data-manipulation-statements.html


/*                                     Data Manipulation Language (DML)


    ○ Its statements allow us

                   * to manipulate the data in the TABLES of a database


                                        Data Manipulation Statements


    1. CALL Statement
                   * https://dev.mysql.com/doc/refman/8.0/en/call.html

    2. DELETE Statement
                   * https://dev.mysql.com/doc/refman/8.0/en/delete.html

    3. DO Statement
                   * https://dev.mysql.com/doc/refman/8.0/en/do.html

    4. HANDLER Statement
                   * https://dev.mysql.com/doc/refman/8.0/en/handler.html

    5. IMPORT TABLE Statement
                   * https://dev.mysql.com/doc/refman/8.0/en/import-table.html

    6. INSERT Statement
                   * https://dev.mysql.com/doc/refman/8.0/en/insert.html

    7. LOAD DATA Statement
                  * https://dev.mysql.com/doc/refman/8.0/en/load-data.html

    8. LOAD XML Statement
                  * https://dev.mysql.com/doc/refman/8.0/en/load-xml.html

    9. REPLACE Statement
                  *  https://dev.mysql.com/doc/refman/8.0/en/replace.html

    10. SELECT Statement
                 * https://dev.mysql.com/doc/refman/8.0/en/select.html

    11. Subqueries
                * https://dev.mysql.com/doc/refman/8.0/en/subqueries.html

    12. TABLE Statement
               * https://dev.mysql.com/doc/refman/8.0/en/table.html

    13. UPDATE Statement
               * https://dev.mysql.com/doc/refman/8.0/en/update.html

    14. VALUES Statement
              * https://dev.mysql.com/doc/refman/8.0/en/values.html

    15. WITH (Common Table Expressions)
             * https://dev.mysql.com/doc/refman/8.0/en/with.html

*/

/*
TOPIC:
    DML schimbă sau citește rânduri și lasă definiția: DELETE FROM employees WHERE first_name = 'Denis' scoate rânduri, nu tabela.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu folosești o propoziție de schemă când vrei să schimbi rânduri. employees rămâne ca obiect.
MECHANISM:
    Serverul execută DELETE ca DML. CREATE TABLE este DDL și lasă zero rânduri. Lista din comentariu include și SELECT, tot în familia de manipulare din manualul de statement-uri.
LOOKALIKES:
    CREATE TABLE employees (...) schimbă schema. TRUNCATE golește, dar este DDL.
BOOK THEORY:
    Manualul 9.7, 15.2.2 DELETE, deschis în tur: DELETE scoate rânduri. Fără WHERE le scoate pe toate. În InnoDB se poate da înapoi dacă tranzacția nu s-a închis. Nu este TRUNCATE.  Comentariul de curs păstrează linkuri către manualul 8.0. Regulile de efect folosite aici sunt din paginile 9.7 deschise pentru DELETE, INSERT, UPDATE și SELECT.
BOOK EXAMPLE:
    dmlDeletesDenis
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/DML.java
BREAK LAB:
    dropIsSchema ține DROP TABLE employees. DROP este DDL. DELETE este DML și poate avea WHERE.
TRADE-OFFS:
    DDL când trebuie să apară sau să dispară obiectul. TRUNCATE când vrei golirea clasată DDL.
ANTI-PATTERNS:
    DROP TABLE employees ca să scoți un prenume. Dispare tabela.
STAFF QUESTIONS:
    dmlDeletesDenis și dropIsSchema. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Scoți un lot greșit cu DELETE. DROP ar scoate tabela de loturi.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă DELETE FROM employees WHERE first_name = 'Denis'?
   Capcană: DROP TABLE este DML pentru că nu mai vezi rânduri.
   Cheie: dmlDeletesDenis: rândurile Denis dispar și obiectul employees rămâne. fără o propoziție DML, DDL nu scoate un rând dintr-o tabelă existentă fără să schimbe definiția sau să o golească pe toată
   Demonstrație: dmlDeletesDenis
2. Enunț: Ce rămâne în tabelă sau în sesiune după DELETE FROM employees WHERE first_name = 'Denis'?
   Capcană: DELETE scoate și definiția.
   Cheie: Efectul este: rândurile Denis dispar și obiectul employees rămâne. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: dmlDeletesDenis
3. Enunț: De ce DELETE FROM employees WHERE first_name = 'Denis' nu este vecinul său?
   Capcană: TRUNCATE este DML pentru că atinge rânduri.
   Cheie: CREATE TABLE employees (...) schimbă schema. TRUNCATE golește, dar este DDL.
   Demonstrație: dmlDeletesDenis
4. Enunț: Cum eșuează DROP TABLE employees?
   Capcană: Orice propoziție din folder schimbă schema.
   Cheie: dropIsSchema: DROP este DDL. DELETE este DML și poate avea WHERE.
   Demonstrație: dropIsSchema
5. Enunț: Când nu mai trimiți DELETE FROM employees WHERE first_name = 'Denis'?
   Capcană: Ca să schimbi rânduri, folosești CREATE.
   Cheie: DDL când trebuie să apară sau să dispară obiectul. TRUNCATE când vrei golirea clasată DDL.
   Demonstrație: dmlDeletesDenis

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE scoți rânduri și păstrezi definiția -> dmlDeletesDenis -> DELETE FROM employees WHERE first_name = 'Denis' -> nu clasifică TRUNCATE ca DML
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> dropIsSchema -> DROP TABLE employees -> DROP este DDL. DELETE este DML și poate avea WHERE.
    3. Granița
           -> DML este propoziția pe rânduri. Lista de nume din comentariu nu execută nimic.
    4. Alternativa
           -> CREATE TABLE employees (...) schimbă schema. TRUNCATE golește, dar este DDL.
    5. Anti-patternul
           -> DROP TABLE employees ca să scoți un prenume. Dispare tabela.
    6. Dovada
           -> dmlDeletesDenis ține propoziția. dropIsSchema ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DML schimbă sau citește rânduri și lasă definiția. DROP scoate obiectul. TRUNCATE golește, dar este DDL.
*/

public class DML {
    /*
    🟢 Începător
        Ce este            rândurile Denis dispar și obiectul employees rămâne
        De ce e nevoie     fără o propoziție DML, DDL nu scoate un rând dintr-o tabelă existentă fără să schimbe definiția sau să o golească pe toată
    🟡 Producție
        Cum se scrie       DELETE FROM employees WHERE first_name = 'Denis'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este DROP TABLE employees, care scoate obiectul
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu DROP TABLE employees, care scoate obiectul.
    */
    // Începător: dmlDeletesDenis păstrează propoziția pentru rândurile Denis dispar și obiectul employees rămâne
    // Producție: public static String dmlDeletesDenis(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. TRUNCATE nu se clasifică aici ca DML, deși golește rânduri.
    public static String dmlDeletesDenis() {
        // Începător: rândurile Denis dispar și obiectul employees rămâne
        // Producție: DELETE FROM employees WHERE first_name = 'Denis'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: TRUNCATE nu se clasifică aici ca DML, deși golește rânduri. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "DELETE FROM employees WHERE first_name = 'Denis'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide dmlDeletesDenis. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dispare obiectul, nu doar Denis
        De ce e nevoie     fără textul greșit, DROP pare un DELETE mai puternic din aceeași familie
    🟡 Producție
        Cum se scrie       DROP TABLE employees
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dispare obiectul, nu doar Denis
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu DROP TABLE employees, care scoate obiectul.
    */
    // Începător: dropIsSchema păstrează textul greșit pentru dispare obiectul, nu doar Denis
    // Producție: public static String dropIsSchema(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DROP este DDL. DELETE este DML și poate avea WHERE.
    public static String dropIsSchema() {
        // Începător: dispare obiectul, nu doar Denis
        // Producție: DROP TABLE employees. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: DROP este DDL. DELETE este DML și poate avea WHERE. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "DROP TABLE employees";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide dropIsSchema. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
