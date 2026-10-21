package _2_SQL_Statements._2_DML._1_SELECT._17_LIMIT;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345200#overview


/*                                           LIMIT


    ▬▬▐ Numbers or rows displayed


                                       ▬▬ LIMIT Query ▬▬


    ○ SELECT column_name(s)
      FROM table_name
      WHERE conditions
      GROUP BY column_name(s)
      HAVING conditions
      ORDER BY column_name(s)
      LIMIT number ;




 */

/*
TOPIC:
    LIMIT 12 taie rezultatul la 12 rânduri după ordine; nu șterge restul din employees.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să primești o pagină, nu toată tabela. Rândurile dincolo de 12 rămân stocate.
MECHANISM:
    Serverul aplică LIMIT la sfârșitul SELECT, după ORDER BY, cum este în 15.2.13. Fără ORDER BY, cele 12 rânduri nu sunt un set stabil.
LOOKALIKES:
    DELETE ... LIMIT 12 șterge rânduri. LIMIT din SELECT doar taie rezultatul.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat.  PDF-ul LIMIT a fost deschis. Manualul: LIMIT row_count sau offset.
BOOK EXAMPLE:
    limitTwelveInTheResult
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_17_LIMIT/DB_LIMIT.java
BREAK LAB:
    deleteLimitRemovesRows ține DELETE FROM employees ORDER BY first_name LIMIT 12. DELETE LIMIT este DML și scoate rânduri. SELECT LIMIT taie rezultatul.
TRADE-OFFS:
    DELETE ... LIMIT când chiar vrei să scoți un număr de rânduri, în InnoDB, înăuntrul unei tranzacții dacă trebuie să dai înapoi.
ANTI-PATTERNS:
    DELETE ... LIMIT 12 ca să afișezi o pagină. Rândurile dispar.
STAFF QUESTIONS:
    limitTwelveInTheResult și deleteLimitRemovesRows. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Prima pagină de loturi: ORDER BY lot_no LIMIT 12. Nu este ștergerea a 12 loturi.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT * FROM employees ORDER BY first_name LIMIT 12?
   Capcană: LIMIT 12 șterge restul rândurilor din employees.
   Cheie: limitTwelveInTheResult: rezultatul are cel mult 12 rânduri, în ordinea prenumelui, iar employees rămâne întreg. fără LIMIT, clientul primește toate rândurile care au trecut de WHERE
   Demonstrație: limitTwelveInTheResult
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT * FROM employees ORDER BY first_name LIMIT 12?
   Capcană: Fără ORDER BY, primele 12 sunt mereu aceleași.
   Cheie: Efectul este: rezultatul are cel mult 12 rânduri, în ordinea prenumelui, iar employees rămâne întreg. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: limitTwelveInTheResult
3. Enunț: De ce SELECT * FROM employees ORDER BY first_name LIMIT 12 nu este vecinul său?
   Capcană: DELETE LIMIT 12 este pagina de afișare.
   Cheie: DELETE ... LIMIT 12 șterge rânduri. LIMIT din SELECT doar taie rezultatul.
   Demonstrație: limitTwelveInTheResult
4. Enunț: Cum eșuează DELETE FROM employees ORDER BY first_name LIMIT 12?
   Capcană: LIMIT stă înainte de WHERE.
   Cheie: deleteLimitRemovesRows: DELETE LIMIT este DML și scoate rânduri. SELECT LIMIT taie rezultatul.
   Demonstrație: deleteLimitRemovesRows
5. Enunț: Când nu mai trimiți SELECT * FROM employees ORDER BY first_name LIMIT 12?
   Capcană: Ca să paginezi, aduci toată tabela și tai în aplicație, iar LIMIT nu schimbă nimic pe server.
   Cheie: DELETE ... LIMIT când chiar vrei să scoți un număr de rânduri, în InnoDB, înăuntrul unei tranzacții dacă trebuie să dai înapoi.
   Demonstrație: limitTwelveInTheResult

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei o pagină de citire, stabilă prin ORDER BY -> limitTwelveInTheResult -> SELECT * FROM employees ORDER BY first_name LIMIT 12 -> nu garantează aceleași 12 rânduri dacă lipsește ORDER BY
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> deleteLimitRemovesRows -> DELETE FROM employees ORDER BY first_name LIMIT 12 -> DELETE LIMIT este DML și scoate rânduri. SELECT LIMIT taie rezultatul.
    3. Granița
           -> LIMIT este clauza. Un pager din client care taie după ce a primit tot setul nu este această propoziție.
    4. Alternativa
           -> DELETE ... LIMIT 12 șterge rânduri. LIMIT din SELECT doar taie rezultatul.
    5. Anti-patternul
           -> DELETE ... LIMIT 12 ca să afișezi o pagină. Rândurile dispar.
    6. Dovada
           -> limitTwelveInTheResult ține propoziția. deleteLimitRemovesRows ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> SELECT LIMIT taie rezultatul. Fără ORDER BY, cele 12 nu sunt stabile. DELETE LIMIT scoate rânduri din tabelă.
*/

public class DB_LIMIT {
    /*
    🟢 Începător
        Ce este            rezultatul are cel mult 12 rânduri, în ordinea prenumelui, iar employees rămâne întreg
        De ce e nevoie     fără LIMIT, clientul primește toate rândurile care au trecut de WHERE
    🟡 Producție
        Cum se scrie       SELECT * FROM employees ORDER BY first_name LIMIT 12
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este DELETE FROM employees ORDER BY first_name LIMIT 12, care scoate 12 rânduri din tabelă
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu DELETE FROM employees ORDER BY first_name LIMIT 12, care scoate 12 rânduri din tabelă.
    */
    // Începător: limitTwelveInTheResult păstrează propoziția pentru rezultatul are cel mult 12 rânduri, în ordinea prenumelui, iar employees rămâne întreg
    // Producție: public static String limitTwelveInTheResult(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. LIMIT fără ORDER BY nu promite care 12 rânduri vin.
    public static String limitTwelveInTheResult() {
        // Începător: rezultatul are cel mult 12 rânduri, în ordinea prenumelui, iar employees rămâne întreg
        // Producție: SELECT * FROM employees ORDER BY first_name LIMIT 12. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: LIMIT fără ORDER BY nu promite care 12 rânduri vin. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees ORDER BY first_name LIMIT 12";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide limitTwelveInTheResult. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dispar 12 rânduri din tabelă, nu doar dintr-un ecran
        De ce e nevoie     fără textul greșit, LIMIT pare întotdeauna o pagină
    🟡 Producție
        Cum se scrie       DELETE FROM employees ORDER BY first_name LIMIT 12
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dispar 12 rânduri din tabelă, nu doar dintr-un ecran
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu DELETE FROM employees ORDER BY first_name LIMIT 12, care scoate 12 rânduri din tabelă.
    */
    // Începător: deleteLimitRemovesRows păstrează textul greșit pentru dispar 12 rânduri din tabelă, nu doar dintr-un ecran
    // Producție: public static String deleteLimitRemovesRows(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DELETE LIMIT este DML și scoate rânduri. SELECT LIMIT taie rezultatul.
    public static String deleteLimitRemovesRows() {
        // Începător: dispar 12 rânduri din tabelă, nu doar dintr-un ecran
        // Producție: DELETE FROM employees ORDER BY first_name LIMIT 12. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: DELETE LIMIT este DML și scoate rânduri. SELECT LIMIT taie rezultatul. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "DELETE FROM employees ORDER BY first_name LIMIT 12";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide deleteLimitRemovesRows. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
