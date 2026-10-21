package _2_SQL_Statements._2_DML._6_SUBQUERIES.COMPLEX;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8565128#questions/3352670


/*                                                         COMPLEX


    ○ TASK

      Fill emp_manager with data about employees, the number of the department they are working in, and their managers.

        Your query skeleton must be:

          Insert INTO emp_manager SELECT

       U.*

      FROM

                 (A)

       UNION (B) UNION (C) UNION (D) AS U;

      A and B should be the same subsets used in the last lecture (SQL Subqueries Nested in SELECT and FROM).
      In other words, assign employee number 110022 as a manager to all employees from 10001 to 10020 (this must be subset A),
      and employee number 110039 as a manager to all employees from 10021 to 10040 (this must be subset B).

      Use the structure of subset A to create subset C, where you must assign employee number 110039 as a manager to
      employee 110022.

      Following the same logic, create subset D. Here you must do the opposite - assign employee 110022 as a manager
      to employee 110039.

      Your output must contain 42 rows.


    ○ ANSWER

DROP TABLE IF EXISTS emp_manager;

CREATE TABLE emp_manager (

   emp_no INT(11) NOT NULL,

   dept_no CHAR(4) NULL,

   manager_no INT(11) NOT NULL

);

    INSERT INTO emp_manager
SELECT
    u.*
FROM
    (SELECT
        a.*
    FROM
        (SELECT
        e.emp_no AS employee_ID,
            MIN(de.dept_no) AS department_code,
            (SELECT
                    emp_no
                FROM
                    dept_manager
                WHERE
                    emp_no = 110022) AS manager_ID
    FROM
        employees e
    JOIN dept_emp de ON e.emp_no = de.emp_no
    WHERE
        e.emp_no <= 10020
    GROUP BY e.emp_no
    ORDER BY e.emp_no) AS a UNION SELECT
        b.*
    FROM
        (SELECT
        e.emp_no AS employee_ID,
            MIN(de.dept_no) AS department_code,
            (SELECT
                    emp_no
                FROM
                    dept_manager
                WHERE
                    emp_no = 110039) AS manager_ID
    FROM
        employees e
    JOIN dept_emp de ON e.emp_no = de.emp_no
    WHERE
        e.emp_no > 10020
    GROUP BY e.emp_no
    ORDER BY e.emp_no
    LIMIT 20) AS b UNION SELECT
        c.*
    FROM
        (SELECT
        e.emp_no AS employee_ID,
            MIN(de.dept_no) AS department_code,
            (SELECT
                    emp_no
                FROM
                    dept_manager
                WHERE
                    emp_no = 110039) AS manager_ID
    FROM
        employees e
    JOIN dept_emp de ON e.emp_no = de.emp_no
    WHERE
        e.emp_no = 110022
    GROUP BY e.emp_no) AS c UNION SELECT
        d.*
    FROM
        (SELECT
        e.emp_no AS employee_ID,
            MIN(de.dept_no) AS department_code,
            (SELECT
                    emp_no
                FROM
                    dept_manager
                WHERE
                    emp_no = 110022) AS manager_ID
    FROM
        employees e
    JOIN dept_emp de ON e.emp_no = de.emp_no
    WHERE
        e.emp_no = 110039
    GROUP BY e.emp_no) AS d) as u;


 */



