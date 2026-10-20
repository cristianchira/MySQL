package _12_DB_Optimization.INDEX_OPTIMIZATION;

// https://dev.mysql.com/doc/refman/8.0/en/create-index.html

// https://dev.mysql.com/doc/refman/8.0/en/mysql-indexes.html
// HISTORICAL: legăturile de mai sus sunt manualul 8.0. Paginile deschise în tur sunt 9.7: 15.1.18 CREATE INDEX, 10.3.1, 10.8.2.



/*                                           INDEX


    ▬▬▐ Index of a table:
                 * an organizational unit that helps retrieve data more easily
                 * it takes more time to update a table because indexes must be updated, too,
                 * and that's time-consuming.


                             ▬▬ How MySQL Uses Indexes ▬▬


    ○ Indexes are used to
                   * find rows with specific column values quickly.
           Without an index, MySQL must begin with the first row
                   * and then read through the entire table to find the relevant rows.
           The larger the table, the more this costs.
                   * If the table has an index for the columns in question,
                   * MySQL can quickly determine the position to seek to in the middle of the data file
                   * without having to look at all the data.
           This is much faster than reading every row sequentially.



                              ▬▬ Optimization and Indexes ▬▬


    1. How MySQL Uses Indexes
                   * https://dev.mysql.com/doc/refman/8.0/en/mysql-indexes.html

    2. Primary Key Optimization
                   * https://dev.mysql.com/doc/refman/8.0/en/primary-key-optimization.html

    3. SPATIAL Index Optimization
                   * https://dev.mysql.com/doc/refman/8.0/en/spatial-index-optimization.html

    4. Foreign Key Optimization
                   * https://dev.mysql.com/doc/refman/8.0/en/foreign-key-optimization.html

    5. Column Indexes
                   * https://dev.mysql.com/doc/refman/8.0/en/column-indexes.html

    6. Multiple-Column Indexes
                   * https://dev.mysql.com/doc/refman/8.0/en/multiple-column-indexes.html

    7. Verifying Index Usage
                   * https://dev.mysql.com/doc/refman/8.0/en/verifying-index-usage.html

    8. InnoDB and MyISAM Index Statistics Collection
                   * https://dev.mysql.com/doc/refman/8.0/en/index-statistics.html

    9. Comparison of B-Tree and Hash Indexes
                   * https://dev.mysql.com/doc/refman/8.0/en/index-btree-hash.html

     10. Use of Index Extensions
                   * https://dev.mysql.com/doc/refman/8.0/en/index-extensions.html

    11. Optimizer Use of Generated Column Indexes

                   * https://dev.mysql.com/doc/refman/8.0/en/generated-column-index-optimizations.html

    12. Invisible Indexes

                   * https://dev.mysql.com/doc/refman/8.0/en/invisible-indexes.html

    13. Descending Indexes

                   * https://dev.mysql.com/doc/refman/8.0/en/descending-indexes.html

    14. Indexed Lookups from TIMESTAMP Columns

                   * https://dev.mysql.com/doc/refman/8.0/en/timestamp-lookups.html


                                  ▬▬ Index are stored in: ▬▬


    ○  https://dev.mysql.com/doc/refman/8.0/en/index-btree-hash.html


    ○ B-trees             -- https://dev.mysql.com/doc/refman/8.0/en/index-btree-hash.html#btree-index-characteristics

                  * PRIMARY KEY,
                  * UNIQUE,
                  * INDEX,
                  * and FULLTEXT
                  HISTORICAL: lista de deasupra este începutul din manualul 9.7, 10.3.1, deschis în tur. Excepția citită acolo: pe InnoDB, FULLTEXT este listă inversată, nu B-tree. Indexul spațial este R-tree. Tabela MEMORY poate folosi hash.

    ○ Hash index

                  * https://dev.mysql.com/doc/refman/8.0/en/index-btree-hash.html#hash-index-characteristics


 */

