package _10_JOINs._5_UNION_VS_UNION_ALL;


// Martin G. UNION VS UNION ALL
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8510164#questions


/*                                                       UNION  --  UNION ALL

    ○ SELECT
          column1, column2, ...
       FROM
          table1
      UNION

      SELECT

          column1, column2, ...

      FROM table2;

    ○ The SQL UNION operator is used to

                * combine(returns all) the result sets of two or more SELECT queries into a single result set.

        It returns all unique records from the combined queries.

                * To use the UNION operator, the SELECT statements being combined

                            - must have the same number of columns

                            - and corresponding columns should have compatible data types.

    ○ UNION ALL

                  * used to combine a few SELECT statements in a single output

                  * you can think of it as a tool that allows you to unify tables


    ○ When uniting two identically organized tables

                 * UNION

                               - displays only distinct values in the output

                               - uses more MySQL resources(computational power and storage space)

                 * UNION ALL

                                - retrieves the duplicates as well


    ○ Looking for better results?

               * use UNION
                                  HISTORICAL: propoziția de deasupra nu este regula. Manualul 9.7, 15.2.14, deschis în tur: UNION fără ALL scoate rândurile duplicate. Nu îmbunătățește valorile. UNION ALL le păstrează. „Rezultat mai bun” nu este efectul pe rânduri.

    ○ Seeking to optimize performance?

               * opt for UNION ALL


                                                         SQL


    ○ SELECT  N columns

      FROM table_1

          UNION [ALL]

      SELECT

            N columns

     FROM table_2;

    ○ We have to select the  ------------------------------------------------------------------------------------------

                   * same number of columns from each table

    ○ These column should have ---------------------------------------------------------------------------------------

                  * same name
                                  HISTORICAL: propoziția de deasupra nu este regula. Manualul 9.7, 15.2.14, deschis în tur: numele coloanelor din rezultat vin din primul bloc. Pozițiile corespunzătoare cer tipuri compatibile. Numele nu trebuie să coincidă.

                  * same order

                  * contain related data type

                  * use NULL AS

                                - to achieve these goals


                                                        EXAMPLE


    ○  SELECT
           e.emp_no,
           e.first_name,
           e.last_name,
           NULL AS dept_no,
           NULL AS from_date

       FROM

          employees_dup e

      WHERE

          e.emp_no = '10001'

     UNION [ALL] SELECT

         NULL AS emp_no,
         NULL AS first_name,
         NULL AS last_name,
         m.dept_no,
         m.from_date

     FROM

        dept_manager m;


 */

