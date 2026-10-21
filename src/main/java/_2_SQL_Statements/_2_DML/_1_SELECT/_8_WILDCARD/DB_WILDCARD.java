 package _2_SQL_Statements._2_DML._1_SELECT._8_WILDCARD;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345112#overview


/*                                    Wildcard Characters


    ○  *

    ○  %

    ○ _

    ○ You would need a wild card character
                  * whenever you wish to put anything on its place.

 */


/*
TOPIC:
    În LIKE, % ține locul unui șir și _ ține locul unui caracter; * din SELECT * nu este tipar pe un prenume.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu amesteci steluța din lista de coloane cu tiparul. LIKE 'Mar_' lasă prenumele de patru litere care încep cu Mar.
MECHANISM:
    Serverul interpretează % și _ doar în predicatul LIKE sau în alte tipare care le definesc. SELECT * înseamnă toate coloanele, nu orice prenume.
LOOKALIKES:
    LIKE 'Mar%' lasă orice lungime după Mar. SELECT * FROM employees nu filtrează first_name.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat. PDF-urile din folderele SELECT au fost deschise în tur; multe au pe prima pagină doar titlul. DB_WILDCARD numește *, % și _. Manualul SELECT spune că * necalificat este toate coloanele, nu un tipar de șir.
BOOK EXAMPLE:
    underscoreKeepsFourLetters
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_8_WILDCARD/DB_WILDCARD.java
BREAK LAB:
    starIsNotANamePattern ține SELECT * FROM employees WHERE first_name LIKE '*'. * din lista de coloane nu este % și nu este _. Predicatul de un caracter este _.
TRADE-OFFS:
    LIKE 'Mar%' când lungimea de după Mar poate varia.
ANTI-PATTERNS:
    LIKE '*' ca să prinzi orice prenume. Nu prinde Mark.
STAFF QUESTIONS:
    underscoreKeepsFourLetters și starIsNotANamePattern. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un cod de locație are trei litere și o cifră. LIKE 'ABC_' lasă ABC și o poziție. LIKE 'ABC%' lasă și sufixe mai lungi.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT * FROM employees WHERE first_name LIKE 'Mar_'?
   Capcană: * din SELECT * este tiparul care prinde orice first_name.
   Cheie: underscoreKeepsFourLetters: rămân prenumele de patru caractere care încep cu Mar. fără _, LIKE 'Mar%' lasă și prenume mai lungi
   Demonstrație: underscoreKeepsFourLetters
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT * FROM employees WHERE first_name LIKE 'Mar_'?
   Capcană: LIKE '*' întoarce toate prenumele.
   Cheie: Efectul este: rămân prenumele de patru caractere care încep cu Mar. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: underscoreKeepsFourLetters
3. Enunț: De ce SELECT * FROM employees WHERE first_name LIKE 'Mar_' nu este vecinul său?
   Capcană: % și _ sunt același semn, diferă doar lungimea din curs.
   Cheie: LIKE 'Mar%' lasă orice lungime după Mar. SELECT * FROM employees nu filtrează first_name.
   Demonstrație: underscoreKeepsFourLetters
4. Enunț: Cum eșuează SELECT * FROM employees WHERE first_name LIKE '*'?
   Capcană: Fără LIKE, WHERE first_name = 'Mar_' prinde Mark.
   Cheie: starIsNotANamePattern: * din lista de coloane nu este % și nu este _. Predicatul de un caracter este _.
   Demonstrație: starIsNotANamePattern
5. Enunț: Când nu mai trimiți SELECT * FROM employees WHERE first_name LIKE 'Mar_'?
   Capcană: Ca să limitezi un caracter, folosești LIMIT 4.
   Cheie: LIKE 'Mar%' când lungimea de după Mar poate varia.
   Demonstrație: underscoreKeepsFourLetters

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei exact un caracter după Mar -> underscoreKeepsFourLetters -> SELECT * FROM employees WHERE first_name LIKE 'Mar_' -> nu garantează că * din SELECT filtrează rânduri
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> starIsNotANamePattern -> SELECT * FROM employees WHERE first_name LIKE '*' -> * din lista de coloane nu este % și nu este _. Predicatul de un caracter este _.
    3. Granița
           -> Tiparul este în LIKE. Steluța din lista de coloane este alt mecanism, în aceeași propoziție.
    4. Alternativa
           -> LIKE 'Mar%' lasă orice lungime după Mar. SELECT * FROM employees nu filtrează first_name.
    5. Anti-patternul
           -> LIKE '*' ca să prinzi orice prenume. Nu prinde Mark.
    6. Dovada
           -> underscoreKeepsFourLetters ține propoziția. starIsNotANamePattern ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> În LIKE, % este un șir și _ este un caracter. * din SELECT este lista de coloane. LIKE '*' nu înseamnă orice prenume.
*/

public class DB_WILDCARD {
    /*
    🟢 Începător
        Ce este            rămân prenumele de patru caractere care încep cu Mar
        De ce e nevoie     fără _, LIKE 'Mar%' lasă și prenume mai lungi
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE first_name LIKE 'Mar_'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este SELECT * FROM employees, care nu filtrează prenumele
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT * FROM employees, care nu filtrează prenumele.
    */
    // Începător: underscoreKeepsFourLetters păstrează propoziția pentru rămân prenumele de patru caractere care încep cu Mar
    // Producție: public static String underscoreKeepsFourLetters(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. * nu stă în tiparul de prenume. LIKE '*' caută steluța ca literă doar dacă nu ești într-un predicat care o definește altfel; aici tiparul corect este _.
    public static String underscoreKeepsFourLetters() {
        // Începător: rămân prenumele de patru caractere care încep cu Mar
        // Producție: SELECT * FROM employees WHERE first_name LIKE 'Mar_'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: * nu stă în tiparul de prenume. LIKE '*' caută steluța ca literă doar dacă nu ești într-un predicat care o definește altfel; aici tiparul corect este _. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE first_name LIKE 'Mar_'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide underscoreKeepsFourLetters. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            nu rămân toate prenumele; tiparul este o steluță, nu orice caracter
        De ce e nevoie     fără textul greșit, * din SELECT pare tiparul de pe coloană
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE first_name LIKE '*'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care nu rămân toate prenumele; tiparul este o steluță, nu orice caracter
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT * FROM employees, care nu filtrează prenumele.
    */
    // Începător: starIsNotANamePattern păstrează textul greșit pentru nu rămân toate prenumele; tiparul este o steluță, nu orice caracter
    // Producție: public static String starIsNotANamePattern(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. * din lista de coloane nu este % și nu este _. Predicatul de un caracter este _.
    public static String starIsNotANamePattern() {
        // Începător: nu rămân toate prenumele; tiparul este o steluță, nu orice caracter
        // Producție: SELECT * FROM employees WHERE first_name LIKE '*'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: * din lista de coloane nu este % și nu este _. Predicatul de un caracter este _. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE first_name LIKE '*'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide starIsNotANamePattern. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
