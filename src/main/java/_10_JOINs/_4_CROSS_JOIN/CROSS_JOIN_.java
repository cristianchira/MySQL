package _10_JOINs._4_CROSS_JOIN;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/10653042#questions


/*                                                     CROSS JOIN


    ○ a cross join will take the values from a certain table

                  * and connect them with all the values from the tables we want to join it with


    ○ CROSS JOIN

                  * connects all the values, not just those that match

                  * the Cartesian product of the values of two or more sets

                  * particularly useful when the tables in a database

                                - are not well-connected

    ○ JOIN

                 * typically connects only the matching values


   ○ EXAMPLE CROSS JOIN 2 table

                *  SELECT dm.*, d.*
                   FROM dept_manager dm

                       CROSS JOIN departments d

                  ORDER BY dm.emp_no, d.dept_no;


 ○ EXAMPLE CROSS JOIN 3 table


               * SELECT e.*, d.*
                 FROM departments d

                 CROSS JOIN dept_manager dm
                 JOIN employees e on dm.emp_no = e.emp_no

	             WHERE d.dept_no <> dm.dept_no
                 ORDER BY dm.emp_no, d.dept_no;


    ○  EXAMPLE

               * Return a list with the first 10 employees with all the departments they can be assigned to.

               SELECT  e.*, d.*
               FROM employees e

                   CROSS JOIN  departments d

               WHERE  e.emp_no < 10011
               ORDER BY e.emp_no, d.dept_name;


 */


/*
TOPIC:
    CROSS JOIN scrie produsul: fiecare rând din dept_manager cu fiecare rând din departments. Nu păstrează doar dept_no comun.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Lista „fiecare manager, cu fiecare departament la care ar putea fi mutat” este produsul. Perechea de azi este INNER JOIN ON.
MECHANISM:
    Serverul execută everyManagerWithEveryDepartment. Fără condiție de potrivire, fiecare rând din stânga se combină cu fiecare rând din dreapta. Efectul este în rezultat, la sfârșitul SELECT-ului. Tabela nu primește rânduri noi.
LOOKALIKES:
    În MySQL, JOIN fără ON este același produs, dar nu este un CROSS declarat. INNER JOIN ON păstrează doar perechea. WHERE după CROSS taie din produs; nu schimbă faptul că produsul a fost forma de la care s-a pornit.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2, deschis în tur: INNER JOIN și virgula, fără condiție, produc produsul cartezian; JOIN, CROSS JOIN și INNER JOIN sunt echivalente sintactic în MySQL, spre deosebire de standard, unde CROSS este forma fără ON. Un JOIN amestecat cu virgula poate da Unknown column în ON, pentru că virgula are precedență mai mică. CROSS+JOIN.pdf, deschis în tur, are pe primele două pagini doar titlul.
BOOK EXAMPLE:
    everyManagerWithEveryDepartment
REPO DEMO:
    src/main/java/_10_JOINs/_4_CROSS_JOIN/CROSS_JOIN_.java
BREAK LAB:
    joinWithoutOnIsNotADeclaredCross este produsul fără cuvântul CROSS. Șirul nu a fost trimis.
TRADE-OFFS:
    INNER JOIN ON când vrei departamentul actual. CROSS când vrei matricea de asignare. Produsul a trei tabele crește cu fiecare tabelă; 61 este plafonul de referințe dintr-un join, nu o scuză să le înmulțești pe toate.
ANTI-PATTERNS:
    JOIN fără ON, lăsat din greșeală, citit apoi ca listă de departamente reale.
STAFF QUESTIONS:
    Cele cinci de mai jos.
NOVEL TRANSFER:
    Fiecare colet cu fiecare raft este matricea de mutare. Coletul care stă azi pe un raft este INNER JOIN, nu acest produs.
SPACED REVIEW:
    O zi: 21 octombrie 2026, fiecare cu fiecare. Șapte zile: 27 octombrie 2026, JOIN fără ON nu este CROSS-ul scris. Treizeci de zile: 19 noiembrie 2026, WHERE taie din produs, nu îl declară.

1. Enunț: Ce problemă rezolvă CROSS JOIN între dept_manager și departments?
   Capcană: Leagă managerul de departamentul lui, pentru că ambele tabele au dept_no.
   Cheie: everyManagerWithEveryDepartment nu compară dept_no. Fiecare manager apare cu fiecare departament. Perechea reală este alt mecanism, cu ON.
   Demonstrație: everyManagerWithEveryDepartment
2. Enunț: Ce se întâmplă cu rândurile din cele două tabele?
   Capcană: Serverul scrie combinațiile într-o tabelă nouă, la COMMIT.
   Cheie: Rezultatul are câte un rând pentru fiecare combinație. dept_manager și departments rămân neschimbate. Efectul se vede la sfârșitul SELECT-ului.
   Demonstrație: everyManagerWithEveryDepartment
3. Enunț: De ce JOIN fără ON nu este acest CROSS declarat?
   Capcană: În MySQL sunt echivalente, deci forma scrisă nu contează la interviu.
   Cheie: joinWithoutOnIsNotADeclaredCross dă același produs în MySQL 9.7. Nu spune că ai cerut produsul. Un ON uitat arată ca o pereche și este produsul. CROSS este produsul scris.
   Demonstrație: joinWithoutOnIsNotADeclaredCross
4. Enunț: Cum eșuează produsul când îl citești ca pe departamentul actual al managerului?
   Capcană: dept_no egal se filtrează singur, pentru că ambele coloane există.
   Cheie: Fără ON, un manager cu 9 departamente în departments apare de 9 ori, inclusiv la departamente care nu sunt ale lui. Lista nu este asignarea.
   Demonstrație: joinWithoutOnIsNotADeclaredCross
5. Enunț: Când lași CROSS și când treci la INNER JOIN ON?
   Capcană: CROSS este mereu mai complet, pentru că nu pierde combinații.
   Cheie: CROSS când întrebarea este matricea. INNER JOIN ON m.dept_no = d.dept_no când întrebarea este departamentul actual. WHERE după CROSS poate tăia, dar pornește de la produs.
   Demonstrație: everyManagerWithEveryDepartment

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE fiecare manager cu fiecare departament -> everyManagerWithEveryDepartment -> CROSS JOIN departments d -> nu garantează că tabelele au rânduri în tur
    2. Eșecul cel mai mic
           -> DECIDE JOIN fără ON, citit ca asignare -> joinWithoutOnIsNotADeclaredCross -> același produs, nedeclarat -> nu garantează o eroare de sintaxă în MySQL
    3. Granița
           -> Produsul este mecanismul. Șirul Java nu îl execută. Workbench nu adaugă ON dacă l-ai omis.
    4. Alternativa
           -> INNER JOIN ON când vrei perechea. LEFT JOIN când vrei și managerul fără departament. Produsul nu ține locul lor.
    5. Anti-patternul
           -> JOIN fără ON. Fiecare manager apare cu fiecare departament, iar lista pare o asignare.
    6. Dovada
           -> everyManagerWithEveryDepartment scrie CROSS. joinWithoutOnIsNotADeclaredCross nu îl scrie. 1045 a oprit trimiterea.
    7. Interviu
           -> CROSS este produsul scris. JOIN fără ON este același produs în MySQL, nu o pereche. ON este alt mecanism.
*/

