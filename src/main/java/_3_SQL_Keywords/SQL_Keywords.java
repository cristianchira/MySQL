package _3_SQL_Keywords;
// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338034#questions

/*
TOPIC:
    Cuvântul rezervat necotat, pus în locul unui identificator, este sintaxă: parserul oprește propoziția cu ERROR 1064 și nu creează obiectul.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0
INTENT:
    Numele de tabelă sau de coloană se lovește de un cuvânt pe care parserul îl vrea pentru sine. După 1064 schema rămâne neschimbată. După citare rămâne obiectul, cu 0 rânduri dacă propoziția era CREATE.
MECHANISM:
    Parserul serverului citește cuvântul înainte de InnoDB. Rezervatul necotat nu ajunge să ia lacăt și nu construiește index. Efectul se vede la parsare, nu la commit.
LOOKALIKES:
    BEGIN este cuvânt cheie, dar nu este rezervat: poate fi coloană fără backtick. ALTER în CREATE TABLE alter este nume și cade; ALTER TABLE ... ADD COLUMN este același cuvânt ca sintaxă și adaugă o coloană.
BOOK THEORY:
    Manualul 9.7, secțiunea Keywords and Reserved Words: cuvântul rezervat (INTERVAL, ALTER, ADD, CREATE, RANK, LIBRARY, ORDER, marcate (R); și _FILENAME) nu poate fi identificator necotat. CREATE TABLE interval (begin INT, end INT) dă ERROR 1064 (42000) și nu creează tabela. CREATE TABLE `interval` (begin INT, end INT) răspunde Query OK, 0 rows affected. BEGIN și END nu sunt rezervate și stau necotate ca coloane. După punct, mydb.interval nu se citează. INFORMATION_SCHEMA.KEYWORDS are WORD și RESERVED, 1 sau 0. Unele cuvinte nerezervate se citează doar ca etichetă (BEGIN, TRUNCATE, COMMIT) sau ca rol (SUPER). MANUAL și PARALLEL au devenit nerezervate în 9.7.2. Între 8.4 și 9.7 nu s-a scos niciun cuvânt; LIBRARY și EXTERNAL au devenit rezervate. PDF-ul de curs spune că un keyword nu poate numi un obiect și arată CREATE TABLE alter (purchase_number INT) și ALTER TABLE sales ADD COLUMN date_of_purchase DATE. Afirmația din PDF este mai largă decât manualul: doar rezervatul necotat este respins.
BOOK EXAMPLE:
    SQL_Keywords.rejectedInterval, quotedInterval, beginColumnOnSales, alterAsTableName, qualifiedInterval, addPurchaseDate
REPO DEMO:
    src/main/java/_3_SQL_Keywords/SQL_Keywords.java
BREAK LAB:
    CREATE TABLE interval (begin INT, end INT) — ERROR 1064 (42000), nicio tabelă nouă
TRADE-OFFS:
    Redenumești în aplicație când domeniul nu cere cuvântul. Citezi cu backtick când numele trebuie să rămână interval, order sau rank. Califici mydb.interval doar cât punctul rămâne în propoziție.
ANTI-PATTERNS:
    CREATE TABLE alter (purchase_number INT) pune ALTER în poziția numelui. Nu apare tabela și nu se adaugă date_of_purchase.
STAFF QUESTIONS:
    rejectedInterval, quotedInterval, beginColumnOnSales, alterAsTableName, qualifiedInterval, addPurchaseDate, reservedFlag, unquotedRank, quotedRank, libraryTable, filenameTable, selectFromQuotedInterval, manualTable, countColumn, countWithSpace, truncateAsName, commitAsName, unquotedOrder, quotedOrder, grantAsColumn
NOVEL TRANSFER:
    O tabelă de comenzi nu se numește order necotat. ORDER este rezervat, aceeași 1064 ca interval, pe un domeniu care nu este demo-ul sales.
SPACED REVIEW:
    o zi: INTERVAL rezervat față de BEGIN nerezervat, pe o propoziție. șapte zile: 1064 la parsare, fără lacăt și fără index. treizeci de zile: pe order, rank sau library alegi citare, calificare sau redenumire.
*/

/*
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE -> SQL_Keywords.quotedInterval -> CREATE TABLE `interval` (begin INT, end INT) lasă o tabelă cu 0 rânduri -> nu garantează că SELECT-ul următor poate scrie FROM interval fără backtick
    2. Eșecul cel mai mic
           -> DECIDE -> SQL_Keywords.rejectedInterval -> CREATE TABLE interval (begin INT, end INT) -> ERROR 1064 (42000), nicio tabelă -> nu garantează un text de eroare care spune reserved word
    3. Granița
           -> cuvântul rezervat este regula parserului MySQL. JDBC și Connector/J doar trimit textul. Click-ul din Workbench nu schimbă RESERVED. Numele de pachet Java nu este în INFORMATION_SCHEMA.KEYWORDS.
    4. Alternativa
           -> BEGIN este nerezervat: SQL_Keywords.beginColumnOnSales trece fără citare pe begin. Schimbi alegerea când RESERVED este 0. mydb.interval, în qualifiedInterval, nu cere backtick cât timp punctul rămâne.
    5. Anti-patternul
           -> SQL_Keywords.alterAsTableName: CREATE TABLE alter (purchase_number INT) citește ALTER ca sintaxă, dă 1064 și nu adaugă date_of_purchase. Coloana se adaugă doar cu addPurchaseDate.
    6. Dovada
           -> rejectedInterval, quotedInterval și addPurchaseDate țin propozițiile. Rularea lor pe un server este NERULAT AICI: în repo nu este o conexiune configurată.
    7. Interviu
           -> INTERVAL necotat nu creează tabela, pentru că 1064 apare la parsare. Forma cu backtick creează o tabelă goală. BEGIN pe coloană nu este același caz.
*/