/*
TOPIC:
    Indexul i_status lasă serverul să caute coletele cu status deschis fără să pornească de la primul rând, doar când planul îl alege. CREATE INDEX nu schimbă valorile. Un index nefolosit lasă citirea tabelei.
FAMILIE:
    Optimization
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Clientul local mysql.exe este 9.2.0 și nu este versiunea serverului. Portul 3306 ascultă. root@localhost a primit ERROR 1045 (28000) Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Căutarea după status nu trebuie să citească fiecare colet. După CREATE INDEX, valorile din shipment rămân. După un SELECT fără restricție pe status, planul poate tot citi tabela.
MECHANISM:
    Serverul, dacă primește indexThePlanCanSeek, adaugă i_status. explainNamesTheKey cere planul pentru status = 'open'. 10.8.2: key NULL înseamnă că niciun index nu a fost ales. scanWhenTheStatementReadsTheTable nu are WHERE, deci 10.2.1.23 nu are restricție utilizabilă.
LOOKALIKES:
    SELECT cu (status = 'open' AND 1 = 1) este plierea din 10.2.1.1, nu i_status. YEAR(shipped_at) = 2026 este o funcție pe care 10.2.1 o poate chema la fiecare rând; i_status este pe status, nu pe acea funcție. FULLTEXT pe InnoDB este listă inversată, nu acest index.
BOOK THEORY:
    Manualul 9.7, 10.3.1, deschis în tur: indexul găsește rânduri cu valori de coloană; fără el, serverul pornește de la primul rând și citește tabela; cu el, caută poziția. PRIMARY KEY, UNIQUE, INDEX și FULLTEXT stau de obicei în B-tree; pe InnoDB, FULLTEXT este listă inversată; spațial este R-tree; MEMORY poate fi hash. Prefixul din stânga poate fi folosit. Dacă toate coloanele cerute sunt în index, valorile pot veni din arbore; la InnoDB, indexul secundar ține și cheia primară. Când propoziția prelucrează majoritatea rândurilor, citirea secvențială poate fi mai rapidă. 10.8.2: key este indexul ales; key NULL înseamnă că nu s-a găsit un index; Using index înseamnă citire din arbore. 10.2.1.23: type ALL este scanarea, inclusiv fără restricție utilizabilă în WHERE. 10.2.1: prima verificare la un WHERE lent este indexul pe coloanele din WHERE. open_books.py nu a găsit niciun PDF în acest folder.
BOOK EXAMPLE:
    indexThePlanCanSeek
REPO DEMO:
    src/main/java/_12_DB_Optimization/INDEX_OPTIMIZATION/Index_Optimization.java
BREAK LAB:
    scanWhenTheStatementReadsTheTable ține EXPLAIN fără WHERE. Indexul pe status nu este o restricție. În tur șirul nu a fost trimis.
TRADE-OFFS:
    Lași scanarea când raportul citește majoritatea rândurilor. Treci la propoziție când singura schimbare este 1 = 1 sau o funcție pe shipped_at. Nu pui FULLTEXT ca să cauți status = 'open'.
ANTI-PATTERNS:
    CREATE INDEX i_status și apoi un SELECT fără WHERE, crezând că type ALL dispare.
STAFF QUESTIONS:
    indexThePlanCanSeek, explainNamesTheKey, scanWhenTheStatementReadsTheTable, coveringStatusFromTheTree. Banca de 50 stă la părintele DbOptimization_Staff_Interview. Acest copil nu are al doilea folder fast.
NOVEL TRANSFER:
    Rafturile unui depozit: index pe aisle pentru aisle = 'A3'. Un raport care listează toate rafturile poate tot citi tabela.
SPACED REVIEW:
    O zi: 21 octombrie 2026, i_status nu schimbă statusul stocat. Șapte zile: 27 octombrie 2026, key NULL nu este Using index. Treizeci de zile: 19 noiembrie 2026, FULLTEXT InnoDB nu este i_status.

Definiția unui index (index):
       Indexul i_status este structura de pe coloana status. Serverul îl folosește doar dacă planul îl alege. Valorile coletelor nu se schimbă la CREATE INDEX.

Exemplu:
       Index_Optimization.indexThePlanCanSeek()
       Două colete deschise rămân două colete deschise.

Definiția unei coloane (column):
       Coloana status este valoarea căutată. shipped_at nu intră în i_status.

Exemplu:
       Index_Optimization.explainNamesTheKey()
       WHERE status = 'open' este restricția pe coloana indexată.

Definiția unei tabele (table):
       Tabela shipment ține rândurile. Fără restricție utilizabilă, type ALL o citește.

Exemplu:
       Index_Optimization.scanWhenTheStatementReadsTheTable()
       SELECT-ul fără WHERE poate parcurge toată tabela chiar dacă i_status există.

Definiția unei propoziții (statement):
       EXPLAIN este propoziția care arată key și type. Nu înlocuiește CREATE INDEX și nu șterge colete.

Exemplu:
       Index_Optimization.explainNamesTheKey()
       Planul nu este rescrierea cu AND 1 = 1.

1. Enunț: Ce problemă rezolvă i_status pe shipment.status?
   Capcană: Orice index grăbește orice SELECT, inclusiv pe o tabelă de câteva rânduri.
   Cheie: indexThePlanCanSeek creează structura prin care egalitatea status = 'open' poate sări peste citirea de la primul rând. 10.3.1: pe o tabelă mică sau când se cer majoritatea rândurilor, scanarea poate fi mai rapidă. Valorile stocate nu se schimbă.
   Demonstrație: indexThePlanCanSeek
2. Enunț: Ce se întâmplă cu rândurile și cu planul după CREATE INDEX i_status?
   Capcană: Coletele care nu sunt deschise dispar, iar key devine i_status pentru orice SELECT.
   Cheie: CREATE INDEX schimbă schema, nu statusul stocat. explainNamesTheKey cere planul doar pentru status = 'open'. key NULL înseamnă că indexul nu a fost ales. În tur nu s-a citit un plan: 1045.
   Demonstrație: explainNamesTheKey
3. Enunț: De ce (status = 'open' AND 1 = 1) nu este i_status?
   Capcană: Rescrierea parantezelor construiește indexul.
   Cheie: Plierea din 10.2.1.1 nu este CREATE INDEX. i_status este structura. YEAR(shipped_at) nu este coloana acestui index.
   Demonstrație: indexThePlanCanSeek
4. Enunț: Cum eșuează i_status pe EXPLAIN SELECT id, status, shipped_at FROM shipment, fără WHERE?
   Capcană: Indexul există, deci type nu mai poate fi ALL.
   Cheie: scanWhenTheStatementReadsTheTable nu are restricție utilizabilă pe status. 10.2.1.23: type ALL este scanarea. Un index nefolosit nu grăbește propoziția. shipped_at nu este în i_status, deci nu este un covering doar pe status.
   Demonstrație: scanWhenTheStatementReadsTheTable
5. Enunț: Când lași scanarea și când treci la propoziție în loc de încă un index?
   Capcană: Adaugi un index pe fiecare coloană din SELECT, iar FORCE INDEX îl impune.
   Cheie: Lași scanarea când raportul citește majoritatea rândurilor, cum spune 10.3.1. Treci la propoziție când schimbi doar 1 = 1 sau când funcția este YEAR(shipped_at). coveringStatusFromTheTree cere doar status, coloana din index. FORCE INDEX nu este acest CREATE.
   Demonstrație: coveringStatusFromTheTree

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE cauți status deschis prin index -> indexThePlanCanSeek -> CREATE INDEX i_status ON shipment (status) -> nu garantează key = i_status, pentru că EXPLAIN nu a fost citit
    2. Eșecul cel mai mic
           -> DECIDE indexul există, deci scanarea a murit -> scanWhenTheStatementReadsTheTable -> EXPLAIN fără WHERE -> nu garantează că type nu este ALL
    3. Granița
           -> Indexul este mecanismul MySQL. Șirul Java nu îl creează pe server. Workbench nu alege key. Numele i_status, fără să apară în coloana key, nu este folosirea indexului.
    4. Alternativa
           -> Scanarea când se cer majoritatea rândurilor. Propoziția pliată sau YEAR(shipped_at) când coloana din index nu este cea din funcție.
    5. Anti-patternul
           -> CREATE INDEX i_status și un SELECT fără WHERE. Tabela tot poate fi citită. FULLTEXT nu caută status = 'open'.
    6. Dovada
           -> indexThePlanCanSeek ține CREATE INDEX. explainNamesTheKey ține EXPLAIN pe status. root@localhost a primit 1045 (using password: NO).
    7. Interviu
           -> i_status nu schimbă valorile. key NULL înseamnă că nu a fost ales. Fără WHERE, type ALL poate rămâne. AND 1 = 1 nu este indexul.
*/

