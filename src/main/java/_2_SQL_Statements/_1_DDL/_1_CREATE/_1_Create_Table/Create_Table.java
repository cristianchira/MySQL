package _2_SQL_Statements._1_DDL._1_CREATE._1_Create_Table;

//Martin G. Create Table
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339058#questions

/*                                          Create Table



 */

/*
TOPIC:
    CREATE TABLE lasă pe disc definiția unei tabele noi; până nu vine un INSERT, tabela nu are rânduri.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să apară obiectul sales cu o coloană, nu un rând de vânzare. După propoziție, purchase_number există ca definiție și nu are valori.
MECHANISM:
    Serverul execută CREATE TABLE la sfârșitul propoziției și schimbă schema. În InnoDB, DDL atomic fie se încheie, fie nu lasă tabela pe jumătate.
LOOKALIKES:
    INSERT INTO sales adaugă rânduri într-o tabelă care există deja. CREATE DATABASE face o schemă, nu o tabelă.
BOOK THEORY:
    Manualul 9.7, pagina CREATE TABLE, deschisă în tur: propoziția creează o tabelă. PDF-ul Creating_Tables.pdf din folder a fost deschis: primele pagini sunt titlul, fără efectul pe rânduri. Efectul de rânduri goale este consecința că CREATE TABLE nu este INSERT.
BOOK EXAMPLE:
    createSalesLeavesNoRows
REPO DEMO:
    src/main/java/_2_SQL_Statements/_1_DDL/_1_CREATE/_1_Create_Table/Create_Table.java
BREAK LAB:
    createWithoutAColumn ține CREATE TABLE sales (). Serverul nu creează sales. Niciun rând nu apare, pentru că nici obiectul nu apare.
TRADE-OFFS:
    CREATE DATABASE când îți lipsește schema, nu tabela. INSERT când tabela există și vrei un rând.
ANTI-PATTERNS:
    CREATE TABLE sales () nu lasă obiectul. Un INSERT trimis înainte de CREATE nu are tabelă.
STAFF QUESTIONS:
    createSalesLeavesNoRows și createWithoutAColumn. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un registru de loturi de vaccin are nevoie de tabelă înainte de primul flacon. CREATE TABLE lots (lot_no INT) lasă lot_no fără flacoane. Flaconul vine cu INSERT.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE sales (purchase_number INT)?
   Capcană: Propoziția și salvează prima vânzare, fiindcă ai numit purchase_number.
   Cheie: createSalesLeavesNoRows creează definiția. purchase_number nu are încă o valoare. INSERT este propoziția care ar pune rândul.
   Demonstrație: createSalesLeavesNoRows
2. Enunț: Ce rămâne în schemă după ce serverul termină CREATE TABLE?
   Capcană: Tabela primește un rând NULL ca să ocupe coloana.
   Cheie: Rămâne obiectul sales, cu coloana INT, și zero rânduri. Efectul este la sfârșitul propoziției. Nu se deschide o tranzacție de DML pe care să o dai înapoi ca pe un INSERT.
   Demonstrație: createSalesLeavesNoRows
3. Enunț: De ce nu este INSERT INTO sales?
   Capcană: Ambele adaugă purchase_number în tabelă.
   Cheie: INSERT cere tabela gata și scrie un rând. CREATE TABLE scrie definiția. Fără tabelă, INSERT nu are destinație.
   Demonstrație: createSalesLeavesNoRows
4. Enunț: Cum eșuează CREATE TABLE sales ()?
   Capcană: Paranteza goală creează tabela și așteaptă coloanele la primul INSERT.
   Cheie: createWithoutAColumn nu are nicio coloană. Serverul nu lasă sales. Rânduri nu există, pentru că nu există nici tabela.
   Demonstrație: createWithoutAColumn
5. Enunț: Când creezi schema și când inserezi rândul?
   Capcană: Orice nume nou trebuie să treacă prin INSERT, altfel nu este în bază.
   Cheie: CREATE TABLE când lipsește obiectul. INSERT când obiectul există și vrei o valoare. CREATE DATABASE când lipsește schema în care stă tabela.
   Demonstrație: createSalesLeavesNoRows

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei obiectul sales, încă fără vânzări -> createSalesLeavesNoRows -> CREATE TABLE sales (purchase_number INT) -> nu garantează un rând, un index sau că numele sales era liber
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> createWithoutAColumn -> CREATE TABLE sales () -> Serverul nu creează sales. Niciun rând nu apare, pentru că nici obiectul nu apare.
    3. Granița
           -> CREATE TABLE este DDL pe server. Comentariul de curs și click-ul de create din Workbench nu creează tabela.
    4. Alternativa
           -> INSERT INTO sales adaugă rânduri într-o tabelă care există deja. CREATE DATABASE face o schemă, nu o tabelă.
    5. Anti-patternul
           -> CREATE TABLE sales () nu lasă obiectul. Un INSERT trimis înainte de CREATE nu are tabelă.
    6. Dovada
           -> createSalesLeavesNoRows ține propoziția. createWithoutAColumn ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> CREATE TABLE sales (purchase_number INT) face schema și lasă zero rânduri. INSERT ar fi adăugat rândul. Fără coloană, serverul nu creează tabela.
*/

public class Create_Table {
    /*
    🟢 Începător
        Ce este            apare tabela sales cu coloana purchase_number și zero rânduri
        De ce e nevoie     fără ea, un INSERT INTO sales nu are unde să scrie purchase_number
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este INSERT, care cere tabela deja creată și adaugă rânduri
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu INSERT, care cere tabela deja creată și adaugă rânduri.
    */
    // Începător: createSalesLeavesNoRows păstrează propoziția pentru apare tabela sales cu coloana purchase_number și zero rânduri
    // Producție: public static String createSalesLeavesNoRows(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CREATE TABLE nu filtrează și nu este un index. Nu ține loc de PRIMARY KEY dacă nu ai scris constrângerea.
    public static String createSalesLeavesNoRows() {
        // Începător: apare tabela sales cu coloana purchase_number și zero rânduri
        // Producție: CREATE TABLE sales (purchase_number INT). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: CREATE TABLE nu filtrează și nu este un index. Nu ține loc de PRIMARY KEY dacă nu ai scris constrângerea. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sales (purchase_number INT)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide createSalesLeavesNoRows. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            serverul respinge tabela fără nicio coloană și schema rămâne fără sales
        De ce e nevoie     fără textul greșit, pare că o tabelă poate exista fără coloană
    🟡 Producție
        Cum se scrie       CREATE TABLE sales ()
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care serverul respinge tabela fără nicio coloană și schema rămâne fără sales
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu INSERT, care cere tabela deja creată și adaugă rânduri.
    */
    // Începător: createWithoutAColumn păstrează textul greșit pentru serverul respinge tabela fără nicio coloană și schema rămâne fără sales
    // Producție: public static String createWithoutAColumn(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Serverul nu creează sales. Niciun rând nu apare, pentru că nici obiectul nu apare.
    public static String createWithoutAColumn() {
        // Începător: serverul respinge tabela fără nicio coloană și schema rămâne fără sales
        // Producție: CREATE TABLE sales (). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Serverul nu creează sales. Niciun rând nu apare, pentru că nici obiectul nu apare. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sales ()";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide createWithoutAColumn. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
