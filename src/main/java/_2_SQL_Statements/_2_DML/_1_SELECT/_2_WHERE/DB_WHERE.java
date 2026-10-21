package _2_SQL_Statements._2_DML._1_SELECT._2_WHERE;

//Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8344766#questions

/*                                 WHERE clause


    ○ It will allow us to
                   * set a condition upon which we will specify
                   * what part of the data we want to retrieve from the database


                                    ▬▬ WHERE query ▬▬


    1. SELECT column_1, column_2,… column_n
       FROM table_name;
       HISTORICAL: punctul și virgula de pe rândul de deasupra închide SELECT-ul.
       Regula citită în tur: MySQL 9.7, 15.2.13 SELECT. WHERE stă în aceeași propoziție, după FROM.
       WHERE condition;
                   * This condition can be, for example,
                                  - a verification that certain information is available,
                                  - a check
                                  - or a mathematical expression.


                                       Example


    1. SELECT * FROM employees
       WHERE first_name ='Denis';


 */

/*
TOPIC:
    WHERE first_name = 'Denis' lasă în rezultat doar rândurile pentru care expresia este adevărată; angajatul care nu este Denis nu apare.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu primești toți angajații. După propoziție, rezultatul are doar Denis, iar tabela employees rămâne întreagă.
MECHANISM:
    Serverul evaluează WHERE la citire, înainte de grupare. Manualul 9.7: fără WHERE se selectează toate rândurile, iar agregatele nu sunt permise în WHERE.
LOOKALIKES:
    HAVING filtrează grupuri după GROUP BY și poate folosi agregate. WHERE nu poate.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat. PDF-urile din folderele SELECT au fost deschise în tur; multe au pe prima pagină doar titlul. WHERE.pdf a fost deschis și arată SELECT * FROM employees; apoi WHERE pe rândul următor. Manualul spune că WHERE este clauză în aceeași propoziție.
BOOK EXAMPLE:
    whereKeepsDenis
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_2_WHERE/DB_WHERE.java
BREAK LAB:
    whereAfterSemicolon ține SELECT * FROM employees; WHERE first_name = 'Denis'. Manualul 9.7, 15.2.13: WHERE stă în aceeași propoziție, după FROM. Nu este a doua propoziție.
TRADE-OFFS:
    HAVING când condiția este pe un agregat, după GROUP BY.
ANTI-PATTERNS:
    SELECT * FROM employees; WHERE first_name = 'Denis' întoarce toți angajații și apoi eșuează.
STAFF QUESTIONS:
    whereKeepsDenis și whereAfterSemicolon. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un control de acces lasă doar ecusonul 'D-14'. WHERE badge = 'D-14' îi scoate pe ceilalți din rezultat, nu din tabela de angajați.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT * FROM employees WHERE first_name = 'Denis'?
   Capcană: WHERE șterge din employees pe oricine nu este Denis.
   Cheie: whereKeepsDenis: în rezultat rămân doar rândurile cu first_name Denis. fără WHERE, SELECT * FROM employees îi aduce și pe cei care nu sunt Denis
   Demonstrație: whereKeepsDenis
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT * FROM employees WHERE first_name = 'Denis'?
   Capcană: Filtrul se aplică după COUNT, deci poți scrie WHERE COUNT(*) > 1.
   Cheie: Efectul este: în rezultat rămân doar rândurile cu first_name Denis. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: whereKeepsDenis
3. Enunț: De ce SELECT * FROM employees WHERE first_name = 'Denis' nu este vecinul său?
   Capcană: HAVING first_name = 'Denis' este același filtru, doar că se scrie mai jos.
   Cheie: HAVING filtrează grupuri după GROUP BY și poate folosi agregate. WHERE nu poate.
   Demonstrație: whereKeepsDenis
4. Enunț: Cum eșuează SELECT * FROM employees; WHERE first_name = 'Denis'?
   Capcană: Punctul și virgula înainte de WHERE este opțional și serverul leagă clauzele.
   Cheie: whereAfterSemicolon: Manualul 9.7, 15.2.13: WHERE stă în aceeași propoziție, după FROM. Nu este a doua propoziție.
   Demonstrație: whereAfterSemicolon
5. Enunț: Când nu mai trimiți SELECT * FROM employees WHERE first_name = 'Denis'?
   Capcană: Ca să filtrezi un agregat, îl pui în WHERE.
   Cheie: HAVING când condiția este pe un agregat, după GROUP BY.
   Demonstrație: whereKeepsDenis

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei doar rândurile Denis, înainte de orice grup -> whereKeepsDenis -> SELECT * FROM employees WHERE first_name = 'Denis' -> nu garantează un index pe first_name și nu ține lacăt după ce rezultatul a plecat
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> whereAfterSemicolon -> SELECT * FROM employees; WHERE first_name = 'Denis' -> Manualul 9.7, 15.2.13: WHERE stă în aceeași propoziție, după FROM. Nu este a doua propoziție.
    3. Granița
           -> WHERE este clauza. Punctul și virgula din PDF-ul de curs nu este sintaxa serverului.
    4. Alternativa
           -> HAVING filtrează grupuri după GROUP BY și poate folosi agregate. WHERE nu poate.
    5. Anti-patternul
           -> SELECT * FROM employees; WHERE first_name = 'Denis' întoarce toți angajații și apoi eșuează.
    6. Dovada
           -> whereKeepsDenis ține propoziția. whereAfterSemicolon ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> WHERE filtrează rânduri înainte de grup și nu acceptă agregate. HAVING filtrează grupuri. Punctul și virgula înainte de WHERE taie propoziția.
*/

