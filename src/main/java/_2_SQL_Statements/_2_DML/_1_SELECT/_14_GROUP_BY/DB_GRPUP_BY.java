package _2_SQL_Statements._2_DML._1_SELECT._14_GROUP_BY;

// MArtin G. GRPOP BY
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345180#overview


/*                                       GROUP BY


    ○ SELECT column_name(s)
      FROM table_name
      WHERE conditions
          GROUP BY column_name(s)
       ORDER BY column_name(s);

    ○ Example
      SELECT first_name, count(first_name)
      FROM employees
          GROUP BY first_name
      ORDER BY first_name DESC;

    ○ When working in SQL,
                  * results can be grouped according to a specific
                                - field
                                - or fields

    ○ GROUP BY must be PLACED:
                 * immediately after the WHERE conditions, if any,
                 * and just before the ORDER BY clause

    ○ In most cases, when you need an Aggregate Function,
                 * you must add a GROUP BY clause in your query, too.

    ○ GROUP BY:
                 * Always include the field you have grouped your results by
                             - in the SELECT statement!

                  * GROUP BY is one of the most powerful and useful tools in SQL

    ○ In most cases, when you need an Aggregate Functions,
                * you must add a GROUP BY clause in your query too.

    ○ Example
                * Assume you need a list composed of two fields.
                            - The first must contain a distinct first name of the employee,
                            - and the second the number of times this name
                                        • is encountered in our database.

                * SELECT
                      first_name, count(first_name) as appearance
                  FROM
                      employees
                  GROUP BY first_name;


 */


/*
TOPIC:
    GROUP BY first_name face un grup pe prenume ca să poți număra; nu este filtrul care scoate un angajat înainte de agregare.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să numeri câte rânduri au același prenume. Rezultatul are un rând pe grup, tabela rămâne.
MECHANISM:
    Serverul grupează după WHERE și înainte de HAVING, în ordinea din 15.2.13. Agregatul fără grup, pe o coloană neagregată, nu este această propoziție.
LOOKALIKES:
    WHERE first_name = 'Denis' filtrează înainte de grup. DISTINCT first_name nu numără.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat.  PDF-ul GROUP+BY a fost deschis. Comentariul de curs pune GROUP BY după WHERE și înainte de ORDER BY, în acord cu ordinea din manual.
BOOK EXAMPLE:
    groupByFirstNameCounts
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_14_GROUP_BY/DB_GRPUP_BY.java
BREAK LAB:
    whereIsNotAGroup ține SELECT first_name, COUNT(first_name) FROM employees WHERE first_name = 'Denis'. WHERE filtrează rânduri. GROUP BY face grupul pe care COUNT îl umple.
TRADE-OFFS:
    WHERE când scoți rânduri înainte de număr. HAVING când scoți grupuri după număr.
ANTI-PATTERNS:
    WHERE first_name = 'Denis' cu COUNT, fără GROUP BY, ca înlocuitor al grupării pe toate prenumele.
STAFF QUESTIONS:
    groupByFirstNameCounts și whereIsNotAGroup. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Numărul de cutii pe lot: SELECT lot_no, COUNT(*) FROM box GROUP BY lot_no. WHERE lot_no = 'L1' nu numără fiecare lot.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name?
   Capcană: GROUP BY șterge angajații până rămâne un rând pe prenume în tabelă.
   Cheie: groupByFirstNameCounts: rezultatul are un rând pe prenume și numărul de apariții; employees rămâne. fără GROUP BY, COUNT pe lângă first_name nu are grupul pe care cursul îl cere în SELECT
   Demonstrație: groupByFirstNameCounts
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name?
   Capcană: WHERE COUNT(first_name) > 1 este filtrul de grup.
   Cheie: Efectul este: rezultatul are un rând pe prenume și numărul de apariții; employees rămâne. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: groupByFirstNameCounts
3. Enunț: De ce SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name nu este vecinul său?
   Capcană: DISTINCT first_name numără aparițiile.
   Cheie: WHERE first_name = 'Denis' filtrează înainte de grup. DISTINCT first_name nu numără.
   Demonstrație: groupByFirstNameCounts
4. Enunț: Cum eșuează SELECT first_name, COUNT(first_name) FROM employees WHERE first_name = 'Denis'?
   Capcană: Fără first_name în SELECT, grupul este deja corect pe orice mod.
   Cheie: whereIsNotAGroup: WHERE filtrează rânduri. GROUP BY face grupul pe care COUNT îl umple.
   Demonstrație: whereIsNotAGroup
5. Enunț: Când nu mai trimiți SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name?
   Capcană: Ca să numeri, filtrezi cu WHERE și nu mai grupezi.
   Cheie: WHERE când scoți rânduri înainte de număr. HAVING când scoți grupuri după număr.
   Demonstrație: groupByFirstNameCounts

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei un rând de rezultat pe prenume, cu număr -> groupByFirstNameCounts -> SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name -> nu garantează că grupul cu un singur angajat dispare
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> whereIsNotAGroup -> SELECT first_name, COUNT(first_name) FROM employees WHERE first_name = 'Denis' -> WHERE filtrează rânduri. GROUP BY face grupul pe care COUNT îl umple.
    3. Granița
           -> GROUP BY este clauza. Ortografia GRPUP din numele clasei rămâne și nu este propoziția.
    4. Alternativa
           -> WHERE first_name = 'Denis' filtrează înainte de grup. DISTINCT first_name nu numără.
    5. Anti-patternul
           -> WHERE first_name = 'Denis' cu COUNT, fără GROUP BY, ca înlocuitor al grupării pe toate prenumele.
    6. Dovada
           -> groupByFirstNameCounts ține propoziția. whereIsNotAGroup ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> GROUP BY face grupuri ca să agregi, după WHERE și înainte de HAVING. DISTINCT nu numără. WHERE nu înlocuiește grupul.
*/

