// Începător: pachetul nu schimbă rânduri. Ține lecția care arată cum citești un cuvânt rezervat.
// Producție: package _3_SQL_Keywords.fast. Fără linie, clasa nu mai stă lângă SQL_Keywords.
// Expert: pachetul nu este un rând din INFORMATION_SCHEMA.KEYWORDS și nu rezervă INTERVAL pe server.
package _3_SQL_Keywords.fast;

/*
TOPIC:
    Cuvântul rezervat necotat, pus în locul numelui, este sintaxă: parserul dă ERROR 1064 și nu creează tabela. Același cuvânt, ca ALTER sau ADD în poziție de propoziție, schimbă schema.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0
INTENT:
    Să alegi propoziția după cuvânt: nume citat lasă o tabelă goală, nume necotat nu lasă nimic, ADD adaugă o coloană.
MECHANISM:
    Parserul decide înainte de InnoDB. 1064 nu ia lacăt și nu construiește index.
LOOKALIKES:
    BEGIN este cuvânt cheie nerezervat și poate fi coloană. ALTER ca nume de tabelă cade; ALTER TABLE ... ADD COLUMN adaugă coloana.
BOOK THEORY:
    Manualul 9.7: INTERVAL rezervat necotat dă 1064; `interval` lasă 0 rânduri; mydb.interval nu se citează; BEGIN și END nu sunt rezervate. PDF-ul de curs arată și CREATE TABLE alter și ALTER TABLE sales ADD COLUMN date_of_purchase DATE, dar spune prea larg că niciun keyword nu poate fi nume.
BOOK EXAMPLE:
    SqlKeywords_Fast.main
REPO DEMO:
    src/main/java/_3_SQL_Keywords/fast/SqlKeywords_Fast.java
BREAK LAB:
    CREATE TABLE interval (begin INT, end INT) — ERROR 1064, nicio tabelă
TRADE-OFFS:
    Redenumești în aplicație când poți. Citezi când domeniul cere cuvântul rezervat.
ANTI-PATTERNS:
    CREATE TABLE alter (purchase_number INT) nu adaugă date_of_purchase.
STAFF QUESTIONS:
    SQL_Keywords.rejectedInterval, quotedInterval, addPurchaseDate
NOVEL TRANSFER:
    Tabela de comenzi nu se numește order necotat. ORDER este rezervat.
SPACED REVIEW:
    o zi: interval față de `interval`. șapte zile: 1064 fără lacăt. treizeci de zile: ADD este sintaxă, nu nume.
*/

/*
Definiția unui cuvânt cheie rezervat (reserved keyword):
       Cuvântul rezervat este citit ca sintaxă. Necotat, în locul unui identificator, propoziția cade cu ERROR 1064 și obiectul nu apare.

Exemplu:
       SqlKeywords_Terms.reservedKeyword()
       CREATE TABLE interval (begin INT, end INT) nu lasă o tabelă.

Definiția unui identificator (identifier):
       Identificatorul este numele obiectului. Un cuvânt rezervat devine identificator între backtick-uri, sau după punct într-un nume calificat.

Exemplu:
       SqlKeywords_Terms.identifier()
       CREATE TABLE `interval` (begin INT, end INT) lasă o tabelă cu 0 rânduri.

Definiția unui cuvânt cheie nerezervat (nonreserved keyword):
       Cuvântul cheie nerezervat are rol în sintaxă și poate fi identificator fără citare. BEGIN și END sunt în acest grup în MySQL 9.7.

Exemplu:
       SqlKeywords_Terms.nonreservedKeyword()
       Coloana begin din CREATE TABLE sales (purchase_number INT, begin INT) nu cere backtick.

Definiția unui nume calificat (qualified name):
       Numele calificat leagă obiectul de schemă cu un punct. Cuvântul de după punct este identificator, chiar dacă este rezervat, și nu se citează.

Exemplu:
       SqlKeywords_Terms.qualifiedName()
       CREATE TABLE mydb.interval (begin INT, end INT) trece fără backtick pe interval.

Definiția unui cuvânt cheie restricționat (restricted keyword):
       Unele cuvinte nerezervate trebuie citate doar într-un context: etichetă de program stocat, nume de rol, sau ambele. BEGIN se citează ca etichetă, nu ca coloană.

Exemplu:
       SqlKeywords_Terms.restrictedKeyword()
       BEGIN are RESERVED 0 și tot trebuie citat când este etichetă.

Definiția unei tabele (table):
       Tabela apare doar dacă numele trece de parser. INTERVAL necotat nu lasă tabelă. `interval` lasă o tabelă goală.

Exemplu:
       SqlKeywords_Terms.tableName()
       Query OK, 0 rows affected pe CREATE TABLE `interval`.

Definiția unei coloane (column):
       Coloana este identificatorul din lista tabelului. begin poate fi coloană necotată. rank necotat nu poate, pentru că RANK este rezervat.

Exemplu:
       SqlKeywords_Terms.columnName()
       ALTER TABLE sales ADD COLUMN rank INT cade cu 1064 și coloana nu apare.
*/

