package _2_SQL_Statements._2_DML._3_DELETE;

// DELETE
// https://dev.mysql.com/doc/refman/8.0/en/delete.html


/*                                                  DELETE Statement


   ○ DELETE is a DML statement that

                  * remove rows from a table.

    ○ functions similarly with truncate

                   * but unlike TRUNCATE where delete all records

                   * with DELETE

                                 - you can specify precisely what you would like to be removed



    ○ A DELETE statement can start with a WITH clause to define common table expressions accessible within the DELETE.

                   * See Section 13.2.15, “WITH (Common Table Expressions)”.



                                                              SQL


    ○ DELETE FROM table_name
       WHERE conditions;

    ○ ON DELETE CASCADE

                   * if a specific value from the parent table’s primary key has been deleted,

                   * all the records from the child table referring to this value

                                 * will be removed as well




                                            Single-Table Syntax


DELETE [LOW_PRIORITY] [QUICK] [IGNORE] FROM tbl_name [[AS] tbl_alias]
    [PARTITION (partition_name [, partition_name] ...)]
    [WHERE where_condition]
    [ORDER BY ...]
    [LIMIT row_count]





 */


/*
TOPIC:
    DELETE FROM employees WHERE first_name = 'Denis' scoate doar rândurile Denis; fără WHERE, DELETE le scoate pe toate și tot este DML.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să dispară Denis, nu toți angajații. Definiția employees rămâne.
MECHANISM:
    Serverul execută DELETE și întoarce numărul de rânduri șterse. Într-o tranzacție InnoDB deschisă, ROLLBACK le poate aduce înapoi. TRUNCATE nu face asta.
LOOKALIKES:
    TRUNCATE TABLE employees golește tot, resetează AUTO_INCREMENT și face commit implicit. DROP scoate obiectul.
BOOK THEORY:
    Manualul 9.7, 15.2.2 DELETE, deschis în tur: DELETE scoate rânduri. Fără WHERE le scoate pe toate. În InnoDB se poate da înapoi dacă tranzacția nu s-a închis. Nu este TRUNCATE. Manualul 9.7, 15.1.44 TRUNCATE, deschis în tur: TRUNCATE este DDL, commit implicit, resetează AUTO_INCREMENT, nu se dă înapoi ca DELETE. 
BOOK EXAMPLE:
    deleteDenisKeepsTheTable
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_3_DELETE/DELETE.java
BREAK LAB:
    truncateIsNotAFilteredDelete ține TRUNCATE TABLE employees. TRUNCATE nu acceptă WHERE și nu se dă înapoi ca DELETE.
TRADE-OFFS:
    TRUNCATE când vrei tabela goală, fără WHERE, fără ROLLBACK și cu AUTO_INCREMENT resetat.
ANTI-PATTERNS:
    TRUNCATE când voiai doar pe Denis. Pleacă toată tabela.
STAFF QUESTIONS:
    deleteDenisKeepsTheTable și truncateIsNotAFilteredDelete. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Scoți un lot retras: DELETE FROM lot WHERE lot_no = 'L9'. TRUNCATE ar goli depozitul.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă DELETE FROM employees WHERE first_name = 'Denis'?
   Capcană: DELETE WHERE este același lucru cu TRUNCATE, doar că mai lung.
   Cheie: deleteDenisKeepsTheTable: rândurile cu prenumele Denis dispar, ceilalți angajați și definiția rămân. fără WHERE, DELETE FROM employees îi scoate pe toți
   Demonstrație: deleteDenisKeepsTheTable
2. Enunț: Ce rămâne în tabelă sau în sesiune după DELETE FROM employees WHERE first_name = 'Denis'?
   Capcană: DELETE scoate și definiția employees.
   Cheie: Efectul este: rândurile cu prenumele Denis dispar, ceilalți angajați și definiția rămân. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: deleteDenisKeepsTheTable
3. Enunț: De ce DELETE FROM employees WHERE first_name = 'Denis' nu este vecinul său?
   Capcană: ROLLBACK nu poate aduce un DELETE InnoDB necomis.
   Cheie: TRUNCATE TABLE employees golește tot, resetează AUTO_INCREMENT și face commit implicit. DROP scoate obiectul.
   Demonstrație: deleteDenisKeepsTheTable
4. Enunț: Cum eșuează TRUNCATE TABLE employees?
   Capcană: Poți DELETE din employees și SELECT din employees în aceeași subcerere.
   Cheie: truncateIsNotAFilteredDelete: TRUNCATE nu acceptă WHERE și nu se dă înapoi ca DELETE.
   Demonstrație: truncateIsNotAFilteredDelete
5. Enunț: Când nu mai trimiți DELETE FROM employees WHERE first_name = 'Denis'?
   Capcană: Fără WHERE, DELETE este refuzat, deci este sigur.
   Cheie: TRUNCATE când vrei tabela goală, fără WHERE, fără ROLLBACK și cu AUTO_INCREMENT resetat.
   Demonstrație: deleteDenisKeepsTheTable

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE scoți doar rândurile Denis și vrei să poți da înapoi dacă tranzacția e deschisă -> deleteDenisKeepsTheTable -> DELETE FROM employees WHERE first_name = 'Denis' -> nu resetează AUTO_INCREMENT și nu trece peste o subcerere pe aceeași tabelă
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> truncateIsNotAFilteredDelete -> TRUNCATE TABLE employees -> TRUNCATE nu acceptă WHERE și nu se dă înapoi ca DELETE.
    3. Granița
           -> DELETE este DML. Un click de delete în grid nu este propoziția până nu o trimite.
    4. Alternativa
           -> TRUNCATE TABLE employees golește tot, resetează AUTO_INCREMENT și face commit implicit. DROP scoate obiectul.
    5. Anti-patternul
           -> TRUNCATE când voiai doar pe Denis. Pleacă toată tabela.
    6. Dovada
           -> deleteDenisKeepsTheTable ține propoziția. truncateIsNotAFilteredDelete ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DELETE cu WHERE scoate rândurile potrivite și lasă definiția. Fără WHERE le scoate pe toate și rămâne DML. TRUNCATE este DDL și nu are WHERE.
*/

