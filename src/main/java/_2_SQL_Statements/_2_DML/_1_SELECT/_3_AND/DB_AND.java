package _2_SQL_Statements._2_DML._1_SELECT._3_AND;

//Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8344960#overview


/*                                           AND


    ○ SELECT column_1, column_2,… column_n
       FROM table_name
       WHERE condition_1 AND condition_2;

    ○ Conditions set on DIFFERENT columns
    HISTORICAL: AND nu cere coloane diferite. Manualul 9.7, 15.2.13: WHERE este o expresie; salary > 1 AND salary < 9 este tot AND.

    ○ = equal operator

    ○ Example
               * SELECT emp_no, salary, dept_name
                 FROM employees
                 WHERE salary > 50000 AND dept_name = 'Engineering';



    ○ in SQL, there are
                   * many other linking keywords
                   * and symbols,
           called operators,
                   * that you can use with the WHERE clause
                                 - AND
                                  - OR
                                  - IN   - NOT IN
                                  - LIKE  - NOT LIKE
                                  - BETWEEN… AND…
                                  - EXISTS
                                  - IS NULL  - IS NOT NULL
                                  - NOT EXISTS
                                  - comparison operators


                                               AND query


    1. SELECT column_1, column_2,… column_n

      FROM table_name

      WHERE condition_1 AND condition_2;

 */

/*
TOPIC:
    AND cere ca ambele teste să fie adevărate pe același rând; salary > 50000 AND dept_name = 'Engineering' lasă doar rândul care le are pe amândouă.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu rămână un inginer cu salariu mic sau un salariu mare din alt departament. Rândul iese din rezultat dacă un test cade.
MECHANISM:
    Serverul evaluează AND în WHERE, pe rând, înainte de grupare. AND se leagă înaintea lui OR. Nu este o regulă că testele stau pe coloane diferite.
LOOKALIKES:
    OR păstrează rândul dacă un singur test este adevărat. HAVING AND ar filtra grupuri, nu rândul de salariu înainte de grup.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat. PDF-urile din folderele SELECT au fost deschise în tur; multe au pe prima pagină doar titlul. AND.pdf a fost deschis. Comentariul de curs spune coloane diferite; regula din manual este expresia, nu numărul de coloane.
BOOK EXAMPLE:
    andKeepsBothTests
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_3_AND/DB_AND.java
BREAK LAB:
    orKeepsEitherTest ține SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 OR dept_name = 'Engineering'. OR acceptă una dintre condiții. AND le cere pe amândouă pe același rând.
TRADE-OFFS:
    OR când este de ajuns un singur test. Paranteze când AND stă lângă OR și vrei altă legare.
ANTI-PATTERNS:
    OR în loc de AND lasă rânduri care au trecut un singur test.
STAFF QUESTIONS:
    andKeepsBothTests și orKeepsEitherTest. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un lot de vaccin trebuie să fie și neexpirat, și din depozitul rece. expiry > CURDATE() AND depot = 'COLD' le cere pe amândouă.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 AND dept_name = 'Engineering'?
   Capcană: AND se poate scrie doar pe coloane diferite, cum spune cursul.
   Cheie: andKeepsBothTests: rămâne rândul care are și salariul peste 50000, și departamentul Engineering. fără AND, un singur test lasă și salariile mari din alte departamente, sau toți inginerii
   Demonstrație: andKeepsBothTests
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 AND dept_name = 'Engineering'?
   Capcană: AND șterge rândul din tabelă dacă un test cade.
   Cheie: Efectul este: rămâne rândul care are și salariul peste 50000, și departamentul Engineering. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: andKeepsBothTests
3. Enunț: De ce SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 AND dept_name = 'Engineering' nu este vecinul său?
   Capcană: OR și AND întorc același rând, doar că OR este mai scurt.
   Cheie: OR păstrează rândul dacă un singur test este adevărat. HAVING AND ar filtra grupuri, nu rândul de salariu înainte de grup.
   Demonstrație: andKeepsBothTests
4. Enunț: Cum eșuează SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 OR dept_name = 'Engineering'?
   Capcană: salary > 50000 OR dept_name = 'Engineering' lasă doar inginerii bine plătiți.
   Cheie: orKeepsEitherTest: OR acceptă una dintre condiții. AND le cere pe amândouă pe același rând.
   Demonstrație: orKeepsEitherTest
5. Enunț: Când nu mai trimiți SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 AND dept_name = 'Engineering'?
   Capcană: Ca să legi teste pe aceeași coloană, ești obligat să folosești BETWEEN.
   Cheie: OR când este de ajuns un singur test. Paranteze când AND stă lângă OR și vrei altă legare.
   Demonstrație: andKeepsBothTests

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE rândul trebuie să treacă ambele teste -> andKeepsBothTests -> SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 AND dept_name = 'Engineering' -> nu garantează că dept_name este în employees și nu filtrează după grupare
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> orKeepsEitherTest -> SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 OR dept_name = 'Engineering' -> OR acceptă una dintre condiții. AND le cere pe amândouă pe același rând.
    3. Granița
           -> AND este operator în WHERE. Fraza din curs despre coloane diferite nu este regula serverului.
    4. Alternativa
           -> OR păstrează rândul dacă un singur test este adevărat. HAVING AND ar filtra grupuri, nu rândul de salariu înainte de grup.
    5. Anti-patternul
           -> OR în loc de AND lasă rânduri care au trecut un singur test.
    6. Dovada
           -> andKeepsBothTests ține propoziția. orKeepsEitherTest ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> AND cere ambele teste pe rând, înainte de grup. OR cere unul. AND nu obligă coloane diferite și se leagă înaintea lui OR.
*/