/*
TOPIC:
    O tabelă derivată în FROM este o subcerere care produce rânduri pentru INSERT, nu un JOIN scris în același nivel.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să bagi în emp_manager rândurile unui subset, de exemplu angajații 10001–10020 cu managerul 110022.
MECHANISM:
    Serverul materializează sau contopește tabelă derivată și INSERT primește rândurile ei. JOIN ar împerechea tabele în același FROM, fără acest înveliș.
LOOKALIKES:
    INNER JOIN employees pe un interval nu atribuie managerul 110022 ca valoare constantă în rezultat.
BOOK THEORY:
    PDF-ul de subcereri a fost deschis. Manualul INSERT SELECT a fost deschis. Exercițiul din comentariu rămâne; propoziția nouă este subsetul A, gata de trimis, fără INT(11).
BOOK EXAMPLE:
    insertManagedSubset
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_6_SUBQUERIES/COMPLEX/COMPLEX_.java
BREAK LAB:
    displayWidthIsNotTheSubset ține CREATE TABLE emp_manager (emp_no INT(11) NOT NULL). CREATE TABLE face obiectul. INSERT SELECT umple subsetul. Lățimea de afișare nu atribuie managerul.
TRADE-OFFS:
    JOIN când managerul vine dintr-o coloană pereche, nu dintr-o constantă.
ANTI-PATTERNS:
    CREATE TABLE cu INT(11) ca și cum ai fi rezolvat subsetul. Tabela goală nu are pe 10001.
STAFF QUESTIONS:
    insertManagedSubset și displayWidthIsNotTheSubset. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Atribui un responsabil unui interval de cutii: INSERT INTO box_owner (box_no, owner_no) SELECT box_no, 50 FROM box WHERE box_no BETWEEN 1 AND 20.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă INSERT INTO emp_manager (emp_no, dept_no, manager_no) SELECT e.emp_no, e.dept_no, 110022 FROM employees e WHERE e.emp_no BETWEEN 10001 AND 10020?
   Capcană: INT(11) este mecanismul care atribuie managerul.
   Cheie: insertManagedSubset: emp_manager primește angajații 10001–10020 cu managerul 110022. fără subset, INSERT nu are rândurile pe care le cere exercițiul
   Demonstrație: insertManagedSubset
2. Enunț: Ce rămâne în tabelă sau în sesiune după INSERT INTO emp_manager (emp_no, dept_no, manager_no) SELECT e.emp_no, e.dept_no, 110022 FROM employees e WHERE e.emp_no BETWEEN 10001 AND 10020?
   Capcană: JOIN pe emp_no scrie singur 110022.
   Cheie: Efectul este: emp_manager primește angajații 10001–10020 cu managerul 110022. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: insertManagedSubset
3. Enunț: De ce INSERT INTO emp_manager (emp_no, dept_no, manager_no) SELECT e.emp_no, e.dept_no, 110022 FROM employees e WHERE e.emp_no BETWEEN 10001 AND 10020 nu este vecinul său?
   Capcană: CREATE TABLE emp_manager umple și rândurile.
   Cheie: INNER JOIN employees pe un interval nu atribuie managerul 110022 ca valoare constantă în rezultat.
   Demonstrație: insertManagedSubset
4. Enunț: Cum eșuează CREATE TABLE emp_manager (emp_no INT(11) NOT NULL)?
   Capcană: BETWEEN 10001 AND 10020 este exclusiv la capete.
   Cheie: displayWidthIsNotTheSubset: CREATE TABLE face obiectul. INSERT SELECT umple subsetul. Lățimea de afișare nu atribuie managerul.
   Demonstrație: displayWidthIsNotTheSubset
5. Enunț: Când nu mai trimiți INSERT INTO emp_manager (emp_no, dept_no, manager_no) SELECT e.emp_no, e.dept_no, 110022 FROM employees e WHERE e.emp_no BETWEEN 10001 AND 10020?
   Capcană: Un singur subset este tot exercițiul cu 42 de rânduri.
   Cheie: JOIN când managerul vine dintr-o coloană pereche, nu dintr-o constantă.
   Demonstrație: insertManagedSubset

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei rândurile unui interval, cu un manager constant -> insertManagedSubset -> INSERT INTO emp_manager (emp_no, dept_no, manager_no) SELECT e.emp_no, e.dept_no, 110022 FROM employees e WHERE e.emp_no BETWEEN 10001 AND 10020 -> nu construiește și subseturile B, C și D din exercițiu
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> displayWidthIsNotTheSubset -> CREATE TABLE emp_manager (emp_no INT(11) NOT NULL) -> CREATE TABLE face obiectul. INSERT SELECT umple subsetul. Lățimea de afișare nu atribuie managerul.
    3. Granița
           -> Subcererea sau SELECT-ul din INSERT este propoziția. Scheletul din comentariu nu rulează până nu este SQL complet.
    4. Alternativa
           -> INNER JOIN employees pe un interval nu atribuie managerul 110022 ca valoare constantă în rezultat.
    5. Anti-patternul
           -> CREATE TABLE cu INT(11) ca și cum ai fi rezolvat subsetul. Tabela goală nu are pe 10001.
    6. Dovada
           -> insertManagedSubset ține propoziția. displayWidthIsNotTheSubset ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> INSERT SELECT umple un subset. JOIN aduce perechi. INT(11) nu este atribuirea managerului. Exercițiul complet are mai multe subseturi; aici este intervalul 10001–10020.
*/

