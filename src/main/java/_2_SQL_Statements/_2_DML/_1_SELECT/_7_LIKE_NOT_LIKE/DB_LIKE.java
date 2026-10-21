package _2_SQL_Statements._2_DML._1_SELECT._7_LIKE_NOT_LIKE;

// Martin G. Like - Not LIke
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345106#overview


/*                                          LIKE


    ▬▬▐ When you need to look for a
                - specific pattern in a column.
                - then use the LIKE operator.

    ○ It is succeeded by parentheses
                   * within which the pattern must be indicated.

    ○ That's why the implemented technique is called
                  * pattern matching.

   ○ all search are case-insensitive


                                     ▬▬ LIKE query % ▬▬


    ○ The percentage sign is necessary for matching a sequence of characters.
                * the percentage sign can be seen as a substitute for a sequence of characters.

                1. SELECT * FROM employees  WHERE first_name
                            - LIKE ('Dek%');
                2. LIKE ('%sr%');
                3. LIKE ('%sr);

                                     ▬▬ LIKE query _ ▬▬


    ○ underscore helps you
                  * match a single character.

    ○ If you would like to fetch all names,
                  * starting with Mar
                  * and written with four letters,
          you would type:
                   * SELECT * FROM employees
                     WHERE first_name
                     LIKE ('Mar_');


                                    ▬▬ NOT LIKE ▬▬


    ○ is just opposite to LIKE

 */

/*
TOPIC:
    LIKE 'Dek%' păstrează prenumele care încep cu Dek; în =, semnul % este caracter, nu tipar.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să cauți un tipar, nu un prenume întreg. Rândul care nu se potrivește tiparului nu intră în rezultat.
MECHANISM:
    Serverul aplică tiparul LIKE în WHERE. % este un șir de caractere, _ este un caracter. Parantezele din curs nu sunt cerute de predicat.
LOOKALIKES:
    first_name = 'Dek%' caută exact acele patru caractere. Wildcard-ul singur, fără LIKE, nu este predicat.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat. PDF-urile din folderele SELECT au fost deschise în tur; multe au pe prima pagină doar titlul. LIKE din comentariul de curs folosește paranteze și spune că orice căutare este case-insensitive. Comparația depinde de colaționare, nu de LIKE ca regulă universală. Pagina LIKE ca URL separat este NERULAT AICI.
BOOK EXAMPLE:
    likeKeepsDekPrefix
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_7_LIKE_NOT_LIKE/DB_LIKE.java
BREAK LAB:
    equalsTreatsPercentAsLetter ține SELECT * FROM employees WHERE first_name = 'Dek%'. În =, % este caracter. LIKE este predicatul de tipar.
TRADE-OFFS:
    = când valoarea este exactă și % trebuie să fie literă.
ANTI-PATTERNS:
    first_name = 'Dek%' ca să prinzi prefixul. Nu prinde Dekker.
STAFF QUESTIONS:
    likeKeepsDekPrefix și equalsTreatsPercentAsLetter. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un căutător de loturi vrea prefixul LOT-. LIKE 'LOT-%' îl prinde. = 'LOT-%' caută acel text cu procent.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT * FROM employees WHERE first_name LIKE 'Dek%'?
   Capcană: LIKE trebuie scris cu paranteze, altfel serverul nu vede tiparul.
   Cheie: likeKeepsDekPrefix: rămân prenumele care încep cu Dek. fără LIKE, egalul păstrează doar rândul al cărui prenume este exact textul, inclusiv un % scris ca literă
   Demonstrație: likeKeepsDekPrefix
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT * FROM employees WHERE first_name LIKE 'Dek%'?
   Capcană: LIKE șterge prenumele care nu încep cu Dek.
   Cheie: Efectul este: rămân prenumele care încep cu Dek. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: likeKeepsDekPrefix
3. Enunț: De ce SELECT * FROM employees WHERE first_name LIKE 'Dek%' nu este vecinul său?
   Capcană: = 'Dek%' prinde și pe Dekker, pentru că % este mereu tipar.
   Cheie: first_name = 'Dek%' caută exact acele patru caractere. Wildcard-ul singur, fără LIKE, nu este predicat.
   Demonstrație: likeKeepsDekPrefix
4. Enunț: Cum eșuează SELECT * FROM employees WHERE first_name = 'Dek%'?
   Capcană: Orice LIKE este case-insensitive, pe orice coloană.
   Cheie: equalsTreatsPercentAsLetter: În =, % este caracter. LIKE este predicatul de tipar.
   Demonstrație: equalsTreatsPercentAsLetter
5. Enunț: Când nu mai trimiți SELECT * FROM employees WHERE first_name LIKE 'Dek%'?
   Capcană: Ca să cauți un prefix, folosești BETWEEN.
   Cheie: = când valoarea este exactă și % trebuie să fie literă.
   Demonstrație: likeKeepsDekPrefix

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE cauți un prefix, nu un șir exact -> likeKeepsDekPrefix -> SELECT * FROM employees WHERE first_name LIKE 'Dek%' -> nu garantează că potrivirea ignoră majusculele și nu folosește un index pe orice tipar care începe cu %
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> equalsTreatsPercentAsLetter -> SELECT * FROM employees WHERE first_name = 'Dek%' -> În =, % este caracter. LIKE este predicatul de tipar.
    3. Granița
           -> LIKE este predicatul. Parantezele din curs și click-ul de search nu sunt tiparul.
    4. Alternativa
           -> first_name = 'Dek%' caută exact acele patru caractere. Wildcard-ul singur, fără LIKE, nu este predicat.
    5. Anti-patternul
           -> first_name = 'Dek%' ca să prinzi prefixul. Nu prinde Dekker.
    6. Dovada
           -> likeKeepsDekPrefix ține propoziția. equalsTreatsPercentAsLetter ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> LIKE aplică tiparul. = tratează % ca literă. Parantezele nu sunt cerute. Majusculele țin de colaționare, nu de cuvântul LIKE.
*/

