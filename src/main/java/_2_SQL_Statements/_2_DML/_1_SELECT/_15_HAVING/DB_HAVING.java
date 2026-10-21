package _2_SQL_Statements._2_DML._1_SELECT._15_HAVING;

// MArtin G. Having
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345186#overview


/*                                       HAVING


    ▬▬▐ Refines the output from records that do not satisfy a certain condition
                  * frequently implemented with GROUP BY
                  * Having needs to be inserted between the group by and order by clauses.
                  * HAVING is like WHERE but applied to the GROUP BY block

    ○ Syntax
                  * SELECT column_name(s)
                    FROM table_name
                    WHERE conditions (not mandatory)
                    GROUP BY column_name(s)
                        HAVING conditions
                    ORDER BY column_name(s)
                    LIMIT n;


    ○ Example

                   * SELECT first_name, last_name, COUNT(gender) as appearance
                     FROM employees
                     WHERE gender = 'M'
                     GROUP BY first_name, last_name
                     HAVING COUNT(*) > 1
                     ORDER BY first_name, last_name desc
                     limit 12;


                             ▬▬ WHERE vs. HAVING ▬▬


    ○ After HAVING,
                  * you can have a condition with an Aggregate Functions,

    ○ While WHERE
                 * cannot use Aggregate Functions within its conditions


 */

/*
TOPIC:
    HAVING COUNT(*) > 1 scoate grupurile care nu au mai mult de un rând; WHERE nu poate purta agregatul.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să rămână prenumele care apar de mai multe ori. Grupul cu un singur om cade din rezultat, nu din tabelă.
MECHANISM:
    Serverul filtrează grupurile cu HAVING, după GROUP BY și înainte de ORDER BY. Agregatele sunt interzise în WHERE.
LOOKALIKES:
    WHERE gender = 'M' filtrează rândurile înainte de grup. HAVING nu este un WHERE mutat mai jos pentru coloane negregate, deși o expresie negregată poate apărea; locul filtrului de rând este WHERE.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat.  PDF-ul HAVING a fost deschis. Manualul: HAVING este where_condition după GROUP BY. Agregatele nu stau în WHERE.
BOOK EXAMPLE:
    havingKeepsRepeatedNames
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_15_HAVING/DB_HAVING.java
BREAK LAB:
    whereCountIsRejected ține SELECT first_name, COUNT(*) FROM employees WHERE COUNT(*) > 1 GROUP BY first_name. Manualul 9.7: în WHERE nu folosești funcții de agregare. HAVING este după grup.
TRADE-OFFS:
    WHERE când condiția este pe rând, înainte de grup, de exemplu gender = 'M'.
ANTI-PATTERNS:
    WHERE COUNT(*) > 1. Propoziția nu pornește.
STAFF QUESTIONS:
    havingKeepsRepeatedNames și whereCountIsRejected. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Loturile cu mai mult de o cutie: GROUP BY lot_no HAVING COUNT(*) > 1. WHERE qty > 1 filtrează cutia, nu lotul.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT first_name, COUNT(*) FROM employees GROUP BY first_name HAVING COUNT(*) > 1?
   Capcană: HAVING COUNT(*) > 1 șterge din employees oamenii cu prenume unic.
   Cheie: havingKeepsRepeatedNames: în rezultat rămân doar prenumele care au cel puțin două rânduri. fără HAVING, GROUP BY lasă și prenumele care apar o dată
   Demonstrație: havingKeepsRepeatedNames
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT first_name, COUNT(*) FROM employees GROUP BY first_name HAVING COUNT(*) > 1?
   Capcană: WHERE COUNT(*) > 1 este forma corectă, pentru că WHERE este filtrul.
   Cheie: Efectul este: în rezultat rămân doar prenumele care au cel puțin două rânduri. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: havingKeepsRepeatedNames
3. Enunț: De ce SELECT first_name, COUNT(*) FROM employees GROUP BY first_name HAVING COUNT(*) > 1 nu este vecinul său?
   Capcană: HAVING și WHERE sunt aceeași clauză, mutată.
   Cheie: WHERE gender = 'M' filtrează rândurile înainte de grup. HAVING nu este un WHERE mutat mai jos pentru coloane negregate, deși o expresie negregată poate apărea; locul filtrului de rând este WHERE.
   Demonstrație: havingKeepsRepeatedNames
4. Enunț: Cum eșuează SELECT first_name, COUNT(*) FROM employees WHERE COUNT(*) > 1 GROUP BY first_name?
   Capcană: Fără GROUP BY, HAVING COUNT(*) > 1 tot face un grup pe prenume.
   Cheie: whereCountIsRejected: Manualul 9.7: în WHERE nu folosești funcții de agregare. HAVING este după grup.
   Demonstrație: whereCountIsRejected
5. Enunț: Când nu mai trimiți SELECT first_name, COUNT(*) FROM employees GROUP BY first_name HAVING COUNT(*) > 1?
   Capcană: Ca să ții grupurile mari, filtrezi în aplicație după SELECT fără HAVING.
   Cheie: WHERE când condiția este pe rând, înainte de grup, de exemplu gender = 'M'.
   Demonstrație: havingKeepsRepeatedNames

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE filtrul este pe numărul grupului, nu pe un rând -> havingKeepsRepeatedNames -> SELECT first_name, COUNT(*) FROM employees GROUP BY first_name HAVING COUNT(*) > 1 -> nu garantează că rândurile duplicate au fost șterse din employees
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> whereCountIsRejected -> SELECT first_name, COUNT(*) FROM employees WHERE COUNT(*) > 1 GROUP BY first_name -> Manualul 9.7: în WHERE nu folosești funcții de agregare. HAVING este după grup.
    3. Granița
           -> HAVING este clauza. Un filtru de grid după ce ai exportat nu este propoziția.
    4. Alternativa
           -> WHERE gender = 'M' filtrează rândurile înainte de grup. HAVING nu este un WHERE mutat mai jos pentru coloane negregate, deși o expresie negregată poate apărea; locul filtrului de rând este WHERE.
    5. Anti-patternul
           -> WHERE COUNT(*) > 1. Propoziția nu pornește.
    6. Dovada
           -> havingKeepsRepeatedNames ține propoziția. whereCountIsRejected ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> HAVING filtrează grupuri și poate purta agregatul. WHERE filtrează rânduri și nu primește agregatul. Grupul respins nu este șters din tabelă.
*/

