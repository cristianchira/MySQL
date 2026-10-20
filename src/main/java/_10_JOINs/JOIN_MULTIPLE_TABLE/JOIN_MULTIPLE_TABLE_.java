package _10_JOINs.JOIN_MULTIPLE_TABLE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8499204#questions



/*                                                       JOIN MULTIPLE TABLE


    ○ When creating a query that joins multiple tables,

                   * you must back it with strong intuition

                   * and a crystal-clear idea of how you would like the tables to be connected


    ○ EXAMPLE  -- join 4 table

SELECT
    e.emp_no,
    e.first_name,
    e.last_name,
    e.hire_date,
    dm.from_date,
    d.dept_name,
    AVG(s.salary) AS avg_salary,
    t.title
FROM
    employees e
        JOIN
    titles t ON e.emp_no = t.emp_no
        JOIN
    salaries s ON e.emp_no = s.emp_no
        JOIN
    dept_manager dm ON e.emp_no = dm.emp_no
        JOIN
    departments d ON d.dept_no = dm.dept_no
GROUP BY e.emp_no
HAVING avg_salary > 50000
ORDER BY d.dept_name , dm.from_date;
                  HISTORICAL: sub sql_mode implicit ONLY_FULL_GROUP_BY, manualul 9.7, 14.19.3, deschis în tur, o coloană neagregată care nu depinde funcțional de e.emp_no este respinsă cu ERROR 1055 (42000). d.dept_name, t.title și dm.from_date nu sunt determinate de emp_no dacă managerul are mai multe departamente sau titluri. AVG(s.salary) după JOIN-ul cu titles numără fiecare salariu de câte ori are titluri.



 */


