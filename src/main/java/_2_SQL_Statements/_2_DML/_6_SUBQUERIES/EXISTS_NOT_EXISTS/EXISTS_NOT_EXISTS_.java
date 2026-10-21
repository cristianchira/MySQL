package _2_SQL_Statements._2_DML._6_SUBQUERIES.EXISTS_NOT_EXISTS;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8565124#questions/3352670


/*                            SQL Subqueries with EXISTS-NOT EXISTS Nested Inside WHERE


    ○ Checks whether certain row values are found within a subquery

                   * this check is conducted row by row

                   * it returns a Boolean value


   1. If a row value of a subquery   EXISTS            ->  TRUE    ->     the corresponding record of
                                                                          the outer query is extracted

   2. If a row value of a subquery DOESN'T EXISTS     ->  FALSE   ->     no row value from the outer
                                                                         query is extracted

   ○ ORDER BY (nested queries)

                 * it is more professional to apply ORDER BY

                               - in the OUTER QUERY

                 * it is more acceptable logically

                                - to sort the final version of your dataset


    ○ Some, though not all,

                * NESTED QUERIES can be rewritten

                                - using JOINs, which are more efficient in general

                * this is true particularly for INNER queries using

                                - the WHERE clause


                                          EXAMPLE


    ○ SELECT
           e.first_name, e.last_name
       FROM
          employees e
      WHERE
          EXISTS( SELECT
                *
            FROM
                dept_manager dm
            WHERE
                dm.emp_no = e.emp_no);


                    1.  EXISTS                                    2.     IN


               tests row values for existence                   searches among values

                quicker in retrieving                          faster with smaller datasets
                large amounts of data



    1. SELECT
          e.first_name, e.last_name
      FROM
         employees e
      WHERE

          EXISTS( SELECT
                *
            FROM
                dept_manager dm
            WHERE
                dm.emp_no = e.em);


    2. SELECT
          e.first_name, e.last_name
      FROM
          employees e
      WHERE
          emp_no IN (SELECT
                dm.emp_no
            FROM
                dept_manager dm);





 */

/*
TOPIC:
    EXISTS răspunde dacă subcererea întoarce un rând; nu compară first_name cu o listă, cum face IN.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să rămână angajatul care are un rând în dept_manager. Cine nu are manager în acea tabelă cade din rezultat.
MECHANISM:
    Serverul evaluează EXISTS pe fiecare rând din afară. TRUE dacă subcererea dă un rând. Nu îi pasă de valorile din lista selectată, de aceea SELECT 1 este de ajuns.
LOOKALIKES:
    IN compară o valoare cu o listă. NULL în listă schimbă IN, nu întrebarea EXISTS.
BOOK THEORY:
    PDF-ul Subqueries+EXISTS+WHERE a fost deschis. Manualul SELECT a fost deschis; pagina EXISTS separată este NERULAT AICI. Comentariul de curs spune că EXISTS întoarce boolean pe rând.
BOOK EXAMPLE:
    existsKeepsManagedEmployees
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_6_SUBQUERIES/EXISTS_NOT_EXISTS/EXISTS_NOT_EXISTS_.java
BREAK LAB:
    inListIsNotExistence ține SELECT e.first_name, e.last_name FROM employees e WHERE e.first_name IN ('Cathie', 'Mark'). IN compară o valoare cu o listă. EXISTS întreabă dacă subcererea întoarce un rând.
TRADE-OFFS:
    IN când compari cu o listă de literali și nu cauți un rând pereche.
ANTI-PATTERNS:
    IN pe prenume ca să afli cine este manager. Nu te uiți în dept_manager.
STAFF QUESTIONS:
    existsKeepsManagedEmployees și inListIsNotExistence. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un lot rămâne dacă există o cutie: EXISTS (SELECT 1 FROM box b WHERE b.lot_no = lot.lot_no). IN ('A','B') nu întreabă asta.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT e.first_name, e.last_name FROM employees e WHERE EXISTS (SELECT 1 FROM dept_manager dm WHERE dm.emp_no = e.emp_no)?
   Capcană: EXISTS compară first_name cu lista din subcerere, ca IN.
   Cheie: existsKeepsManagedEmployees: rămân angajații pentru care dept_manager are un rând. fără EXISTS, IN pe o listă de nume nu întreabă dacă există rândul de manager
   Demonstrație: existsKeepsManagedEmployees
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT e.first_name, e.last_name FROM employees e WHERE EXISTS (SELECT 1 FROM dept_manager dm WHERE dm.emp_no = e.emp_no)?
   Capcană: EXISTS șterge angajații fără manager.
   Cheie: Efectul este: rămân angajații pentru care dept_manager are un rând. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: existsKeepsManagedEmployees
3. Enunț: De ce SELECT e.first_name, e.last_name FROM employees e WHERE EXISTS (SELECT 1 FROM dept_manager dm WHERE dm.emp_no = e.emp_no) nu este vecinul său?
   Capcană: IN ('Cathie','Mark') răspunde dacă există un manager.
   Cheie: IN compară o valoare cu o listă. NULL în listă schimbă IN, nu întrebarea EXISTS.
   Demonstrație: existsKeepsManagedEmployees
4. Enunț: Cum eșuează SELECT e.first_name, e.last_name FROM employees e WHERE e.first_name IN ('Cathie', 'Mark')?
   Capcană: SELECT * în EXISTS este obligatoriu, altfel testul eșuează.
   Cheie: inListIsNotExistence: IN compară o valoare cu o listă. EXISTS întreabă dacă subcererea întoarce un rând.
   Demonstrație: inListIsNotExistence
5. Enunț: Când nu mai trimiți SELECT e.first_name, e.last_name FROM employees e WHERE EXISTS (SELECT 1 FROM dept_manager dm WHERE dm.emp_no = e.emp_no)?
   Capcană: ORDER BY din subcerere ordonează angajații din afară.
   Cheie: IN când compari cu o listă de literali și nu cauți un rând pereche.
   Demonstrație: existsKeepsManagedEmployees

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE întrebarea este dacă există un rând pereche, nu dacă valoarea este într-o listă -> existsKeepsManagedEmployees -> SELECT e.first_name, e.last_name FROM employees e WHERE EXISTS (SELECT 1 FROM dept_manager dm WHERE dm.emp_no = e.emp_no) -> nu garantează că un JOIN ar avea același plan
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> inListIsNotExistence -> SELECT e.first_name, e.last_name FROM employees e WHERE e.first_name IN ('Cathie', 'Mark') -> IN compară o valoare cu o listă. EXISTS întreabă dacă subcererea întoarce un rând.
    3. Granița
           -> EXISTS este predicatul. Un semn de check din client nu este propoziția.
    4. Alternativa
           -> IN compară o valoare cu o listă. NULL în listă schimbă IN, nu întrebarea EXISTS.
    5. Anti-patternul
           -> IN pe prenume ca să afli cine este manager. Nu te uiți în dept_manager.
    6. Dovada
           -> existsKeepsManagedEmployees ține propoziția. inListIsNotExistence ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> EXISTS întreabă dacă subcererea dă un rând. IN compară cu o listă. NULL în listă schimbă IN, nu EXISTS. ORDER BY dinăuntru nu sortează rezultatul din afară.
*/