/*
TOPIC:
    UNION pune două SELECT-uri unul după altul și scoate rândul duplicat. UNION ALL îl păstrează. Nu potrivește coloane între tabele.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Angajatul 10001 și rândul lui din dept_manager trebuie să încapă în același rezultat, unul sub altul. Dacă aceeași linie apare în ambele blocuri, ALL o arată de două ori, iar UNION o arată o dată.
MECHANISM:
    Serverul execută set operation la sfârșitul expresiei. DISTINCT este implicit. ALL oprește scoaterea duplicatelor. Numele coloanelor vin din primul bloc. Un DISTINCT la dreapta acoperă un ALL din stânga lui.
LOOKALIKES:
    INNER JOIN aduce coloane din două tabele pe același rând, nu două blocuri. INTERSECT păstrează doar rândul comun și tot scoate duplicatele, dacă nu scrii ALL. ORDER BY înăuntrul unui bloc, fără paranteze și fără LIMIT, nu ordonează rezultatul unit.
BOOK THEORY:
    Manualul 9.7, 15.2.14, deschis în tur: UNION combină blocurile și, implicit, omite duplicatele; ALL le include; DISTINCT este explicitarea implicitului; un DISTINCT acoperă ALL-ul din stânga; numele vin din primul bloc; ORDER BY pe numele vechi, nu pe alias, dă Unknown column în order clause; ORDER BY fără LIMIT în interiorul unui bloc este eliminat, pentru că nu ordonează rezultatul final. UNION+vs+UNION+ALL.pdf, deschis în tur, are pe primele două pagini titlul, nu regula de duplicate.
BOOK EXAMPLE:
    unionAllKeepsBothCopies
REPO DEMO:
    src/main/java/_10_JOINs/_5_UNION_VS_UNION_ALL/UNION_VS_UNION_ALL.java
BREAK LAB:
    orderByEmployeeNumberFails ordonează după emp_no, deși primul bloc l-a numit id. Șirul nu a fost trimis. Pagina deschisă arată Unknown column în order clause pentru acest tip de nume.
TRADE-OFFS:
    UNION ALL când copia trebuie să rămână și când nu vrei costul de scoatere a duplicatelor. UNION când lista unită nu are voie să repete rândul. JOIN când ai nevoie de coloanele din ambele tabele pe același rând.
ANTI-PATTERNS:
    UNION ca să „iasă un rezultat mai bun”, sau ORDER BY emp_no după ce primul bloc a numit coloana id.
STAFF QUESTIONS:
    Cele cinci de mai jos.
NOVEL TRANSFER:
    Două liste de colete, de la două depozite, puse una sub alta. ALL păstrează coletul scris în ambele liste. UNION îl arată o dată. Join-ul l-ar fi pus lângă raft, nu sub celălalt colet.
SPACED REVIEW:
    O zi: 21 octombrie 2026, ALL păstrează duplicatul. Șapte zile: 27 octombrie 2026, numele vin din primul bloc. Treizeci de zile: 19 noiembrie 2026, JOIN nu este UNION.

1. Enunț: Ce problemă rezolvă UNION ALL între employees_dup și dept_manager?
   Capcană: Potrivește emp_no și umple coloanele goale din cealaltă tabelă, ca un join.
   Cheie: unionAllKeepsBothCopies pune rândurile unul sub altul. NULL AS umple poziția care nu există în acel bloc. Nu compară emp_no între tabele.
   Demonstrație: unionAllKeepsBothCopies
2. Enunț: Ce se întâmplă cu un rând identic care iese din ambele blocuri?
   Capcană: UNION ALL îl curăță, pentru că „all” înseamnă tot ce e unic. UNION îl păstrează de două ori, ca să nu pierzi date.
   Cheie: ALL îl lasă de două ori. UNION fără ALL îl lasă o dată. Tabela nu se schimbă. Efectul este în rezultat, la sfârșitul expresiei.
   Demonstrație: unionDropsTheRepeatedRow
3. Enunț: De ce INNER JOIN nu este acest UNION?
   Capcană: Ambele aduc date din două tabele, deci rândurile și planul coincid.
   Cheie: JOIN potrivește în același FROM și pune coloanele pe un rând. UNION lipește blocuri și cere același număr de poziții. Un emp_no comun nu este o pereche de join.
   Demonstrație: unionAllKeepsBothCopies
4. Enunț: Cum eșuează ORDER BY emp_no când primul bloc a scris emp_no AS id?
   Capcană: Serverul caută emp_no în ambele tabele și ordonează rezultatul.
   Cheie: orderByEmployeeNumberFails. Numele rezultatului este id, din primul bloc. Pagina 15.2.14 arată Unknown column în order clause când ORDER BY folosește numele dinainte de alias.
   Demonstrație: orderByEmployeeNumberFails
5. Enunț: Când lași UNION ALL și când treci la UNION sau la JOIN?
   Capcană: UNION este mereu mai bun, iar JOIN se alege când vrei mai puține duplicate.
   Cheie: ALL când copia rămâne. UNION când rândul identic trebuie să apară o dată. JOIN când dept_name trebuie să stea pe același rând cu emp_no, nu într-un bloc separat. Scoaterea duplicatelor costă; nu îmbunătățește valoarea.
   Demonstrație: unionDropsTheRepeatedRow

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE ambele blocuri, cu copia păstrată -> unionAllKeepsBothCopies -> UNION ALL și NULL AS pe pozițiile lipsă -> nu garantează că 10001 există în employees_dup
    2. Eșecul cel mai mic
           -> DECIDE ORDER BY emp_no după aliasul id -> orderByEmployeeNumberFails -> Unknown column în order clause, cum arată pagina deschisă pe același tip de nume -> nu garantează că șirul a fost trimis în tur
    3. Granița
           -> Set operation este mecanismul. Șirul Java nu unește rânduri. Workbench nu pune ALL dacă ai uitat cuvântul.
    4. Alternativa
           -> UNION când duplicatul trebuie să cadă. JOIN când coloanele stau pe același rând. INTERSECT când vrei doar rândul care este în ambele blocuri; tot scoate duplicatele dacă nu scrii ALL.
    5. Anti-patternul
           -> UNION ca rezultat „mai bun”. El doar scoate rândul identic. ORDER BY pe numele dinainte de alias cade.
    6. Dovada
           -> unionAllKeepsBothCopies ține ALL. unionDropsTheRepeatedRow nu îl ține. orderByEmployeeNumberFails ține ORDER BY emp_no. 1045 a oprit trimiterea.
    7. Interviu
           -> UNION scoate duplicatul. ALL îl păstrează. Numele vin din primul bloc. JOIN nu lipește blocuri.
*/

