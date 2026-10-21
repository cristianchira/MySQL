package _2_SQL_Statements._2_DML._1_SELECT._15_WHERE_OR_HAVING;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345188#overview


/*                                                   WHERE_HAVING


    ○ WHERE

                   * allows us to set conditions

                   * that refer to subsets of individual rows

                   * applied before re-organizing the output into groups


    ○ HAVING

                  * cannot have both

                                - an aggregated

                                - and a non aggregated condition

                        in the having clause.


    ○ Aggregate function

                 * use Group By and Having

    ○ General condition

                 * WHERE


    ○ EXAMPLE

                 * SELECT  first_name, COUNT(first_name)

                   FROM  employees

                   WHERE   hire_date > '1998-01-01'

                   GROUP BY first_name

                   HAVING COUNT(first_name) < 100

                   ORDER BY first_name DESC;


 */


/*
TOPIC:
    WHERE hire_date > '1998-01-01' scoate rândurile înainte de grup; HAVING COUNT(first_name) < 100 scoate grupurile după.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu pui data în HAVING și numărul în WHERE. Rândul vechi nu intră în număr; grupul prea mare nu intră în rezultat.
MECHANISM:
    Serverul aplică WHERE pe rând, apoi GROUP BY, apoi HAVING pe grup. Aceeași propoziție le poate avea pe amândouă, în această ordine.
LOOKALIKES:
    O singură clauză nu face ambele filtre. HAVING hire_date > '1998-01-01' nu este locul datei de pe rând.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat.  PDF-ul WHERE+vs+HAVING a fost deschis. Manualul pune WHERE înainte de GROUP BY și HAVING după.
BOOK EXAMPLE:
    whereThenHaving
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_15_WHERE_OR_HAVING/WHERE_OR_HAVING.java
BREAK LAB:
    havingOnTheRowDate ține SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name HAVING hire_date > '1998-01-01'. hire_date este pe rând. WHERE o aplică înainte de grup. HAVING este pentru grup.
TRADE-OFFS:
    Doar WHERE când nu agregi. Doar HAVING când nu ai condiție pe rând.
ANTI-PATTERNS:
    Să muți hire_date în HAVING și să crezi că ai exclus angajații vechi înainte de număr.
STAFF QUESTIONS:
    whereThenHaving și havingOnTheRowDate. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Facturile de după 2024, pe client, doar clienții cu sub 100 de facturi: WHERE issued > '2024-01-01' și HAVING COUNT(*) < 100.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT first_name, COUNT(first_name) FROM employees WHERE hire_date > '1998-01-01' GROUP BY first_name HAVING COUNT(first_name) < 100?
   Capcană: HAVING hire_date > '1998-01-01' exclude angajații vechi înainte de COUNT.
   Cheie: whereThenHaving: se numără doar angajații de după 1998, iar prenumele cu 100 sau mai mulți cad din rezultat. fără WHERE, în număr intră și angajații vechi; fără HAVING, prenumele foarte dese rămân în rezultat
   Demonstrație: whereThenHaving
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT first_name, COUNT(first_name) FROM employees WHERE hire_date > '1998-01-01' GROUP BY first_name HAVING COUNT(first_name) < 100?
   Capcană: Cele două clauze se pot scrie în orice ordine.
   Cheie: Efectul este: se numără doar angajații de după 1998, iar prenumele cu 100 sau mai mulți cad din rezultat. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: whereThenHaving
3. Enunț: De ce SELECT first_name, COUNT(first_name) FROM employees WHERE hire_date > '1998-01-01' GROUP BY first_name HAVING COUNT(first_name) < 100 nu este vecinul său?
   Capcană: WHERE COUNT(first_name) < 100 este filtrul de grup.
   Cheie: O singură clauză nu face ambele filtre. HAVING hire_date > '1998-01-01' nu este locul datei de pe rând.
   Demonstrație: whereThenHaving
4. Enunț: Cum eșuează SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name HAVING hire_date > '1998-01-01'?
   Capcană: Dacă ai HAVING, WHERE este ignorat.
   Cheie: havingOnTheRowDate: hire_date este pe rând. WHERE o aplică înainte de grup. HAVING este pentru grup.
   Demonstrație: havingOnTheRowDate
5. Enunț: Când nu mai trimiți SELECT first_name, COUNT(first_name) FROM employees WHERE hire_date > '1998-01-01' GROUP BY first_name HAVING COUNT(first_name) < 100?
   Capcană: Ca să le combini, faci două interogări în aplicație.
   Cheie: Doar WHERE când nu agregi. Doar HAVING când nu ai condiție pe rând.
   Demonstrație: whereThenHaving

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE ai și un filtru de rând, și un filtru de număr -> whereThenHaving -> SELECT first_name, COUNT(first_name) FROM employees WHERE hire_date > '1998-01-01' GROUP BY first_name HAVING COUNT(first_name) < 100 -> nu garantează că hire_date din HAVING înseamnă același lucru cu WHERE
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> havingOnTheRowDate -> SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name HAVING hire_date > '1998-01-01' -> hire_date este pe rând. WHERE o aplică înainte de grup. HAVING este pentru grup.
    3. Granița
           -> Cele două clauze sunt mecanisme diferite în aceeași propoziție. Numele folderului nu le unește.
    4. Alternativa
           -> O singură clauză nu face ambele filtre. HAVING hire_date > '1998-01-01' nu este locul datei de pe rând.
    5. Anti-patternul
           -> Să muți hire_date în HAVING și să crezi că ai exclus angajații vechi înainte de număr.
    6. Dovada
           -> whereThenHaving ține propoziția. havingOnTheRowDate ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> WHERE filtrează rândurile înainte de grup. HAVING filtrează grupurile. Data stă în WHERE, numărul în HAVING.
*/

