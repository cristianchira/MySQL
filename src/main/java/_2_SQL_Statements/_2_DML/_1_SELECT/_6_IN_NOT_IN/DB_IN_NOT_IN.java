package _2_SQL_Statements._2_DML._1_SELECT._6_IN_NOT_IN;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345098#overview


/*                                    IN NOT IN

                                 ▬▬ IN query ▬▬


    ▬▬▐ IN operator in SQL can be used to
                * replace multiple OR operators when checking if
                            - a column's value matches
                            - any value in a given set.

    ○ SELECT * FROM employees
       WHERE first_name
       IN ('Cathie' , 'Mark', 'Nathan');


                                          ▬▬ NOT IN query ▬▬


    ○ SELECT * FROM employees
       WHERE first_name
       NOT IN ('Cathie' , 'Mark', 'Nathan');



                                         ▬▬ NOTE ▬▬


    ○ IN structure
                   * is quicker than
                   *  the OR structure.
           When applied to larger data sets,
                   * it will grow(differences between execution time)
           Minimizing execution time on query
                   * is a primary task for SQL professionals.

    ○ when many conditions are applied in a query
                  * IN operator is preferred


 */

/*
TOPIC:
    IN ('Cathie','Mark','Nathan') păstrează rândul al cărui first_name este în listă; NOT IN îl scoate din rezultat.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu scrii trei OR pe aceeași coloană. Lista goală de potriviri lasă rezultatul fără acele prenume.
MECHANISM:
    Serverul compară valoarea cu lista din WHERE. NULL în listă schimbă IN: o comparație cu NULL nu este adevărată.
LOOKALIKES:
    EXISTS întreabă dacă subcererea întoarce un rând. Trei OR pe first_name sunt același filtru de listă doar când nu apare NULL.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat. PDF-urile din folderele SELECT au fost deschise în tur; multe au pe prima pagină doar titlul. Comentariul de curs spune că IN este mai rapid decât OR. Manualul deschis nu spune că IN este mereu un alt plan. Viteza rămâne la EXPLAIN, nu la operator.
BOOK EXAMPLE:
    inKeepsThreeNames
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_6_IN_NOT_IN/DB_IN_NOT_IN.java
BREAK LAB:
    notInWithNullDropsRows ține SELECT * FROM employees WHERE first_name NOT IN ('Cathie', 'Mark', NULL). NULL în IN sau NOT IN nu este un prenume. EXISTS nu are această listă.
TRADE-OFFS:
    EXISTS când întrebarea este dacă o subcerere întoarce un rând, nu dacă valoarea este în listă.
ANTI-PATTERNS:
    NOT IN cu NULL în listă. Rezultatul nu mai este restul prenumelelor.
STAFF QUESTIONS:
    inKeepsThreeNames și notInWithNullDropsRows. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un ghișeu lasă doar formularele A, B și C. IN ('A','B','C') este lista. EXISTS ar întreba dacă există o plată, nu dacă codul este în listă.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT * FROM employees WHERE first_name IN ('Cathie', 'Mark', 'Nathan')?
   Capcană: IN este mereu mai rapid decât OR, deci planul este deja ales.
   Cheie: inKeepsThreeNames: rămân doar rândurile cu unul din cele trei prenume. fără IN, trei egalități legate cu OR sunt mai lungi și, cu NULL, nu mai sunt același test
   Demonstrație: inKeepsThreeNames
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT * FROM employees WHERE first_name IN ('Cathie', 'Mark', 'Nathan')?
   Capcană: IN șterge din employees prenumele din afara listei.
   Cheie: Efectul este: rămân doar rândurile cu unul din cele trei prenume. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: inKeepsThreeNames
3. Enunț: De ce SELECT * FROM employees WHERE first_name IN ('Cathie', 'Mark', 'Nathan') nu este vecinul său?
   Capcană: EXISTS și IN sunt același predicat, pentru că ambele stau în WHERE.
   Cheie: EXISTS întreabă dacă subcererea întoarce un rând. Trei OR pe first_name sunt același filtru de listă doar când nu apare NULL.
   Demonstrație: inKeepsThreeNames
4. Enunț: Cum eșuează SELECT * FROM employees WHERE first_name NOT IN ('Cathie', 'Mark', NULL)?
   Capcană: NULL în NOT IN este ignorat și rămân celelalte prenume.
   Cheie: notInWithNullDropsRows: NULL în IN sau NOT IN nu este un prenume. EXISTS nu are această listă.
   Demonstrație: notInWithNullDropsRows
5. Enunț: Când nu mai trimiți SELECT * FROM employees WHERE first_name IN ('Cathie', 'Mark', 'Nathan')?
   Capcană: Ca să testezi o listă, ești obligat să faci un JOIN.
   Cheie: EXISTS când întrebarea este dacă o subcerere întoarce un rând, nu dacă valoarea este în listă.
   Demonstrație: inKeepsThreeNames

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE compari o coloană cu o listă de valori cunoscute, fără NULL -> inKeepsThreeNames -> SELECT * FROM employees WHERE first_name IN ('Cathie', 'Mark', 'Nathan') -> nu garantează un plan mai scurt decât OR și nu ține loc de EXISTS
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> notInWithNullDropsRows -> SELECT * FROM employees WHERE first_name NOT IN ('Cathie', 'Mark', NULL) -> NULL în IN sau NOT IN nu este un prenume. EXISTS nu are această listă.
    3. Granița
           -> IN este predicatul. Afirmația din curs că IN este mereu mai rapid nu este planul.
    4. Alternativa
           -> EXISTS întreabă dacă subcererea întoarce un rând. Trei OR pe first_name sunt același filtru de listă doar când nu apare NULL.
    5. Anti-patternul
           -> NOT IN cu NULL în listă. Rezultatul nu mai este restul prenumelelor.
    6. Dovada
           -> inKeepsThreeNames ține propoziția. notInWithNullDropsRows ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> IN compară cu o listă. EXISTS întreabă dacă subcererea dă un rând. NULL în NOT IN nu lasă restul rândurilor. Viteza nu este promisă de operator.
*/

