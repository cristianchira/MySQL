package _2_SQL_Statements._2_DML._1_SELECT._12_DISTINCT;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345160#overview


/*                         Distinct


    ▬▬▐ Selects all
             distinct, different data values


    SELECT DISTINCT column_1, column_2,… column_n
        FROM table_name;


    ○ Example:
                  * SELECT DISTINCT gender
                     FROM
                     employees;


 */


/*
TOPIC:
    SELECT DISTINCT gender lasă fiecare valoare de gen o dată în rezultat; rândurile din employees rămân toate.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu repeți aceeași valoare în rezultat. Tabela nu se deduplică.
MECHANISM:
    DISTINCT acționează pe rândul de rezultat. GROUP BY face grupuri ca să poți agrega. Aceeași listă de valori nu este același plan.
LOOKALIKES:
    GROUP BY gender cu COUNT numără. DISTINCT gender nu numără.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat.  PDF-ul SELECT+DISTINCT a fost deschis. Manualul pune DISTINCT în lista SELECT, înainte de expresii.
BOOK EXAMPLE:
    distinctGendersInTheResult
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_12_DISTINCT/DB_DISTINCT.java
BREAK LAB:
    groupByCountsInstead ține SELECT gender, COUNT(*) FROM employees GROUP BY gender. GROUP BY există ca să agregi. DISTINCT elimină duplicate din rezultat și nu numără.
TRADE-OFFS:
    GROUP BY când trebuie să numeri sau să sumarizezi pe grup.
ANTI-PATTERNS:
    GROUP BY gender doar ca să vezi genurile, apoi ignori COUNT și crezi că ai DISTINCT.
STAFF QUESTIONS:
    distinctGendersInTheResult și groupByCountsInstead. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O listă de depozite distincte din scanări: SELECT DISTINCT depot FROM scans. COUNT pe depot este altă întrebare.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT DISTINCT gender FROM employees?
   Capcană: DISTINCT șterge din employees rândurile duplicate.
   Cheie: distinctGendersInTheResult: rezultatul are fiecare gen o dată, iar employees păstrează toate rândurile. fără DISTINCT, fiecare angajat repetă genul în rezultat
   Demonstrație: distinctGendersInTheResult
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT DISTINCT gender FROM employees?
   Capcană: GROUP BY gender fără agregat este mereu același plan cu DISTINCT.
   Cheie: Efectul este: rezultatul are fiecare gen o dată, iar employees păstrează toate rândurile. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: distinctGendersInTheResult
3. Enunț: De ce SELECT DISTINCT gender FROM employees nu este vecinul său?
   Capcană: DISTINCT gender numără angajații.
   Cheie: GROUP BY gender cu COUNT numără. DISTINCT gender nu numără.
   Demonstrație: distinctGendersInTheResult
4. Enunț: Cum eșuează SELECT gender, COUNT(*) FROM employees GROUP BY gender?
   Capcană: Fără DISTINCT, serverul păstrează în tabelă o singură copie.
   Cheie: groupByCountsInstead: GROUP BY există ca să agregi. DISTINCT elimină duplicate din rezultat și nu numără.
   Demonstrație: groupByCountsInstead
5. Enunț: Când nu mai trimiți SELECT DISTINCT gender FROM employees?
   Capcană: Ca să deduplici, folosești DELETE, nu DISTINCT.
   Cheie: GROUP BY când trebuie să numeri sau să sumarizezi pe grup.
   Demonstrație: distinctGendersInTheResult

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei valorile distincte, fără număr -> distinctGendersInTheResult -> SELECT DISTINCT gender FROM employees -> nu garantează că tabela a rămas fără duplicate stocate
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> groupByCountsInstead -> SELECT gender, COUNT(*) FROM employees GROUP BY gender -> GROUP BY există ca să agregi. DISTINCT elimină duplicate din rezultat și nu numără.
    3. Granița
           -> DISTINCT este clauza pe rezultat. Un pivot din client care ascunde duplicate nu este propoziția.
    4. Alternativa
           -> GROUP BY gender cu COUNT numără. DISTINCT gender nu numără.
    5. Anti-patternul
           -> GROUP BY gender doar ca să vezi genurile, apoi ignori COUNT și crezi că ai DISTINCT.
    6. Dovada
           -> distinctGendersInTheResult ține propoziția. groupByCountsInstead ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DISTINCT scoate duplicate din rezultat și lasă tabela. GROUP BY face grupuri pentru agregate. Nu șterge rânduri.
*/

public class DB_DISTINCT {
    /*
    🟢 Începător
        Ce este            rezultatul are fiecare gen o dată, iar employees păstrează toate rândurile
        De ce e nevoie     fără DISTINCT, fiecare angajat repetă genul în rezultat
    🟡 Producție
        Cum se scrie       SELECT DISTINCT gender FROM employees
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este SELECT gender, COUNT(*) FROM employees GROUP BY gender, care face grupuri și numără
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT gender, COUNT(*) FROM employees GROUP BY gender, care face grupuri și numără.
    */
    // Începător: distinctGendersInTheResult păstrează propoziția pentru rezultatul are fiecare gen o dată, iar employees păstrează toate rândurile
    // Producție: public static String distinctGendersInTheResult(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DISTINCT nu șterge duplicatele din tabelă și nu înlocuiește GROUP BY când ai nevoie de un agregat.
    public static String distinctGendersInTheResult() {
        // Începător: rezultatul are fiecare gen o dată, iar employees păstrează toate rândurile
        // Producție: SELECT DISTINCT gender FROM employees. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: DISTINCT nu șterge duplicatele din tabelă și nu înlocuiește GROUP BY când ai nevoie de un agregat. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT DISTINCT gender FROM employees";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide distinctGendersInTheResult. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            apare o coloană de număr, nu lista simplă de genuri
        De ce e nevoie     fără textul greșit, GROUP BY pare doar un DISTINCT mai lung
    🟡 Producție
        Cum se scrie       SELECT gender, COUNT(*) FROM employees GROUP BY gender
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care apare o coloană de număr, nu lista simplă de genuri
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT gender, COUNT(*) FROM employees GROUP BY gender, care face grupuri și numără.
    */
    // Începător: groupByCountsInstead păstrează textul greșit pentru apare o coloană de număr, nu lista simplă de genuri
    // Producție: public static String groupByCountsInstead(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. GROUP BY există ca să agregi. DISTINCT elimină duplicate din rezultat și nu numără.
    public static String groupByCountsInstead() {
        // Începător: apare o coloană de număr, nu lista simplă de genuri
        // Producție: SELECT gender, COUNT(*) FROM employees GROUP BY gender. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: GROUP BY există ca să agregi. DISTINCT elimină duplicate din rezultat și nu numără. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT gender, COUNT(*) FROM employees GROUP BY gender";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide groupByCountsInstead. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
