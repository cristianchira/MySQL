package _2_SQL_Statements._2_DML._1_SELECT._10_IS_NOT_NULL_IS_NULL;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345126#overview


/*                                          IS NOT NULL


    ○ It is used to extract values that are NOT NULL


                                       IS_NOT_NULL Query


    ○ SELECT column_1, column_2,… column_n
      FROM table_name
      WHERE column_name IS NOT NULL;


                                           IS NULL


    ○ used to extract values that are null

    ○ SELECT column_1, column_2,… column_n
      FROM table_name
      WHERE column_name IS NULL;


    ○ Example
                * SELECT first_name, last_name, email
                  FROM employees
                  WHERE email IS NULL;



 */


/*
TOPIC:
    WHERE email IS NULL lasă rândurile fără email; email = NULL nu este adevărat și nu le găsește.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să vezi lipsa, nu un șir gol. Rândul cu email NULL intră doar la IS NULL.
MECHANISM:
    Serverul testează NULL cu logica de trei valori. = NULL nu este adevărat. IS NULL este testul. IS NOT NULL îi scoate pe cei fără email din acest rezultat.
LOOKALIKES:
    email = '' caută șirul gol, care este o valoare. email = NULL nu selectează nimic.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat.  PDF-ul IS+NOT+NULL a fost deschis. Manualul SELECT spune că WHERE este o expresie adevărată; NULL nu este egal cu NULL.
BOOK EXAMPLE:
    isNullKeepsMissingEmail
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_10_IS_NOT_NULL_IS_NULL/DB_IS_NOT_NULL_IS_NULL.java
BREAK LAB:
    equalsNullFindsNothing ține SELECT first_name, last_name, email FROM employees WHERE email = NULL. În logica cu trei valori, = NULL nu este adevărat. IS NULL este testul.
TRADE-OFFS:
    email = '' când valoarea prezentă este șirul gol și vrei să o vezi separat de NULL.
ANTI-PATTERNS:
    email = NULL ca să găsești lipsa. Rezultatul nu are rândul fără email.
STAFF QUESTIONS:
    isNullKeepsMissingEmail și equalsNullFindsNothing. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O fișă fără telefon: phone IS NULL. phone = NULL nu o aduce. phone = '' aduce fișa cu telefon scris gol.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT first_name, last_name, email FROM employees WHERE email IS NULL?
   Capcană: email = NULL întoarce rândurile fără email.
   Cheie: isNullKeepsMissingEmail: rămân angajații al căror email lipsește. fără IS NULL, email = NULL nu întoarce rândul fără adresă
   Demonstrație: isNullKeepsMissingEmail
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT first_name, last_name, email FROM employees WHERE email IS NULL?
   Capcană: IS NULL șterge email-ul din tabelă.
   Cheie: Efectul este: rămân angajații al căror email lipsește. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: isNullKeepsMissingEmail
3. Enunț: De ce SELECT first_name, last_name, email FROM employees WHERE email IS NULL nu este vecinul său?
   Capcană: '' și NULL sunt același lucru pentru WHERE.
   Cheie: email = '' caută șirul gol, care este o valoare. email = NULL nu selectează nimic.
   Demonstrație: isNullKeepsMissingEmail
4. Enunț: Cum eșuează SELECT first_name, last_name, email FROM employees WHERE email = NULL?
   Capcană: IS NOT NULL este negația scrisă cu <>.
   Cheie: equalsNullFindsNothing: În logica cu trei valori, = NULL nu este adevărat. IS NULL este testul.
   Demonstrație: equalsNullFindsNothing
5. Enunț: Când nu mai trimiți SELECT first_name, last_name, email FROM employees WHERE email IS NULL?
   Capcană: Ca să cauți lipsa, folosești COALESCE în WHERE egal cu NULL.
   Cheie: email = '' când valoarea prezentă este șirul gol și vrei să o vezi separat de NULL.
   Demonstrație: isNullKeepsMissingEmail

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE cauți lipsa valorii, nu un text -> isNullKeepsMissingEmail -> SELECT first_name, last_name, email FROM employees WHERE email IS NULL -> nu garantează că șirul gol este tot NULL
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> equalsNullFindsNothing -> SELECT first_name, last_name, email FROM employees WHERE email = NULL -> În logica cu trei valori, = NULL nu este adevărat. IS NULL este testul.
    3. Granița
           -> IS NULL este predicatul. O celulă goală desenată în client poate fi '' sau NULL; propoziția le desparte.
    4. Alternativa
           -> email = '' caută șirul gol, care este o valoare. email = NULL nu selectează nimic.
    5. Anti-patternul
           -> email = NULL ca să găsești lipsa. Rezultatul nu are rândul fără email.
    6. Dovada
           -> isNullKeepsMissingEmail ține propoziția. equalsNullFindsNothing ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> IS NULL găsește lipsa. = NULL nu este adevărat. Șirul gol nu este NULL.
*/

public class DB_IS_NOT_NULL_IS_NULL {
    /*
    🟢 Începător
        Ce este            rămân angajații al căror email lipsește
        De ce e nevoie     fără IS NULL, email = NULL nu întoarce rândul fără adresă
    🟡 Producție
        Cum se scrie       SELECT first_name, last_name, email FROM employees WHERE email IS NULL
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este email = NULL, care nu este testul de lipsă
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu email = NULL, care nu este testul de lipsă.
    */
    // Începător: isNullKeepsMissingEmail păstrează propoziția pentru rămân angajații al căror email lipsește
    // Producție: public static String isNullKeepsMissingEmail(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. IS NULL nu găsește șirul gol. Șirul gol este o valoare prezentă.
    public static String isNullKeepsMissingEmail() {
        // Începător: rămân angajații al căror email lipsește
        // Producție: SELECT first_name, last_name, email FROM employees WHERE email IS NULL. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: IS NULL nu găsește șirul gol. Șirul gol este o valoare prezentă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT first_name, last_name, email FROM employees WHERE email IS NULL";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide isNullKeepsMissingEmail. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            condiția nu este adevărată, iar rândul fără email nu apare
        De ce e nevoie     fără textul greșit, = NULL pare sinonim cu IS NULL
    🟡 Producție
        Cum se scrie       SELECT first_name, last_name, email FROM employees WHERE email = NULL
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care condiția nu este adevărată, iar rândul fără email nu apare
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu email = NULL, care nu este testul de lipsă.
    */
    // Începător: equalsNullFindsNothing păstrează textul greșit pentru condiția nu este adevărată, iar rândul fără email nu apare
    // Producție: public static String equalsNullFindsNothing(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. În logica cu trei valori, = NULL nu este adevărat. IS NULL este testul.
    public static String equalsNullFindsNothing() {
        // Începător: condiția nu este adevărată, iar rândul fără email nu apare
        // Producție: SELECT first_name, last_name, email FROM employees WHERE email = NULL. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: În logica cu trei valori, = NULL nu este adevărat. IS NULL este testul. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT first_name, last_name, email FROM employees WHERE email = NULL";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide equalsNullFindsNothing. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
