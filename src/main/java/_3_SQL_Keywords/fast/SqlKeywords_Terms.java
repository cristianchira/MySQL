// Începător: pachetul nu schimbă schema. Ține definițiile cuvântului rezervat și ale numelui.
// Producție: package _3_SQL_Keywords.fast. Fără linie, glosarul nu stă lângă lecție.
// Expert: pachetul nu este un identificator citat și nu rezervă un cuvânt pe server.
package _3_SQL_Keywords.fast;

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
       SELECT pentru BEGIN are RESERVED 0, iar lista de etichete tot cere citare.

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

// Începător: clasa ține propozițiile din definiții, câte una pentru fiecare termen.
// Producție: SqlKeywords_Terms. Fără clasă, Exemplu: nu are metodă de arătat.
// Expert: glosarul nu copiază banca de la WHERE sau JOIN. Termenul este cuvântul rezervat.
/*
🟢 Începător
    Ce este            numele pe care parserul îl ia ca sintaxă sau ca identificator
    De ce e nevoie     fără propoziție, definiția nu arată ce rămâne în schemă
🟡 Producție
    Cum se scrie       CREATE TABLE `interval` (begin INT, end INT)
🔴 Staff
    Unde               parserul 9.7, înainte de InnoDB
Pași                   1. Rezervatul necotat cade.
                       2. Citarea sau punctul lasă obiectul.
                       3. BEGIN trece ca coloană și nu trece ca etichetă necotată.
*/
public class SqlKeywords_Terms {

    // Începător: metoda ține propoziția care nu creează tabela.
    // Producție: reservedKeyword întoarce CREATE TABLE interval (begin INT, end INT). Fără metodă, definiția nu are exemplu.
    // Expert: INTERVAL rezervat nu este BEGIN. 1064 este înainte de lacăt.
    /*
    🟢 Începător
        Ce este            cuvântul rezervat în poziția numelui
        De ce e nevoie     fără el, definiția nu arată tabela care lipsește
    🟡 Producție
        Cum se scrie       CREATE TABLE interval (begin INT, end INT)
    🔴 Staff
        Unde               parserul, ERROR 1064 (42000)
    Pași                   1. Vede INTERVAL necotat.
                           2. Oprește propoziția.
                           3. Nu lasă tabelă.
    */
    public static String reservedKeyword() {
        // Începător: nu apare tabela interval.
        // Producție: CREATE TABLE interval (begin INT, end INT) cade cu ERROR 1064 (42000). Fără linie, termenul nu are propoziție.
        // Expert: BEGIN și END de pe linie nu sunt cauza. Nu se ia lacăt de rând.
        return "CREATE TABLE interval (begin INT, end INT)";
        // Începător: închiderea nu șterge o tabelă care nu există.
        // Producție: acolada închide reservedKeyword. Fără ea, exemplul definiției nu compilează.
        // Expert: acolada nu este ROLLBACK. Nu a existat schimbare în InnoDB.
    }

    // Începător: metoda ține numele citat, care lasă o tabelă goală.
    // Producție: identifier întoarce CREATE TABLE `interval` (begin INT, end INT). Fără metodă, identificatorul nu este în cod.
    // Expert: backtick-ul nu este un index și nu umple rânduri.
    /*
    🟢 Începător
        Ce este            cuvântul rezervat scris ca nume
        De ce e nevoie     fără citare, reservedKeyword nu creează obiectul
    🟡 Producție
        Cum se scrie       CREATE TABLE `interval` (begin INT, end INT)
    🔴 Staff
        Unde               identificator citat, apoi 0 rânduri
    Pași                   1. Backtick-ul schimbă rolul cuvântului.
                           2. Tabela apare.
                           3. Are 0 rânduri.
    */
    public static String identifier() {
        // Începător: rămâne o tabelă fără rânduri, cu coloanele begin și end.
        // Producție: CREATE TABLE `interval` (begin INT, end INT) răspunde Query OK, 0 rows affected. Fără linie, definiția de identificator nu are SQL.
        // Expert: citarea nu se moștenește la SELECT. mydb.interval este cealaltă cale, fără backtick.
        return "CREATE TABLE `interval` (begin INT, end INT)";
        // Începător: închiderea nu inserează rândul lipsă.
        // Producție: acolada închide identifier. Fără ea, CREATE citat nu rămâne în glosar.
        // Expert: acolada nu face din backtick o regulă JDBC. Driverul trimite textul așa cum este.
    }