public class Index_Optimization {

    /*
    🟢 Începător
        Ce este            indexul pe status, fără să schimbe coletele
        De ce e nevoie     fără el, căutarea deschisă pornește de la primul rând
    🟡 Producție
        Cum se scrie       CREATE INDEX i_status ON shipment (status)
    🔴 Staff
        Unde               schema primește B-tree pe status; InnoDB FULLTEXT nu este acest index; planul poate tot să nu îl aleagă
    Pași
        1. Ține CREATE INDEX pe status.
        2. Nu îl trimite.
        3. Nu îl numi FULLTEXT.
    */
    // Începător: metoda păstrează indexul prin care statusul poate fi căutat.
    // Producție: public static String indexThePlanCanSeek(). Dacă dispare, copilul de index nu mai are propoziția.
    // Expert: Nu deschide JDBC. Nu pune FORCE INDEX. Nu este indexul pe shipped_at.
    public static String indexThePlanCanSeek() {
        // Începător: apare i_status, iar statusul fiecărui colet rămâne ce era.
        // Producție: CREATE INDEX i_status ON shipment (status). Dacă linia dispare, rămâne doar lista de linkuri din comentariu.
        // Expert: Pe InnoDB acest INDEX este B-tree, nu lista inversată de la FULLTEXT. Un SELECT fără WHERE tot poate fi type ALL.
        return "CREATE INDEX i_status ON shipment (status)";
        // Începător: metoda a înapoiat indexul, nu un rând schimbat.
        // Producție: acolada închide indexThePlanCanSeek. SHOW INDEX nu a rulat.
        // Expert: 1045 nu este key NULL. EXPLAIN stă în explainNamesTheKey.
    }