/*
1. Ce problemă rezolvă mecanismul
   Enunț: De ce nu poți numi tabela interval dacă scrii cuvântul exact ca în propoziție, fără semne în jur?
   Capcană: Orice cuvânt din lista de keywords este interzis ca nume, inclusiv BEGIN.
   Cheie: INTERVAL este rezervat. Necotat, parserul îl ia ca sintaxă, dă ERROR 1064 (42000) și nu creează tabela. BEGIN nu este rezervat și poate fi coloană.
   Demonstrație: SQL_Keywords.rejectedInterval
2. Ce se întâmplă cu rândurile, schema, indexul sau tranzacția
   Enunț: Ce rămâne după CREATE TABLE `interval` (begin INT, end INT) și ce nu se întâmplă la forma fără backtick?
   Capcană: ERROR 1064 înseamnă că InnoDB a refuzat rândul și a ținut lacătul.
   Cheie: Fără backtick nu există tabelă, nici index, nici lacăt: eșecul este la parsare. Cu backtick rămâne o tabelă cu 0 rânduri. Citarea nu face COMMIT pe o tranzacție de rânduri.
   Demonstrație: SQL_Keywords.quotedInterval
3. De ce nu este mecanismul vecin
   Enunț: BEGIN deschide un bloc în procedură. De ce begin poate fi totuși coloană necotată?
   Capcană: BEGIN este rezervat pentru tranzacție, deci coloana trebuie citată la fel ca INTERVAL.
   Cheie: BEGIN este cuvânt cheie nerezervat. Ca coloană în CREATE TABLE sales (purchase_number INT, begin INT) nu cere backtick. Ca etichetă de program stocat, lista restricționată din 9.7 cere citare. INTERVAL rămâne rezervat și ca nume de tabelă.
   Demonstrație: SQL_Keywords.beginColumnOnSales
4. Cum eșuează când este folosit greșit
   Enunț: Ce lasă în schemă CREATE TABLE alter (purchase_number INT)?
   Capcană: CREATE TABLE alter creează tabela, iar un ALTER de după îi adaugă coloana.
   Cheie: ALTER este rezervat. În poziția numelui, propoziția cade cu 1064 și tabela alter nu apare. ALTER TABLE sales ADD COLUMN date_of_purchase DATE este același cuvânt ca sintaxă și adaugă coloana, fără un rând nou.
   Demonstrație: SQL_Keywords.alterAsTableName
5. Ce alegi în locul lui și în ce condiție
   Enunț: Numele trebuie să rămână un cuvânt rezervat. Citezi, califici sau redenumești în aplicație?
   Capcană: Pui ghilimele duble peste tot, sau scoți mydb. și te aștepți ca interval să rămână legal.
   Cheie: Backtick-ul face identificator din INTERVAL când stă singur. mydb.interval nu cere citare doar cât există punctul. Dacă domeniul poate schimba numele, aplicația nu trimite rezervatul: verifică RESERVED în INFORMATION_SCHEMA.KEYWORDS înainte de DDL. Filtrul de replicare nu repară un rank necotat, pentru că replica parsează evenimentul înainte de filtru.
   Demonstrație: SQL_Keywords.qualifiedInterval
*/

/*
🟢 Începător
    Ce este            cuvântul rezervat, citit ca sintaxă atunci când stă necotat în locul numelui
    De ce e nevoie     fără citare sau fără alt nume, CREATE nu lasă tabela
🟡 Producție
    Cum se scrie       CREATE TABLE `interval` (begin INT, end INT)
🔴 Staff
    Unde               parserul, înainte de lacăt, index și InnoDB
Pași                   1. Vezi dacă RESERVED este 1.
                       2. Necotat, aștepți 1064 și zero obiecte.
                       3. Cu backtick, sau după punct, numele trece și CREATE lasă 0 rânduri.
*/
public class SQL_Keywords {

    // Începător: metoda ține propoziția care nu creează tabela interval.
    // Producție: rejectedInterval întoarce CREATE TABLE interval (begin INT, end INT). Fără metodă, eșecul 1064 nu stă în cod.
    // Expert: metoda nu este mecanismul. Mecanismul este că INTERVAL rezervat oprește parsarea înainte de motor.
    /*
    🟢 Începător
        Ce este            INTERVAL necotat în poziția numelui de tabelă
        De ce e nevoie     fără propoziție, 1064 rămâne o poveste
    🟡 Producție
        Cum se scrie       CREATE TABLE interval (begin INT, end INT)
    🔴 Staff
        Unde               parserul, ERROR 1064 (42000)
    Pași                   1. Parserul vede INTERVAL rezervat, necotat.
                           2. Oprește propoziția.
                           3. Nu rămâne o tabelă nouă.
    */
    public static String rejectedInterval() {
        // Începător: serverul nu creează tabela, pentru că interval este citit ca sintaxă.
        // Producție: CREATE TABLE interval (begin INT, end INT) cade cu ERROR 1064 (42000). Fără linie, nu există propoziție de trimis.
        // Expert: BEGIN și END de pe aceeași linie nu sunt rezervate. Eșecul este INTERVAL, la parsare, fără lacăt de rând.
        return "CREATE TABLE interval (begin INT, end INT)";
        // Începător: închiderea nu șterge rânduri, pentru că nicio tabelă nu a fost creată.
        // Producție: acolada închide rejectedInterval. Fără ea, propoziția cu INTERVAL necotat nu mai compilează.
        // Expert: acolada nu este ROLLBACK. Parsarea nu a deschis o schimbare în InnoDB.
    }