    // Începător: metoda ține coloana begin, cuvânt cheie care nu este rezervat.
    // Producție: nonreservedKeyword întoarce CREATE TABLE sales (purchase_number INT, begin INT). Fără metodă, vecinul nu este în glosar.
    // Expert: același BEGIN, ca etichetă, tot se citează. Nu este INTERVAL.
    /*
    🟢 Începător
        Ce este            keyword care poate fi coloană
        De ce e nevoie     altfel orice cuvânt din listă pare interzis
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, begin INT)
    🔴 Staff
        Unde               BEGIN fără (R); eticheta este lista restricționată
    Pași                   1. sales nu este rezervat.
                           2. begin trece necotat.
                           3. Eticheta cu același nume nu trece necotată.
    */
    public static String nonreservedKeyword() {
        // Începător: apare tabela sales și coloana begin, fără backtick.
        // Producție: CREATE TABLE sales (purchase_number INT, begin INT). Fără linie, definiția nerezervatului nu are propoziție.
        // Expert: END ar trece la fel. Înlocuiești sales cu interval și coloana nu mai apucă să apară.
        return "CREATE TABLE sales (purchase_number INT, begin INT)";
        // Începător: închiderea nu deschide o tranzacție numită begin.
        // Producție: acolada închide nonreservedKeyword. Fără ea, coloana begin nu compilează în glosar.
        // Expert: acolada nu este eticheta care trebuie citată. Este doar sfârșitul metodei.
    }

    // Începător: metoda ține interval după punct, fără citare.
    // Producție: qualifiedName întoarce CREATE TABLE mydb.interval (begin INT, end INT). Fără metodă, excepția punctului nu este în glosar.
    // Expert: scoți mydb. și rămâne 1064. Punctul nu dezactivează alte cuvinte rezervate din propoziție.
    /*
    🟢 Începător
        Ce este            schema și tabela legate prin punct
        De ce e nevoie     fără punct, INTERVAL cere backtick
    🟡 Producție
        Cum se scrie       CREATE TABLE mydb.interval (begin INT, end INT)
    🔴 Staff
        Unde               excepția din secțiunea de identificatori, citată la 11.3
    Pași                   1. mydb este schema.
                           2. Cuvântul de după punct este identificator.
                           3. Tabela apare fără backtick.
    */
    public static String qualifiedName() {
        // Începător: tabela interval apare în mydb.
        // Producție: CREATE TABLE mydb.interval (begin INT, end INT). Fără linie, numele calificat nu poate fi trimis din glosar.
        // Expert: excepția nu acoperă rank necotat pe replică și nu acoperă un interval fără punct.
        return "CREATE TABLE mydb.interval (begin INT, end INT)";
        // Începător: închiderea nu mută tabela în altă schemă.
        // Producție: acolada închide qualifiedName. Fără ea, propoziția cu punct nu rămâne.
        // Expert: acolada nu păstrează punctul dacă textul este rescris fără mydb.
    }

