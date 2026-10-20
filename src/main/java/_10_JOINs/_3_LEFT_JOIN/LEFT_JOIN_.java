package _10_JOINs._3_LEFT_JOIN;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8441432#questions


/*                                              LEFT JOIN

   ○ When working with left joins,

                 * the order in which you join tables matters

                 * All matching values of the two tables +

                 * All values from the left table

                               * that match no values from the right table


                                                SQL


    ○ SELECT  t1.column_name, t1.column_name, …, t2.column_name, …

       FROM  table_1 t1

       LEFT JOIN  table_2 t2 ON t1.column_name = t2.column_name

       WHERE  column_name … IS NULL;     --   use this statement to obtain only rows from left column which sre not in
                                              right table



    ○ SELECT  m.emp_no, m.dept_no, d.dept_name

       FROM  dept_manager_dup m

       LEFT JOIN departments_dup d on m.dept_no = d.dept_no

       ORDER BY m.emp_no;




 */


/*
TOPIC:
    LEFT JOIN păstrează fiecare rând din dept_manager_dup. Dacă departments_dup nu are dept_no, dept_name rămâne NULL în rezultat.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Lista trebuie să arate și managerul care nu are departament. INNER l-ar fi scos.
MECHANISM:
    Serverul execută managerKeptWhenDepartmentMissing. Condiția din ON caută în dreapta. WHERE nu este folosit la citirea dreptei. Dacă stânga trece de WHERE și dreapta nu are pereche, se generează un rând cu NULL. 10.2.1.9.
LOOKALIKES:
    INNER JOIN scoate managerul. RIGHT JOIN păstrează cealaltă parte și, în optimizer, este rescris ca LEFT. WHERE d.dept_name = 'Sales' este fals pe NULL și poate transforma LEFT în inner.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2, deschis în tur: fără pereche la LEFT, coloanele din dreapta sunt NULL; exemplul cu WHERE right_tbl.id IS NULL găsește stânga fără corespondent; RIGHT este analog, iar LEFT este forma portabilă. 10.2.1.9: WHERE care este mereu fals pe rândul NULL generat schimbă LEFT în inner join, ca în WHERE t2.column2 = 5. LEFT+JOIN.pdf, deschis în tur, are pe primele două pagini doar titlul.
BOOK EXAMPLE:
    managerKeptWhenDepartmentMissing
REPO DEMO:
    src/main/java/_10_JOINs/_3_LEFT_JOIN/LEFT_JOIN_.java
BREAK LAB:
    whereOnDepartmentNameDropsTheNullRow filtrează d.dept_name = 'Sales'. Managerul fără departament cade. Șirul nu a fost trimis.
TRADE-OFFS:
    INNER când nu vrei rândul fără pereche. IS NULL în WHERE, pe o coloană NOT NULL din dreapta, când vrei doar managerul fără departament. RIGHT doar dacă nu poți întoarce tabelele; manualul recomandă LEFT.
ANTI-PATTERNS:
    WHERE pe o coloană din dreapta care nu poate fi NULL, pus ca să „limitezi” un LEFT JOIN.
STAFF QUESTIONS:
    Cele cinci de mai jos. Alegerea dintre copii stă în Joins_Staff_Interview.
NOVEL TRANSFER:
    Coletul fără raft rămâne în listă, cu raftul gol. Filtrul „raftul se numește A1” scoate coletul fără raft, deși ai scris LEFT.
SPACED REVIEW:
    O zi: 21 octombrie 2026, stânga rămâne, dreapta lipsă este NULL. Șapte zile: 27 octombrie 2026, WHERE pe numele din dreapta poate face inner. Treizeci de zile: 19 noiembrie 2026, RIGHT păstrează cealaltă parte.

1. Enunț: Ce problemă rezolvă LEFT JOIN de la dept_manager_dup spre departments_dup?
   Capcană: Este INNER scris cu altă ordine a tabelelor, deci managerul fără departament tot cade.
   Cheie: managerKeptWhenDepartmentMissing păstrează fiecare manager. dept_name este NULL când dept_no nu are pereche. Ordinea tabelelor alege partea păstrată.
   Demonstrație: managerKeptWhenDepartmentMissing
2. Enunț: Ce apare în rezultat pentru managerul al cărui dept_no nu este în departments_dup?
   Capcană: Rândul nu apare, iar în tabelă dept_name se scrie NULL la COMMIT.
   Cheie: Managerul apare în rezultat. Coloanele din departments_dup sunt NULL. dept_manager_dup nu este actualizat. Efectul se vede la sfârșitul SELECT-ului.
   Demonstrație: managerKeptWhenDepartmentMissing
3. Enunț: De ce RIGHT JOIN nu este altă potrivire?
   Capcană: RIGHT ține alte rânduri decât LEFT pe aceeași condiție, pentru că optimizerul citește dreapta întâi.
   Cheie: Aceeași potrivire. Se schimbă partea păstrată. Manualul spune că RIGHT este analog și că LEFT este forma portabilă. Optimizerul poate rescrie RIGHT ca LEFT.
   Demonstrație: managerKeptWhenDepartmentMissing
4. Enunț: Cum eșuează WHERE d.dept_name = 'Sales' pus după acest LEFT JOIN?
   Capcană: Rămân managerii din Sales și, separat, managerii fără departament, cu NULL.
   Cheie: whereOnDepartmentNameDropsTheNullRow: NULL nu este 'Sales', deci rândul generat cade. 10.2.1.9 permite transformarea în inner join. Managerul fără departament nu rămâne.
   Demonstrație: whereOnDepartmentNameDropsTheNullRow
5. Enunț: Când lași LEFT și când treci la INNER sau la IS NULL?
   Capcană: LEFT se folosește mereu, iar IS NULL se pune în ON ca să ștergi potrivirile.
   Cheie: LEFT când fiecare manager trebuie să apară. INNER când vrei doar perechea. WHERE d.dept_no IS NULL, dacă dept_no din dreapta este NOT NULL, lasă doar managerul fără departament. Testul pus în ON, nu în WHERE, nu face aceeași tăiere.
   Demonstrație: whereOnDepartmentNameDropsTheNullRow

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE fiecare manager, cu departament sau fără -> managerKeptWhenDepartmentMissing -> LEFT JOIN ON m.dept_no = d.dept_no -> nu garantează un rând NULL în tur: șirul nu a fost trimis
    2. Eșecul cel mai mic
           -> DECIDE doar Sales, pe coloana din dreapta, după LEFT -> whereOnDepartmentNameDropsTheNullRow -> managerul fără departament cade -> nu garantează că planul rămâne outer join
    3. Granița
           -> Rândul NULL generat este mecanismul. Șirul Java nu este execuția. Workbench nu mută condiția din WHERE în ON.
    4. Alternativa
           -> INNER când lipsa nu trebuie să apară. IS NULL în WHERE când vrei doar lipsa. RIGHT când nu poți schimba ordinea și accepți forma mai puțin portabilă.
    5. Anti-patternul
           -> WHERE d.dept_name = 'Sales' pe un LEFT scris ca să păstreze managerul fără departament. El cade.
    6. Dovada
           -> managerKeptWhenDepartmentMissing ține LEFT fără filtru pe dreapta. whereOnDepartmentNameDropsTheNullRow ține filtrul. 1045 a oprit trimiterea.
    7. Interviu
           -> LEFT păstrează stânga. Dreapta lipsă este NULL. WHERE care respinge NULL îl face inner. RIGHT păstrează cealaltă parte.
*/