    // Începător: metoda ține propoziția care lasă o tabelă goală.
    // Producție: quotedInterval întoarce CREATE TABLE `interval` (begin INT, end INT). Fără metodă, forma care trece nu este în repo.
    // Expert: backtick-ul nu umple rânduri. 0 rows affected este schema creată, nu un filtru WHERE.
    /*
    🟢 Începător
        Ce este            același nume, scris ca identificator
        De ce e nevoie     fără citare, forma de deasupra nu creează nimic
    🟡 Producție
        Cum se scrie       CREATE TABLE `interval` (begin INT, end INT)
    🔴 Staff
        Unde               parserul acceptă identificatorul, apoi CREATE lasă 0 rânduri
    Pași                   1. Backtick-ul scoate INTERVAL din sintaxă.
                           2. Tabela apare.
                           3. Nu conține rânduri.
    */
    public static String quotedInterval() {
        // Începător: rămâne o tabelă fără rânduri, cu coloanele begin și end.
        // Producție: CREATE TABLE `interval` (begin INT, end INT) răspunde Query OK, 0 rows affected. Fără backtick, este rejectedInterval.
        // Expert: citarea nu se moștenește la propoziția următoare. SELECT din interval necotat cade din nou cu 1064. mydb.interval nu are nevoie de backtick.
        return "CREATE TABLE `interval` (begin INT, end INT)";
        // Începător: închiderea nu inserează rândul care lipsește din tabela nouă.
        // Producție: acolada închide quotedInterval. Fără ea, forma citată nu rămâne în clasă.
        // Expert: acolada nu face COMMIT. Durabilitatea tabelei goale este a propoziției DDL, nu a acoladei Java.
    }

    // Începător: metoda ține o coloană begin pe tabela sales, fără citare.
    // Producție: beginColumnOnSales întoarce CREATE TABLE sales (purchase_number INT, begin INT). Fără metodă, vecinul nerezervat nu este în cod.
    // Expert: BEGIN nerezervat nu se confundă cu eticheta de program, care tot trebuie citată.
    /*
    🟢 Începător
        Ce este            un cuvânt cheie care poate fi coloană
        De ce e nevoie     fără el, orice keyword pare rezervat
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT, begin INT)
    🔴 Staff
        Unde               lista 9.7: BEGIN fără (R); lista de etichete îl cere citat
    Pași                   1. Numele tabelei sales nu este rezervat.
                           2. Coloana begin trece necotată.
                           3. Aceeași literă, ca etichetă, nu trece necotată.
    */
    public static String beginColumnOnSales() {
        // Începător: apare tabela sales, iar begin este o coloană de numere, nu un bloc de procedură.
        // Producție: CREATE TABLE sales (purchase_number INT, begin INT). Fără linie, PDF-ul sales și manualul BEGIN nu stau în aceeași propoziție.
        // Expert: END ar fi la fel de necotat. INTERVAL în locul lui sales cade înainte să se vadă coloana. Ca etichetă, BEGIN este în lista restricționată.
        return "CREATE TABLE sales (purchase_number INT, begin INT)";
        // Începător: închiderea nu transformă coloana begin într-o tranzacție.
        // Producție: acolada închide beginColumnOnSales. Fără ea, vecinul nerezervat dispare din compilare.
        // Expert: acolada nu deschide START TRANSACTION. BEGIN aici este nume de coloană.
    }

    // Începător: metoda ține numele alter pus unde parserul așteaptă o tabelă.
    // Producție: alterAsTableName întoarce CREATE TABLE alter (purchase_number INT). Fără metodă, exemplul din PDF nu este propoziție.
    // Expert: ALTER rezervat ca nume nu adaugă date_of_purchase. Asta o face doar ADD în poziție de sintaxă.
    /*
    🟢 Începător
        Ce este            cuvântul ALTER folosit ca nume, nu ca schimbare de schemă
        De ce e nevoie     cursul arată forma aceasta și o lasă neexplicată față de ADD
    🟡 Producție
        Cum se scrie       CREATE TABLE alter (purchase_number INT)
    🔴 Staff
        Unde               ALTER este (R) în 9.7
    Pași                   1. Parserul vede ALTER rezervat.
                           2. 1064, tabela nu apare.
                           3. Coloana date_of_purchase nu este adăugată.
    */
    public static String alterAsTableName() {
        // Începător: nu apare nicio tabelă numită alter și purchase_number nu ajunge într-o coloană.
        // Producție: CREATE TABLE alter (purchase_number INT) cade cu ERROR 1064 (42000). Fără linie, anti-patternul din PDF nu este executabil.
        // Expert: ALTER TABLE sales ADD COLUMN date_of_purchase DATE folosește aceleași cuvinte rezervate ca sintaxă și schimbă schema. Poziția în propoziție le separă.
        return "CREATE TABLE alter (purchase_number INT)";
        // Începător: închiderea nu repară numele și nu creează tabela ratată.
        // Producție: acolada închide alterAsTableName. Fără ea, CREATE cu ALTER necotat nu compilează în clasă.
        // Expert: acolada nu emite ADD COLUMN. Schema sales rămâne neatinsă de această metodă.
    }