public class UNION_VS_UNION_ALL {

    /*
    🟢 Începător
        Ce este            rândul angajatului și rândul de departament, unul sub altul, cu copia păstrată
        De ce e nevoie     fără ALL, rândul identic dispare din rezultat
    🟡 Producție
        Cum se scrie       SELECT ... FROM employees_dup e WHERE e.emp_no = 10001 UNION ALL SELECT ... FROM dept_manager m
    🔴 Staff
        Unde               numele vin din primul bloc; DISTINCT la dreapta ar acoperi acest ALL
    Pași
        1. Pune același număr de poziții, cu NULL AS unde blocul nu are coloana.
        2. Nu trimite șirul.
        3. Nu scrie JOIN între ele.
    */
    // Începător: metoda păstrează lipirea care nu aruncă copia.
    // Producție: public static String unionAllKeepsBothCopies(). Dacă dispare, exemplul din comentariu nu mai este propoziție.
    // Expert: Nu deschide JDBC. Nu promite că numele coloanelor se iau din ambele tabele.
    public static String unionAllKeepsBothCopies() {
        // Începător: angajatul 10001 stă deasupra, iar departamentul din dept_manager stă dedesubt. O linie identică ar apărea de două ori.
        // Producție: UNION ALL, cu NULL AS dept_no în primul bloc și NULL AS emp_no în al doilea. Dacă linia dispare, nu mai ai păstrarea copiei.
        // Expert: emp_no = 10001 nu este o potrivire cu dept_manager. Pozițiile trebuie să fie cinci și cinci. Un nume diferit pe aceeași poziție rămâne legal: rezultatul poartă numele din primul bloc.
        return "SELECT e.emp_no, e.first_name, e.last_name, NULL AS dept_no, NULL AS from_date FROM employees_dup e WHERE e.emp_no = 10001 UNION ALL SELECT NULL AS emp_no, NULL AS first_name, NULL AS last_name, m.dept_no, m.from_date FROM dept_manager m";
        // Începător: metoda a înapoiat lipirea, fără să unească tabelele pe disc.
        // Producție: acolada închide unionAllKeepsBothCopies. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este o copie scoasă. Scoaterea este UNION fără ALL, în cealaltă metodă.
    }