public class LEFT_JOIN_ {

    /*
    🟢 Începător
        Ce este            toți managerii, iar departamentul lipsă rămâne gol în rezultat
        De ce e nevoie     fără LEFT, managerul fără departament dispare din listă
    🟡 Producție
        Cum se scrie       SELECT m.emp_no, m.dept_no, d.dept_name FROM dept_manager_dup m LEFT JOIN departments_dup d ON m.dept_no = d.dept_no ORDER BY m.emp_no
    🔴 Staff
        Unde               ON citește dreapta; WHERE de după poate arunca rândul NULL; RIGHT ar păstra departments_dup
    Pași
        1. Ține LEFT și ON, fără WHERE.
        2. Nu trimite șirul.
        3. Nu muta tabelele ca să „repare” un RIGHT.
    */
    // Începător: metoda păstrează propoziția în care niciun manager nu cade din rezultat.
    // Producție: public static String managerKeptWhenDepartmentMissing(). Dacă dispare, exemplul din comentariu nu mai este propoziție.
    // Expert: Nu deschide JDBC. Nu pune filtrul de Sales aici. Nu promite un lacăt: pagina JOIN nu numește unul.
    public static String managerKeptWhenDepartmentMissing() {
        // Începător: fiecare manager rămâne. Dacă departamentul nu există, numele lui este gol în rezultat.
        // Producție: LEFT JOIN departments_dup d ON m.dept_no = d.dept_no. Dacă linia dispare, nu mai ai păstrarea stângii.
        // Expert: INNER cu același ON ar fi scos managerul. IS NULL în WHERE, pe dept_no din dreapta dacă este NOT NULL, ar fi lăsat doar lipsa. Aici nu este acel filtru.
        return "SELECT m.emp_no, m.dept_no, d.dept_name FROM dept_manager_dup m LEFT JOIN departments_dup d ON m.dept_no = d.dept_no ORDER BY m.emp_no";
        // Începător: metoda a înapoiat lista, nu o actualizare a tabelei.
        // Producție: acolada închide managerKeptWhenDepartmentMissing. Șirul nu a plecat la 3306.
        // Expert: 1045 nu este rândul NULL. NULL-ul s-ar vedea în coloana d.dept_name doar după execuție.
    }