    // Începător: metoda ține interval după un punct, fără backtick.
    // Producție: qualifiedInterval întoarce CREATE TABLE mydb.interval (begin INT, end INT). Fără metodă, excepția din manual nu este în cod.
    // Expert: punctul obligă identificatorul. Scos mydb., rămâne rejectedInterval.
    /*
    🟢 Începător
        Ce este            numele calificat, în care cuvântul rezervat stă după punct
        De ce e nevoie     fără punct, același cuvânt cere backtick
    🟡 Producție
        Cum se scrie       CREATE TABLE mydb.interval (begin INT, end INT)
    🔴 Staff
        Unde               excepția din Schema Object Names, citată de secțiunea 11.3
    Pași                   1. mydb este schema.
                           2. interval de după punct este identificator.
                           3. Tabela apare fără backtick.
    */
    public static String qualifiedInterval() {
        // Începător: tabela interval apare în mydb, fără semne în jurul numelui.
        // Producție: CREATE TABLE mydb.interval (begin INT, end INT). Fără linie, excepția „după punct nu se citează” nu este propoziție.
        // Expert: excepția nu iartă un interval necotat în afara punctului și nu iartă rank pe o replică. Filtrul replicate-ignore nu sare peste parsare.
        return "CREATE TABLE mydb.interval (begin INT, end INT)";
        // Începător: închiderea nu mută tabela în altă schemă.
        // Producție: acolada închide qualifiedInterval. Fără ea, numele calificat nu rămâne în fișier.
        // Expert: acolada nu păstrează punctul dacă cineva rescrie propoziția fără mydb.
    }

    // Începător: metoda ține propoziția care adaugă o coloană, nu un rând.
    // Producție: addPurchaseDate întoarce ALTER TABLE sales ADD COLUMN date_of_purchase DATE. Fără metodă, cuvântul rezervat ca sintaxă nu este în cod.
    // Expert: ADD aici nu este numele unei tabele. CREATE TABLE alter este celălalt loc.
    /*
    🟢 Începător
        Ce este            ALTER și ADD folosite ca să schimbe schema
        De ce e nevoie     fără ele, cuvântul rezervat pare doar o interdicție de nume
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD COLUMN date_of_purchase DATE
    🔴 Staff
        Unde               ambele cuvinte sunt (R), în poziție de propoziție
    Pași                   1. ALTER spune că obiectul există și se schimbă.
                           2. ADD COLUMN adaugă date_of_purchase.
                           3. Rândurile vechi rămân, fără un INSERT.
    */
    public static String addPurchaseDate() {
        // Începător: sales capătă coloana date_of_purchase. Nu se adaugă un rând.
        // Producție: ALTER TABLE sales ADD COLUMN date_of_purchase DATE. Fără linie, exemplul de schemă din PDF nu poate fi trimis.
        // Expert: dacă sales lipsește, eșecul este tabelă inexistentă, nu 1064 de cuvânt rezervat. ADD necotat ca nume de tabelă ar fi celălalt eșec.
        return "ALTER TABLE sales ADD COLUMN date_of_purchase DATE";
        // Începător: închiderea nu șterge coloana pe care propoziția ar adăuga-o.
        // Producție: acolada închide addPurchaseDate. Fără ea, ALTER din curs nu compilează aici.
        // Expert: acolada nu este un DDL implicit. Coloana apare doar când serverul termină propoziția cu succes.
    }

    // Începător: metoda citește din lista serverului dacă INTERVAL este rezervat.
    // Producție: reservedFlag întoarce SELECT WORD, RESERVED FROM INFORMATION_SCHEMA.KEYWORDS WHERE WORD = 'INTERVAL'. Fără metodă, decizia nu are sursa din manual.
    // Expert: SELECT-ul nu schimbă schema și nu înlocuiește parserul. RESERVED=1 este faptul, nu un index.
    /*
    🟢 Începător
        Ce este            lista de cuvinte a instanței, cu steagul rezervat
        De ce e nevoie     o listă copiată în aplicație rămâne în urma serverului
    🟡 Producție
        Cum se scrie       SELECT WORD, RESERVED FROM INFORMATION_SCHEMA.KEYWORDS WHERE WORD = 'INTERVAL'
    🔴 Staff
        Unde               INFORMATION_SCHEMA, citire, fără lacăt pe sales
    Pași                   1. WORD este INTERVAL.
                           2. RESERVED 1 înseamnă necotat respins.
                           3. Rezultatul nu creează tabela.
    */
    public static String reservedFlag() {
        // Începător: întrebarea nu adaugă și nu șterge o tabelă. Întoarce cuvântul și un număr.
        // Producție: SELECT WORD, RESERVED FROM INFORMATION_SCHEMA.KEYWORDS WHERE WORD = 'INTERVAL'. Fără linie, nu există cum să citești steagul înainte de DDL.
        // Expert: 1 pe INTERVAL nu spune nimic despre BEGIN, care este 0. Citirea nu ia lacăt de rând pe tabela pe care urmează să o creezi.
        return "SELECT WORD, RESERVED FROM INFORMATION_SCHEMA.KEYWORDS WHERE WORD = 'INTERVAL'";
        // Începător: închiderea nu scrie steagul înapoi în server.
        // Producție: acolada închide reservedFlag. Fără ea, interogarea din manual nu rămâne metodă.
        // Expert: acolada nu îngheață lista 8.4. LIBRARY, nou rezervat în 9.7, nu este în rezultatul acestei propoziții.
    }