    /*
    🟢 Începător
        Ce este            aceeași lipire, din care rândul identic rămâne o singură dată
        De ce e nevoie     fără ea, ALL și UNION par același cuvânt
    🟡 Producție
        Cum se scrie       aceleași două SELECT-uri, cu UNION, fără ALL
    🔴 Staff
        Unde               DISTINCT este implicit; un DISTINCT mai la dreapta acoperă un ALL din stânga
    Pași
        1. Schimbă doar ALL.
        2. Nu o trimite.
        3. Nu o numi join mai bun.
    */
    // Începător: metoda păstrează lipirea care aruncă rândul repetat.
    // Producție: public static String unionDropsTheRepeatedRow(). Dacă dispare, nu mai ai deosebirea de ALL.
    // Expert: Nu îmbunătățește first_name. Costul de scoatere nu este un index și nu ține un lacăt după rezultat.
    public static String unionDropsTheRepeatedRow() {
        // Începător: aceleași două liste, dar o linie la fel din ambele blocuri apare o dată.
        // Producție: UNION fără ALL. Dacă linia dispare, rămâne doar forma cu ALL.
        // Expert: INTERSECT ar fi păstrat doar rândul care este în ambele blocuri, tot fără duplicate dacă nu scrii ALL. Nu este această propoziție. Tabela employees_dup nu pierde rândul.
        return "SELECT e.emp_no, e.first_name, e.last_name, NULL AS dept_no, NULL AS from_date FROM employees_dup e WHERE e.emp_no = 10001 UNION SELECT NULL AS emp_no, NULL AS first_name, NULL AS last_name, m.dept_no, m.from_date FROM dept_manager m";
        // Începător: metoda a păstrat scoaterea duplicatului, fără execuție.
        // Producție: acolada închide unionDropsTheRepeatedRow. Șirul a rămas în proces.
        // Expert: Ordinea rândurilor din rezultat nu este ordinea blocurilor. ORDER BY final, după ultimul bloc, este cel care sortează tot rezultatul.
    }

    /*
    🟢 Începător
        Ce este            o sortare după un nume care nu mai este numele coloanei
        De ce e nevoie     fără ea, aliasul pare optional la ORDER BY
    🟡 Producție
        Cum se scrie       (SELECT e.emp_no AS id FROM employees_dup e) UNION (SELECT m.emp_no FROM dept_manager m) ORDER BY emp_no
    🔴 Staff
        Unde               15.2.14: ORDER BY a cade când primul bloc a numit coloana b; eroarea din pagină este Unknown column în order clause
    Pași
        1. Aliasul stă în primul bloc.
        2. ORDER BY folosește numele vechi, după UNION.
        3. Nu o trimite.
    */
    // Începător: metoda păstrează sortarea care nu găsește coloana.
    // Producție: public static String orderByEmployeeNumberFails(). Dacă dispare, eșecul de nume rămâne doar în comentariul HISTORICAL.
    // Expert: ORDER BY id ar folosi numele din primul bloc. Poziția numerică în ORDER BY este depreciată pe pagina deschisă. Nu o folosesc aici.
    public static String orderByEmployeeNumberFails() {
        // Începător: rezultatul nu se sortează după emp_no, pentru că primul bloc a pus alt nume.
        // Producție: ORDER BY emp_no după SELECT e.emp_no AS id. Dacă linia dispare, nu mai ai Unknown column din pagina de set operations.
        // Expert: ORDER BY în interiorul unui bloc, fără LIMIT, este scos la optimizare și nu ordonează UNION-ul. Parantezele de aici țin blocurile; ORDER BY este pe tot rezultatul și tot vede id, nu emp_no.
        return "(SELECT e.emp_no AS id FROM employees_dup e) UNION (SELECT m.emp_no FROM dept_manager m) ORDER BY emp_no";
        // Începător: metoda a păstrat sortarea greșită, fără să o ruleze.
        // Producție: acolada închide orderByEmployeeNumberFails. Șirul nu a plecat.
        // Expert: 1045 nu este Unknown column. Eroarea de coloană este cea descrisă în 15.2.14, nu cea de autentificare.
    }
    // Începător: clasa se oprește la copia păstrată, la copia scoasă și la sortarea după un nume greșit.
    // Producție: acolada închide UNION_VS_UNION_ALL. Niciun JOIN nu stă în aceste trei șiruri.
    // Expert: INTERSECT nu este aici. DISTINCT la dreapta unui ALL tot nu se învață din această acoladă.
}
