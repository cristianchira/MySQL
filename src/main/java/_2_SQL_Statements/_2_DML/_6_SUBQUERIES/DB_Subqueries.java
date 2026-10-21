package _2_SQL_Statements._2_DML._6_SUBQUERIES;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8510168#questions/3352670

//Subqueries
//https://dev.mysql.com/doc/refman/8.0/en/subqueries.html


/*                                   Subqueries with IN Nested Inside WHERE


    ▬▬▐ Subqueries = Inner Queries = Nested Queries = Inner Select
                * are queries embedded in a query.
                * subqueries are part of another query
                            - called Outer Query (Outer Select)
                * A subquery is a SELECT statement within another statement.
                * All subquery forms and operations that the SQL standard requires are supported,
                      as well as a few features that are MySQL-specific.

    ○ Here is an example of a subquery:
                 * SELECT * FROM t1 WHERE column1 = (SELECT column1 FROM t2);
                             - This query will return all rows from t1 where
                                         • the value of column1 matches the value of column1 in t2.


                                          A subquery may return


    1.Single value (a scalar)

                * SELECT
                      emp_no, salary
                   FROM
                       salaries
                  WHERE salary = (SELECT MIN(salary) FROM salaries);


    2. Single row,

    3. Single column,

    4. Entire table


                                          A subquery can be executed like


    1. Standalone query

    2. You can have a lot more than one subquery in your outer query

                   * it is possible to nest inner queries

                                 - within other inner queries

            in that case, the SQL engine would execute

                  * the innermost query first,

                  * and then each subsequent query,

                  * until it runs the outermost query last


 ○ Subqueries:


                  * allow for better structuring of the OUTER QUERY

                  * thus, each inner query can be thought of in isolation

                  * hence the name of SQL

                                – Structured Query Language!

                  * in some situations, the use of subqueries

                                - is much more intuitive

                                - compared to the use of complex joins and unions

                  * many users prefer subqueries simply because

                                - they offer enhanced code readability



                                      FROM PROCESSING POINT OF VIEW


    1) the SQL engine starts by

                   * running the INNER QUERY(SUBQUERY OR NESTED QUERY)

     2) then it uses its returned output,

                   * which is intermediate,

                   * to execute the OUTER QUERY




 */


/*
TOPIC:
    WHERE salary = (SELECT MIN(salary) FROM salaries) lasă rândurile cu salariul minim; subcererea este o interogare în alta, nu un JOIN.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să compari cu un scalar calculat. Rezultatul are salariile egale cu minimul, tabela rămâne.
MECHANISM:
    Serverul execută subcererea și folosește valoarea în cea din afară. Același rezultat ca un JOIN nu este același plan.
LOOKALIKES:
    INNER JOIN salaries s ON s.salary = employees.salary lipește tabele în același FROM. Nu întreabă minimul.
BOOK THEORY:
    Manualul 9.7, 15.2.13, deschis în tur, trimite subcererile la secțiunea lor. PDF-ul Subqueries+IN+WHERE a fost deschis. Scalarul MIN este exemplul din comentariul de curs.
BOOK EXAMPLE:
    salaryEqualsTheMinimum
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_6_SUBQUERIES/DB_Subqueries.java
BREAK LAB:
    joinIsNotTheMinimum ține SELECT a.emp_no, a.salary FROM salaries a INNER JOIN salaries b ON a.salary = b.salary. JOIN lipește în același FROM. Subcererea scalară furnizează o valoare pentru comparație.
TRADE-OFFS:
    JOIN când trebuie să aduci coloane dintr-o pereche, nu un scalar.
ANTI-PATTERNS:
    JOIN pe salary ca să găsești minimul. Găsești perechi, nu minimul.
STAFF QUESTIONS:
    salaryEqualsTheMinimum și joinIsNotTheMinimum. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Facturile egale cu minimul: WHERE amount = (SELECT MIN(amount) FROM invoice). Un JOIN pe amount lipește facturi între ele.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT emp_no, salary FROM salaries WHERE salary = (SELECT MIN(salary) FROM salaries)?
   Capcană: JOIN pe salary întoarce doar minimul.
   Cheie: salaryEqualsTheMinimum: rămân rândurile al căror salary este minimul, iar salaries rămâne. fără subcerere, nu ai minimul în același pas cu filtrul
   Demonstrație: salaryEqualsTheMinimum
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT emp_no, salary FROM salaries WHERE salary = (SELECT MIN(salary) FROM salaries)?
   Capcană: Subcererea șterge salariile care nu sunt minime.
   Cheie: Efectul este: rămân rândurile al căror salary este minimul, iar salaries rămâne. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: salaryEqualsTheMinimum
3. Enunț: De ce SELECT emp_no, salary FROM salaries WHERE salary = (SELECT MIN(salary) FROM salaries) nu este vecinul său?
   Capcană: Inner query este un INNER JOIN.
   Cheie: INNER JOIN salaries s ON s.salary = employees.salary lipește tabele în același FROM. Nu întreabă minimul.
   Demonstrație: salaryEqualsTheMinimum
4. Enunț: Cum eșuează SELECT a.emp_no, a.salary FROM salaries a INNER JOIN salaries b ON a.salary = b.salary?
   Capcană: MIN se poate pune în WHERE fără subcerere: WHERE MIN(salary) = salary.
   Cheie: joinIsNotTheMinimum: JOIN lipește în același FROM. Subcererea scalară furnizează o valoare pentru comparație.
   Demonstrație: joinIsNotTheMinimum
5. Enunț: Când nu mai trimiți SELECT emp_no, salary FROM salaries WHERE salary = (SELECT MIN(salary) FROM salaries)?
   Capcană: Planul este mereu același dacă rândurile coincid.
   Cheie: JOIN când trebuie să aduci coloane dintr-o pereche, nu un scalar.
   Demonstrație: salaryEqualsTheMinimum

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE compari cu un singur număr calculat din aceeași tabelă -> salaryEqualsTheMinimum -> SELECT emp_no, salary FROM salaries WHERE salary = (SELECT MIN(salary) FROM salaries) -> nu promite planul unui JOIN și nu șterge salariile mai mari
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> joinIsNotTheMinimum -> SELECT a.emp_no, a.salary FROM salaries a INNER JOIN salaries b ON a.salary = b.salary -> JOIN lipește în același FROM. Subcererea scalară furnizează o valoare pentru comparație.
    3. Granița
           -> Subcererea este propoziția dinăuntru. Numele inner query din curs nu o face JOIN.
    4. Alternativa
           -> INNER JOIN salaries s ON s.salary = employees.salary lipește tabele în același FROM. Nu întreabă minimul.
    5. Anti-patternul
           -> JOIN pe salary ca să găsești minimul. Găsești perechi, nu minimul.
    6. Dovada
           -> salaryEqualsTheMinimum ține propoziția. joinIsNotTheMinimum ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> Subcererea stă într-o altă interogare. JOIN stă în același FROM. Același rezultat nu este același plan. MIN în subcerere este scalarul.
*/