    // Începător: metoda încearcă să adauge coloana rank fără citare.
    // Producție: unquotedRank întoarce ALTER TABLE sales ADD COLUMN rank INT. Fără metodă, cazul de replicare nu este propoziție.
    // Expert: RANK este rezervat din 8.0. Replica 9.7 dă 1064 la parsare, înainte de replicate-ignore-table.
    /*
    🟢 Începător
        Ce este            un nume de coloană care a devenit rezervat pe serverul nou
        De ce e nevoie     filtrul de replicare pare că poate sări peste tabelă, dar parsarea este înainte
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD COLUMN rank INT
    🔴 Staff
        Unde               parserul replicii, Error 1064, înainte de filtrele do/ignore
    Pași                   1. Evenimentul conține rank necotat.
                           2. Replica parsează și cade.
                           3. Coloana nu apare, chiar dacă tabela era ignorată.
    */
    public static String unquotedRank() {
        // Începător: coloana rank nu se adaugă la sales.
        // Producție: ALTER TABLE sales ADD COLUMN rank INT cade cu ERROR 1064 (42000) pe 9.7. Fără linie, exemplul RANK din manualul de replicare nu este în cod.
        // Expert: --replicate-ignore-table nu este ocolirea. Parserul trebuie să știe obiectul înainte de filtru. Citarea este quotedRank.
        return "ALTER TABLE sales ADD COLUMN rank INT";
        // Începător: închiderea nu filtrează evenimentul de replicare.
        // Producție: acolada închide unquotedRank. Fără ea, forma necotată a lui rank nu compilează.
        // Expert: acolada nu aplică replicate-do-db. Eșecul rămâne unul de parsare.
    }

    // Începător: metoda adaugă coloana rank pentru că numele este citat.
    // Producție: quotedRank întoarce ALTER TABLE sales ADD COLUMN `rank` INT. Fără metodă, reparația de replicare nu este în cod.
    // Expert: citarea trece de parser. Dacă sales nu există, eroarea de după nu mai este 1064 de cuvânt rezervat.
    /*
    🟢 Începător
        Ce este            același nume de coloană, acceptat ca identificator
        De ce e nevoie     fără backtick, unquotedRank nu lasă coloana
    🟡 Producție
        Cum se scrie       ALTER TABLE sales ADD COLUMN `rank` INT
    🔴 Staff
        Unde               identificator citat, după ce parserul a trecut
    Pași                   1. Backtick-ul face din RANK un nume.
                           2. Coloana se adaugă dacă sales există.
                           3. Următorul SELECT rank fără citare cade din nou.
    */
    public static String quotedRank() {
        // Începător: sales primește coloana rank. Rândurile care erau deja rămân.
        // Producție: ALTER TABLE sales ADD COLUMN `rank` INT. Fără linie, forma care trece de un cuvânt rezervat pe coloană nu poate fi trimisă.
        // Expert: reparația pe sursă sau citarea în fiecare propoziție ține. Filtrul de replicare nu ține. MANUAL devenit nerezervat în 9.7.2 nu este acest caz: RANK rămâne (R).
        return "ALTER TABLE sales ADD COLUMN `rank` INT";
        // Începător: închiderea nu redenumește coloana înapoi.
        // Producție: acolada închide quotedRank. Fără ea, ALTER citat nu rămâne în clasă.
        // Expert: acolada nu actualizează binlog-ul. Evenimentul vechi, necotat, tot cade pe replică.
    }

    // Începător: metoda încearcă o tabelă library, cuvânt nou rezervat în 9.7.
    // Producție: libraryTable întoarce CREATE TABLE library (purchase_number INT). Fără metodă, diferența față de 8.4 nu este propoziție.
    // Expert: LIBRARY este (R) nou față de 8.4. O listă înghețată în aplicație nu vede cuvântul.
    /*
    🟢 Începător
        Ce este            un nume care trecea pe 8.4 și nu mai trece pe 9.7
        De ce e nevoie     upgrade-ul rezervă cuvinte fără să șteargă altele
    🟡 Producție
        Cum se scrie       CREATE TABLE library (purchase_number INT)
    🔴 Staff
        Unde               lista New Keywords: LIBRARY (R), EXTERNAL (R)
    Pași                   1. Pe 9.7 cuvântul este rezervat.
                           2. CREATE necotat dă 1064.
                           3. Tabela nu apare.
    */
    public static String libraryTable() {
        // Începător: tabela library nu apare, deși purchase_number ar fi fost o coloană obișnuită.
        // Producție: CREATE TABLE library (purchase_number INT) cade cu ERROR 1064 (42000) pe 9.7. Fără linie, cuvântul nou rezervat nu este în demo.
        // Expert: între 8.4 și 9.7 manualul nu scoate cuvinte. Eșecul este o adăugare, nu un keyword șters. EXTERNAL este în aceeași listă nouă, nu este LIBRARY.
        return "CREATE TABLE library (purchase_number INT)";
        // Începător: închiderea nu copiază lista de cuvinte în aplicație.
        // Producție: acolada închide libraryTable. Fără ea, CREATE cu LIBRARY necotat nu compilează.
        // Expert: acolada nu face cuvântul nerezervat. Doar un backtick sau alt nume schimbă soarta propoziției.
    }

