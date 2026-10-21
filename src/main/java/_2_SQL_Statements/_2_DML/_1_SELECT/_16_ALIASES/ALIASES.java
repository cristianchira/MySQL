package _2_SQL_Statements._2_DML._1_SELECT._16_ALIASES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345184#overview


/*                                                            ALIASES


    ○ alias(alias name)

                  * is used to rename a selection from your query

                  * use AS keyword to allocate alias name

    ○ Example

      SELECT first_name, count(first_name) AS name_count

      FROM employees

      GROUP BY first_name

      ORDER BY first_name desc;


 */


/*
TOPIC:
    AS name_count dă nume coloanei de rezultat COUNT(first_name); nu redenumește coloana din employees.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să citești numărul sub un nume. Tabela rămâne cu first_name.
MECHANISM:
    Serverul aplică aliasul pe expresia din lista SELECT. ORDER BY poate vedea aliasul; WHERE nu filtrează după aliasul de agregat, pentru că WHERE este înainte de rezultat.
LOOKALIKES:
    ALTER TABLE ... CHANGE COLUMN redenumește coloana stocată. AS nu face DDL.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat.  Aliasul este select_expr din manual. Comentariul de curs: AS name_count pe COUNT.
BOOK EXAMPLE:
    aliasNamesTheCount
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_16_ALIASES/ALIASES.java
BREAK LAB:
    alterRenamesTheStoredColumn ține ALTER TABLE employees CHANGE COLUMN first_name name_count VARCHAR(14). AS nu schimbă schema. CHANGE COLUMN o schimbă.
TRADE-OFFS:
    ALTER TABLE când trebuie să se schimbe numele stocat.
ANTI-PATTERNS:
    ALTER ca să numești un COUNT. Schema se schimbă, numărul dispare.
STAFF QUESTIONS:
    aliasNamesTheCount și alterRenamesTheStoredColumn. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un raport de cutii vrea coloana box_count. COUNT(*) AS box_count o numește în rezultat. Tabela box rămâne.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT first_name, COUNT(first_name) AS name_count FROM employees GROUP BY first_name?
   Capcană: AS name_count redenumește first_name în tabelă.
   Cheie: aliasNamesTheCount: rezultatul are coloana name_count, iar employees.first_name rămâne neschimbat. fără AS, numărul există dar nu are numele pe care îl citești în client ca name_count
   Demonstrație: aliasNamesTheCount
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT first_name, COUNT(first_name) AS name_count FROM employees GROUP BY first_name?
   Capcană: WHERE name_count > 1 filtrează grupurile.
   Cheie: Efectul este: rezultatul are coloana name_count, iar employees.first_name rămâne neschimbat. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: aliasNamesTheCount
3. Enunț: De ce SELECT first_name, COUNT(first_name) AS name_count FROM employees GROUP BY first_name nu este vecinul său?
   Capcană: ALTER CHANGE COLUMN este aliasul, pentru că vezi noul nume.
   Cheie: ALTER TABLE ... CHANGE COLUMN redenumește coloana stocată. AS nu face DDL.
   Demonstrație: aliasNamesTheCount
4. Enunț: Cum eșuează ALTER TABLE employees CHANGE COLUMN first_name name_count VARCHAR(14)?
   Capcană: Fără AS, COUNT nu se calculează.
   Cheie: alterRenamesTheStoredColumn: AS nu schimbă schema. CHANGE COLUMN o schimbă.
   Demonstrație: alterRenamesTheStoredColumn
5. Enunț: Când nu mai trimiți SELECT first_name, COUNT(first_name) AS name_count FROM employees GROUP BY first_name?
   Capcană: Ca să numești o coloană, faci mereu ALTER.
   Cheie: ALTER TABLE când trebuie să se schimbe numele stocat.
   Demonstrație: aliasNamesTheCount

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei un nume pe coloana de rezultat -> aliasNamesTheCount -> SELECT first_name, COUNT(first_name) AS name_count FROM employees GROUP BY first_name -> nu garantează că WHERE name_count > 1 este legal
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> alterRenamesTheStoredColumn -> ALTER TABLE employees CHANGE COLUMN first_name name_count VARCHAR(14) -> AS nu schimbă schema. CHANGE COLUMN o schimbă.
    3. Granița
           -> AS este în propoziție. Titlul de coloană pus doar în grid nu există pentru server.
    4. Alternativa
           -> ALTER TABLE ... CHANGE COLUMN redenumește coloana stocată. AS nu face DDL.
    5. Anti-patternul
           -> ALTER ca să numești un COUNT. Schema se schimbă, numărul dispare.
    6. Dovada
           -> aliasNamesTheCount ține propoziția. alterRenamesTheStoredColumn ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> AS numește expresia din rezultat. Nu redenumește coloana stocată. Agregatul se filtrează cu HAVING, nu cu WHERE pe alias.
*/

public class ALIASES {
    /*
    🟢 Începător
        Ce este            rezultatul are coloana name_count, iar employees.first_name rămâne neschimbat
        De ce e nevoie     fără AS, numărul există dar nu are numele pe care îl citești în client ca name_count
    🟡 Producție
        Cum se scrie       SELECT first_name, COUNT(first_name) AS name_count FROM employees GROUP BY first_name
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este ALTER TABLE employees CHANGE COLUMN first_name name_count VARCHAR(14), care schimbă schema
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE employees CHANGE COLUMN first_name name_count VARCHAR(14), care schimbă schema.
    */
    // Începător: aliasNamesTheCount păstrează propoziția pentru rezultatul are coloana name_count, iar employees.first_name rămâne neschimbat
    // Producție: public static String aliasNamesTheCount(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Nu poți folosi name_count în WHERE ca să filtrezi agregatul. Locul este HAVING.
    public static String aliasNamesTheCount() {
        // Începător: rezultatul are coloana name_count, iar employees.first_name rămâne neschimbat
        // Producție: SELECT first_name, COUNT(first_name) AS name_count FROM employees GROUP BY first_name. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Nu poți folosi name_count în WHERE ca să filtrezi agregatul. Locul este HAVING. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT first_name, COUNT(first_name) AS name_count FROM employees GROUP BY first_name";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide aliasNamesTheCount. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            se schimbă numele coloanei stocate, nu doar eticheta numărului din rezultat
        De ce e nevoie     fără textul greșit, AS pare DDL
    🟡 Producție
        Cum se scrie       ALTER TABLE employees CHANGE COLUMN first_name name_count VARCHAR(14)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care se schimbă numele coloanei stocate, nu doar eticheta numărului din rezultat
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu ALTER TABLE employees CHANGE COLUMN first_name name_count VARCHAR(14), care schimbă schema.
    */
    // Începător: alterRenamesTheStoredColumn păstrează textul greșit pentru se schimbă numele coloanei stocate, nu doar eticheta numărului din rezultat
    // Producție: public static String alterRenamesTheStoredColumn(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. AS nu schimbă schema. CHANGE COLUMN o schimbă.
    public static String alterRenamesTheStoredColumn() {
        // Începător: se schimbă numele coloanei stocate, nu doar eticheta numărului din rezultat
        // Producție: ALTER TABLE employees CHANGE COLUMN first_name name_count VARCHAR(14). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: AS nu schimbă schema. CHANGE COLUMN o schimbă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "ALTER TABLE employees CHANGE COLUMN first_name name_count VARCHAR(14)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide alterRenamesTheStoredColumn. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
