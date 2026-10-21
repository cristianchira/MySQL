package _2_SQL_Statements._1_DDL._1_CREATE;

// https://dev.mysql.com/doc/refman/8.0/en/create-database.html

//  https://dev.mysql.com/doc/refman/8.0/en/create-table.html


/*                                      Create a statement


    ○ CREATE object_type object_name (column_name data type);

                  * CREATE STATEMENT CREATES AN OBJECT WITH A CERTAIN NAME

                  * It is used for creating entire databases and database objects as tables.


                                      Example creating statement


    ○ CREATE {DATABASE | SCHEMA} [IF NOT EXISTS] db_name;


                                      Example creating table 'sales'


   ○ CREATE TABLE sales(purchase_number INT);

                  * must add at least 1 column

                  * Table name can coincide with database name, this is common practice




 */
/*
TOPIC:
    CREATE DATABASE lasă o schemă goală; nu creează tabela sales și nu scrie un rând.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să existe schema înainte de tabelă. După propoziție, schema este goală.
MECHANISM:
    Serverul execută CREATE DATABASE la sfârșitul propoziției și adaugă schema. IF NOT EXISTS nu schimbă nimic dacă numele există deja.
LOOKALIKES:
    CREATE TABLE sales (purchase_number INT) face tabela în schema curentă. USE doar o alege.
BOOK THEORY:
    Pagina CREATE DATABASE nu a fost adusă separat: NERULAT AICI pentru acel URL. Pagina CREATE TABLE a fost deschisă: tabela cere cel puțin o coloană. PDF-ul Creating_Table.pdf din folder a fost deschis.
    HISTORICAL: pagina 15.1.14 a fost adusă după aceea. CREATE DATABASE face directorul schemei, fără tabele și fără să schimbe schema sesiunii. CREATE TABLE face obiectul cu cel puțin o coloană.
BOOK EXAMPLE:
    createDatabaseLeavesNoTable
REPO DEMO:
    src/main/java/_2_SQL_Statements/_1_DDL/_1_CREATE/Create.java
BREAK LAB:
    createDatabaseWithAColumn ține CREATE DATABASE sales (purchase_number INT). Coloanele nu stau pe schemă. Obiectul cu coloană este CREATE TABLE.
TRADE-OFFS:
    CREATE TABLE când schema există și vrei obiectul. USE când schema există și vrei să o faci curentă.
ANTI-PATTERNS:
    CREATE DATABASE sales (purchase_number INT) nu creează nici schema, nici tabela.
STAFF QUESTIONS:
    createDatabaseLeavesNoTable și createDatabaseWithAColumn. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un laborator nou are nevoie de schema clinic înainte de tabela de probe. CREATE DATABASE clinic lasă probele necreate.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE DATABASE sales?
   Capcană: CREATE DATABASE sales și creează tabela sales cu un rând gol.
   Cheie: createDatabaseLeavesNoTable: apare schema sales, fără tabela purchase și fără rânduri. fără schemă, CREATE TABLE nu are unde să pună obiectul dacă nu există o schemă curentă
   Demonstrație: createDatabaseLeavesNoTable
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE DATABASE sales?
   Capcană: După propoziție, sesiunea este deja în sales și are un rând.
   Cheie: Efectul este: apare schema sales, fără tabela purchase și fără rânduri. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: createDatabaseLeavesNoTable
3. Enunț: De ce CREATE DATABASE sales nu este vecinul său?
   Capcană: CREATE DATABASE și CREATE TABLE sunt același DDL, deci ambele fac coloana.
   Cheie: CREATE TABLE sales (purchase_number INT) face tabela în schema curentă. USE doar o alege.
   Demonstrație: createDatabaseLeavesNoTable
4. Enunț: Cum eșuează CREATE DATABASE sales (purchase_number INT)?
   Capcană: Paranteza cu INT este ignorată și schema apare împreună cu coloana.
   Cheie: createDatabaseWithAColumn: Coloanele nu stau pe schemă. Obiectul cu coloană este CREATE TABLE.
   Demonstrație: createDatabaseWithAColumn
5. Enunț: Când nu mai trimiți CREATE DATABASE sales?
   Capcană: Orice nume nou se face doar cu CREATE TABLE.
   Cheie: CREATE TABLE când schema există și vrei obiectul. USE când schema există și vrei să o faci curentă.
   Demonstrație: createDatabaseLeavesNoTable

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei schema sales, încă fără tabele -> createDatabaseLeavesNoTable -> CREATE DATABASE sales -> nu garantează o tabelă și nu schimbă schema sesiunii până la USE
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> createDatabaseWithAColumn -> CREATE DATABASE sales (purchase_number INT) -> Coloanele nu stau pe schemă. Obiectul cu coloană este CREATE TABLE.
    3. Granița
           -> CREATE DATABASE este DDL. Click-ul de schemă din Workbench nu este propoziția.
    4. Alternativa
           -> CREATE TABLE sales (purchase_number INT) face tabela în schema curentă. USE doar o alege.
    5. Anti-patternul
           -> CREATE DATABASE sales (purchase_number INT) nu creează nici schema, nici tabela.
    6. Dovada
           -> createDatabaseLeavesNoTable ține propoziția. createDatabaseWithAColumn ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> CREATE DATABASE sales face schema goală. CREATE TABLE face tabela. Coloanele pe CREATE DATABASE eșuează.
*/

public class Create {
    /*
    🟢 Începător
        Ce este            apare schema sales, fără tabela purchase și fără rânduri
        De ce e nevoie     fără schemă, CREATE TABLE nu are unde să pună obiectul dacă nu există o schemă curentă
    🟡 Producție
        Cum se scrie       CREATE DATABASE sales
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este CREATE TABLE, care cere o coloană și face tabelă, nu schemă
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE, care cere o coloană și face tabelă, nu schemă.
    */
    // Începător: createDatabaseLeavesNoTable păstrează propoziția pentru apare schema sales, fără tabela purchase și fără rânduri
    // Producție: public static String createDatabaseLeavesNoTable(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CREATE DATABASE nu este USE și nu copiază tabele.
    public static String createDatabaseLeavesNoTable() {
        // Începător: apare schema sales, fără tabela purchase și fără rânduri
        // Producție: CREATE DATABASE sales. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: CREATE DATABASE nu este USE și nu copiază tabele. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE DATABASE sales";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide createDatabaseLeavesNoTable. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul respinge lista de coloane pe schemă și schema nu apare din acest text
        De ce e nevoie     fără textul greșit, pare că schema ține coloane ca o tabelă
    🟡 Producție
        Cum se scrie       CREATE DATABASE sales (purchase_number INT)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul respinge lista de coloane pe schemă și schema nu apare din acest text
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE, care cere o coloană și face tabelă, nu schemă.
    */
    // Începător: createDatabaseWithAColumn păstrează textul greșit pentru serverul respinge lista de coloane pe schemă și schema nu apare din acest text
    // Producție: public static String createDatabaseWithAColumn(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Coloanele nu stau pe schemă. Obiectul cu coloană este CREATE TABLE.
    public static String createDatabaseWithAColumn() {
        // Începător: serverul respinge lista de coloane pe schemă și schema nu apare din acest text
        // Producție: CREATE DATABASE sales (purchase_number INT). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Coloanele nu stau pe schemă. Obiectul cu coloană este CREATE TABLE. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE DATABASE sales (purchase_number INT)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide createDatabaseWithAColumn. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