    /*
    🟢 Începător
        Ce este            planul care spune dacă i_status a fost ales pentru coletele deschise
        De ce e nevoie     fără el, indexul creat pare deja folosit
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT id FROM shipment WHERE status = 'open'
    🔴 Staff
        Unde               key i_status înseamnă că a fost ales; key NULL înseamnă că nu; EXPLAIN nu șterge
    Pași
        1. Ține EXPLAIN pe egalitatea de status.
        2. Nu îl trimite.
        3. Nu îl înlocui cu EXPLAIN ANALYZE DELETE.
    */
    // Începător: metoda păstrează planul căutării după status.
    // Producție: public static String explainNamesTheKey(). Dacă dispare, nu mai ai unde citi key.
    // Expert: Nu este DESCRIBE shipment, care arată coloanele. Nu este ANALYZE pe DELETE.
    public static String explainNamesTheKey() {
        // Începător: vezi cum ar fi căutate coletele deschise, iar ele rămân în tabelă.
        // Producție: EXPLAIN SELECT id FROM shipment WHERE status = 'open'. Dacă linia dispare, folosirea indexului rămâne o poveste.
        // Expert: id poate sta în indexul secundar InnoDB pentru că cheia primară este copiată acolo, dar shipped_at nu este în i_status. AND 1 = 1 nu este acest WHERE.
        return "EXPLAIN SELECT id FROM shipment WHERE status = 'open'";
        // Începător: metoda a înapoiat planul, nu indexul creat.
        // Producție: acolada închide explainNamesTheKey. Coloana key nu a fost citită.
        // Expert: type ALL nu se inventează aici. 1045 a oprit sesiunea.
    }