    // Începător: metoda încearcă o tabelă al cărei nume este _filename.
    // Producție: filenameTable întoarce CREATE TABLE _filename (purchase_number INT). Fără metodă, rezervarea din afara listei (R) nu este în cod.
    // Expert: manualul spune că _FILENAME este rezervat pe lângă cuvintele marcate (R). Nu este o cale de fișier.
    /*
    🟢 Începător
        Ce este            un nume rezervat care nu apare ca rând (R) în alfabet
        De ce e nevoie     cine citește doar lista cu (R) îl scapă
    🟡 Producție
        Cum se scrie       CREATE TABLE _filename (purchase_number INT)
    🔴 Staff
        Unde               propoziția din 11.3: în plus, _FILENAME este rezervat
    Pași                   1. Parserul tratează _FILENAME ca rezervat.
                           2. 1064.
                           3. Nu rămâne tabelă.
    */
    public static String filenameTable() {
        // Începător: nu se creează tabela _filename.
        // Producție: CREATE TABLE _filename (purchase_number INT) cade cu ERROR 1064 (42000). Fără linie, excepția _FILENAME nu poate fi trimisă.
        // Expert: nu este numele fișierului .ibd și nu este un index. Este tot un identificator respins la parsare, înainte de motor.
        return "CREATE TABLE _filename (purchase_number INT)";
        // Începător: închiderea nu creează fișierul pe disc.
        // Producție: acolada închide filenameTable. Fără ea, propoziția nu rămâne în clasă.
        // Expert: acolada nu mapează identificatorul la un fișier. Maparea din manual este alt subiect și nu salvează 1064.
    }

    // Începător: metoda citește coloanele din tabela citată, fără să adauge rânduri.
    // Producție: selectFromQuotedInterval întoarce SELECT begin, end FROM `interval`. Fără metodă, citarea de la CREATE pare că ține și după.
    // Expert: backtick-ul este pe această propoziție. FROM interval necotat cade iar cu 1064, fără plan de index.
    /*
    🟢 Începător
        Ce este            citirea unei tabele al cărei nume rămâne rezervat
        De ce e nevoie     CREATE citat nu scutește propoziția următoare
    🟡 Producție
        Cum se scrie       SELECT begin, end FROM `interval`
    🔴 Staff
        Unde               identificator citat în FROM; fără el, parserul, nu optimizerul
    Pași                   1. Tabela trebuie să existe deja.
                           2. Backtick-ul lasă SELECT-ul să treacă.
                           3. Nu se schimbă rândurile.
    */
    public static String selectFromQuotedInterval() {
        // Începător: ies coloanele begin și end, dacă tabela există. Nu se scrie un rând nou.
        // Producție: SELECT begin, end FROM `interval`. Fără linie, lecția arată doar CREATE și lasă impresia că citarea se moștenește.
        // Expert: dacă tabela lipsește, eroarea este obiect inexistent, după parsare. Fără backtick, nici nu se ajunge la „tabela lipsește”: este 1064.
        return "SELECT begin, end FROM `interval`";
        // Începător: închiderea nu golește rezultatul pe server, pentru că metoda doar ține textul.
        // Producție: acolada închide selectFromQuotedInterval. Fără ea, SELECT-ul citat nu compilează.
        // Expert: acolada nu păstrează un plan. EXPLAIN nici nu pornește dacă FROM interval este necotat.
    }

    // Începător: metoda creează tabela manual fără citare, pe regula din 9.7.2.
    // Producție: manualTable întoarce CREATE TABLE manual (purchase_number INT). Fără metodă, schimbarea de steag din patch nu este propoziție.
    // Expert: MANUAL a devenit nerezervat în 9.7.2. Un test care așteaptă 1064 pe acest patch este fals. PARALLEL este cealaltă schimbare, nu același cuvânt.
    /*
    🟢 Începător
        Ce este            un cuvânt care nu mai trebuie citat după 9.7.2
        De ce e nevoie     același text SQL își schimbă soarta între patch-uri
    🟡 Producție
        Cum se scrie       CREATE TABLE manual (purchase_number INT)
    🔴 Staff
        Unde               nota din lista 9.7: became nonreserved in 9.7.2
    Pași                   1. Pe 9.7.2 MANUAL nu este rezervat.
                           2. CREATE trece fără backtick.
                           3. Pe un patch mai vechi din 9.7, același text era rezervat.
    */
    public static String manualTable() {
        // Începător: pe 9.7.2 poate apărea tabela manual, cu coloana purchase_number.
        // Producție: CREATE TABLE manual (purchase_number INT). Fără linie, nu ai ce compara cu un test vechi care aștepta 1064.
        // Expert: nu extinde regula la RANK sau LIBRARY, care rămân (R). Citarea `manual` tot este un identificator valid, dar nu mai este obligatorie pe 9.7.2.
        return "CREATE TABLE manual (purchase_number INT)";
        // Începător: închiderea nu îngheață patch-ul 9.7.2 în aplicație.
        // Producție: acolada închide manualTable. Fără ea, propoziția cu MANUAL nerezervat nu rămâne.
        // Expert: acolada nu schimbă steagul PARALLEL. Acela a devenit nerezervat în același patch, dar nu este această propoziție.
    }

    // Începător: metoda pune count ca nume de coloană, nu ca număr de rânduri.
    // Producție: countColumn întoarce CREATE TABLE sales (count INT). Fără metodă, regula funcției din manual nu are identificatorul.
    // Expert: COUNT poate fi coloană. Apelul este COUNT( lipit. Spațiul din countWithSpace nu este acest CREATE.
    /*
    🟢 Începător
        Ce este            numele unei funcții folosit ca coloană
        De ce e nevoie     altfel COUNT pare rezervat ca INTERVAL
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (count INT)
    🔴 Staff
        Unde               Function Name Parsing: numele built-in poate fi identificator
    Pași                   1. count stă în lista de coloane.
                           2. Nu este urmat de (.
                           3. Tabela sales apare cu coloana, dacă numele tabelei este liber.
    */
    public static String countColumn() {
        // Începător: sales primește o coloană numită count. Propoziția nu numără rânduri.
        // Producție: CREATE TABLE sales (count INT). Fără linie, „COUNT este acceptabil ca nume de coloană” rămâne doar în manual.
        // Expert: dacă sales există deja, eșecul este tabelă duplicată, nu cuvânt rezervat. Spațiul înainte de ( este countWithSpace, nu această linie.
        return "CREATE TABLE sales (count INT)";
        // Începător: închiderea nu calculează câte rânduri are sales.
        // Producție: acolada închide countColumn. Fără ea, coloana count nu este în clasă.
        // Expert: acolada nu pornește agregarea. Nu este COUNT(*) și nu este GROUP BY.
    }