public class DB_GRPUP_BY {
    /*
    🟢 Începător
        Ce este            rezultatul are un rând pe prenume și numărul de apariții; employees rămâne
        De ce e nevoie     fără GROUP BY, COUNT pe lângă first_name nu are grupul pe care cursul îl cere în SELECT
    🟡 Producție
        Cum se scrie       SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este WHERE first_name = 'Denis', care scoate rânduri înainte de orice număr
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu WHERE first_name = 'Denis', care scoate rânduri înainte de orice număr.
    */
    // Începător: groupByFirstNameCounts păstrează propoziția pentru rezultatul are un rând pe prenume și numărul de apariții; employees rămâne
    // Producție: public static String groupByFirstNameCounts(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. GROUP BY nu filtrează grupurile. Grupul cu număr 1 tot apare, până scrii HAVING.
    public static String groupByFirstNameCounts() {
        // Începător: rezultatul are un rând pe prenume și numărul de apariții; employees rămâne
        // Producție: SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: GROUP BY nu filtrează grupurile. Grupul cu număr 1 tot apare, până scrii HAVING. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide groupByFirstNameCounts. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            lipsește grupul: propoziția nu este numărătoarea pe fiecare prenume
        De ce e nevoie     fără textul greșit, WHERE pare că înlocuiește GROUP BY
    🟡 Producție
        Cum se scrie       SELECT first_name, COUNT(first_name) FROM employees WHERE first_name = 'Denis'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care lipsește grupul: propoziția nu este numărătoarea pe fiecare prenume
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu WHERE first_name = 'Denis', care scoate rânduri înainte de orice număr.
    */
    // Începător: whereIsNotAGroup păstrează textul greșit pentru lipsește grupul: propoziția nu este numărătoarea pe fiecare prenume
    // Producție: public static String whereIsNotAGroup(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. WHERE filtrează rânduri. GROUP BY face grupul pe care COUNT îl umple.
    public static String whereIsNotAGroup() {
        // Începător: lipsește grupul: propoziția nu este numărătoarea pe fiecare prenume
        // Producție: SELECT first_name, COUNT(first_name) FROM employees WHERE first_name = 'Denis'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: WHERE filtrează rânduri. GROUP BY face grupul pe care COUNT îl umple. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT first_name, COUNT(first_name) FROM employees WHERE first_name = 'Denis'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide whereIsNotAGroup. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
