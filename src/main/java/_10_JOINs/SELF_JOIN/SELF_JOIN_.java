package _10_JOINs.SELF_JOIN;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8565170#questions/3352670


/*                                          SQL Self Join


    ○ Applied when a table must join itself

                  * if you’d like to combine CERTAIN ROWS of a table

                  * with OTHER ROWS of the same table,

                                - you need a self-join

    ○ Using ALIASES is OBLIGATORY -------------------------------------------------------------------------------------

    ○ These REFERENCES to the original table

                 * let you use different blocks of the available data

    ○ You can either

                 * filter both in the join,

                 * or you can filter

                               - one of them in the WHERE clause,

                               - and the other one – in the join

    ○ In a self-join statement, you will have to comply  with the same

                 * logical

                 * and syntactic structure

    ○ The 2 tables will be identical
                  HISTORICAL: propoziția de deasupra nu este regula. Nu sunt două tabele stocate. Manualul 9.7, 15.2.13.2, deschis în tur: sunt două referințe, cu alias, către aceeași tabelă. Datele vin dintr-o singură sursă, cum spune comentariul de mai jos.

                  * to the table you’ll be using in the self-join

    ○ You can think of them as virtual projections

                 * of the underlying, base table

    ○ The self-join will

                 * reference both implied tables

                 * and will treat them as two separate tables in its operations

    ○ The data used will come

                 * from a single source,

                 * which is the underlying table that stores data physically


                                               EXAMPLE


    ○ SELECT
          e1.*
      FROM
          emp_manager e1

        JOIN   emp_manager e2  ON  e1.emp_no = e2.manager_no

      GROUP BY emp_no;


                                               When is it appropriate to use self-joins?


    ○ When a column table is referenced in the same table

 */