    /*
    🟢 Începător
        Ce este            un filtru de nume care scoate și managerul fără departament
        De ce e nevoie     fără ea, WHERE pare că păstrează LEFT
    🟡 Producție
        Cum se scrie       LEFT JOIN ... ON m.dept_no = d.dept_no WHERE d.dept_name = 'Sales'
    🔴 Staff
        Unde               10.2.1.9: condiția falsă pe NULL permite rescrierea ca inner join
    Pași
        1. Pune egalitatea pe coloana din dreapta, în WHERE.
        2. Nu o trimite.
        3. Nu o muta în ON: acolo ar însemna altă potrivire.
    */
    // Începător: metoda păstrează propoziția care pare că lasă toți managerii și nu îi lasă.
    // Producție: public static String whereOnDepartmentNameDropsTheNullRow(). Dacă dispare, eșecul rămâne o poveste.
    // Expert: Nu este anti-join-ul cu IS NULL. Acela stă în JOIN_TRICKS. Aici 'Sales' respinge NULL.
    public static String whereOnDepartmentNameDropsTheNullRow() {
        // Începător: managerul fără departament nu rămâne, pentru că numele gol nu este Sales.
        // Producție: WHERE d.dept_name = 'Sales' după LEFT JOIN. Dacă linia dispare, nu mai ai transformarea în inner.
        // Expert: Optimizerul poate citi departments_dup înainte, după rescriere. STRAIGHT_JOIN ar forța ordinea și poate opri un index, dar nu este această propoziție. Rândul NULL nu așteaptă COMMIT.
        return "SELECT m.emp_no, d.dept_name FROM dept_manager_dup m LEFT JOIN departments_dup d ON m.dept_no = d.dept_no WHERE d.dept_name = 'Sales'";
        // Începător: metoda a păstrat filtrul care strică păstrarea stângii.
        // Producție: acolada închide whereOnDepartmentNameDropsTheNullRow. Șirul a rămas în proces.
        // Expert: Nu s-a citit EXPLAIN. Rescrierea în inner este permisă de manual, nu este un plan măsurat în tur.
    }
    // Începător: clasa se oprește la managerul păstrat și la filtrul care îl pierde.
    // Producție: acolada închide LEFT_JOIN_. Anti-join-ul cu IS NULL rămâne în JOIN_TRICKS.
    // Expert: RIGHT nu este rescris aici. Optimizerul îl poate transforma în LEFT, cum spune 10.2.1.9, în afara acestei acolade.
}