    // Începător: metoda scrie COUNT cu un spațiu înainte de paranteză, deci nu este apelul.
    // Producție: countWithSpace întoarce SELECT COUNT (purchase_number) FROM sales. Fără metodă, regula de spațiu nu este propoziție.
    // Expert: lipsa spațiului dintre nume și ( este ce face apelul. Spațiul nu rezervă cuvântul și nu filtrează ca WHERE.
    /*
    🟢 Începător
        Ce este            un nume de funcție despărțit de paranteză
        De ce e nevoie     parserul deosebește apelul de identificator prin spațiul care lipsește
    🟡 Producție
        Cum se scrie       SELECT COUNT (purchase_number) FROM sales
    🔴 Staff
        Unde               regula din 11.3: niciun spațiu între numele funcției și (
    Pași                   1. Spațiul rupe apelul.
                           2. Parserul nu vede built-in-ul COUNT.
                           3. Nu se numără rândurile.
    */
    public static String countWithSpace() {
        // Începător: serverul nu numără purchase_number, pentru că spațiul nu lasă apelul de funcție.
        // Producție: SELECT COUNT (purchase_number) FROM sales. Fără linie, forma lipită COUNT(purchase_number) pare singura scriere posibilă.
        // Expert: nu da ca sigur codul 1064 dacă nu l-ai văzut pe această formă. Sigur este doar că nu este invocarea built-in. Nu este nici un keyword rezervat în poziție de tabelă.
        return "SELECT COUNT (purchase_number) FROM sales";
        // Începător: închiderea nu umple un COUNT care nu a rulat.
        // Producție: acolada închide countWithSpace. Fără ea, propoziția cu spațiu nu compilează.
        // Expert: acolada nu scoate spațiul din text. Cât rămâne spațiul, nu este apelul.
    }

    // Începător: metoda folosește truncate ca nume de tabelă, nu ca golire.
    // Producție: truncateAsName întoarce CREATE TABLE truncate (purchase_number INT). Fără metodă, TRUNCATE fără (R) se confundă cu propoziția care șterge rânduri.
    // Expert: TRUNCATE nu este rezervat. Ca etichetă trebuie citat. TRUNCATE TABLE ar goli rândurile și nu este această linie.
    /*
    🟢 Începător
        Ce este            un cuvânt de propoziție care tot poate fi nume
        De ce e nevoie     altfel TRUNCATE pare interzis ca ALTER
    🟡 Producție
        Cum se scrie       CREATE TABLE truncate (purchase_number INT)
    🔴 Staff
        Unde               lista 9.7: TRUNCATE fără (R); lista de etichete îl cere citat
    Pași                   1. Ca nume de tabelă, trece necotat.
                           2. Nu se șterge niciun rând.
                           3. Ca etichetă, același cuvânt se citează.
    */
    public static String truncateAsName() {
        // Începător: poate apărea o tabelă numită truncate, cu coloana purchase_number. Rândurile din alte tabele rămân.
        // Producție: CREATE TABLE truncate (purchase_number INT). Fără linie, diferența față de TRUNCATE TABLE nu este în cod.
        // Expert: propoziția nu golește sales. Eticheta truncate într-un program stocat este contextul care cere citare, nu acest CREATE.
        return "CREATE TABLE truncate (purchase_number INT)";
        // Începător: închiderea nu golește tabela care tocmai ar fi fost creată.
        // Producție: acolada închide truncateAsName. Fără ea, CREATE cu TRUNCATE nerezervat nu rămâne.
        // Expert: acolada nu emite TRUNCATE TABLE sales. Acel text ar șterge rânduri și nu este aici.
    }

    // Începător: metoda folosește commit ca nume de tabelă.
    // Producție: commitAsName întoarce CREATE TABLE commit (purchase_number INT). Fără metodă, COMMIT nerezervat se confundă cu încheierea tranzacției.
    // Expert: identificatorul commit nu face schimbarea durabilă. Propoziția COMMIT, singură, este celălalt mecanism.
    /*
    🟢 Începător
        Ce este            cuvântul COMMIT pus ca nume, nu ca sfârșit de tranzacție
        De ce e nevoie     altfel orice commit pare că închide tranzacția
    🟡 Producție
        Cum se scrie       CREATE TABLE commit (purchase_number INT)
    🔴 Staff
        Unde               COMMIT fără (R) în 9.7; ca etichetă trebuie citat
    Pași                   1. CREATE cu numele commit trece.
                           2. Nu se închide o tranzacție.
                           3. Eticheta commit tot se citează.
    */
    public static String commitAsName() {
        // Începător: poate apărea tabela commit. Tranzacția deschisă de dinainte nu se închide prin acest nume.
        // Producție: CREATE TABLE commit (purchase_number INT). Fără linie, numele și propoziția COMMIT sunt unul și același cuvânt în cap.
        // Expert: durabilitatea este propoziția COMMIT, nu identificatorul. ROLLBACK rămâne celălalt sfârșit și nu este această tabelă.
        return "CREATE TABLE commit (purchase_number INT)";
        // Începător: închiderea metodei nu salvează rânduri în InnoDB.
        // Producție: acolada închide commitAsName. Fără ea, CREATE cu numele commit nu compilează.
        // Expert: acolada Java nu este propoziția COMMIT și nu ține loc de ea.
    }

