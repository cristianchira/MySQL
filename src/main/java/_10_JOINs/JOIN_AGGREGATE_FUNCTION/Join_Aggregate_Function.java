package _10_JOINs.JOIN_AGGREGATE_FUNCTION;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8499202#questions


/*                                Join Aggregate Function


    ○ column we choose for select clause must be meaningful.



 */


/*
TOPIC:
    Media salariului se calculează pe rândurile de salariu, grupate, înainte să lipești departamentul. Un JOIN cu titles înainte de AVG numără același salariu de mai multe ori.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    avg_salary de lângă dept_name trebuie să fie media salariilor omului, nu media înmulțită cu numărul de titluri.
MECHANISM:
    Serverul execută întâi grupul din tabela derivată: AVG(salary) pe emp_no. Apoi JOIN-ul doar atașează departamentul. HAVING filtrează grupul. WHERE ar fi scos rânduri de salariu înainte de medie. ONLY_FULL_GROUP_BY este implicit.
LOOKALIKES:
    WHERE s.salary > 50000 înainte de AVG schimbă numerele care intră în medie. HAVING avg_salary > 50000 scoate grupul după medie. GROUP BY singur, fără join, nu aduce dept_name. ANY_VALUE nu repară o medie înmulțită.
BOOK THEORY:
    Manualul 9.7, 14.19.3, deschis în tur: ONLY_FULL_GROUP_BY respinge coloana neagregată care nu este în GROUP BY și nu depinde funcțional, cu ERROR 1055; fără GROUP BY, coloana neagregată lângă AVG dă ERROR 1140; HAVING poate folosi aliasul agregatului, extensie MySQL; WHERE nu este HAVING. 15.2.13.2: join-ul produce rândurile pe care agregatul le vede. Pagina de join nu spune că AVG după un al doilea join păstrează o singură copie a salariului.
BOOK EXAMPLE:
    averageSalaryBeforeTheDepartmentJoin
REPO DEMO:
    src/main/java/_10_JOINs/JOIN_AGGREGATE_FUNCTION/Join_Aggregate_Function.java
BREAK LAB:
    averageAfterTitlesMultipliesRows joinează titles înainte de AVG(s.salary). Un om cu două titluri își dublează fiecare salariu în medie. Șirul nu a fost trimis.
TRADE-OFFS:
    ANY_VALUE sau o coloană în GROUP BY când vrei o valoare care nu intră în medie și depinde de grup. Aplicația când media se face pe un catalog de salarii care nu este tabelă. Indexul nu oprește înmulțirea.
ANTI-PATTERNS:
    JOIN titles t înainte de AVG(s.salary), apoi GROUP BY e.emp_no, cu titlul și departamentul în SELECT.
STAFF QUESTIONS:
    Cele cinci de mai jos.
NOVEL TRANSFER:
    Media unei facturi se calculează pe liniile facturii. Join-ul cu fiecare colet din linie, înainte de AVG, numără factura de câte ori are colete. Media corectă stă într-o tabelă derivată, apoi se lipește depozitul.
SPACED REVIEW:
    O zi: 21 octombrie 2026, media înainte de join-ul care înmulțește. Șapte zile: 27 octombrie 2026, WHERE taie rânduri, HAVING taie grupuri. Treizeci de zile: 19 noiembrie 2026, 1055 nu repară media.

1. Enunț: Ce problemă rezolvă media într-o tabelă derivată, înainte de departament?
   Capcană: AVG după toate JOIN-urile este aceeași medie, pentru că AVG ignoră duplicatele de la join.
   Cheie: averageSalaryBeforeTheDepartmentJoin face AVG(salary) pe emp_no, în salaries, apoi lipește dept_manager și departments. Un titlu în plus nu mai înmulțește salariul.
   Demonstrație: averageSalaryBeforeTheDepartmentJoin
2. Enunț: Ce rânduri intră în medie și când vezi numărul?
   Capcană: Media se scrie în salaries la COMMIT, iar join-ul doar o citește.
   Cheie: AVG citește rândurile de salariu din grup. Rezultatul este al SELECT-ului, la sfârșitul lui. salaries nu primește o coloană nouă. Un al doilea salariu al aceluiași emp_no intră în medie o dată, în forma din derived table.
   Demonstrație: averageSalaryBeforeTheDepartmentJoin
3. Enunț: De ce WHERE s.salary > 50000 nu este HAVING avg_salary > 50000?
   Capcană: Ambele taie salariile mici, deci media iese la fel.
   Cheie: WHERE scoate rândul înainte de AVG, deci media se face doar pe salariile rămase. HAVING, după agregare, scoate grupul a cărui medie nu trece de 50000. Manualul 14.19.3 lasă aliasul în HAVING. Nu este același număr.
   Demonstrație: averageSalaryBeforeTheDepartmentJoin
4. Enunț: Cum eșuează AVG(s.salary) după JOIN titles și JOIN salaries, grupat pe emp_no?
   Capcană: Titlul este doar o coloană afișată, deci nu schimbă media.
   Cheie: averageAfterTitlesMultipliesRows: fiecare salariu se repetă pentru fiecare titlu înainte de AVG. Doi titluri duc același salariu de două ori în medie. În plus, title și dept_name lângă GROUP BY emp_no pot lua ERROR 1055 dacă nu depind de emp_no.
   Demonstrație: averageAfterTitlesMultipliesRows
5. Enunț: Când agregi înainte și când lași join-ul sau aplicația?
   Capcană: ANY_VALUE(title) repară media, pentru că face propoziția legală.
   Cheie: Derived table când media trebuie să vadă salariul o dată. ANY_VALUE face legală coloana, nu desface înmulțirea. Aplicația când salariul nu este în această bază. Indexul pe emp_no nu schimbă de câte ori rândul intră în AVG.
   Demonstrație: averageAfterTitlesMultipliesRows

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE media salariilor omului, apoi departamentul -> averageSalaryBeforeTheDepartmentJoin -> AVG în derived table, GROUP BY emp_no, apoi JOIN -> nu garantează un singur departament: două roluri dau două rânduri cu aceeași medie
    2. Eșecul cel mai mic
           -> DECIDE AVG după JOIN titles -> averageAfterTitlesMultipliesRows -> același salariu intră de câte ori sunt titluri -> nu garantează ERROR 1055: media poate fi și legală, și greșită
    3. Granița
           -> Rândurile care intră în AVG sunt mecanismul. Șirul Java nu calculează media. Workbench nu mută JOIN-ul după grup.
    4. Alternativa
           -> HAVING când vrei să arunci grupul după medie. WHERE când vrei să scoți salariul înainte. Fără join, când nu îți trebuie dept_name.
    5. Anti-patternul
           -> JOIN titles înainte de AVG(s.salary). Media nu mai este media salariilor.
    6. Dovada
           -> averageSalaryBeforeTheDepartmentJoin ține grupul înainte de departament. averageAfterTitlesMultipliesRows ține titles înainte de AVG. 1045 a oprit trimiterea.
    7. Interviu
           -> Join-ul înmulțește rândurile pe care AVG le vede. Grupează salariul întâi. WHERE taie rânduri, HAVING taie grupuri. 1055 este coloana neagregată, nu media corectă.
*/