public class EXISTS_NOT_EXISTS_ {
    /*
    🟢 Începător
        Ce este            rămân angajații pentru care dept_manager are un rând
        De ce e nevoie     fără EXISTS, IN pe o listă de nume nu întreabă dacă există rândul de manager
    🟡 Producție
        Cum se scrie       SELECT e.first_name, e.last_name FROM employees e WHERE EXISTS (SELECT 1 FROM dept_manager dm WHERE dm.emp_no = e.emp_no)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este first_name IN ('Cathie','Mark'), care compară cu o listă
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu first_name IN ('Cathie','Mark'), care compară cu o listă.
    */
    // Începător: existsKeepsManagedEmployees păstrează propoziția pentru rămân angajații pentru care dept_manager are un rând
    // Producție: public static String existsKeepsManagedEmployees(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. ORDER BY în subcererea EXISTS nu ordonează rezultatul din afară.
    public static String existsKeepsManagedEmployees() {
        // Începător: rămân angajații pentru care dept_manager are un rând
        // Producție: SELECT e.first_name, e.last_name FROM employees e WHERE EXISTS (SELECT 1 FROM dept_manager dm WHERE dm.emp_no = e.emp_no). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: ORDER BY în subcererea EXISTS nu ordonează rezultatul din afară. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT e.first_name, e.last_name FROM employees e WHERE EXISTS (SELECT 1 FROM dept_manager dm WHERE dm.emp_no = e.emp_no)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide existsKeepsManagedEmployees. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            rămân două prenume, chiar dacă nu au rând în dept_manager
        De ce e nevoie     fără textul greșit, IN pare testul de existență
    🟡 Producție
        Cum se scrie       SELECT e.first_name, e.last_name FROM employees e WHERE e.first_name IN ('Cathie', 'Mark')
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care rămân două prenume, chiar dacă nu au rând în dept_manager
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu first_name IN ('Cathie','Mark'), care compară cu o listă.
    */
    // Începător: inListIsNotExistence păstrează textul greșit pentru rămân două prenume, chiar dacă nu au rând în dept_manager
    // Producție: public static String inListIsNotExistence(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. IN compară o valoare cu o listă. EXISTS întreabă dacă subcererea întoarce un rând.
    public static String inListIsNotExistence() {
        // Începător: rămân două prenume, chiar dacă nu au rând în dept_manager
        // Producție: SELECT e.first_name, e.last_name FROM employees e WHERE e.first_name IN ('Cathie', 'Mark'). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: IN compară o valoare cu o listă. EXISTS întreabă dacă subcererea întoarce un rând. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT e.first_name, e.last_name FROM employees e WHERE e.first_name IN ('Cathie', 'Mark')";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide inListIsNotExistence. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