public class DB_IN_NOT_IN {
    /*
    🟢 Începător
        Ce este            rămân doar rândurile cu unul din cele trei prenume
        De ce e nevoie     fără IN, trei egalități legate cu OR sunt mai lungi și, cu NULL, nu mai sunt același test
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE first_name IN ('Cathie', 'Mark', 'Nathan')
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este EXISTS (SELECT 1 FROM departments d WHERE d.emp_no = employees.emp_no), care întreabă dacă există un rând, nu dacă valoarea este în listă
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu EXISTS (SELECT 1 FROM departments d WHERE d.emp_no = employees.emp_no), care întreabă dacă există un rând, nu dacă valoarea este în listă.
    */
    // Începător: inKeepsThreeNames păstrează propoziția pentru rămân doar rândurile cu unul din cele trei prenume
    // Producție: public static String inKeepsThreeNames(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. NOT IN cu NULL în listă nu păstrează rândurile pe care le aștepți, pentru că comparația cu NULL nu este adevărată.
    public static String inKeepsThreeNames() {
        // Începător: rămân doar rândurile cu unul din cele trei prenume
        // Producție: SELECT * FROM employees WHERE first_name IN ('Cathie', 'Mark', 'Nathan'). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: NOT IN cu NULL în listă nu păstrează rândurile pe care le aștepți, pentru că comparația cu NULL nu este adevărată. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE first_name IN ('Cathie', 'Mark', 'Nathan')";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide inKeepsThreeNames. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            condiția nu rămâne adevărată pentru prenumele din afara listei, iar rezultatul se golește
        De ce e nevoie     fără textul greșit, NULL în listă pare un element ignorat
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE first_name NOT IN ('Cathie', 'Mark', NULL)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care condiția nu rămâne adevărată pentru prenumele din afara listei, iar rezultatul se golește
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu EXISTS (SELECT 1 FROM departments d WHERE d.emp_no = employees.emp_no), care întreabă dacă există un rând, nu dacă valoarea este în listă.
    */
    // Începător: notInWithNullDropsRows păstrează textul greșit pentru condiția nu rămâne adevărată pentru prenumele din afara listei, iar rezultatul se golește
    // Producție: public static String notInWithNullDropsRows(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. NULL în IN sau NOT IN nu este un prenume. EXISTS nu are această listă.
    public static String notInWithNullDropsRows() {
        // Începător: condiția nu rămâne adevărată pentru prenumele din afara listei, iar rezultatul se golește
        // Producție: SELECT * FROM employees WHERE first_name NOT IN ('Cathie', 'Mark', NULL). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: NULL în IN sau NOT IN nu este un prenume. EXISTS nu are această listă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE first_name NOT IN ('Cathie', 'Mark', NULL)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide notInWithNullDropsRows. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