public class DB_WHERE {
    /*
    🟢 Începător
        Ce este            în rezultat rămân doar rândurile cu first_name Denis
        De ce e nevoie     fără WHERE, SELECT * FROM employees îi aduce și pe cei care nu sunt Denis
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE first_name = 'Denis'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este HAVING, care filtrează după grupare
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu HAVING, care filtrează după grupare.
    */
    // Începător: whereKeepsDenis păstrează propoziția pentru în rezultat rămân doar rândurile cu first_name Denis
    // Producție: public static String whereKeepsDenis(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Un punct și virgulă înainte de WHERE închide SELECT-ul. WHERE rămas nu filtrează pe nimeni.
    public static String whereKeepsDenis() {
        // Începător: în rezultat rămân doar rândurile cu first_name Denis
        // Producție: SELECT * FROM employees WHERE first_name = 'Denis'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Un punct și virgulă înainte de WHERE închide SELECT-ul. WHERE rămas nu filtrează pe nimeni. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE first_name = 'Denis'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide whereKeepsDenis. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul termină citirea tuturor angajaților, apoi respinge WHERE rămas
        De ce e nevoie     fără textul greșit, punctul și virgula din curs pare regula
    🟡 Producție
        Cum se scrie       SELECT * FROM employees; WHERE first_name = 'Denis'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul termină citirea tuturor angajaților, apoi respinge WHERE rămas
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu HAVING, care filtrează după grupare.
    */
    // Începător: whereAfterSemicolon păstrează textul greșit pentru serverul termină citirea tuturor angajaților, apoi respinge WHERE rămas
    // Producție: public static String whereAfterSemicolon(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Manualul 9.7, 15.2.13: WHERE stă în aceeași propoziție, după FROM. Nu este a doua propoziție.
    public static String whereAfterSemicolon() {
        // Începător: serverul termină citirea tuturor angajaților, apoi respinge WHERE rămas
        // Producție: SELECT * FROM employees; WHERE first_name = 'Denis'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Manualul 9.7, 15.2.13: WHERE stă în aceeași propoziție, după FROM. Nu este a doua propoziție. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees; WHERE first_name = 'Denis'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide whereAfterSemicolon. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