    // Începător: metoda citește steagul lui BEGIN, care este 0, fără să creeze o etichetă.
    // Producție: restrictedKeyword întoarce SELECT WORD, RESERVED FROM INFORMATION_SCHEMA.KEYWORDS WHERE WORD = 'BEGIN'. Fără metodă, restricția nu are citire.
    // Expert: RESERVED 0 nu scutește eticheta. Lista de etichete din 9.7 îl cere citat. SUPER este restricția de rol, nu acest SELECT.
    /*
    🟢 Începător
        Ce este            un keyword nerezervat care tot se citează într-un singur context
        De ce e nevoie     steagul 0 pare libertate totală
    🟡 Producție
        Cum se scrie       SELECT WORD, RESERVED FROM INFORMATION_SCHEMA.KEYWORDS WHERE WORD = 'BEGIN'
    🔴 Staff
        Unde               citire din KEYWORDS; eticheta este altă listă
    Pași                   1. WORD este BEGIN.
                           2. RESERVED este 0.
                           3. Ca etichetă, citarea rămâne obligatorie.
    */
    public static String restrictedKeyword() {
        // Începător: întrebarea nu creează o procedură și nu adaugă o coloană. Întoarce cuvântul și 0.
        // Producție: SELECT WORD, RESERVED FROM INFORMATION_SCHEMA.KEYWORDS WHERE WORD = 'BEGIN'. Fără linie, nu arăți că BEGIN nu este rezervat.
        // Expert: rezultatul nu spune nimic despre etichetă. BEGIN în beginColumnOnSales este coloană. Ca etichetă, manualul cere citare. Nu inventa aici textul procedurii.
        return "SELECT WORD, RESERVED FROM INFORMATION_SCHEMA.KEYWORDS WHERE WORD = 'BEGIN'";
        // Începător: închiderea nu scrie o etichetă în server.
        // Producție: acolada închide restrictedKeyword. Fără ea, SELECT-ul pe BEGIN nu rămâne.
        // Expert: acolada nu citează eticheta. Citarea stă în programul stocat, nu în această metodă.
    }

    // Începător: metoda ține CREATE-ul care lasă tabela goală.
    // Producție: tableName întoarce CREATE TABLE `interval` (begin INT, end INT). Fără metodă, definiția tabelei nu are obiectul care apare.
    // Expert: forma necotată nu lasă tabelă. 0 rows affected nu este un lacăt.
    /*
    🟢 Începător
        Ce este            obiectul care apare doar dacă numele trece
        De ce e nevoie     fără forma citată, rămâne doar eșecul
    🟡 Producție
        Cum se scrie       CREATE TABLE `interval` (begin INT, end INT)
    🔴 Staff
        Unde               după parser, CREATE, 0 rânduri
    Pași                   1. Numele citat trece.
                           2. Tabela există.
                           3. Nu are rânduri.
    */
    public static String tableName() {
        // Începător: rămâne tabela interval, goală.
        // Producție: CREATE TABLE `interval` (begin INT, end INT). Fără linie, definiția de tabelă nu arată Query OK, 0 rows affected.
        // Expert: nu se construiește un index secundar prin citare. Indexul grupat, dacă apare, este al tabelei create, nu al eșecului 1064.
        return "CREATE TABLE `interval` (begin INT, end INT)";
        // Începător: închiderea nu umple tabela.
        // Producție: acolada închide tableName. Fără ea, CREATE citat nu rămâne la termenul tabelă.
        // Expert: acolada nu este DROP. Tabela există doar dacă serverul a primit propoziția.
    }

    // Începător: metoda încearcă coloana rank, care este rezervată.
    // Producție: columnName întoarce ALTER TABLE sales ADD COLUMN rank INT. Fără metodă, definiția de coloană nu arată eșecul.
    // Expert: RANK rezervat din 8.0. begin de pe sales nu este acest cuvânt. Replica dă 1064 înainte de filtru.
    /*
    🟢 Începător
        Ce este            un nume de coloană pe care parserul nu îl acceptă necotat
        De ce e nevoie     begin trece și dă impresia că orice coloană trece
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD COLUMN rank INT
    🔴 Staff
        Unde               RANK (R), poziție de coloană, înainte de filtru de replicare
    Pași                   1. Parserul vede rank necotat.
                           2. 1064.
                           3. Coloana nu apare.
    */
    public static String columnName() {
        // Începător: coloana rank nu se adaugă la sales.
        // Producție: ALTER TABLE sales ADD COLUMN rank INT cade cu ERROR 1064 (42000). Fără linie, termenul coloană nu are rezervatul.
        // Expert: ADD COLUMN `rank` este forma care trece. Filtrul replicate-ignore-table nu repară textul necotat.
        return "ALTER TABLE sales ADD COLUMN rank INT";
        // Începător: închiderea nu adaugă coloana ratată.
        // Producție: acolada închide columnName. Fără ea, ALTER cu rank necotat nu rămâne în glosar.
        // Expert: acolada nu citează rank. Fără backtick în text, propoziția rămâne cea care cade.
    }
}
