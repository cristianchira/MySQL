package _10_JOINs._1_INNER_JOIN;

// Martin G. INNER JOINS   --Part 1
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8429210#questions

// Martin G. INNER JOINS   --Part 2
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8429212#questions/10290372


// Martin G. A note using Joins
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8441428#questions


// JOIN Clause
// https://dev.mysql.com/doc/refman/8.0/en/join.html



/*                                             INNER JOINs


    ○ SELECT
          table_1.column_name(s), table_2.column_name(s)
       FROM
          table_1
       JOIN
          table_2
       ON table_1.column_name = table_2.column_name;

    ○ Example using aliases

                * select
                      m.emp_no, d.dept_name
                  from
                      dept_manager_dup m
                  inner join
                      departments_dup d
                  on m.dept_no = d.dept_no;

                              - inner is mandatory
                                  HISTORICAL: propoziția de deasupra nu este regula. Manualul 9.7, 15.2.13.2, deschis în tur: în MySQL, JOIN, CROSS JOIN și INNER JOIN sunt echivalente sintactic. INNER nu este obligatoriu. Fără ON, JOIN și virgula produc produsul cartezian.

    ○ Extract only records in which the values in the related columns match.
                * The SAME values appearing in two or more tables are
                            - matching values also called
                            - matching records.
                  * Null values,
                  * or values appearing in just one of the two tables and not appearing in the other,
                  * are NOT displayed
          Only non-null matching values are in play.
                  * And what if such matching values did not exist?
                  * Simply, the result set will be empty.
                  * There will be no link between the two tables.

    ○ Result set
                  * the area that belongs to both circles, which is filled with red,
                  * represents all records belonging to both the
                                - “Department Manager Duplicate”
                                - and the “Departments Duplicate” tables
                 * inner join can help us to extract result set


                                            SQL


    ○ SELECT t1.column_name, t1.column_name, …, t2.column_name, …
       FROM table_1 t1
       JOIN table_2 t2
       ON t1.column_name = t2.column_name;


    ○ EXAMPLE - INNER JOIN can be shortcut to JOIN( SQL will understand INNER JOIN)

                  * SELECT m.dept_no, m.emp_no, d.dept_name
                    FROM dept_manager_dup m
                    INNER JOIN departments_dup d
                    ON m.dept_no = d.dept_no
                    ORDER BY m.dept_no;


                                              Venn diagram


    ○ a mathematical tool representing all possible logical relations
                * between a finite collection of sets

 */

