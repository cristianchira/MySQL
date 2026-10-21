package _2_SQL_Statements._2_DML._1_SELECT._9_BETWEEN_AND;

// Martin G. BETWEEN... AND
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345114#overview


/*                                            BETWEEN ... AND


    ○ Helps us designate the interval to which a given value belongs
                  * Not used only for date values
                  * It could also be applied to strings and numbers
                  * It is always used in combination with the AND operator.

    ○ SELECT * FROM employees
      WHERE hire_date
      BETWEEN '1990-01-01' AND '2000-01-01';

                  * '1990-01-01' AND '2000-01-01'
                                - WILL be included in the retrieved list of records


                                          ▬▬ NOT BETWEEN… AND… ▬▬


    ○ Will refer to an interval composed of two parts:
                  * an interval below the first value indicated
                  * a second interval above the second value


    ○ SELECT * FROM employees
       WHERE hire_date
       NOT BETWEEN '1990-01-01' AND '2000-01-01';

                 * '1990-01-01' AND '2000-01-01’
                                - ARE NOT included in the intervals


 */



/*
TOPIC:
    BETWEEN '1990-01-01' AND '2000-01-01' este inclusiv la ambele capete; hire_date egal cu un capăt rămâne în rezultat.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să incluzi intervalul închis. Rândul angajat în afara capetelor nu apare.
MECHANISM:
    Serverul tratează BETWEEN ca interval închis în WHERE. Nu este doar pentru date: comentariul de curs spune și numere și șiruri. Capetele intră.
LOOKALIKES:
    hire_date > '1990-01-01' AND hire_date < '2000-01-01' pierde capetele. NOT BETWEEN lasă afara intervalului, tot cu capetele excluse din acea afară.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat. PDF-urile din folderele SELECT au fost deschise în tur; multe au pe prima pagină doar titlul. BETWEEN_AND.pdf a fost deschis. Comentariul de curs spune că ambele capete intră. Două comparații stricte nu sunt același interval.
BOOK EXAMPLE:
    betweenKeepsBothEnds
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_9_BETWEEN_AND/DB_BETWEEN_AND.java
BREAK LAB:
    strictComparisonsDropEnds ține SELECT * FROM employees WHERE hire_date > '1990-01-01' AND hire_date < '2000-01-01'. Capetele sunt în BETWEEN și nu sunt în comparațiile stricte.
TRADE-OFFS:
    Comparații stricte când capătul trebuie să cadă.
ANTI-PATTERNS:
    > și < crezând că ai inclus zilele capetelor.
STAFF QUESTIONS:
    betweenKeepsBothEnds și strictComparisonsDropEnds. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O campanie ține de la 1 martie la 31 martie, inclusiv. sold_on BETWEEN '2026-03-01' AND '2026-03-31' lasă ambele zile.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT * FROM employees WHERE hire_date BETWEEN '1990-01-01' AND '2000-01-01'?
   Capcană: BETWEEN este deschis: ziua de 1990-01-01 cade.
   Cheie: betweenKeepsBothEnds: rămân angajații angajați între capete, inclusiv în zilele capetelor. fără BETWEEN închis, o comparație strictă pierde angajatul din 1990-01-01
   Demonstrație: betweenKeepsBothEnds
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT * FROM employees WHERE hire_date BETWEEN '1990-01-01' AND '2000-01-01'?
   Capcană: BETWEEN șterge angajații din afara intervalului.
   Cheie: Efectul este: rămân angajații angajați între capete, inclusiv în zilele capetelor. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: betweenKeepsBothEnds
3. Enunț: De ce SELECT * FROM employees WHERE hire_date BETWEEN '1990-01-01' AND '2000-01-01' nu este vecinul său?
   Capcană: > și < întorc aceleași rânduri ca BETWEEN.
   Cheie: hire_date > '1990-01-01' AND hire_date < '2000-01-01' pierde capetele. NOT BETWEEN lasă afara intervalului, tot cu capetele excluse din acea afară.
   Demonstrație: betweenKeepsBothEnds
4. Enunț: Cum eșuează SELECT * FROM employees WHERE hire_date > '1990-01-01' AND hire_date < '2000-01-01'?
   Capcană: Capetele sunt excluse, cum scrie uneori la intervale matematice.
   Cheie: strictComparisonsDropEnds: Capetele sunt în BETWEEN și nu sunt în comparațiile stricte.
   Demonstrație: strictComparisonsDropEnds
5. Enunț: Când nu mai trimiți SELECT * FROM employees WHERE hire_date BETWEEN '1990-01-01' AND '2000-01-01'?
   Capcană: Pentru date folosești doar YEAR(), nu BETWEEN.
   Cheie: Comparații stricte când capătul trebuie să cadă.
   Demonstrație: betweenKeepsBothEnds

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE ambele capete trebuie să rămână -> betweenKeepsBothEnds -> SELECT * FROM employees WHERE hire_date BETWEEN '1990-01-01' AND '2000-01-01' -> nu garantează că șirurile se compară calendaristic dacă coloana nu este dată
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> strictComparisonsDropEnds -> SELECT * FROM employees WHERE hire_date > '1990-01-01' AND hire_date < '2000-01-01' -> Capetele sunt în BETWEEN și nu sunt în comparațiile stricte.
    3. Granița
           -> BETWEEN este predicatul. Un slider din client nu este intervalul închis.
    4. Alternativa
           -> hire_date > '1990-01-01' AND hire_date < '2000-01-01' pierde capetele. NOT BETWEEN lasă afara intervalului, tot cu capetele excluse din acea afară.
    5. Anti-patternul
           -> > și < crezând că ai inclus zilele capetelor.
    6. Dovada
           -> betweenKeepsBothEnds ține propoziția. strictComparisonsDropEnds ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> BETWEEN este inclusiv la capete. > și < le pierd. NOT BETWEEN lasă exteriorul. Nu este doar pentru date.
*/

