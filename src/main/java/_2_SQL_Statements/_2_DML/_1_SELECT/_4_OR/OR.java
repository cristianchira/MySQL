package _2_SQL_Statements._2_DML._1_SELECT._4_OR;

//Martin G.   OR
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8344962#overview


/*                                                   OR


    ○ SELECT column_1, column_2,… column_n
       FROM table_name
       WHERE condition_1 OR condition_2;


    ○ OR conditions set on the SAME column
    HISTORICAL: OR nu este limitat la aceeași coloană. Manualul 9.7, 15.2.13: OR este expresie. Aceeași coloană este doar exemplul din curs.


                                        can combine AND with OR


    ○ SELECT * FROM employees
       WHERE (first_name='Denis' OR first_name= "Elvis") AND gender='M';






 */


/*
TOPIC:
    OR păstrează rândul dacă first_name este Denis sau Elvis; AND gender = 'M' din paranteză îi lasă doar pe bărbații dintre ei.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să accepți mai multe prenume fără să pierzi filtrul de gen. Rândul care nu este nici Denis, nici Elvis nu apare.
MECHANISM:
    Serverul evaluează OR în WHERE. Fără paranteze, AND se leagă înainte. OR nu este limitat la aceeași coloană.
LOOKALIKES:
    AND cere toate testele. IN ('Denis','Elvis') este lista pe o coloană, nu OR amestecat cu AND.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat. PDF-urile din folderele SELECT au fost deschise în tur; multe au pe prima pagină doar titlul. OR.pdf a fost deschis. Comentariul de curs spune că OR stă pe aceeași coloană; manualul descrie expresia, nu această limită.
BOOK EXAMPLE:
    orKeepsDenisOrElvis
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_4_OR/OR.java
BREAK LAB:
    andRequiresBothNames ține SELECT * FROM employees WHERE first_name = 'Denis' AND first_name = 'Elvis'. AND pe două egalități ale aceleiași coloane cere un rând imposibil. OR acceptă unul dintre nume.
TRADE-OFFS:
    IN ('Denis','Elvis') când lista este pe o singură coloană și nu amesteci AND pe alt test în aceeași listă.
ANTI-PATTERNS:
    AND între Denis și Elvis lasă zero rânduri.
STAFF QUESTIONS:
    orKeepsDenisOrElvis și andRequiresBothNames. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un control lasă ecusoanele A sau B, dar doar tura de noapte. (badge = 'A' OR badge = 'B') AND shift = 'NIGHT'.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT * FROM employees WHERE (first_name = 'Denis' OR first_name = 'Elvis') AND gender = 'M'?
   Capcană: OR se scrie doar pe aceeași coloană, altfel serverul îl respinge.
   Cheie: orKeepsDenisOrElvis: rămân bărbații al căror prenume este Denis sau Elvis. fără OR, un singur egal lasă un singur prenume
   Demonstrație: orKeepsDenisOrElvis
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT * FROM employees WHERE (first_name = 'Denis' OR first_name = 'Elvis') AND gender = 'M'?
   Capcană: OR șterge din tabelă prenumele care nu sunt în listă.
   Cheie: Efectul este: rămân bărbații al căror prenume este Denis sau Elvis. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: orKeepsDenisOrElvis
3. Enunț: De ce SELECT * FROM employees WHERE (first_name = 'Denis' OR first_name = 'Elvis') AND gender = 'M' nu este vecinul său?
   Capcană: AND între Denis și Elvis îi aduce pe amândoi.
   Cheie: AND cere toate testele. IN ('Denis','Elvis') este lista pe o coloană, nu OR amestecat cu AND.
   Demonstrație: orKeepsDenisOrElvis
4. Enunț: Cum eșuează SELECT * FROM employees WHERE first_name = 'Denis' AND first_name = 'Elvis'?
   Capcană: Fără paranteze, rezultatul este același, pentru că serverul citește de la stânga la dreapta.
   Cheie: andRequiresBothNames: AND pe două egalități ale aceleiași coloane cere un rând imposibil. OR acceptă unul dintre nume.
   Demonstrație: andRequiresBothNames
5. Enunț: Când nu mai trimiți SELECT * FROM employees WHERE (first_name = 'Denis' OR first_name = 'Elvis') AND gender = 'M'?
   Capcană: Ca să alegi două prenume, ești obligat să faci două SELECT-uri unite cu UNION.
   Cheie: IN ('Denis','Elvis') când lista este pe o singură coloană și nu amesteci AND pe alt test în aceeași listă.
   Demonstrație: orKeepsDenisOrElvis

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE prenumele poate fi oricare din listă, iar genul rămâne bărbat -> orKeepsDenisOrElvis -> SELECT * FROM employees WHERE (first_name = 'Denis' OR first_name = 'Elvis') AND gender = 'M' -> nu garantează că parantezele sunt opționale lângă AND
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> andRequiresBothNames -> SELECT * FROM employees WHERE first_name = 'Denis' AND first_name = 'Elvis' -> AND pe două egalități ale aceleiași coloane cere un rând imposibil. OR acceptă unul dintre nume.
    3. Granița
           -> OR este operatorul. Click-ul de filtru din grid nu este propoziția.
    4. Alternativa
           -> AND cere toate testele. IN ('Denis','Elvis') este lista pe o coloană, nu OR amestecat cu AND.
    5. Anti-patternul
           -> AND între Denis și Elvis lasă zero rânduri.
    6. Dovada
           -> orKeepsDenisOrElvis ține propoziția. andRequiresBothNames ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> OR păstrează rândul la un test adevărat. AND îl cere pe amândoi. Fără paranteze, AND se leagă înainte și schimbă rezultatul.
*/