public class Join_Aggregate_Function {

    /*
    🟢 Începător
        Ce este            media salariilor unui om, apoi numele departamentului lângă ea
        De ce e nevoie     fără grupul dinainte, titlul sau departamentul pot înmulți salariul
    🟡 Producție
        Cum se scrie       SELECT d.dept_name, a.avg_salary FROM (SELECT emp_no, AVG(salary) AS avg_salary FROM salaries GROUP BY emp_no) a JOIN ...
    🔴 Staff
        Unde               HAVING ar filtra media; WHERE în interior ar filtra salariul; ONLY_FULL_GROUP_BY este implicit
    Pași
        1. AVG doar pe salaries, grupat pe emp_no.
        2. JOIN după aceea.
        3. Nu trimite șirul.
    */
    // Începător: metoda păstrează media care vede fiecare salariu o dată.
    // Producție: public static String averageSalaryBeforeTheDepartmentJoin(). Dacă dispare, nu mai ai propoziția care nu înmulțește.
    // Expert: Nu deschide JDBC. Nu pune titles în derived table. Nu promite că AVG ignoră rândurile duplicate de la un join pe care nu îl scrii.
    public static String averageSalaryBeforeTheDepartmentJoin() {
        // Începător: întâi se face media salariilor omului. Abia apoi apare departamentul. Doi titluri nu schimbă media.
        // Producție: SELECT emp_no, AVG(salary) AS avg_salary FROM salaries GROUP BY emp_no, apoi JOIN dept_manager și departments. Dacă linia dispare, rămâne doar exemplul vechi, care joinează înainte.
        // Expert: Aliasul a este cerut la tabela derivată. Două rânduri în dept_manager pentru același emp_no dau două rezultate cu aceeași medie, nu o medie dublată. HAVING a.avg_salary > 50000 ar scoate grupul; nu este în acest șir. ERROR 1140 ar fi dacă numele ar sta lângă AVG fără GROUP BY.
        return "SELECT d.dept_name, a.avg_salary FROM (SELECT emp_no, AVG(salary) AS avg_salary FROM salaries GROUP BY emp_no) a JOIN dept_manager dm ON dm.emp_no = a.emp_no JOIN departments d ON d.dept_no = dm.dept_no";
        // Începător: metoda a înapoiat media, nu o coloană scrisă în salaries.
        // Producție: acolada închide averageSalaryBeforeTheDepartmentJoin. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este 1055. Coloanele din SELECT-ul exterior sunt grupul deja făcut și dept_name venit din join, nu din GROUP BY emp_no al salariilor.
    }

