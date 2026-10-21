package _2_SQL_Statements._1_DDL;

// Martin G. DDL
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8777552#questions

// MySql DDL
//  https://dev.mysql.com/doc/refman/8.0/en/sql-data-definition-statements.html


/*                                    Data Definition Language (DDL)



    ○ A syntax

    ○ A set of statements that allow the user

                   * to define

                   * or modify data structure and objects, such as table


                                               Commands


    1. CREATE

                  * use for creating

                                - entire databases

                                - and database objects as tables.

    2. ALTER

    3. DROP

    4. RENAME

    5. TRUNCATE



 */

/*
TOPIC:
    DDL schimbă schema: CREATE TABLE sales (purchase_number INT) lasă un obiect și zero rânduri, iar DML ar fi schimbat rânduri.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să alegi propoziția care schimbă definiția, nu rândurile. După CREATE TABLE, sales există goală.
MECHANISM:
    Serverul execută propoziția DDL la sfârșitul ei și schimbă catalogul de obiecte. Un INSERT în aceeași familie de cuvinte ar fi scris un rând.
LOOKALIKES:
    DML, de exemplu DELETE FROM sales WHERE purchase_number = 1, schimbă rânduri și lasă definiția.
BOOK THEORY:
    Manualul 9.7 deschis pe CREATE TABLE, TRUNCATE, SELECT, INSERT, DELETE, UPDATE: DDL din acest folder este CREATE, ALTER, DROP, RENAME, TRUNCATE. TRUNCATE este clasat DDL, deși golește rânduri. PDF-ul DDL.pdf din folder a fost deschis.
BOOK EXAMPLE:
    ddlCreatesSales
REPO DEMO:
    src/main/java/_2_SQL_Statements/_1_DDL/DDL.java
BREAK LAB:
    deleteIsNotDdl ține DELETE FROM sales WHERE purchase_number = 1. DELETE nu creează și nu scoate obiectul. Este DML.
TRADE-OFFS:
    DML când obiectul există și vrei să schimbi rânduri. TRUNCATE când vrei golirea clasată DDL.
ANTI-PATTERNS:
    DELETE FROM sales WHERE purchase_number = 1 ca să apară schema. Nu apare nicio tabelă nouă.
STAFF QUESTIONS:
    ddlCreatesSales și deleteIsNotDdl. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Înainte de primul lot într-un depozit, îți trebuie tabela, nu ștergerea unui lot. CREATE TABLE lot (lot_no INT) este DDL.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE sales (purchase_number INT)?
   Capcană: CREATE TABLE sales (purchase_number INT) inserează și un rând NULL.
   Cheie: ddlCreatesSales: schema are sales și niciun rând de cumpărare. fără o propoziție DDL, nu apare obiectul pe care DML să îl umple
   Demonstrație: ddlCreatesSales
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE sales (purchase_number INT)?
   Capcană: Efectul de schemă se vede abia la COMMIT, ca la INSERT.
   Cheie: Efectul este: schema are sales și niciun rând de cumpărare. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: ddlCreatesSales
3. Enunț: De ce CREATE TABLE sales (purchase_number INT) nu este vecinul său?
   Capcană: DELETE este DDL pentru că schimbă ce vezi în tabelă.
   Cheie: DML, de exemplu DELETE FROM sales WHERE purchase_number = 1, schimbă rânduri și lasă definiția.
   Demonstrație: ddlCreatesSales
4. Enunț: Cum eșuează DELETE FROM sales WHERE purchase_number = 1?
   Capcană: DELETE creează sales dacă lipsește, ca să aibă de unde șterge.
   Cheie: deleteIsNotDdl: DELETE nu creează și nu scoate obiectul. Este DML.
   Demonstrație: deleteIsNotDdl
5. Enunț: Când nu mai trimiți CREATE TABLE sales (purchase_number INT)?
   Capcană: Ca să schimbi schema, folosești doar DML, fiindcă se poate da înapoi.
   Cheie: DML când obiectul există și vrei să schimbi rânduri. TRUNCATE când vrei golirea clasată DDL.
   Demonstrație: ddlCreatesSales

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei obiectul, nu un rând în minus -> ddlCreatesSales -> CREATE TABLE sales (purchase_number INT) -> nu garantează rânduri și nu clasifică TRUNCATE ca DML
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> deleteIsNotDdl -> DELETE FROM sales WHERE purchase_number = 1 -> DELETE nu creează și nu scoate obiectul. Este DML.
    3. Granița
           -> Propoziția DDL este mecanismul. Lista de cuvinte din comentariu nu schimbă schema până nu o trimiți.
    4. Alternativa
           -> DML, de exemplu DELETE FROM sales WHERE purchase_number = 1, schimbă rânduri și lasă definiția.
    5. Anti-patternul
           -> DELETE FROM sales WHERE purchase_number = 1 ca să apară schema. Nu apare nicio tabelă nouă.
    6. Dovada
           -> ddlCreatesSales ține propoziția. deleteIsNotDdl ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DDL schimbă schema. CREATE TABLE lasă zero rânduri. DELETE schimbă rânduri. TRUNCATE golește, dar rămâne DDL: commit implicit, definiția rămâne.
*/

public class DDL {
    /*
    🟢 Începător
        Ce este            schema are sales și niciun rând de cumpărare
        De ce e nevoie     fără o propoziție DDL, nu apare obiectul pe care DML să îl umple
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este DELETE FROM sales WHERE purchase_number = 1, care cere obiectul și scoate un rând
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu DELETE FROM sales WHERE purchase_number = 1, care cere obiectul și scoate un rând.
    */
    // Începător: ddlCreatesSales păstrează propoziția pentru schema are sales și niciun rând de cumpărare
    // Producție: public static String ddlCreatesSales(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Nu orice propoziție care atinge rânduri este DML: TRUNCATE golește și este DDL.
    public static String ddlCreatesSales() {
        // Începător: schema are sales și niciun rând de cumpărare
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Nu orice propoziție care atinge rânduri este DML: TRUNCATE golește și este DDL. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sales (purchase_number INT)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide ddlCreatesSales. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dacă sales există, dispare un rând și definiția rămâne; dacă nu există, propoziția nu creează sales
        De ce e nevoie     fără textul greșit, pare că orice ștergere schimbă schema
    🟡 Producție
        Cum se scrie       DELETE FROM sales WHERE purchase_number = 1
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dacă sales există, dispare un rând și definiția rămâne; dacă nu există, propoziția nu creează sales
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu DELETE FROM sales WHERE purchase_number = 1, care cere obiectul și scoate un rând.
    */
    // Începător: deleteIsNotDdl păstrează textul greșit pentru dacă sales există, dispare un rând și definiția rămâne; dacă nu există, propoziția nu creează sales
    // Producție: public static String deleteIsNotDdl(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DELETE nu creează și nu scoate obiectul. Este DML.
    public static String deleteIsNotDdl() {
        // Începător: dacă sales există, dispare un rând și definiția rămâne; dacă nu există, propoziția nu creează sales
        // Producție: DELETE FROM sales WHERE purchase_number = 1. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: DELETE nu creează și nu scoate obiectul. Este DML. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "DELETE FROM sales WHERE purchase_number = 1";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide deleteIsNotDdl. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