/*
TOPIC:
    INNER JOIN păstrează doar rândul din dept_manager_dup care are pereche în departments_dup pe dept_no. Managerul fără pereche cade din rezultat.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Lista de manageri cu numele departamentului nu are voie să conțină un manager al cărui dept_no nu este în departments_dup.
MECHANISM:
    Serverul execută matchingManagerAndDepartment. ON compară dept_no. NULL nu este egal cu NULL, deci o cheie NULL nu ține perechea. Efectul este rezultatul, la sfârșitul SELECT-ului.
LOOKALIKES:
    LEFT JOIN ar păstra managerul, cu NULL la dept_name. JOIN fără ON nu este „INNER obligatoriu”: în MySQL este produsul, ca CROSS. Un index pe dept_no nu scoate și nu adaugă o pereche.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2, deschis în tur: fără condiție, INNER JOIN și virgula dau produsul cartezian; JOIN, CROSS JOIN și INNER JOIN se pot înlocui; ON este search_condition; la LEFT fără pereche dreapta este NULL, deci INNER este cazul în care acel rând nu intră. INNER+JOIN.pdf, deschis în tur, are pe primele două pagini doar titlul. Nu spune că INNER este obligatoriu.
BOOK EXAMPLE:
    matchingManagerAndDepartment
REPO DEMO:
    src/main/java/_10_JOINs/_1_INNER_JOIN/INNER_JOIN.java
BREAK LAB:
    joinWithoutOnIsTheProduct nu are ON. Fiecare manager apare cu fiecare departament. Șirul nu a fost trimis.
TRADE-OFFS:
    LEFT JOIN când managerul fără departament trebuie să rămână în rezultat. Index pe dept_no când planul citește prea mult, fără să schimbe perechea.
ANTI-PATTERNS:
    JOIN fără ON, crezut INNER, sau WHERE d.dept_no IS NULL după INNER ca să cauți lipsa.
STAFF QUESTIONS:
    Cele cinci de mai jos. Banca de alegere este Joins_Staff_Interview, nu o a doua bancă de cincizeci aici.
NOVEL TRANSFER:
    Un colet rămâne în lista „colet pe raft” doar dacă raftul există. Coletul fără raft nu apare. Lista tuturor coletelor cu toate rafturile este produsul, nu această pereche.
SPACED REVIEW:
    O zi: 21 octombrie 2026, fără pereche rândul cade. Șapte zile: 27 octombrie 2026, JOIN fără ON este produsul. Treizeci de zile: 19 noiembrie 2026, INNER nu este obligatoriu ca cuvânt.

1. Enunț: Ce problemă rezolvă INNER JOIN între dept_manager_dup și departments_dup?
   Capcană: Aduce toate rândurile din ambele tabele, iar cele care nu se potrivesc vin cu spații goale.
   Cheie: matchingManagerAndDepartment păstrează doar dept_no comun. Managerul fără departament nu este în rezultat. Tabela nu pierde rândul.
   Demonstrație: matchingManagerAndDepartment
2. Enunț: Ce se întâmplă cu un manager al cărui dept_no este NULL sau nu există în departments_dup?
   Capcană: NULL se potrivește cu NULL, deci managerul rămâne dacă și departamentul are dept_no NULL.
   Cheie: = nu este adevărat pentru NULL. Rândul nu intră în rezultat. Nu se șterge din dept_manager_dup și nu așteaptă COMMIT.
   Demonstrație: matchingManagerAndDepartment
3. Enunț: De ce LEFT JOIN nu este acest mecanism?
   Capcană: LEFT este INNER plus un sort, deci aceleași rânduri, în altă ordine.
   Cheie: LEFT păstrează fiecare manager. Departamentul lipsă este NULL. INNER îl scoate din rezultat. Cuvântul INNER nu este obligatoriu: JOIN cu același ON este aceeași pereche.
   Demonstrație: matchingManagerAndDepartment
4. Enunț: Cum eșuează JOIN scris fără ON?
   Capcană: Fără ON, serverul refuză propoziția, pentru că INNER cere condiția.
   Cheie: joinWithoutOnIsTheProduct este legal în MySQL și dă produsul: fiecare manager cu fiecare departament. Nu este lista de perechi pe dept_no.
   Demonstrație: joinWithoutOnIsTheProduct
5. Enunț: Când lași INNER și când treci la LEFT sau la un index?
   Capcană: Indexul pe dept_no ține locul lui ON, iar LEFT se alege când vrei mai puține rânduri.
   Cheie: INNER când întrebarea este perechea. LEFT când managerul fără departament trebuie să apară. Indexul schimbă planul, nu rândul care se potrivește.
   Demonstrație: joinWithoutOnIsTheProduct

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE doar managerul care are departament -> matchingManagerAndDepartment -> INNER JOIN ON m.dept_no = d.dept_no -> nu garantează că tabelele _dup există pe server
    2. Eșecul cel mai mic
           -> DECIDE JOIN fără ON, crezut tot INNER -> joinWithoutOnIsTheProduct -> fiecare manager cu fiecare departament -> nu garantează o eroare de sintaxă
    3. Granița
           -> Perechea este mecanismul. Șirul Java nu rulează pe 3306. Workbench nu adaugă ON-ul omis. Aliasul d nu este o tabelă nouă.
    4. Alternativa
           -> LEFT JOIN când lipsa rămâne în rezultat. CROSS sau JOIN fără ON doar când vrei produsul, și atunci îl scrii ca produs, nu ca pereche.
    5. Anti-patternul
           -> JOIN fără ON. Rezultatul este produsul, nu managerii potriviți.
    6. Dovada
           -> matchingManagerAndDepartment ține ON. joinWithoutOnIsTheProduct nu îl are. 1045 a oprit trimiterea.
    7. Interviu
           -> INNER păstrează perechea. Fără ON, în MySQL, este produsul. LEFT ar fi păstrat managerul cu NULL. Efectul este în rezultat.
*/

public class INNER_JOIN {