public class DB_LIKE {
    /*
    🟢 Începător
        Ce este            rămân prenumele care încep cu Dek
        De ce e nevoie     fără LIKE, egalul păstrează doar rândul al cărui prenume este exact textul, inclusiv un % scris ca literă
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE first_name LIKE 'Dek%'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este first_name = 'Dek%', care nu este tipar
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu first_name = 'Dek%', care nu este tipar.
    */
    // Începător: likeKeepsDekPrefix păstrează propoziția pentru rămân prenumele care încep cu Dek
    // Producție: public static String likeKeepsDekPrefix(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. LIKE nu cere paranteze. Tiparul '%sr' neterminat nu este o propoziție.
    public static String likeKeepsDekPrefix() {
        // Începător: rămân prenumele care încep cu Dek
        // Producție: SELECT * FROM employees WHERE first_name LIKE 'Dek%'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: LIKE nu cere paranteze. Tiparul '%sr' neterminat nu este o propoziție. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE first_name LIKE 'Dek%'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide likeKeepsDekPrefix. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            rămâne doar rândul al cărui prenume este chiar Dek%, cu procentul ca literă, nu prefixul Dek
        De ce e nevoie     fără textul greșit, = pare că înțelege tiparul
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE first_name = 'Dek%'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care rămâne doar rândul al cărui prenume este chiar Dek%, cu procentul ca literă, nu prefixul Dek
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu first_name = 'Dek%', care nu este tipar.
    */
    // Începător: equalsTreatsPercentAsLetter păstrează textul greșit pentru rămâne doar rândul al cărui prenume este chiar Dek%, cu procentul ca literă, nu prefixul Dek
    // Producție: public static String equalsTreatsPercentAsLetter(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. În =, % este caracter. LIKE este predicatul de tipar.
    public static String equalsTreatsPercentAsLetter() {
        // Începător: rămâne doar rândul al cărui prenume este chiar Dek%, cu procentul ca literă, nu prefixul Dek
        // Producție: SELECT * FROM employees WHERE first_name = 'Dek%'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: În =, % este caracter. LIKE este predicatul de tipar. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE first_name = 'Dek%'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide equalsTreatsPercentAsLetter. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