public class DB_AND {
    /*
    🟢 Începător
        Ce este            rămâne rândul care are și salariul peste 50000, și departamentul Engineering
        De ce e nevoie     fără AND, un singur test lasă și salariile mari din alte departamente, sau toți inginerii
    🟡 Producție
        Cum se scrie       SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 AND dept_name = 'Engineering'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este OR, care păstrează rândul la un singur test adevărat
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu OR, care păstrează rândul la un singur test adevărat.
    */
    // Începător: andKeepsBothTests păstrează propoziția pentru rămâne rândul care are și salariul peste 50000, și departamentul Engineering
    // Producție: public static String andKeepsBothTests(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. AND nu înseamnă coloane diferite. salary > 50000 AND salary < 80000 este tot AND, pe aceeași coloană.
    public static String andKeepsBothTests() {
        // Începător: rămâne rândul care are și salariul peste 50000, și departamentul Engineering
        // Producție: SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 AND dept_name = 'Engineering'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: AND nu înseamnă coloane diferite. salary > 50000 AND salary < 80000 este tot AND, pe aceeași coloană. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 AND dept_name = 'Engineering'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide andKeepsBothTests. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            rămâne și inginerul cu salariu mic, și salariul mare din alt departament
        De ce e nevoie     fără textul greșit, OR pare același legător
    🟡 Producție
        Cum se scrie       SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 OR dept_name = 'Engineering'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care rămâne și inginerul cu salariu mic, și salariul mare din alt departament
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu OR, care păstrează rândul la un singur test adevărat.
    */
    // Începător: orKeepsEitherTest păstrează textul greșit pentru rămâne și inginerul cu salariu mic, și salariul mare din alt departament
    // Producție: public static String orKeepsEitherTest(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. OR acceptă una dintre condiții. AND le cere pe amândouă pe același rând.
    public static String orKeepsEitherTest() {
        // Începător: rămâne și inginerul cu salariu mic, și salariul mare din alt departament
        // Producție: SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 OR dept_name = 'Engineering'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: OR acceptă una dintre condiții. AND le cere pe amândouă pe același rând. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT emp_no, salary, dept_name FROM employees WHERE salary > 50000 OR dept_name = 'Engineering'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide orKeepsEitherTest. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