public class DB_HAVING {
    /*
    🟢 Începător
        Ce este            în rezultat rămân doar prenumele care au cel puțin două rânduri
        De ce e nevoie     fără HAVING, GROUP BY lasă și prenumele care apar o dată
    🟡 Producție
        Cum se scrie       SELECT first_name, COUNT(*) FROM employees GROUP BY first_name HAVING COUNT(*) > 1
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este WHERE first_name = 'Denis', care alege rânduri înainte de număr
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu WHERE first_name = 'Denis', care alege rânduri înainte de număr.
    */
    // Începător: havingKeepsRepeatedNames păstrează propoziția pentru în rezultat rămân doar prenumele care au cel puțin două rânduri
    // Producție: public static String havingKeepsRepeatedNames(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. WHERE COUNT(*) > 1 nu este filtrul de grup. Serverul nu acceptă agregatul în WHERE.
    public static String havingKeepsRepeatedNames() {
        // Începător: în rezultat rămân doar prenumele care au cel puțin două rânduri
        // Producție: SELECT first_name, COUNT(*) FROM employees GROUP BY first_name HAVING COUNT(*) > 1. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: WHERE COUNT(*) > 1 nu este filtrul de grup. Serverul nu acceptă agregatul în WHERE. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT first_name, COUNT(*) FROM employees GROUP BY first_name HAVING COUNT(*) > 1";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide havingKeepsRepeatedNames. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul respinge agregatul în WHERE și nu lasă grupurile repetate
        De ce e nevoie     fără textul greșit, WHERE pare locul oricărui filtru
    🟡 Producție
        Cum se scrie       SELECT first_name, COUNT(*) FROM employees WHERE COUNT(*) > 1 GROUP BY first_name
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul respinge agregatul în WHERE și nu lasă grupurile repetate
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu WHERE first_name = 'Denis', care alege rânduri înainte de număr.
    */
    // Începător: whereCountIsRejected păstrează textul greșit pentru serverul respinge agregatul în WHERE și nu lasă grupurile repetate
    // Producție: public static String whereCountIsRejected(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Manualul 9.7: în WHERE nu folosești funcții de agregare. HAVING este după grup.
    public static String whereCountIsRejected() {
        // Începător: serverul respinge agregatul în WHERE și nu lasă grupurile repetate
        // Producție: SELECT first_name, COUNT(*) FROM employees WHERE COUNT(*) > 1 GROUP BY first_name. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Manualul 9.7: în WHERE nu folosești funcții de agregare. HAVING este după grup. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT first_name, COUNT(*) FROM employees WHERE COUNT(*) > 1 GROUP BY first_name";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide whereCountIsRejected. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