/*
TOPIC:
    Mai multe JOIN-uri stau în același FROM. ON vede doar operanzii acelei îmbinări. Virgula se leagă mai slab decât JOIN, deci e.emp_no poate deveni Unknown column în ON.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Numele angajatului și numele departamentului cer două potriviri în lanț: employees la dept_manager, apoi departments. O virgulă între ele schimbă operanzii.
MECHANISM:
    Serverul execută managerDepartmentInOneFrom de la stânga la dreapta, fiecare ON pe operanzii lui. Plafonul din manual este 61 de referințe, inclusiv cele contopite din view sau derived table. Efectul este rezultatul.
LOOKALIKES:
    O subcerere în FROM este o tabelă derivată și cere alias; nu este al treilea JOIN. GROUP BY e.emp_no care selectează titlul și departamentul poate lua ERROR 1055. AVG după încă un JOIN înmulțește rândurile și stă la copilul de agregare.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2, deschis în tur: ON nu poate vedea o coloană care nu este operand; SELECT cu ON (i1 = i3) înainte ca t3 să fie operand cade cu Unknown column în on clause; t1, t2 JOIN t3 ON t1.i1 = t3.i3 este (t1, (t2 JOIN t3)) și cade la fel; maxim 61 de tabele. 14.19.3: ERROR 1055 când coloana nu depinde de GROUP BY. JOIN+Multiple+Tables.pdf: pagina 1 este „FRAGILE / HANDLE WITH CARE”, pagina 2 este titlul. Nu explică precedența virgulei.
BOOK EXAMPLE:
    managerDepartmentInOneFrom
REPO DEMO:
    src/main/java/_10_JOINs/JOIN_MULTIPLE_TABLE/JOIN_MULTIPLE_TABLE_.java
BREAK LAB:
    commaThenJoinHidesTheFirstTable amestecă virgula cu JOIN și pune e.emp_no în ON. Șirul nu a fost trimis. Pagina arată Unknown column în on clause pentru această precedență.
TRADE-OFFS:
    O tabelă derivată când agregatul trebuie calculat înainte de următorul JOIN. Rămâi în aplicație când a patra sursă nu este tabelă pe acest server. Nu urca spre 61 ca să eviți o coloană lipsă.
ANTI-PATTERNS:
    employees e, dept_manager dm JOIN departments d ON e.emp_no = d.dept_no. Virgula face ca ON să nu îl vadă pe e.
STAFF QUESTIONS:
    Cele cinci de mai jos.
NOVEL TRANSFER:
    Colet, palet și raft, în lanț, fiecare ON doar între vecinii scriși. O virgulă între colet și palet face ca ON-ul raftului să nu vadă coletul.
SPACED REVIEW:
    O zi: 21 octombrie 2026, fiecare ON își vede operanzii. Șapte zile: 27 octombrie 2026, virgula se leagă mai slab. Treizeci de zile: 19 noiembrie 2026, 1055 nu este Unknown column.

1. Enunț: Ce problemă rezolvă două JOIN-uri, employees la dept_manager și apoi la departments?
   Capcană: A doua condiție poate folosi orice coloană de mai sus, pentru că toate sunt în FROM.
   Cheie: managerDepartmentInOneFrom leagă e de dm pe emp_no, apoi dm de d pe dept_no. Fiecare ON vede operanzii lui. Angajatul care nu este în dept_manager cade, pentru că ambele sunt INNER.
   Demonstrație: managerDepartmentInOneFrom
2. Enunț: Ce se întâmplă cu rândul de angajat care nu este manager?
   Capcană: Rămâne, cu dept_name NULL, pentru că sunt mai multe tabele și serverul păstrează stânga.
   Cheie: INNER îl scoate din rezultat. Tabela employees nu pierde rândul. Ca să rămână, primul JOIN ar fi LEFT. Efectul se vede la sfârșitul SELECT-ului.
   Demonstrație: managerDepartmentInOneFrom
3. Enunț: De ce o tabelă derivată în FROM nu este al treilea JOIN?
   Capcană: Orice lucru din FROM este un JOIN, deci aliasul cerut la subcerere este optional.
   Cheie: Tabela derivată este o subcerere și manualul îi cere alias. JOIN este potrivirea dintre referințe. Același rezultat nu este același plan. Agregatul dinaintea join-ului stă la copilul de agregare.
   Demonstrație: managerDepartmentInOneFrom
4. Enunț: Cum eșuează employees e, dept_manager dm JOIN departments d ON e.emp_no = d.dept_no?
   Capcană: Virgula este un INNER, deci e este operand al lui ON.
   Cheie: commaThenJoinHidesTheFirstTable. JOIN se leagă înaintea virgulei, deci ON vede dm și d, nu e. Pagina 15.2.13.2 arată Unknown column în on clause.
   Demonstrație: commaThenJoinHidesTheFirstTable
5. Enunț: Când rămâi la lanțul de JOIN și când te oprești la GROUP BY sau la aplicație?
   Capcană: GROUP BY e.emp_no face legale titlul și departamentul, oricâte rânduri ar avea omul.
   Cheie: Lanțul când fiecare legătură este o pereche pe cheie. ERROR 1055 când selectezi title sau dept_name fără să fie determinate de emp_no. Aplicația când a patra sursă nu este pe acest server. 61 este plafonul, nu ținta.
   Demonstrație: commaThenJoinHidesTheFirstTable

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE angajat, manager, departament, fiecare ON pe vecinii lui -> managerDepartmentInOneFrom -> JOIN dm ON emp_no, apoi JOIN d ON dept_no -> nu garantează un singur departament per angajat
    2. Eșecul cel mai mic
           -> DECIDE virgulă apoi JOIN, cu e.emp_no în ON -> commaThenJoinHidesTheFirstTable -> Unknown column în on clause -> nu garantează că șirul a ajuns la server
    3. Granița
           -> Operanzii lui ON sunt mecanismul. Șirul Java nu este planul. Workbench nu pune parantezele în jurul virgulei.
    4. Alternativa
           -> (e JOIN dm) JOIN d când vrei lanțul explicit. Tabelă derivată când agregi înainte. LEFT pe primul hop când angajatul fără manager trebuie să rămână.
    5. Anti-patternul
           -> e, dm JOIN d ON e.emp_no = d.dept_no. e nu este operand.
    6. Dovada
           -> managerDepartmentInOneFrom ține două JOIN-uri. commaThenJoinHidesTheFirstTable ține virgula. Exemplul vechi cu patru tabele și GROUP BY rămâne în comentariu, marcat la 1055. 1045 a oprit trimiterea.
    7. Interviu
           -> Fiecare ON își vede perechea. Virgula se leagă mai slab și dă Unknown column. 1055 este alt eșec, cel de GROUP BY, nu cel de ON.
*/

public class JOIN_MULTIPLE_TABLE_ {