public class DB_Subqueries {
    /*
    🟢 Începător
        Ce este            rămân rândurile al căror salary este minimul, iar salaries rămâne
        De ce e nevoie     fără subcerere, nu ai minimul în același pas cu filtrul
    🟡 Producție
        Cum se scrie       SELECT emp_no, salary FROM salaries WHERE salary = (SELECT MIN(salary) FROM salaries)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este un JOIN pe salary, care împerechează rânduri și nu calculează MIN
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu un JOIN pe salary, care împerechează rânduri și nu calculează MIN.
    */
    // Începător: salaryEqualsTheMinimum păstrează propoziția pentru rămân rândurile al căror salary este minimul, iar salaries rămâne
    // Producție: public static String salaryEqualsTheMinimum(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Nu poți spune că același set de rânduri înseamnă același plan.
    public static String salaryEqualsTheMinimum() {
        // Începător: rămân rândurile al căror salary este minimul, iar salaries rămâne
        // Producție: SELECT emp_no, salary FROM salaries WHERE salary = (SELECT MIN(salary) FROM salaries). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Nu poți spune că același set de rânduri înseamnă același plan. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT emp_no, salary FROM salaries WHERE salary = (SELECT MIN(salary) FROM salaries)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide salaryEqualsTheMinimum. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            rămân perechile cu același salariu, nu doar salariul minim
        De ce e nevoie     fără textul greșit, JOIN pare subcererea
    🟡 Producție
        Cum se scrie       SELECT a.emp_no, a.salary FROM salaries a INNER JOIN salaries b ON a.salary = b.salary
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care rămân perechile cu același salariu, nu doar salariul minim
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu un JOIN pe salary, care împerechează rânduri și nu calculează MIN.
    */
    // Începător: joinIsNotTheMinimum păstrează textul greșit pentru rămân perechile cu același salariu, nu doar salariul minim
    // Producție: public static String joinIsNotTheMinimum(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. JOIN lipește în același FROM. Subcererea scalară furnizează o valoare pentru comparație.
    public static String joinIsNotTheMinimum() {
        // Începător: rămân perechile cu același salariu, nu doar salariul minim
        // Producție: SELECT a.emp_no, a.salary FROM salaries a INNER JOIN salaries b ON a.salary = b.salary. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: JOIN lipește în același FROM. Subcererea scalară furnizează o valoare pentru comparație. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT a.emp_no, a.salary FROM salaries a INNER JOIN salaries b ON a.salary = b.salary";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide joinIsNotTheMinimum. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
