package _4_Database_Terminology._1_Relational_DB_Essentials;

// Relational database essentials
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338058#overview


/*
TOPIC:
    Rândul se identifică prin cheie, nu prin poziția de pe ecran: SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 citește acel angajat.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu cauți al treilea rând de pe ecran. Angajatul 10001 rămâne în tabelă, rezultatul îl arată.
MECHANISM:
    Serverul execută SELECT cu egal pe cheie. Ordinea de afișare fără ORDER BY nu este identitatea rândului.
LOOKALIKES:
    LIMIT 1 fără ORDER BY ia un rând fără să spună că este 10001. Poziția din grid nu este emp_no.
BOOK THEORY:
    Nu există PDF local în acest folder la deschiderea PDF-urilor. Pagina SELECT 9.7 a fost deschisă: WHERE alege rândul pentru care expresia este adevărată.
BOOK EXAMPLE:
    selectEmployeeByKey
REPO DEMO:
    src/main/java/_4_Database_Terminology/_1_Relational_DB_Essentials/Relational_DB_Essentials.java
BREAK LAB:
    limitIsNotTheKey ține SELECT emp_no, first_name FROM employees LIMIT 1. LIMIT fără ORDER BY nu este cheia. WHERE emp_no = 10001 este identitatea din propoziție.
TRADE-OFFS:
    LIMIT cu ORDER BY când vrei o pagină, nu un angajat anume.
ANTI-PATTERNS:
    LIMIT 1 ca să îl citești pe 10001. Poți primi pe altcineva.
STAFF QUESTIONS:
    selectEmployeeByKey și limitIsNotTheKey. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un lot se citește prin lot_no = 'L9', nu prin faptul că este pe rândul 3 în ecran.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT emp_no, first_name FROM employees WHERE emp_no = 10001?
   Capcană: LIMIT 1 întoarce mereu emp_no 10001.
   Cheie: selectEmployeeByKey: rezultatul este angajatul 10001, nu al treilea rând desenat. fără cheie, nu știi care rând ai citit
   Demonstrație: selectEmployeeByKey
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT emp_no, first_name FROM employees WHERE emp_no = 10001?
   Capcană: Al treilea rând din grid este o cheie.
   Cheie: Efectul este: rezultatul este angajatul 10001, nu al treilea rând desenat. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: selectEmployeeByKey
3. Enunț: De ce SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 nu este vecinul său?
   Capcană: WHERE emp_no = 10001 șterge pe ceilalți.
   Cheie: LIMIT 1 fără ORDER BY ia un rând fără să spună că este 10001. Poziția din grid nu este emp_no.
   Demonstrație: selectEmployeeByKey
4. Enunț: Cum eșuează SELECT emp_no, first_name FROM employees LIMIT 1?
   Capcană: Fără ORDER BY, primul rând este cheia primară.
   Cheie: limitIsNotTheKey: LIMIT fără ORDER BY nu este cheia. WHERE emp_no = 10001 este identitatea din propoziție.
   Demonstrație: limitIsNotTheKey
5. Enunț: Când nu mai trimiți SELECT emp_no, first_name FROM employees WHERE emp_no = 10001?
   Capcană: Ca să identifici un rând, îi ții minte poziția.
   Cheie: LIMIT cu ORDER BY când vrei o pagină, nu un angajat anume.
   Demonstrație: selectEmployeeByKey

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE identitatea este emp_no -> selectEmployeeByKey -> SELECT emp_no, first_name FROM employees WHERE emp_no = 10001 -> nu spune că 10001 este PRIMARY KEY dacă nu ai declarat constrângerea
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> limitIsNotTheKey -> SELECT emp_no, first_name FROM employees LIMIT 1 -> LIMIT fără ORDER BY nu este cheia. WHERE emp_no = 10001 este identitatea din propoziție.
    3. Granița
           -> Cheia din propoziție este mecanismul. Rândul de pe ecran este afișare.
    4. Alternativa
           -> LIMIT 1 fără ORDER BY ia un rând fără să spună că este 10001. Poziția din grid nu este emp_no.
    5. Anti-patternul
           -> LIMIT 1 ca să îl citești pe 10001. Poți primi pe altcineva.
    6. Dovada
           -> selectEmployeeByKey ține propoziția. limitIsNotTheKey ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> Identitatea este valoarea din propoziție. LIMIT 1 nu este cheia. Ecranul nu ține rândul.
*/

public class Relational_DB_Essentials {
    /*
    🟢 Începător
        Ce este            rezultatul este angajatul 10001, nu al treilea rând desenat
        De ce e nevoie     fără cheie, nu știi care rând ai citit
    🟡 Producție
        Cum se scrie       SELECT emp_no, first_name FROM employees WHERE emp_no = 10001
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este SELECT emp_no, first_name FROM employees LIMIT 1, care nu promite 10001
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT emp_no, first_name FROM employees LIMIT 1, care nu promite 10001.
    */
    // Începător: selectEmployeeByKey păstrează propoziția pentru rezultatul este angajatul 10001, nu al treilea rând desenat
    // Producție: public static String selectEmployeeByKey(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Poziția din client se schimbă la o reordonare. emp_no nu se schimbă din cauza grilei.
    public static String selectEmployeeByKey() {
        // Începător: rezultatul este angajatul 10001, nu al treilea rând desenat
        // Producție: SELECT emp_no, first_name FROM employees WHERE emp_no = 10001. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Poziția din client se schimbă la o reordonare. emp_no nu se schimbă din cauza grilei. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT emp_no, first_name FROM employees WHERE emp_no = 10001";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide selectEmployeeByKey. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            vine un rând, dar nu ai cerut 10001
        De ce e nevoie     fără textul greșit, primul rând de pe ecran pare identitatea
    🟡 Producție
        Cum se scrie       SELECT emp_no, first_name FROM employees LIMIT 1
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care vine un rând, dar nu ai cerut 10001
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT emp_no, first_name FROM employees LIMIT 1, care nu promite 10001.
    */
    // Începător: limitIsNotTheKey păstrează textul greșit pentru vine un rând, dar nu ai cerut 10001
    // Producție: public static String limitIsNotTheKey(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. LIMIT fără ORDER BY nu este cheia. WHERE emp_no = 10001 este identitatea din propoziție.
    public static String limitIsNotTheKey() {
        // Începător: vine un rând, dar nu ai cerut 10001
        // Producție: SELECT emp_no, first_name FROM employees LIMIT 1. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: LIMIT fără ORDER BY nu este cheia. WHERE emp_no = 10001 este identitatea din propoziție. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT emp_no, first_name FROM employees LIMIT 1";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide limitIsNotTheKey. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