public class OR {
    /*
    🟢 Începător
        Ce este            rămân bărbații al căror prenume este Denis sau Elvis
        De ce e nevoie     fără OR, un singur egal lasă un singur prenume
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE (first_name = 'Denis' OR first_name = 'Elvis') AND gender = 'M'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este AND fără OR, care ar cere același rând să fie și Denis, și Elvis
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu AND fără OR, care ar cere același rând să fie și Denis, și Elvis.
    */
    // Începător: orKeepsDenisOrElvis păstrează propoziția pentru rămân bărbații al căror prenume este Denis sau Elvis
    // Producție: public static String orKeepsDenisOrElvis(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Fără paranteze, AND gender = 'M' se lipește de un singur OR și schimbă cine rămâne.
    public static String orKeepsDenisOrElvis() {
        // Începător: rămân bărbații al căror prenume este Denis sau Elvis
        // Producție: SELECT * FROM employees WHERE (first_name = 'Denis' OR first_name = 'Elvis') AND gender = 'M'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Fără paranteze, AND gender = 'M' se lipește de un singur OR și schimbă cine rămâne. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE (first_name = 'Denis' OR first_name = 'Elvis') AND gender = 'M'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide orKeepsDenisOrElvis. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            niciun rând nu are ambele prenume, deci rezultatul este gol
        De ce e nevoie     fără textul greșit, AND pare că adună prenumele
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE first_name = 'Denis' AND first_name = 'Elvis'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care niciun rând nu are ambele prenume, deci rezultatul este gol
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu AND fără OR, care ar cere același rând să fie și Denis, și Elvis.
    */
    // Începător: andRequiresBothNames păstrează textul greșit pentru niciun rând nu are ambele prenume, deci rezultatul este gol
    // Producție: public static String andRequiresBothNames(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. AND pe două egalități ale aceleiași coloane cere un rând imposibil. OR acceptă unul dintre nume.
    public static String andRequiresBothNames() {
        // Începător: niciun rând nu are ambele prenume, deci rezultatul este gol
        // Producție: SELECT * FROM employees WHERE first_name = 'Denis' AND first_name = 'Elvis'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: AND pe două egalități ale aceleiași coloane cere un rând imposibil. OR acceptă unul dintre nume. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE first_name = 'Denis' AND first_name = 'Elvis'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide andRequiresBothNames. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