// Începător: clasa ține trei propoziții: numele respins, numele citat și cuvântul care adaugă o coloană.
// Producție: main tipărește CREATE TABLE interval, CREATE TABLE `interval` și ALTER TABLE sales ADD COLUMN date_of_purchase DATE.
// Expert: main nu acoperă etichetele și rolurile. BEGIN nerezervat stă în SQL_Keywords.beginColumnOnSales, nu în aceste trei linii.
/*
🟢 Începător
    Ce este            citirea unui cuvânt: sintaxă rezervată sau nume de tabelă
    De ce e nevoie     fără propoziții, lista de keywords rămâne o enumerare
🟡 Producție
    Cum se scrie       CREATE TABLE `interval` (begin INT, end INT)
🔴 Staff
    Unde               parserul, înainte de InnoDB
Pași                   1. INTERVAL necotat cade.
                       2. Backtick-ul lasă o tabelă goală.
                       3. ADD, ca sintaxă, adaugă date_of_purchase.
*/
public class SqlKeywords_Fast {
    // Începător: main arată cele trei propoziții și nu le trimite el la un server.
    // Producție: trei System.out.println. Fără main, lecția scurtă nu are intrare.
    // Expert: main nu deschide Connector/J. Lipsa rulării nu transformă 1064 într-un rezultat inventat.
    /*
    🟢 Începător
        Ce este            exemplul minim: eșec, citare, coloană adăugată
        De ce e nevoie     fără main nu rămâne o propoziție gata de citit
    🟡 Producție
        Cum se scrie       trei propoziții tipărite, nu un script JDBC
    🔴 Staff
        Unde               consola, nu motorul
    Pași                   1. Tipărește forma respinsă.
                           2. Tipărește forma citată.
                           3. Tipărește ADD COLUMN.
    */
    public static void main(String[] args) {
        // Începător: propoziția nu creează tabela interval.
        // Producție: CREATE TABLE interval (begin INT, end INT) cade cu ERROR 1064 (42000). Fără linie, eșecul dispare din lecție.
        // Expert: BEGIN și END nu sunt motivul eșecului. INTERVAL este rezervat. Linia nu ia lacăt, pentru că nu ajunge la InnoDB.
        System.out.println("CREATE TABLE interval (begin INT, end INT)");
        // Începător: rămâne o tabelă goală, cu coloanele begin și end, dacă serverul primește propoziția.
        // Producție: CREATE TABLE `interval` (begin INT, end INT) răspunde Query OK, 0 rows affected. Fără backtick, este linia de deasupra.
        // Expert: citarea nu umple rânduri și nu se moștenește la SELECT. Numele calificat mydb.interval nici nu cere backtick.
        System.out.println("CREATE TABLE `interval` (begin INT, end INT)");
        // Începător: sales primește coloana date_of_purchase. Rândurile care existau rămân, fără un rând nou.
        // Producție: ALTER TABLE sales ADD COLUMN date_of_purchase DATE. ADD și ALTER sunt rezervate și aici sunt sintaxă. Fără linie, lecția nu arată schimbarea de schemă.
        // Expert: CREATE TABLE alter (purchase_number INT) folosește ALTER ca nume și cade. Nu este același loc în propoziție cu ADD COLUMN.
        System.out.println("ALTER TABLE sales ADD COLUMN date_of_purchase DATE");
        // Începător: închiderea lui main nu anulează o tabelă, pentru că main nu a executat SQL-ul.
        // Producție: acolada închide main. Fără ea, cele trei propoziții nu rămân împreună în clasă.
        // Expert: acolada nu este COMMIT și nu este ROLLBACK. Efectul asupra schemei apare doar când un server primește textul.
    }
    // Începător: închiderea clasei nu scoate cuvintele din lista serverului.
    // Producție: acolada închide SqlKeywords_Fast. Fără ea, lecția nu compilează pe Java 27.
    // Expert: clasa nu înlocuiește INFORMATION_SCHEMA.KEYWORDS. Cine este rezervat depinde de 9.7, nu de fișierul Java.
}