public class DELETE {
    /*
    🟢 Începător
        Ce este            rândurile cu prenumele Denis dispar, ceilalți angajați și definiția rămân
        De ce e nevoie     fără WHERE, DELETE FROM employees îi scoate pe toți
    🟡 Producție
        Cum se scrie       DELETE FROM employees WHERE first_name = 'Denis'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este TRUNCATE TABLE employees, care nu poate avea WHERE și nu se dă înapoi ca DML
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu TRUNCATE TABLE employees, care nu poate avea WHERE și nu se dă înapoi ca DML.
    */
    // Începător: deleteDenisKeepsTheTable păstrează propoziția pentru rândurile cu prenumele Denis dispar, ceilalți angajați și definiția rămân
    // Producție: public static String deleteDenisKeepsTheTable(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Nu poți șterge dintr-o tabelă și selecta din ea în aceeași subcerere.
    public static String deleteDenisKeepsTheTable() {
        // Începător: rândurile cu prenumele Denis dispar, ceilalți angajați și definiția rămân
        // Producție: DELETE FROM employees WHERE first_name = 'Denis'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Nu poți șterge dintr-o tabelă și selecta din ea în aceeași subcerere. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "DELETE FROM employees WHERE first_name = 'Denis'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide deleteDenisKeepsTheTable. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dispar toți angajații, contorul se resetează și nu rămâne un WHERE
        De ce e nevoie     fără textul greșit, TRUNCATE pare DELETE-ul cu filtru uitat
    🟡 Producție
        Cum se scrie       TRUNCATE TABLE employees
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dispar toți angajații, contorul se resetează și nu rămâne un WHERE
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu TRUNCATE TABLE employees, care nu poate avea WHERE și nu se dă înapoi ca DML.
    */
    // Începător: truncateIsNotAFilteredDelete păstrează textul greșit pentru dispar toți angajații, contorul se resetează și nu rămâne un WHERE
    // Producție: public static String truncateIsNotAFilteredDelete(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. TRUNCATE nu acceptă WHERE și nu se dă înapoi ca DELETE.
    public static String truncateIsNotAFilteredDelete() {
        // Începător: dispar toți angajații, contorul se resetează și nu rămâne un WHERE
        // Producție: TRUNCATE TABLE employees. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: TRUNCATE nu acceptă WHERE și nu se dă înapoi ca DELETE. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "TRUNCATE TABLE employees";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide truncateIsNotAFilteredDelete. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
