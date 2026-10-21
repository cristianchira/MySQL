package _2_SQL_Statements._2_DML._1_SELECT._11_MATH_COMPARATORS;

// Martin G.  Math Comparison Operators
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345142#overview


/*                                Math Comparison Operators


    ○ =
                * equal to

      >
                * greater than

      >=
                * greater than or equal to

      <
                 * less than

      <=
                 * less than or equal to

      <>, != , ≠
                 * not equal
                 * different from

    ○ How to get this sign on Windows:
                * Press and hold the 'Alt' key.
                * Type '8800' using the numeric keypad (Num Lock should be on)
                ** or use Character Map from windows

 */

/*
TOPIC:
    salary > 50000 lasă rândul a cărui valoare este mai mare; <> este diferit, nu un filtru de interval.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să compari numere, nu să desenezi semnul ≠ din hartă. Rândul cu salariu egal cu 50000 cade la >.
MECHANISM:
    Serverul evaluează comparația în WHERE. =, <>, <, >, <=, >= sunt predicate. Semnul ≠ tastat ca simbol nu este operatorul.
LOOKALIKES:
    BETWEEN este interval închis. <> lasă tot ce nu este egal, inclusiv NULL-ul care nu trece prin <> așa cum trece un număr.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat.  PDF-ul Math+Comparison+Operators a fost deschis. Comparația stă în WHERE, nu în lista de simboluri din Windows.
BOOK EXAMPLE:
    greaterKeepsAboveSalary
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_11_MATH_COMPARATORS/DB_MATH_COMPARATORS.java
BREAK LAB:
    notEqualIsNotAThreshold ține SELECT emp_no, salary FROM employees WHERE salary <> 50000. <> scoate doar egalul. > este pragul de deasupra.
TRADE-OFFS:
    BETWEEN când ai două capete inclusiv. IS NULL când lipsa nu trebuie tratată ca 0.
ANTI-PATTERNS:
    <> 50000 ca să vezi salariile mari. Intră și salariile mici.
STAFF QUESTIONS:
    greaterKeepsAboveSalary și notEqualIsNotAThreshold. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un prag de stoc: qty > 10 lasă depozitele peste 10. qty <> 10 lasă și depozitul gol.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT emp_no, salary FROM employees WHERE salary > 50000?
   Capcană: salary <> 50000 întoarce doar salariile mari.
   Cheie: greaterKeepsAboveSalary: rămân salariile strict mai mari decât 50000. fără >, salariul egal cu 50000 ar intra într-un >= pe care nu l-ai scris
   Demonstrație: greaterKeepsAboveSalary
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT emp_no, salary FROM employees WHERE salary > 50000?
   Capcană: NULL este 0, deci intră la salary > 50000 dacă 0 nu e peste prag... și tot e comparat.
   Cheie: Efectul este: rămân salariile strict mai mari decât 50000. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: greaterKeepsAboveSalary
3. Enunț: De ce SELECT emp_no, salary FROM employees WHERE salary > 50000 nu este vecinul său?
   Capcană: BETWEEN 50000 AND 50000 este același lucru cu >.
   Cheie: BETWEEN este interval închis. <> lasă tot ce nu este egal, inclusiv NULL-ul care nu trece prin <> așa cum trece un număr.
   Demonstrație: greaterKeepsAboveSalary
4. Enunț: Cum eșuează SELECT emp_no, salary FROM employees WHERE salary <> 50000?
   Capcană: Simbolul ≠ este operatorul pe care îl primește serverul.
   Cheie: notEqualIsNotAThreshold: <> scoate doar egalul. > este pragul de deasupra.
   Demonstrație: notEqualIsNotAThreshold
5. Enunț: Când nu mai trimiți SELECT emp_no, salary FROM employees WHERE salary > 50000?
   Capcană: Ca să compari, convertești salariul în aplicație și nu mai scrii WHERE.
   Cheie: BETWEEN când ai două capete inclusiv. IS NULL când lipsa nu trebuie tratată ca 0.
   Demonstrație: greaterKeepsAboveSalary

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei strict peste 50000 -> greaterKeepsAboveSalary -> SELECT emp_no, salary FROM employees WHERE salary > 50000 -> nu garantează că NULL este 0 și trece prin comparație
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> notEqualIsNotAThreshold -> SELECT emp_no, salary FROM employees WHERE salary <> 50000 -> <> scoate doar egalul. > este pragul de deasupra.
    3. Granița
           -> Comparația este predicatul. Alt+8800 pentru ≠ nu este operatorul serverului; operatorul este <> sau !=.
    4. Alternativa
           -> BETWEEN este interval închis. <> lasă tot ce nu este egal, inclusiv NULL-ul care nu trece prin <> așa cum trece un număr.
    5. Anti-patternul
           -> <> 50000 ca să vezi salariile mari. Intră și salariile mici.
    6. Dovada
           -> greaterKeepsAboveSalary ține propoziția. notEqualIsNotAThreshold ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> > este prag strict. <> scoate doar egalul. NULL nu trece prin >. Semnul ≠ desenat nu este operatorul.
*/

public class DB_MATH_COMPARATORS {
    /*
    🟢 Începător
        Ce este            rămân salariile strict mai mari decât 50000
        De ce e nevoie     fără >, salariul egal cu 50000 ar intra într-un >= pe care nu l-ai scris
    🟡 Producție
        Cum se scrie       SELECT emp_no, salary FROM employees WHERE salary > 50000
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este salary <> 50000, care lasă și salariile mici, și pe cele mari, și pierde egalul
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu salary <> 50000, care lasă și salariile mici, și pe cele mari, și pierde egalul.
    */
    // Începător: greaterKeepsAboveSalary păstrează propoziția pentru rămân salariile strict mai mari decât 50000
    // Producție: public static String greaterKeepsAboveSalary(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. NULL nu este mai mare decât 50000. Rândul cu salary NULL cade.
    public static String greaterKeepsAboveSalary() {
        // Începător: rămân salariile strict mai mari decât 50000
        // Producție: SELECT emp_no, salary FROM employees WHERE salary > 50000. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: NULL nu este mai mare decât 50000. Rândul cu salary NULL cade. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT emp_no, salary FROM employees WHERE salary > 50000";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide greaterKeepsAboveSalary. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            rămân și salariile sub prag, nu doar cele peste 50000
        De ce e nevoie     fără textul greșit, diferit de 50000 pare pragul
    🟡 Producție
        Cum se scrie       SELECT emp_no, salary FROM employees WHERE salary <> 50000
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care rămân și salariile sub prag, nu doar cele peste 50000
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu salary <> 50000, care lasă și salariile mici, și pe cele mari, și pierde egalul.
    */
    // Începător: notEqualIsNotAThreshold păstrează textul greșit pentru rămân și salariile sub prag, nu doar cele peste 50000
    // Producție: public static String notEqualIsNotAThreshold(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. <> scoate doar egalul. > este pragul de deasupra.
    public static String notEqualIsNotAThreshold() {
        // Începător: rămân și salariile sub prag, nu doar cele peste 50000
        // Producție: SELECT emp_no, salary FROM employees WHERE salary <> 50000. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: <> scoate doar egalul. > este pragul de deasupra. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT emp_no, salary FROM employees WHERE salary <> 50000";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide notEqualIsNotAThreshold. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