public class COMPLEX_ {
    /*
    🟢 Începător
        Ce este            emp_manager primește angajații 10001–10020 cu managerul 110022
        De ce e nevoie     fără subset, INSERT nu are rândurile pe care le cere exercițiul
    🟡 Producție
        Cum se scrie       INSERT INTO emp_manager (emp_no, dept_no, manager_no) SELECT e.emp_no, e.dept_no, 110022 FROM employees e WHERE e.emp_no BETWEEN 10001 AND 10020
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este un JOIN care aduce coloane pereche, nu constanta 110022
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu un JOIN care aduce coloane pereche, nu constanta 110022.
    */
    // Începător: insertManagedSubset păstrează propoziția pentru emp_manager primește angajații 10001–10020 cu managerul 110022
    // Producție: public static String insertManagedSubset(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. INT(11) din curs nu este lățimea care mai definește INT pe 9.7; propoziția nouă folosește coloanele deja create.
    public static String insertManagedSubset() {
        // Începător: emp_manager primește angajații 10001–10020 cu managerul 110022
        // Producție: INSERT INTO emp_manager (emp_no, dept_no, manager_no) SELECT e.emp_no, e.dept_no, 110022 FROM employees e WHERE e.emp_no BETWEEN 10001 AND 10020. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: INT(11) din curs nu este lățimea care mai definește INT pe 9.7; propoziția nouă folosește coloanele deja create. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "INSERT INTO emp_manager (emp_no, dept_no, manager_no) SELECT e.emp_no, e.dept_no, 110022 FROM employees e WHERE e.emp_no BETWEEN 10001 AND 10020";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide insertManagedSubset. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            apare o tabelă cu o coloană, nu rândurile 10001–10020, iar (11) nu este subsetul
        De ce e nevoie     fără textul greșit, lățimea din curs pare mecanismul exercițiului
    🟡 Producție
        Cum se scrie       CREATE TABLE emp_manager (emp_no INT(11) NOT NULL)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care apare o tabelă cu o coloană, nu rândurile 10001–10020, iar (11) nu este subsetul
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu un JOIN care aduce coloane pereche, nu constanta 110022.
    */
    // Începător: displayWidthIsNotTheSubset păstrează textul greșit pentru apare o tabelă cu o coloană, nu rândurile 10001–10020, iar (11) nu este subsetul
    // Producție: public static String displayWidthIsNotTheSubset(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CREATE TABLE face obiectul. INSERT SELECT umple subsetul. Lățimea de afișare nu atribuie managerul.
    public static String displayWidthIsNotTheSubset() {
        // Începător: apare o tabelă cu o coloană, nu rândurile 10001–10020, iar (11) nu este subsetul
        // Producție: CREATE TABLE emp_manager (emp_no INT(11) NOT NULL). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: CREATE TABLE face obiectul. INSERT SELECT umple subsetul. Lățimea de afișare nu atribuie managerul. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE emp_manager (emp_no INT(11) NOT NULL)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide displayWidthIsNotTheSubset. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }



}