    /*
    🟢 Începător
        Ce este            managerul împreună cu departamentul doar când numărul coincid
        De ce e nevoie     fără ON, produsul pare tot o potrivire
    🟡 Producție
        Cum se scrie       SELECT m.dept_no, m.emp_no, d.dept_name FROM dept_manager_dup m INNER JOIN departments_dup d ON m.dept_no = d.dept_no ORDER BY m.dept_no
    🔴 Staff
        Unde               cuvântul INNER poate lipsi dacă ON rămâne; fără ON nu mai este această pereche
    Pași
        1. Ține propoziția din comentariul deja scris.
        2. Nu o trimite.
        3. Nu șterge comentariul că INNER ar fi obligatoriu: el stă marcat HISTORICAL.
    */
    // Începător: metoda păstrează perechea manager-departament.
    // Producție: public static String matchingManagerAndDepartment(). Dacă dispare, exemplul din comentariu nu mai este propoziție gata de trimis.
    // Expert: Nu deschide JDBC. Nu transformă INNER în LEFT și nu promite că NULL se potrivește cu NULL.
    public static String matchingManagerAndDepartment() {
        // Începător: rămân doar managerii al căror dept_no este în departments_dup. Cel fără departament nu apare.
        // Producție: INNER JOIN ON m.dept_no = d.dept_no ORDER BY m.dept_no. Dacă linia dispare, nu mai ai propoziția din exemplul deja scris.
        // Expert: ORDER BY nu creează perechea. JOIN cu același ON, fără cuvântul INNER, este aceeași pereche în MySQL. Un index pe dept_no nu ține locul lui ON.
        return "SELECT m.dept_no, m.emp_no, d.dept_name FROM dept_manager_dup m INNER JOIN departments_dup d ON m.dept_no = d.dept_no ORDER BY m.dept_no";
        // Începător: metoda a înapoiat perechea, nu un rând șters.
        // Producție: acolada închide matchingManagerAndDepartment. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este rezultatul gol al unui manager fără departament. Golul acela s-ar vedea doar dacă serverul ar fi primit șirul.
    }

    /*
    🟢 Începător
        Ce este            fiecare manager lângă fiecare departament, fără o coloană comună
        De ce e nevoie     fără ea, lipsa lui ON pare o eroare, nu un produs
    🟡 Producție
        Cum se scrie       SELECT m.emp_no, d.dept_name FROM dept_manager_dup m JOIN departments_dup d
    🔴 Staff
        Unde               echivalent sintactic cu CROSS, în MySQL; nu este perechea de la ON
    Pași
        1. Nu pune ON.
        2. Nu o trimite.
        3. Nu o numi INNER eșuat la sintaxă.
    */
    // Începător: metoda păstrează propoziția care umple rezultatul cu toate combinațiile.
    // Producție: public static String joinWithoutOnIsTheProduct(). Dacă dispare, eșecul rămâne doar în fraza HISTORICAL.
    // Expert: Nu este eroarea Unknown column. Aceea apare când ON vede o coloană din afara operanzilor, la virgulă amestecată cu JOIN.
    public static String joinWithoutOnIsTheProduct() {
        // Începător: fiecare manager apare cu fiecare departament, inclusiv cu departamente care nu sunt ale lui.
        // Producție: JOIN fără ON. Dacă linia dispare, nu mai ai produsul pe care manualul îl pune lângă INNER fără condiție.
        // Expert: În SQL standard, CROSS este produsul scris, iar INNER vine cu ON. În MySQL 9.7 ele se pot înlocui. Produsul nu înseamnă că dept_no s-a potrivit.
        return "SELECT m.emp_no, d.dept_name FROM dept_manager_dup m JOIN departments_dup d";
        // Începător: metoda a păstrat combinațiile, fără să le scrie într-o tabelă.
        // Producție: acolada închide joinWithoutOnIsTheProduct. Șirul a rămas în proces.
        // Expert: CROSS JOIN declarat stă în _4_CROSS_JOIN. Aici lipsa lui ON este eșecul lui INNER, nu lecția produsului.
    }
    // Începător: clasa se oprește la pereche și la produsul fără ON.
    // Producție: acolada închide INNER_JOIN. Comentariul că INNER ar fi obligatoriu rămâne deasupra, marcat HISTORICAL.
    // Expert: LEFT care păstrează managerul nu este în această acoladă. El stă în _3_LEFT_JOIN.
}