/*
TOPIC:
    Self join ține două referințe, e1 și e2, către emp_manager. Rândul de angajat stă lângă rândul de manager din aceeași tabelă. Nu se creează a doua tabelă.
FAMILIE:
    Joins
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Vrei emp_no lângă manager_no, iar ambele valori sunt în emp_manager. O a doua tabelă fizică nu există.
MECHANISM:
    Serverul execută managerRowBesideEmployeeRow. e1.emp_no = e2.manager_no compară două referințe. Fără alias, cele două nume emp_manager nu se deosebesc. Efectul este în rezultat.
LOOKALIKES:
    INNER JOIN între dept_manager și departments leagă două tabele stocate. GROUP BY emp_no cu e1.* nu este self join: sub ONLY_FULL_GROUP_BY poate cădea cu ERROR 1055 dacă emp_no nu determină toate coloanele. Un view nu este a doua referință din această propoziție.
BOOK THEORY:
    Manualul 9.7, 15.2.13.2, deschis în tur: o referință poate primi alias; două referințe pot numi aceeași tabelă; ON vede operanzii lui. 14.19.3: ONLY_FULL_GROUP_BY respinge coloana neagregată care nu depinde de GROUP BY, ERROR 1055. SQL+Self+Join.pdf, deschis în tur, are pe primele două pagini doar titlul. Nu spune că apar două tabele stocate.
BOOK EXAMPLE:
    managerRowBesideEmployeeRow
REPO DEMO:
    src/main/java/_10_JOINs/SELF_JOIN/SELF_JOIN_.java
BREAK LAB:
    secondReferenceWithoutAlias repetă emp_manager fără alias. Șirul nu a fost trimis. Pagina deschisă nu dă numărul erorii; spune că referința se alias-ează ca să poată fi numită.
TRADE-OFFS:
    Două tabele stocate când managerul chiar stă în altă tabelă. Derived table cu alias când vrei întâi un grup și abia apoi perechea. Aplicația când lanțul de manageri trece printr-un serviciu care nu este tabelă.
ANTI-PATTERNS:
    GROUP BY emp_no și e1.* ca să „strângi” self join-ul, sau a doua referință fără alias.
STAFF QUESTIONS:
    Cele cinci de mai jos.
NOVEL TRANSFER:
    Un colet și coletul în care este ambalat stau în aceeași tabelă de colete. Două aliasuri le pun pe același rând. O tabelă nouă de ambalaje ar fi alt model, nu acest self join.
SPACED REVIEW:
    O zi: 21 octombrie 2026, două referințe, o tabelă. Șapte zile: 27 octombrie 2026, fără alias referințele nu se deosebesc. Treizeci de zile: 19 noiembrie 2026, GROUP BY emp_no nu este potrivirea.

1. Enunț: Ce problemă rezolvă două aliasuri pe emp_manager?
   Capcană: Copiază tabela, ca să poți face join între original și copie.
   Cheie: managerRowBesideEmployeeRow nu creează tabelă. e1 este angajatul, e2 este rândul al cărui manager_no este emp_no din e1. Datele rămân în emp_manager.
   Demonstrație: managerRowBesideEmployeeRow
2. Enunț: Ce rânduri rămân în rezultat?
   Capcană: Toate rândurile din emp_manager, de două ori, pentru că tabela se joacă pe ea însăși.
   Cheie: Rămâne perechea în care e1.emp_no = e2.manager_no. Un angajat care nu este manager_no al nimănui nu apare în această formă INNER. Tabela nu primește rânduri.
   Demonstrație: managerRowBesideEmployeeRow
3. Enunț: De ce join-ul dintre dept_manager și departments nu este self join?
   Capcană: Și acolo sunt două nume în FROM, deci este aceeași unealtă.
   Cheie: Acolo sunt două tabele stocate. Aici e1 și e2 sunt două referințe către emp_manager. Fără două aliasuri, coloana emp_no nu are o singură referință.
   Demonstrație: secondReferenceWithoutAlias
4. Enunț: Cum eșuează a doua scriere a lui emp_manager fără alias?
   Capcană: Serverul înțelege singur care copie este managerul, pentru că manager_no spune rolul.
   Cheie: secondReferenceWithoutAlias nu deosebește referințele. ON emp_no = manager_no nu spune din care referință vine fiecare coloană. Propoziția nu este perechea cu e1 și e2.
   Demonstrație: secondReferenceWithoutAlias
5. Enunț: Când lași self join și când grupul sau a doua tabelă?
   Capcană: GROUP BY emp_no cu e1.* este self join-ul corect, pentru că strânge omul o dată.
   Cheie: Self join când două rânduri din aceeași tabelă trebuie pe același rezultat. GROUP BY emp_no cu e1.* poate cădea cu ERROR 1055 dacă coloanele nu depind de emp_no. A doua tabelă când managerul nu stă în emp_manager.
   Demonstrație: managerRowBesideEmployeeRow

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE rândul de angajat lângă rândul de manager, din emp_manager -> managerRowBesideEmployeeRow -> JOIN emp_manager e2 ON e1.emp_no = e2.manager_no -> nu garantează că manager_no este cheie
    2. Eșecul cel mai mic
           -> DECIDE a doua referință fără alias -> secondReferenceWithoutAlias -> coloanele nu au referință unică -> nu garantează numărul de eroare: pagina JOIN nu îl dă
    3. Granița
           -> Două referințe sunt mecanismul. Șirul Java nu copiază tabela. Workbench nu inventează aliasul e2.
    4. Alternativa
           -> Două tabele când managerul este în altă tabelă. LEFT JOIN pe aceeași tabelă când angajatul fără manager trebuie să rămână, cu NULL. Aici forma este INNER.
    5. Anti-patternul
           -> emp_manager JOIN emp_manager fără alias, sau GROUP BY emp_no peste e1.* ca să strângi perechea.
    6. Dovada
           -> managerRowBesideEmployeeRow ține e1 și e2. secondReferenceWithoutAlias nu ține alias. 1045 a oprit trimiterea.
    7. Interviu
           -> Self join este o tabelă, două referințe. Aliasul le deosebește. Nu se scrie a doua tabelă. GROUP BY nu este potrivirea.
*/

public class SELF_JOIN_ {