    /*
    🟢 Începător
        Ce este            media făcută după ce fiecare titlu a repetat salariul
        De ce e nevoie     fără ea, JOIN-ul dinainte de AVG pare inofensiv
    🟡 Producție
        Cum se scrie       FROM employees e JOIN titles t ON e.emp_no = t.emp_no JOIN salaries s ON e.emp_no = s.emp_no ... AVG(s.salary) ... GROUP BY e.emp_no
    🔴 Staff
        Unde               înmulțirea este în rândurile de dinainte de AVG; 1055 poate cădea separat, pe title și dept_name
    Pași
        1. Ține titles înainte de AVG.
        2. Nu o trimite.
        3. Nu o repara cu ANY_VALUE: nu desface înmulțirea.
    */
    // Începător: metoda păstrează media care nu mai este media salariilor.
    // Producție: public static String averageAfterTitlesMultipliesRows(). Dacă dispare, eșecul rămâne doar în comentariul de la mai multe tabele.
    // Expert: Nu este ERROR 1140. Există GROUP BY. Problema este câte rânduri de salariu vede AVG.
    public static String averageAfterTitlesMultipliesRows() {
        // Începător: același salariu intră în medie de câte ori are omul titluri. Doi titluri nu înseamnă două salarii.
        // Producție: JOIN titles t înainte de AVG(s.salary), GROUP BY e.emp_no. Dacă linia dispare, nu mai ai înmulțirea.
        // Expert: title și d.dept_name nu depind de emp_no dacă există mai multe. ONLY_FULL_GROUP_BY poate răspunde ERROR 1055 înainte să vezi media greșită. ANY_VALUE(t.title) ar trece de 1055 și ar lăsa media înmulțită. HAVING avg_salary > 50000 ar filtra grupul deja stricat.
        return "SELECT e.emp_no, t.title, d.dept_name, AVG(s.salary) AS avg_salary FROM employees e JOIN titles t ON e.emp_no = t.emp_no JOIN salaries s ON e.emp_no = s.emp_no JOIN dept_manager dm ON e.emp_no = dm.emp_no JOIN departments d ON d.dept_no = dm.dept_no GROUP BY e.emp_no";
        // Începător: metoda a păstrat media stricată, fără să o calculeze.
        // Producție: acolada închide averageAfterTitlesMultipliesRows. Șirul a rămas în proces.
        // Expert: Nu s-a citit un plan. Indexul pe emp_no nu împarte înapoi rândurile înmulțite.
    }
    // Începător: clasa se oprește la media făcută înainte de departament și la media înmulțită de titluri.
    // Producție: acolada închide Join_Aggregate_Function. HAVING nu este în aceste două șiruri.
    // Expert: ERROR 1055 poate opri a doua propoziție înainte să vezi media. Acolada nu o trimite.
}