public class DB_BETWEEN_AND {
    /*
    🟢 Începător
        Ce este            rămân angajații angajați între capete, inclusiv în zilele capetelor
        De ce e nevoie     fără BETWEEN închis, o comparație strictă pierde angajatul din 1990-01-01
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE hire_date BETWEEN '1990-01-01' AND '2000-01-01'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este două comparații cu > și <, care scot capetele
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu două comparații cu > și <, care scot capetele.
    */
    // Începător: betweenKeepsBothEnds păstrează propoziția pentru rămân angajații angajați între capete, inclusiv în zilele capetelor
    // Producție: public static String betweenKeepsBothEnds(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. BETWEEN nu este deschis la capete. Ordinea capetelor contează: un capăt mare scris primul nu descrie intervalul pe care îl crezi.
    public static String betweenKeepsBothEnds() {
        // Începător: rămân angajații angajați între capete, inclusiv în zilele capetelor
        // Producție: SELECT * FROM employees WHERE hire_date BETWEEN '1990-01-01' AND '2000-01-01'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: BETWEEN nu este deschis la capete. Ordinea capetelor contează: un capăt mare scris primul nu descrie intervalul pe care îl crezi. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE hire_date BETWEEN '1990-01-01' AND '2000-01-01'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide betweenKeepsBothEnds. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            angajatul din ziua de început sau din ziua de sfârșit cade din rezultat
        De ce e nevoie     fără textul greșit, > și < par sinonime cu BETWEEN
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE hire_date > '1990-01-01' AND hire_date < '2000-01-01'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care angajatul din ziua de început sau din ziua de sfârșit cade din rezultat
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu două comparații cu > și <, care scot capetele.
    */
    // Începător: strictComparisonsDropEnds păstrează textul greșit pentru angajatul din ziua de început sau din ziua de sfârșit cade din rezultat
    // Producție: public static String strictComparisonsDropEnds(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Capetele sunt în BETWEEN și nu sunt în comparațiile stricte.
    public static String strictComparisonsDropEnds() {
        // Începător: angajatul din ziua de început sau din ziua de sfârșit cade din rezultat
        // Producție: SELECT * FROM employees WHERE hire_date > '1990-01-01' AND hire_date < '2000-01-01'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Capetele sunt în BETWEEN și nu sunt în comparațiile stricte. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE hire_date > '1990-01-01' AND hire_date < '2000-01-01'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide strictComparisonsDropEnds. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