    /*
    🟢 Începător
        Ce este            citirea tabelei când nu există o restricție pe status
        De ce e nevoie     fără ea, orice index pare că oprește type ALL
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT id, status, shipped_at FROM shipment
    🔴 Staff
        Unde               10.2.1.23: fără restricție utilizabilă, type ALL este scanarea; shipped_at nu este în i_status
    Pași
        1. Ține SELECT-ul fără WHERE.
        2. Nu îl trimite.
        3. Nu îl numi covering pe status.
    */
    // Începător: metoda păstrează citirea care nu filtrează statusul.
    // Producție: public static String scanWhenTheStatementReadsTheTable(). Dacă dispare, eșecul indexului nefolosit dispare.
    // Expert: Nu este WHERE status = 'open'. Nu este CREATE INDEX.
    public static String scanWhenTheStatementReadsTheTable() {
        // Începător: planul poate citi toate coletele, chiar dacă i_status există în schemă.
        // Producție: EXPLAIN SELECT id, status, shipped_at FROM shipment. Dacă linia dispare, nu mai ai scanarea de lângă index.
        // Expert: 10.3.1 spune că la majoritatea rândurilor citirea secvențială poate fi mai rapidă. Using index nu acoperă shipped_at prin i_status.
        return "EXPLAIN SELECT id, status, shipped_at FROM shipment";
        // Începător: metoda a înapoiat citirea tabelei, nu o căutare după status.
        // Producție: acolada închide scanWhenTheStatementReadsTheTable. Niciun type nu a fost observat.
        // Expert: FULLTEXT inversat nu este motivul acestui ALL. 1045 nu este un plan.
    }

    /*
    🟢 Începător
        Ce este            planul care cere doar status, coloana din index
        De ce e nevoie     fără el, orice coloană pare acoperită de i_status
    🟡 Producție
        Cum se scrie       EXPLAIN SELECT status FROM shipment WHERE status = 'open'
    🔴 Staff
        Unde               10.3.1: valorile pot veni din arbore când coloanele cerute sunt în index; Extra Using index este semnul din 10.8.2
    Pași
        1. Ține SELECT status, nu SELECT *.
        2. Nu îl trimite.
        3. Nu promite Using index fără să citești Extra.
    */
    // Începător: metoda păstrează căutarea care cere doar statusul.
    // Producție: public static String coveringStatusFromTheTree(). Dacă dispare, covering-ul rămâne un cuvânt.
    // Expert: Nu promite Using index în tur. SELECT * ar include shipped_at, care nu este în i_status.
    public static String coveringStatusFromTheTree() {
        // Începător: se cer coletele deschise, dar doar coloana status, nu data expedierii.
        // Producție: EXPLAIN SELECT status FROM shipment WHERE status = 'open'. Dacă linia dispare, nu mai ai propoziția de arbore.
        // Expert: InnoDB copiază cheia primară în indexul secundar, deci id poate fi acoperit odată cu status. shipped_at nu. EXPLAIN nu scrie rânduri.
        return "EXPLAIN SELECT status FROM shipment WHERE status = 'open'";
        // Începător: metoda a înapoiat planul pe coloana din index.
        // Producție: acolada închide coveringStatusFromTheTree. Extra nu a fost citit.
        // Expert: key NULL ar anula povestea de covering. AND 1 = 1 nu este acest SELECT.
    }
}
