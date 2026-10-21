package _4_Database_Terminology.SQL_Keywords;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338034#overview


/*                              SQL = Structured Query Language


    1. A programming language

                   * specifically for working with databases

    2. Allow you to write queries that the computer can execute

                   * and then provide database insight in return


 */

/*
TOPIC:
    Cuvântul cheie devine propoziție doar când serverul o poate executa: SELECT emp_no FROM employees citește, lista de cuvinte nu.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu trimiți un glosar. Rezultatul are emp_no, tabela rămâne.
MECHANISM:
    Serverul execută SELECT. Un keyword din listă, fără clauze, nu este propoziție.
LOOKALIKES:
    SHOW DATABASES este altă propoziție, de metadate. Lista ADD, ALTER dintr-un PDF nu citește employees.
BOOK THEORY:
    PDF-ul SQL+Keywords.pdf a fost văzut în alte foldere ca listă de cuvinte, începând cu ADD. Pagina SELECT 9.7 a fost deschisă. Acest folder de terminologie nu înlocuiește propoziția.
BOOK EXAMPLE:
    selectEmpNoIsTheStatement
REPO DEMO:
    src/main/java/_4_Database_Terminology/SQL_Keywords/SQL_Keywords.java
BREAK LAB:
    theWordSelectIsNotAQuery ține SELECT. SELECT singur nu numește o expresie și o sursă. Propoziția este SELECT emp_no FROM employees.
TRADE-OFFS:
    Rămâi la listă doar când înveți numele, și trimiți propoziția când vrei rânduri.
ANTI-PATTERNS:
    Să trimiți SELECT fără expresie și fără FROM. Nu vine emp_no.
STAFF QUESTIONS:
    selectEmpNoIsTheStatement și theWordSelectIsNotAQuery. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un registru cere lot_no. SELECT lot_no FROM lot este propoziția. Cuvântul SELECT dintr-un poster nu este.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT emp_no FROM employees?
   Capcană: Cuvântul SELECT întoarce toate rândurile.
   Cheie: selectEmpNoIsTheStatement: rezultatul are emp_no și employees rămâne. fără propoziție, cuvântul SELECT dintr-o listă nu întoarce rânduri
   Demonstrație: selectEmpNoIsTheStatement
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT emp_no FROM employees?
   Capcană: Lista de cuvinte cheie este un plan.
   Cheie: Efectul este: rezultatul are emp_no și employees rămâne. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: selectEmpNoIsTheStatement
3. Enunț: De ce SELECT emp_no FROM employees nu este vecinul său?
   Capcană: SELECT emp_no FROM employees schimbă schema.
   Cheie: SHOW DATABASES este altă propoziție, de metadate. Lista ADD, ALTER dintr-un PDF nu citește employees.
   Demonstrație: selectEmpNoIsTheStatement
4. Enunț: Cum eșuează SELECT?
   Capcană: SHOW DATABASES este același lucru cu SELECT emp_no.
   Cheie: theWordSelectIsNotAQuery: SELECT singur nu numește o expresie și o sursă. Propoziția este SELECT emp_no FROM employees.
   Demonstrație: theWordSelectIsNotAQuery
5. Enunț: Când nu mai trimiți SELECT emp_no FROM employees?
   Capcană: Ca să citești, trimiți glosarul.
   Cheie: Rămâi la listă doar când înveți numele, și trimiți propoziția când vrei rânduri.
   Demonstrație: selectEmpNoIsTheStatement

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei valorile, nu lista de cuvinte -> selectEmpNoIsTheStatement -> SELECT emp_no FROM employees -> nu execută restul cuvintelor din glosar
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> theWordSelectIsNotAQuery -> SELECT -> SELECT singur nu numește o expresie și o sursă. Propoziția este SELECT emp_no FROM employees.
    3. Granița
           -> Propoziția este mecanismul. Glosarul este vocabularul.
    4. Alternativa
           -> SHOW DATABASES este altă propoziție, de metadate. Lista ADD, ALTER dintr-un PDF nu citește employees.
    5. Anti-patternul
           -> Să trimiți SELECT fără expresie și fără FROM. Nu vine emp_no.
    6. Dovada
           -> selectEmpNoIsTheStatement ține propoziția. theWordSelectIsNotAQuery ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> Serverul execută propoziția, nu cuvântul din listă. SELECT emp_no FROM employees citește. SELECT singur nu este complet.
*/

public class SQL_Keywords {
    /*
    🟢 Începător
        Ce este            rezultatul are emp_no și employees rămâne
        De ce e nevoie     fără propoziție, cuvântul SELECT dintr-o listă nu întoarce rânduri
    🟡 Producție
        Cum se scrie       SELECT emp_no FROM employees
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este o listă de cuvinte cheie, care nu are FROM employees
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu o listă de cuvinte cheie, care nu are FROM employees.
    */
    // Începător: selectEmpNoIsTheStatement păstrează propoziția pentru rezultatul are emp_no și employees rămâne
    // Producție: public static String selectEmpNoIsTheStatement(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Keyword-ul nu este planul și nu este un index.
    public static String selectEmpNoIsTheStatement() {
        // Începător: rezultatul are emp_no și employees rămâne
        // Producție: SELECT emp_no FROM employees. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Keyword-ul nu este planul și nu este un index. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT emp_no FROM employees";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide selectEmpNoIsTheStatement. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul nu are o propoziție completă și nu întoarce emp_no
        De ce e nevoie     fără textul greșit, cuvântul din glosar pare execuția
    🟡 Producție
        Cum se scrie       SELECT
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul nu are o propoziție completă și nu întoarce emp_no
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu o listă de cuvinte cheie, care nu are FROM employees.
    */
    // Începător: theWordSelectIsNotAQuery păstrează textul greșit pentru serverul nu are o propoziție completă și nu întoarce emp_no
    // Producție: public static String theWordSelectIsNotAQuery(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. SELECT singur nu numește o expresie și o sursă. Propoziția este SELECT emp_no FROM employees.
    public static String theWordSelectIsNotAQuery() {
        // Începător: serverul nu are o propoziție completă și nu întoarce emp_no
        // Producție: SELECT. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: SELECT singur nu numește o expresie și o sursă. Propoziția este SELECT emp_no FROM employees. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide theWordSelectIsNotAQuery. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