    // Începător: metoda încearcă o tabelă de comenzi numită order, fără citare.
    // Producție: unquotedOrder întoarce CREATE TABLE order (purchase_number INT). Fără metodă, transferul pe un domeniu nou nu este propoziție.
    // Expert: ORDER este rezervat, ca INTERVAL. Nu este clauza ORDER BY și nu sortează purchase_number.
    /*
    🟢 Începător
        Ce este            același eșec, pe o tabelă de comenzi, nu pe interval
        De ce e nevoie     demo-ul din repo nu este singurul loc în care cuvântul rezervat mușcă
    🟡 Producție
        Cum se scrie       CREATE TABLE order (purchase_number INT)
    🔴 Staff
        Unde               ORDER (R), poziție de identificator
    Pași                   1. Parserul vede ORDER rezervat.
                           2. 1064.
                           3. Nu există tabelă de comenzi.
    */
    public static String unquotedOrder() {
        // Începător: nu apare tabela de comenzi și purchase_number nu este salvat nicăieri.
        // Producție: CREATE TABLE order (purchase_number INT) cade cu ERROR 1064 (42000). Fără linie, transferul rămâne o parafrază a lui interval.
        // Expert: ORDER BY purchase_number ar sorta rânduri care există. Aici nu există rânduri, pentru că tabela nu s-a creat. Nu este un index pe purchase_number.
        return "CREATE TABLE order (purchase_number INT)";
        // Începător: închiderea nu sortează comenzile care nu au fost create.
        // Producție: acolada închide unquotedOrder. Fără ea, CREATE cu ORDER necotat nu rămâne.
        // Expert: acolada nu adaugă BY. Fără BY, cuvântul tot este rezervat în poziția numelui.
    }

    // Începător: metoda creează tabela de comenzi pentru că order este citat.
    // Producție: quotedOrder întoarce CREATE TABLE `order` (purchase_number INT). Fără metodă, alternativa de pe domeniul nou nu este în cod.
    // Expert: citarea lasă 0 rânduri, la fel ca `interval`. Următorul FROM order fără backtick cade iar. Redenumirea în aplicație evită citarea la fiecare propoziție.
    /*
    🟢 Începător
        Ce este            tabela de comenzi al cărei nume rămâne cuvânt rezervat
        De ce e nevoie     domeniul nu poate schimba cuvântul, deci rămâne citarea
    🟡 Producție
        Cum se scrie       CREATE TABLE `order` (purchase_number INT)
    🔴 Staff
        Unde               identificator citat; 0 rânduri după CREATE
    Pași                   1. Backtick-ul face numele legal.
                           2. Tabela apare goală.
                           3. Fiecare propoziție ulterioară citează din nou, sau aplicația redenumește.
    */
    public static String quotedOrder() {
        // Începător: rămâne o tabelă de comenzi fără rânduri, cu coloana purchase_number.
        // Producție: CREATE TABLE `order` (purchase_number INT). Fără linie, transferul nu arată forma care trece.
        // Expert: nu garantează unicitatea comenzii și nu este PRIMARY KEY. Dacă aplicația poate alege alt nume, reservedFlag pe ORDER evită backtick-ul în fiecare DML.
        return "CREATE TABLE `order` (purchase_number INT)";
        // Începător: închiderea nu inserează o comandă în tabela goală.
        // Producție: acolada închide quotedOrder. Fără ea, CREATE citat pe order nu compilează.
        // Expert: acolada nu înlocuiește citarea din INSERT-ul următor. Numele rămâne rezervat.
    }

    // Începător: metoda pune grant ca nume de coloană și nu acordă niciun drept.
    // Producție: grantAsColumn întoarce CREATE TABLE sales (grant INT). Fără metodă, cuvântul GRANT rezervat nu este în cod ca identificator.
    // Expert: poziția coloanei dă 1064. Propoziția GRANT care schimbă privilegiul este DCL, nu acest CREATE.
    /*
    🟢 Începător
        Ce este            GRANT rezervat, scris unde parserul așteaptă o coloană
        De ce e nevoie     altfel cuvântul pare că acordă dreptul doar pentru că apare
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (grant INT)
    🔴 Staff
        Unde               GRANT (R), poziție de identificator, înainte de orice privilegiu
    Pași                   1. Parserul vede GRANT necotat.
                           2. 1064.
                           3. Nu se creează tabela și nu se acordă un drept.
    */
    public static String grantAsColumn() {
        // Începător: nu apare tabela sales din această propoziție și nimeni nu primește un privilegiu nou.
        // Producție: CREATE TABLE sales (grant INT) cade cu ERROR 1064 (42000). Fără linie, GRANT ca nume nu poate fi trimis.
        // Expert: GRANT TO user ar schimba dreptul și nu este aici. ALTER ca nume, în alterAsTableName, este aceeași poziție pentru alt cuvânt (R).
        return "CREATE TABLE sales (grant INT)";
        // Începător: închiderea nu acordă privilegiul ratat de parser.
        // Producție: acolada închide grantAsColumn. Fără ea, CREATE cu GRANT necotat nu compilează.
        // Expert: acolada nu este propoziția GRANT. Privilegiul rămâne neschimbat.
    }
}