public class WHERE_OR_HAVING {
    /*
    🟢 Începător
        Ce este            se numără doar angajații de după 1998, iar prenumele cu 100 sau mai mulți cad din rezultat
        De ce e nevoie     fără WHERE, în număr intră și angajații vechi; fără HAVING, prenumele foarte dese rămân în rezultat
    🟡 Producție
        Cum se scrie       SELECT first_name, COUNT(first_name) FROM employees WHERE hire_date > '1998-01-01' GROUP BY first_name HAVING COUNT(first_name) < 100
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este HAVING hire_date > '1998-01-01', care nu este filtrul de rând de dinainte de grup
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu HAVING hire_date > '1998-01-01', care nu este filtrul de rând de dinainte de grup.
    */
    // Începător: whereThenHaving păstrează propoziția pentru se numără doar angajații de după 1998, iar prenumele cu 100 sau mai mulți cad din rezultat
    // Producție: public static String whereThenHaving(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Agregatul rămâne în HAVING. Data rămâne în WHERE.
    public static String whereThenHaving() {
        // Începător: se numără doar angajații de după 1998, iar prenumele cu 100 sau mai mulți cad din rezultat
        // Producție: SELECT first_name, COUNT(first_name) FROM employees WHERE hire_date > '1998-01-01' GROUP BY first_name HAVING COUNT(first_name) < 100. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Agregatul rămâne în HAVING. Data rămâne în WHERE. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT first_name, COUNT(first_name) FROM employees WHERE hire_date > '1998-01-01' GROUP BY first_name HAVING COUNT(first_name) < 100";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide whereThenHaving. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            data nu este agregatul grupului; filtrul de rând nu s-a aplicat înainte de număr
        De ce e nevoie     fără textul greșit, HAVING pare locul oricărei condiții
    🟡 Producție
        Cum se scrie       SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name HAVING hire_date > '1998-01-01'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care data nu este agregatul grupului; filtrul de rând nu s-a aplicat înainte de număr
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu HAVING hire_date > '1998-01-01', care nu este filtrul de rând de dinainte de grup.
    */
    // Începător: havingOnTheRowDate păstrează textul greșit pentru data nu este agregatul grupului; filtrul de rând nu s-a aplicat înainte de număr
    // Producție: public static String havingOnTheRowDate(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. hire_date este pe rând. WHERE o aplică înainte de grup. HAVING este pentru grup.
    public static String havingOnTheRowDate() {
        // Începător: data nu este agregatul grupului; filtrul de rând nu s-a aplicat înainte de număr
        // Producție: SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name HAVING hire_date > '1998-01-01'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: hire_date este pe rând. WHERE o aplică înainte de grup. HAVING este pentru grup. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT first_name, COUNT(first_name) FROM employees GROUP BY first_name HAVING hire_date > '1998-01-01'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide havingOnTheRowDate. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