public class CROSS_JOIN_ {

    /*
    🟢 Începător
        Ce este            fiecare manager combinat cu fiecare departament
        De ce e nevoie     fără CROSS scris, produsul poate fi un ON uitat
    🟡 Producție
        Cum se scrie       SELECT dm.emp_no, d.dept_no FROM dept_manager dm CROSS JOIN departments d ORDER BY dm.emp_no, d.dept_no
    🔴 Staff
        Unde               în MySQL, JOIN fără ON este același produs; standardul nu le face echivalente
    Pași
        1. Scrie CROSS, fără ON.
        2. Nu trimite șirul.
        3. Nu filtra dept_no aici.
    */
    // Începător: metoda păstrează matricea manager-departament.
    // Producție: public static String everyManagerWithEveryDepartment(). Dacă dispare, exemplul din comentariu nu mai este propoziție.
    // Expert: Nu deschide JDBC. Nu adaugă ON. Trei tabele înmulțesc încă o dată; plafonul din manual este 61 de referințe, nu un număr de rânduri.
    public static String everyManagerWithEveryDepartment() {
        // Începător: fiecare manager rămâne lângă fiecare departament, nu doar lângă al lui.
        // Producție: CROSS JOIN departments d. Dacă linia dispare, produsul declarat dispare.
        // Expert: WHERE d.dept_no <> dm.dept_no, din comentariul cu trei tabele, taie din produs combinația actuală. Nu este această propoziție și nu o face INNER.
        return "SELECT dm.emp_no, d.dept_no FROM dept_manager dm CROSS JOIN departments d ORDER BY dm.emp_no, d.dept_no";
        // Începător: metoda a înapoiat combinațiile, fără să le salveze.
        // Producție: acolada închide everyManagerWithEveryDepartment. Șirul nu a plecat la 3306.
        // Expert: ORDER BY nu reduce produsul. 1045 nu este un rezultat cu zero combinații.
    }

    /*
    🟢 Începător
        Ce este            același fiecare-cu-fiecare, fără să scrii că asta ai cerut
        De ce e nevoie     fără ea, lipsa lui ON pare o pereche
    🟡 Producție
        Cum se scrie       SELECT dm.emp_no, d.dept_name FROM dept_manager dm JOIN departments d
    🔴 Staff
        Unde               echivalent sintactic în MySQL 9.7; nu este CROSS-ul declarat din catalog
    Pași
        1. Nu scrie CROSS și nu scrie ON.
        2. Nu o trimite.
        3. Nu o amesteca cu virgulă: ON-ul ar vedea alți operanzi.
    */
    // Începător: metoda păstrează produsul care nu își spune numele.
    // Producție: public static String joinWithoutOnIsNotADeclaredCross(). Dacă dispare, deosebirea de CROSS rămâne o frază.
    // Expert: Unknown column în ON este eșecul virgulei amestecate cu JOIN, din JOIN_MULTIPLE_TABLE, nu acest șir.
    public static String joinWithoutOnIsNotADeclaredCross() {
        // Începător: fiecare manager apare cu fiecare departament, deși propoziția nu spune CROSS.
        // Producție: JOIN departments d, fără ON. Dacă linia dispare, nu mai ai produsul nedeclarat.
        // Expert: INNER JOIN cu ON pe dept_no ar fi lăsat doar perechea. Acest JOIN fără ON nu compară dept_no. Un index nu oprește produsul.
        return "SELECT dm.emp_no, d.dept_name FROM dept_manager dm JOIN departments d";
        // Începător: metoda a păstrat forma periculoasă, fără execuție.
        // Producție: acolada închide joinWithoutOnIsNotADeclaredCross. Șirul a rămas în proces.
        // Expert: Nu s-a măsurat câte rânduri ies. Numărul este produsul cardinalelor, doar dacă serverul primește șirul.
    }
    // Începător: clasa se oprește la produsul scris și la produsul fără cuvântul CROSS.
    // Producție: acolada închide CROSS_JOIN_. Perechea cu ON nu este în aceste metode.
    // Expert: În MySQL, JOIN fără ON este același produs. Acolada nu îl declară CROSS.
}