    /*
    🟢 Începător
        Ce este            angajatul lângă manager, amândoi citiți din emp_manager
        De ce e nevoie     fără două aliasuri, cele două roluri se amestecă
    🟡 Producție
        Cum se scrie       SELECT e1.emp_no, e2.emp_no AS manager_row FROM emp_manager e1 JOIN emp_manager e2 ON e1.emp_no = e2.manager_no
    🔴 Staff
        Unde               o tabelă stocată; GROUP BY din comentariu nu este această pereche și poate lua 1055
    Pași
        1. Două aliasuri, un ON.
        2. Nu pune GROUP BY.
        3. Nu trimite șirul.
    */
    // Începător: metoda păstrează perechea din aceeași tabelă.
    // Producție: public static String managerRowBesideEmployeeRow(). Dacă dispare, exemplul din comentariu nu mai este propoziție fără GROUP BY.
    // Expert: Nu deschide JDBC. Nu copiază emp_manager. Nu promite că fiecare angajat are un manager.
    public static String managerRowBesideEmployeeRow() {
        // Începător: rămâne angajatul doar când numărul lui este manager_no pe un alt rând din aceeași tabelă.
        // Producție: JOIN emp_manager e2 ON e1.emp_no = e2.manager_no. Dacă linia dispare, nu mai ai două referințe.
        // Expert: Comentariul vechi grupa e1.* după emp_no. Sub ONLY_FULL_GROUP_BY, coloana care nu depinde de emp_no cade cu ERROR 1055. Această propoziție nu grupează. LEFT ar fi păstrat angajatul fără rând de manager.
        return "SELECT e1.emp_no, e2.emp_no AS manager_row FROM emp_manager e1 JOIN emp_manager e2 ON e1.emp_no = e2.manager_no";
        // Începător: metoda a înapoiat perechea, nu o tabelă nouă.
        // Producție: acolada închide managerRowBesideEmployeeRow. Șirul nu a plecat la 3306.
        // Expert: 1045 nu înseamnă că aliasul a eșuat. Eșecul fără alias este cealaltă metodă.
    }

    /*
    🟢 Începător
        Ce este            aceeași tabelă scrisă de două ori, fără nume care să le despartă
        De ce e nevoie     fără ea, aliasul pare un moft
    🟡 Producție
        Cum se scrie       SELECT emp_no FROM emp_manager JOIN emp_manager ON emp_no = manager_no
    🔴 Staff
        Unde               referința fără alias nu poate fi numită de două ori; pagina JOIN nu dă codul de eroare
    Pași
        1. Nu pune e1 și e2.
        2. Nu o trimite.
        3. Nu adăuga un CREATE TABLE ca să „repari”.
    */
    // Începător: metoda păstrează forma în care cele două roluri nu au nume.
    // Producție: public static String secondReferenceWithoutAlias(). Dacă dispare, obligația aliasului rămâne doar în comentariu.
    // Expert: Nu este eroarea 1055. Aceea este pentru GROUP BY. Aici cele două referințe nu se pot deosebi.
    public static String secondReferenceWithoutAlias() {
        // Începător: propoziția nu poate spune care emp_manager este angajatul și care este managerul.
        // Producție: JOIN emp_manager ON emp_no = manager_no, fără alias. Dacă linia dispare, nu mai ai eșecul de referință.
        // Expert: manager_no nu alege referința. Două tabele diferite, dept_manager și departments, nu au această problemă de nume repetat. Un index pe emp_no nu ține locul aliasului.
        return "SELECT emp_no FROM emp_manager JOIN emp_manager ON emp_no = manager_no";
        // Începător: metoda a păstrat forma care nu deosebește rândurile.
        // Producție: acolada închide secondReferenceWithoutAlias. Șirul a rămas în proces.
        // Expert: Nu s-a citit eroarea de pe server. 1045 este autentificarea, nu referința.
    }
    // Începător: clasa se oprește la două aliasuri și la forma care nu le are.
    // Producție: acolada închide SELF_JOIN_. Nu s-a creat o a doua tabelă emp_manager.
    // Expert: ERROR 1055 de la GROUP BY emp_no cu e1.* rămâne în comentariul vechi, nu în aceste două șiruri.
}