    /*
    🟢 Începător
        Ce este            angajatul, apoi rolul de manager, apoi numele departamentului
        De ce e nevoie     fără lanț, a doua condiție nu are de unde să ia dept_no
    🟡 Producție
        Cum se scrie       FROM employees e JOIN dept_manager dm ON dm.emp_no = e.emp_no JOIN departments d ON d.dept_no = dm.dept_no
    🔴 Staff
        Unde               fiecare ON vede doar hop-ul lui; un angajat cu două departamente iese de două ori
    Pași
        1. Două JOIN-uri, fără virgulă.
        2. Nu grupa.
        3. Nu trimite șirul.
    */
    // Începător: metoda păstrează lanțul de la angajat la departament.
    // Producție: public static String managerDepartmentInOneFrom(). Dacă dispare, exemplul din comentariu rămâne fără o propoziție care trece de 1055.
    // Expert: Nu deschide JDBC. Nu selectează title. Nu promite că emp_no determină dept_name.
    public static String managerDepartmentInOneFrom() {
        // Începător: rămâne angajatul care este în dept_manager, cu numele departamentului acelui rol. Angajatul care nu e manager nu apare.
        // Producție: JOIN dept_manager dm ON dm.emp_no = e.emp_no JOIN departments d ON d.dept_no = dm.dept_no. Dacă linia dispare, nu mai ai operanzii corecți.
        // Expert: Comentariul vechi mai joina titles și salaries și grupa după emp_no. title și dept_name pot lua ERROR 1055. Aici nu sunt în SELECT. Două rânduri în dept_manager pentru același om dau două rânduri în rezultat, nu o medie.
        return "SELECT e.emp_no, e.first_name, d.dept_name FROM employees e JOIN dept_manager dm ON dm.emp_no = e.emp_no JOIN departments d ON d.dept_no = dm.dept_no";
        // Începător: metoda a înapoiat lanțul, nu o tabelă nouă.
        // Producție: acolada închide managerDepartmentInOneFrom. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este Unknown column. Aceea este propoziția cu virgulă.
    }

    /*
    🟢 Începător
        Ce este            o virgulă care face ca ON să nu vadă angajatul
        De ce e nevoie     fără ea, virgula pare un JOIN obișnuit
    🟡 Producție
        Cum se scrie       FROM employees e, dept_manager dm JOIN departments d ON e.emp_no = d.dept_no
    🔴 Staff
        Unde               precedența din 15.2.13.2: (e, (dm JOIN d)); e.emp_no nu este operand
    Pași
        1. Pune virgula înainte de JOIN.
        2. Pune e.emp_no în ON.
        3. Nu o trimite.
    */
    // Începător: metoda păstrează propoziția în care angajatul rămâne în afara condiției.
    // Producție: public static String commaThenJoinHidesTheFirstTable(). Dacă dispare, eroarea din manual rămâne fără șir.
    // Expert: Parantezele (employees e, dept_manager dm) ar schimba operanzii. Nu le pun: eșecul este forma fără ele.
    public static String commaThenJoinHidesTheFirstTable() {
        // Începător: condiția vrea numărul angajatului, dar angajatul nu este una din cele două tabele pe care JOIN le leagă acum.
        // Producție: employees e, dept_manager dm JOIN departments d ON e.emp_no = d.dept_no. Dacă linia dispare, nu mai ai Unknown column în on clause.
        // Expert: d.dept_no comparat cu e.emp_no ar fi fost oricum o potrivire greșită de coloane, dar propoziția cade înainte, la precedență. Nu este ERROR 1055. Un index nu face coloana vizibilă în ON.
        return "SELECT e.emp_no, d.dept_name FROM employees e, dept_manager dm JOIN departments d ON e.emp_no = d.dept_no";
        // Începător: metoda a păstrat forma care nu vede angajatul.
        // Producție: acolada închide commaThenJoinHidesTheFirstTable. Șirul a rămas în proces.
        // Expert: Nu s-a citit eroarea de pe server. Textul Unknown column este cel din pagina deschisă, pentru aceeași precedență.
    }
    // Începător: clasa se oprește la lanțul de două JOIN-uri și la virgula care ascunde angajatul.
    // Producție: acolada închide JOIN_MULTIPLE_TABLE_. Exemplul vechi cu patru tabele rămâne în comentariu.
    // Expert: ERROR 1055 și Unknown column nu sunt aceeași eroare. Acolada nu le amestecă într-un singur șir.
}
